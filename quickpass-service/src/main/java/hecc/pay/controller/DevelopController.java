package hecc.pay.controller;

import hecc.pay.entity.QuickPassDevelopEntity;
import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawStatusEnum;
import hecc.pay.enumer.WithdrawTypeEnum;
import hecc.pay.jpa.QuickPassDevelopRepository;
import hecc.pay.jpa.QuickPassWithdrawRepository;
import hecc.pay.vos.ActivityListVO;
import hecc.pay.vos.InvitesListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 拉新控制器
 * @Date: Created In 下午10:44 on 2018/3/19.
 */
@RestController
@RequestMapping("/invites/")
public class DevelopController extends BaseController {

    @Autowired
    private QuickPassDevelopRepository developRepository;
    @Autowired
    private QuickPassWithdrawRepository withdrawRepository;

    @GetMapping("/list")
    public ResponseVO inviteList(@RequestHeader Long tenantId) {

        List<QuickPassDevelopEntity> developEntityList = developRepository.findByTenantTenantIdAndDelIsFalse(tenantId);

        List<QuickPassWithdrawEntity> withdrawEntityList = withdrawRepository.findByTenantTenantIdAndTypeAndDelIsFalse(tenantId, WithdrawTypeEnum.拉新);

        long withdrawFee = withdrawEntityList.stream()
                .filter(withdraw -> WithdrawStatusEnum.已提交.equals(withdraw.status)
                        || WithdrawStatusEnum.提现成功.equals(withdraw.status))
                .mapToLong(withdraw -> withdraw.fee).reduce((f1, f2) -> f1 + f2).orElse(0L);
        long totalProfit = 0L;
        List<InvitesListVO> invites = new ArrayList<>(developEntityList.size());
        for (QuickPassDevelopEntity developEntity : developEntityList) {
            totalProfit += developEntity.profit;
            invites.add(new InvitesListVO(developEntity));
        }
        return successed(new ActivityListVO(totalProfit, totalProfit - withdrawFee, invites));
    }
}
