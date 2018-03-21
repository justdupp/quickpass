package hecc.pay.controller;

import hecc.pay.client.TenantClient;
import hecc.pay.client.route.RouterRouteResponse;
import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassCreditCardEntity;
import hecc.pay.entity.QuickPassOrderEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.enumer.OrderStatusEnum;
import hecc.pay.jpa.QuickPassCreditCardRepository;
import hecc.pay.jpa.QuickPassOrderRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午8:26 on 2018/3/21.
 */
@RestController
@RequestMapping("/order/")
public class OrderController extends BaseController {

    @Autowired
    private TenantClient tenantClient;
    @Autowired
    private QuickPassOrderRepository orderRepository;
    @Autowired
    private QuickPassTenantRepository tenantRepository;
    @Autowired
    private QuickPassCreditCardRepository creditCardRepository;

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseVO payOrder(@RequestHeader String platform, @RequestHeader Long tenantId,
                               @NotNull String fee, @NotNull String payBankAccountNumber, @NotNull String payBankName,
                               @NotNull String payBankMobile, @NotNull String payUserName, @NotNull String payIdCard,
                               @NotNull String receiverBankAccount, @NotNull String receiverBankName,
                               @NotNull String receiverBankMobile, @NotNull String receiverUserName,
                               @NotNull String receiveIdCard) {

        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);

        QuickPassOrderEntity orderEntity = new QuickPassOrderEntity();
        orderEntity.platform = platform;
        orderEntity.tenant = tenantEntity;
        orderEntity.fee = toMoney(fee);
        orderEntity.status = OrderStatusEnum.预下单;
        orderEntity.withdrawFee = tenantEntity.code.withdrawFee;

        orderEntity.payBankAccount = payBankAccountNumber;
        orderEntity.payBankName = payBankName;
        orderEntity.payBankMobile = payBankMobile;
        orderEntity.payUserName = payUserName;
        orderEntity.payIdCard = payIdCard;

        QuickPassCreditCardEntity creditCard = creditCardRepository.findFirstByBankAccountAndDelIsFalse(payBankAccountNumber);
        if (creditCard == null) {
            creditCard = new QuickPassCreditCardEntity();
        }
        creditCard.tenant = tenantEntity;
        creditCard.bankAccount = payBankAccountNumber;
        creditCard.bankName = payBankName;
        creditCard.mobile = payBankMobile;
        creditCard.userName = payUserName;
        creditCard.idCard = payIdCard;
        creditCard.platform = platform;
        creditCard.active = true;
        creditCardRepository.saveAndFlush(creditCard);

        orderEntity.payBankAccount = receiverBankAccount;
        orderEntity.payBankName = receiverBankName;
        orderEntity.payBankMobile = receiverBankMobile;
        orderEntity.payUserName = receiverUserName;
        orderEntity.payIdCard = receiveIdCard;

        TenantEntityVO tenantEntityVO = new TenantEntityVO();
        tenantEntityVO.recieverBankAccount = receiverBankAccount;
        tenantEntityVO.recieverBankName = receiverBankName;
        tenantEntityVO.mobile = receiverBankMobile;
        tenantEntityVO.name = receiverUserName;
        tenantEntityVO.idCard = receiveIdCard;
        tenantEntityVO.bankCardHasPassed = true;
        tenantEntityVO.id = tenantId;
        tenantClient.updateTenant(tenantEntityVO);

        orderRepository.saveAndFlush(orderEntity);

        RouterRouteResponse routeResponse = new RouterRouteResponse();

        routeResponse.setOrderId(orderEntity.id);
        orderEntity.thirdNo = orderEntity.generateOrderId();
        orderRepository.save(orderEntity);
        return successed(routeResponse);
    }

}
