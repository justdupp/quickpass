package hecc.pay.service;

import com.alibaba.fastjson.JSON;
import hecc.pay.entity.*;
import hecc.pay.enumer.OrderStatusEnum;
import hecc.pay.jpa.QuickPassDevelopRepository;
import hecc.pay.jpa.QuickPassOrderRepository;
import hecc.pay.jpa.QuickPassProfitRepository;
import hecc.pay.vos.OrderProfitVO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author xuhoujun
 * @description: 同步处理任务
 * @date: Created In 下午9:38 on 2018/3/22.
 */
@Component
public class AsyncTask {

    private Logger logger = LoggerFactory.getLogger(AsyncTask.class);

    @Autowired
    private QuickPassProfitRepository profitRepository;
    @Autowired
    private QuickPassOrderRepository orderRepository;
    @Autowired
    private QuickPassDevelopRepository developRepository;
    @Autowired
    private OkHttpClient okHttpClient;

    @Async
    public void asyncProfit(long orderId) {
        calculateProfit(orderId);
    }

    @Transactional(rollbackFor = Exception.class)
    void calculateProfit(long orderId) {
        logger.info("calculateProfit:" + orderId);
        if (profitRepository.countByOrderIdAndDelIsFalse(orderId) > 0) {
            return;
        }
        QuickPassOrderEntity order = orderRepository.findOne(orderId);
        QuickPassCodeEntity currentCode = order.tenant.code;
        List<OrderProfitVO> profits = new LinkedList<>();
        Set<Long> linkedCodeIds = new HashSet<>();
        while (currentCode != null && currentCode.tenant != null && currentCode.tenant.code != null) {
            if (linkedCodeIds.contains(currentCode.id)) {
                break;
            }
            QuickPassCodeEntity costCode = currentCode.tenant.code;
            QuickPassProfitEntity profit = new QuickPassProfitEntity();

            profit.order = order;
            profit.tenant = currentCode.tenant;
            profit.code = currentCode;
            profit.platform = order.platform;
            // TODO: 2018/3/22  分润待计算
            profitRepository.save(profit);
            linkedCodeIds.add(currentCode.id);

            profits.add(new OrderProfitVO(order, profit));
            currentCode = costCode;
        }
        if (!profits.isEmpty()) {
            Request request = new Builder().url(order.notifyUrl)
                    .post(RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(profits)))
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {}

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody body = response.body()) {
                        logger.debug(body.string());
                    }
                }
            });
        }

    }


    @Async
    public void asyncDevelop(long orderId) {
        saveDevelop(orderId);
    }

    @Transactional(rollbackFor = Exception.class)
    void saveDevelop(long orderId) {
        logger.info("saveDevelop:" + orderId);
        QuickPassOrderEntity order = orderRepository.findOne(orderId);

        QuickPassTenantEntity register = order.tenant;
        if (orderRepository.countByTenantTenantIdAndStatusAndDelIsFalse(register.tenantId, OrderStatusEnum.交易成功) == 1
                && developRepository.countByIdCardAndDelIsFalse(order.payIdCard) == 0) {
            QuickPassTenantEntity tenantEntity = register.code.tenant;
            if (tenantEntity != null) {
                QuickPassDevelopEntity developEntity = new QuickPassDevelopEntity();
                developEntity.profit = 10000;
                developEntity.register = register;
                developEntity.order = order;
                developEntity.tenant = tenantEntity;
                developEntity.platform = order.platform;
                developEntity.idCard = order.payIdCard;
                developRepository.save(developEntity);
            }
        }
    }


}
