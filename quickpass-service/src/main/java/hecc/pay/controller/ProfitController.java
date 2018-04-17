package hecc.pay.controller;

import hecc.pay.entity.QuickPassProfitEntity;
import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawStatusEnum;
import hecc.pay.enumer.WithdrawTypeEnum;
import hecc.pay.jpa.QuickPassProfitRepository;
import hecc.pay.jpa.QuickPassWithdrawRepository;
import hecc.pay.vos.ProfitListVO;
import hecc.pay.vos.ProfitOrderVO;
import hecc.pay.vos.ProfitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuhoujun
 * @description: 分润控制器
 * @date: Created In 下午9:36 on 2018/3/20.
 */
@RestController
@RequestMapping("/profit/")
public class ProfitController extends BaseController {

    @Autowired
    private QuickPassProfitRepository profitRepository;
    @Autowired
    private QuickPassWithdrawRepository withdrawRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseVO fetchProfits(@RequestHeader Long tenantId) {

        List<QuickPassProfitEntity> profitList = profitRepository.findByTenantTenantIdAndDelIsFalse(tenantId);

        long totalFee = 0L;
        long totalProfit = 0L;
        Map<Long, Map<Long, Integer>> profitCodeMap = new HashMap<>(10);
        Map<Long, ProfitListVO> codeMap = new HashMap<>(10);
        Map<String, ProfitOrderVO> codeTenantMap = new HashMap<>(10);

        for (QuickPassProfitEntity orderEntity : profitList) {
            Long codeKey = orderEntity.code.id;
            Map<Long, Integer> codeValue = profitCodeMap.get(codeKey);
            if (codeValue == null) {
                codeValue = new HashMap<>(10);
            }
            codeMap.put(codeKey, new ProfitListVO(orderEntity.code));
            Long tenantKey = orderEntity.order.tenant.id;
            Integer tenantValue = codeValue.get(tenantKey);
            if (tenantValue == null) {
                tenantValue = 0;
            }
            tenantValue += orderEntity.profit;
            codeTenantMap.put(codeKey + "_" + tenantKey, new ProfitOrderVO(orderEntity.order.payUserName));
            codeValue.put(tenantKey, tenantValue);
            profitCodeMap.put(codeKey, codeValue);
            totalFee += orderEntity.order.fee;
            totalProfit += orderEntity.profit;
        }

        List<QuickPassWithdrawEntity> withdrawList = withdrawRepository
                .findByTenantTenantIdAndTypeAndDelIsFalse(tenantId, WithdrawTypeEnum.快捷支付);
        long withdrawFee = withdrawList.stream()
                .filter(withdraw -> WithdrawStatusEnum.已提交.equals(withdraw.status)
                        || WithdrawStatusEnum.提现成功.equals(withdraw.status))
                .mapToLong(withdraw -> withdraw.fee).reduce((f1, f2) -> f1 + f2)
                .orElse(0L);

        final ProfitVO result = new ProfitVO(totalFee, totalProfit, totalProfit - withdrawFee);
        profitCodeMap.forEach((codeKey, codeValue) -> {
            ProfitListVO profitListVO = codeMap.get(codeKey);
            long totalCodeFee = 0L;
            for (Map.Entry<Long, Integer> entry : codeValue.entrySet()) {
                ProfitOrderVO profitOrderVO = codeTenantMap.get(codeKey + "_" + entry.getKey());
                profitOrderVO.setFee(entry.getValue());
                profitListVO.addTenantOrder(profitOrderVO);
                totalCodeFee += entry.getValue();
            }
            profitListVO.setFee(totalCodeFee);
            result.codes.add(profitListVO);
        });
        return succeed(result);
    }
}
