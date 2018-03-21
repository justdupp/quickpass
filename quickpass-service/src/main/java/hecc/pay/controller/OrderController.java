package hecc.pay.controller;

import hecc.pay.client.TenantClient;
import hecc.pay.client.route.RouterPayResponse;
import hecc.pay.client.route.RouterRequest;
import hecc.pay.client.route.RouterRouteResponse;
import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassCreditCardEntity;
import hecc.pay.entity.QuickPassOrderEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.enumer.OrderStatusEnum;
import hecc.pay.jpa.QuickPassCreditCardRepository;
import hecc.pay.jpa.QuickPassOrderRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

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
                               @NotNull String receiverIdCard) {

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

        orderEntity.receiverBankAccount = receiverBankAccount;
        orderEntity.receiverBankName = receiverBankName;
        orderEntity.receiverBankMobile = receiverBankMobile;
        orderEntity.receiverUserName = receiverUserName;
        orderEntity.receiverIdCard = receiverIdCard;

        TenantEntityVO tenantEntityVO = new TenantEntityVO();
        tenantEntityVO.recieverBankAccount = receiverBankAccount;
        tenantEntityVO.recieverBankName = receiverBankName;
        tenantEntityVO.mobile = receiverBankMobile;
        tenantEntityVO.name = receiverUserName;
        tenantEntityVO.idCard = receiverIdCard;
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

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResponseVO pay(@RequestHeader Long tenantId, Long orderId) {
        QuickPassOrderEntity order = orderRepository.findOne(orderId);
        logger.info("订单OrderId=" + order.id + ";交易金额fee=" + order.fee);
        RouterPayResponse payResponse = getPayResponse(
                new RouterRequest(order.id, order.payBankAccount, order.payBankMobile, order.fee,order.thirdNo,
                        order.payUserName, order.payIdCard, order.receiverBankAccount, order.receiverBankMobile,
                        order.receiverBankName, order.withdrawFee, tenantId, order.platform));

        try {
            if ("SUCCESS".equals(payResponse.getResCode())) {
                order.status = OrderStatusEnum.已提交;
                orderRepository.save(order);
                return successed(payResponse.getData());
            } else {
                order.status = OrderStatusEnum.交易失败;
                orderRepository.save(order);
                return failed(payResponse.getResMsg(), ERROR_CODE_PAY_CODE_FAILED);
            }
        } catch (Exception e) {
            order = orderRepository.findOne(orderId);
            if ("已提交".equals(order.status)) {
                return successed(payResponse.getData());
            } else if ("交易失败".equals(order.status)) {
                return failed(payResponse.getResMsg(), ERROR_CODE_PAY_CODE_FAILED);
            } else {
                orderRepository.save(order);
                return successed(payResponse.getData());
            }
        }
    }

    private RouterPayResponse getPayResponse(@RequestBody RouterRequest request) {
        logger.info("RouterRequest=========" + request);
        // TODO: 2018/3/21 支付请求 
        return null;
    }


}
