package hecc.pay.controller;

import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawTypeEnum;
import hecc.pay.jpa.QuickPassWithdrawRepository;
import hecc.pay.vos.WithdrawVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static hecc.pay.util.PageUtil.generatePage;

/**
 * @Auther xuhoujun
 * @Description: 提现控制器
 * @Date: Created In 下午9:33 on 2018/3/19.
 */
@RestController
@RequestMapping("/withdraw/")
public class WithdrawController extends BaseController {

    @Autowired
    private QuickPassWithdrawRepository withdrawRepository;

    @RequestMapping(value = "/withdraws", method = RequestMethod.GET)
    public ResponseVO listWithdraw( @RequestHeader Long tenantId, Integer page) {
        Page<QuickPassWithdrawEntity> withdrawList = withdrawRepository
                .findByTenantTenantIdAndTypeAndDelIsFalse(tenantId, WithdrawTypeEnum.快捷支付, generatePage(page));
        return successed(
                withdrawList.getContent().stream()
                        .map(withdrawEntity -> new WithdrawVO(withdrawEntity))
                        .collect(Collectors.toList()));
    }
}
