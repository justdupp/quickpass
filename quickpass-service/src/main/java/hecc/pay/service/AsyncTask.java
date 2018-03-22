package hecc.pay.service;

import hecc.pay.entity.*;
import hecc.pay.enumer.OrderStatusEnum;
import hecc.pay.jpa.QuickPassDevelopRepository;
import hecc.pay.jpa.QuickPassOrderRepository;
import hecc.pay.jpa.QuickPassProfitRepository;
import hecc.pay.vos.OrderProfitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:38 on 2018/3/22.
 */
@Component
public class AsyncTask {

    @Autowired
    private QuickPassProfitRepository profitRepository;
    @Autowired
    private QuickPassOrderRepository orderRepository;
    @Autowired
    private QuickPassDevelopRepository developRepository;

    @Async
    public void asyncProfit(long orderId) {
        calculateProfit(orderId);
    }

    @Transactional
    void calculateProfit(long orderId) {
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

    }


    @Async
    public void asyncDevelop(long orderId) {
        saveDevelop(orderId);
    }

    @Transactional
    void saveDevelop(long orderId) {
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
