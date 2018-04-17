package hecc.pay.controller;

import hecc.pay.client.TenantClient;
import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassProfitEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawStatusEnum;
import hecc.pay.enumer.WithdrawTypeEnum;
import hecc.pay.jpa.QuickPassProfitRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import hecc.pay.jpa.QuickPassWithdrawRepository;
import hecc.pay.vos.WithdrawVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static hecc.pay.util.MoneyUtil.toMoney;
import static hecc.pay.util.PageUtil.generatePage;
import static java.util.stream.Collectors.toList;

/**
 * @author xuhoujun
 * @description: 提现控制器
 * @date: Created In 下午9:33 on 2018/3/19.
 */
@RestController
@RequestMapping("/withdraw/")
public class WithdrawController extends BaseController {

    @Autowired
    private QuickPassWithdrawRepository withdrawRepository;
    @Autowired
    private QuickPassProfitRepository profitRepository;
    @Autowired
    private QuickPassTenantRepository tenantRepository;
    @Autowired
    private TenantClient tenantClient;

    @RequestMapping(value = "/withdraws", method = RequestMethod.GET)
    public ResponseVO listWithdraw( @RequestHeader Long tenantId, Integer page) {
        Page<QuickPassWithdrawEntity> withdrawList = withdrawRepository
                .findByTenantTenantIdAndTypeAndDelIsFalse(tenantId, WithdrawTypeEnum.快捷支付, generatePage(page));
        return succeed(
                withdrawList.getContent().stream()
                        .map(withdrawEntity -> new WithdrawVO(withdrawEntity))
                        .collect(toList()));
    }

    @RequestMapping(value = "/withdrawAll", method = RequestMethod.POST)
    public ResponseVO withdrawAll(@RequestHeader Long tenantId) {
        List<QuickPassProfitEntity> profitList = profitRepository.findByTenantTenantIdAndDelIsFalse(tenantId);
        long totalProfit = 0L;
        for (QuickPassProfitEntity orderEntity : profitList) {
            totalProfit += orderEntity.profit;
        }

        List<QuickPassWithdrawEntity> withdrawList = withdrawRepository
                .findByTenantTenantIdAndTypeAndDelIsFalse(tenantId, WithdrawTypeEnum.快捷支付);
        long withdrawFee = withdrawList.stream()
                .filter(withdraw -> WithdrawStatusEnum.已提交.equals(withdraw.status) || WithdrawStatusEnum.提现成功
                        .equals(withdraw.status)).mapToLong(withdraw -> withdraw.fee)
                .reduce((f1, f2) -> f1 + f2)
                .orElse(0L);

        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);

        TenantEntityVO tenantEntityVO = tenantClient.getTenant(tenantEntity.tenantId);

        String result = toMoney(totalProfit - withdrawFee);
        QuickPassWithdrawEntity withdrawEntity = new QuickPassWithdrawEntity();
        Integer fee = toMoney(result);
        if (fee <= 0) {
            return failed("提现余额不足", ERROR_OPERATE_FAILED);
        }
        if (withdrawRepository.countByTenantTenantIdAndStatusNotAndDelIsFalse(tenantId, WithdrawStatusEnum.提现失败) != 0) {
            if (fee < ERROR_WITHDRAW_FEE_LIMIT) {
                return failed("提现余额不足100元", ERROR_OPERATE_FAILED);
            }
        }
        withdrawEntity.fee = fee;
        withdrawEntity.tenant = tenantEntity;
        withdrawEntity.status = WithdrawStatusEnum.已提交;
        withdrawEntity.platform = tenantEntity.platform;
        withdrawEntity.type = WithdrawTypeEnum.快捷支付;
        withdrawEntity.bankAccount = tenantEntityVO.receiverBankAccount;
        withdrawEntity.bankReservedMobile = tenantEntityVO.mobile;
        withdrawEntity.bankName = tenantEntityVO.receiverBankName;
        withdrawEntity.userName = tenantEntityVO.name;
        withdrawEntity.idCard = tenantEntityVO.idCard;
        withdrawRepository.save(withdrawEntity);
        return succeed(null);
    }

}
