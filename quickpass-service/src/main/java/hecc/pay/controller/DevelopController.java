package hecc.pay.controller;

import hecc.pay.client.TenantClient;
import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassDevelopEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawStatusEnum;
import hecc.pay.enumer.WithdrawTypeEnum;
import hecc.pay.jpa.QuickPassDevelopRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import hecc.pay.jpa.QuickPassWithdrawRepository;
import hecc.pay.vos.ActivityListVO;
import hecc.pay.vos.InvitesListVO;
import hecc.pay.vos.WithdrawVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import static hecc.pay.util.MoneyUtil.toMoney;
import static java.util.stream.Collectors.toList;

/**
 * @author xuhoujun
 * @description: 拉新控制器
 * @date: Created In 下午10:44 on 2018/3/19.
 */
@RestController
@RequestMapping("/invites/")
public class DevelopController extends BaseController {

    @Autowired
    private QuickPassDevelopRepository developRepository;
    @Autowired
    private QuickPassWithdrawRepository withdrawRepository;
    @Autowired
    private QuickPassTenantRepository tenantRepository;
    @Autowired
    private TenantClient tenantClient;

    @ApiOperation("拉新列表")
    @GetMapping("/list")
    public ResponseVO inviteList(@RequestHeader Long tenantId) {

        List<QuickPassDevelopEntity> developEntityList = developRepository.findByTenantTenantIdAndDelIsFalse(tenantId);

        List<QuickPassWithdrawEntity> withdrawEntityList = withdrawRepository.findByTenantTenantIdAndTypeAndDelIsFalse(tenantId, WithdrawTypeEnum.拉新);

        long withdrawFee = withdrawEntityList.stream()
                .filter(withdraw -> WithdrawStatusEnum.已提交.equals(withdraw.status)
                        || WithdrawStatusEnum.提现成功.equals(withdraw.status))
                .mapToLong(withdraw -> withdraw.fee).reduce((f1, f2) -> f1 + f2)
                .orElse(0L);

        long totalProfit = 0L;
        List<InvitesListVO> invites = new ArrayList<>(developEntityList.size());

        for (QuickPassDevelopEntity developEntity : developEntityList) {
            totalProfit += developEntity.profit;
            invites.add(new InvitesListVO(developEntity));
        }
        return succeed(new ActivityListVO(totalProfit, totalProfit - withdrawFee, invites));
    }

    @ApiOperation("拉新提现")
    @GetMapping("/withdraw")
    public ResponseVO withdraw(@RequestHeader Long tenantId) {

        List<QuickPassDevelopEntity> developEntityList = developRepository.findByTenantTenantIdAndDelIsFalse(tenantId);
        long totalProfit = 0L;
        for (QuickPassDevelopEntity developEntity : developEntityList) {
            totalProfit += developEntity.profit;
        }

        List<QuickPassWithdrawEntity> withdrawEntityList = withdrawRepository
                .findByTenantTenantIdAndTypeAndDelIsFalse(tenantId, WithdrawTypeEnum.拉新);
        long withdrawFee = withdrawEntityList.stream()
                .filter(withdraw -> WithdrawStatusEnum.已提交.equals(withdraw.status)
                        || WithdrawStatusEnum.提现成功.equals(withdraw.status))
                .mapToLong(withdraw -> withdraw.fee).reduce((f1, f2) -> f1 + f2).orElse(0L);

        QuickPassTenantEntity tenantEntity = tenantRepository
                .findOneByTenantIdAndDelIsFalse(tenantId);
        TenantEntityVO tenantEntityVO = tenantClient.getTenant(tenantEntity.tenantId);

        if ((totalProfit - withdrawFee) <= 0) {
            return failed("余额不足", ERROR_OPERATE_FAILED);
        } else if ((totalProfit - withdrawFee) >= ERROR_WITHDRAW_FEE_LIMIT) {
            String result = toMoney(totalProfit - withdrawFee);
            QuickPassWithdrawEntity withdrawEntity = new QuickPassWithdrawEntity();
            withdrawEntity.fee = toMoney(result);
            withdrawEntity.tenant = tenantEntity;
            withdrawEntity.status = WithdrawStatusEnum.已提交;
            withdrawEntity.platform = tenantEntity.platform;
            withdrawEntity.bankName = tenantEntityVO.receiverBankName;
            withdrawEntity.idCard = tenantEntityVO.idCard;
            withdrawEntity.userName = tenantEntityVO.name;
            withdrawEntity.bankReservedMobile = tenantEntityVO.mobile;
            withdrawEntity.bankAccount = tenantEntityVO.receiverBankAccount;
            withdrawEntity.type = WithdrawTypeEnum.拉新;
            withdrawRepository.save(withdrawEntity);
            return succeed(null);
        } else {
            return failed("提现金额不足100元", ERROR_OPERATE_FAILED);
        }
    }

    @ApiOperation("全部提现")
    @GetMapping("/withdraws")
    public ResponseVO withdraws(@RequestHeader Long tenantId) {
        List<QuickPassWithdrawEntity> withdrawEntityList = withdrawRepository.findByTenantTenantIdAndTypeAndDelIsFalse(tenantId, WithdrawTypeEnum.拉新);
        return succeed(withdrawEntityList.stream()
                .map(withdrawEntity -> new WithdrawVO(withdrawEntity, ""))
                .collect(toList()));
    }

}
