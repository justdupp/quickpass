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
import hecc.pay.service.PayService;
import hecc.pay.vos.OrderListVO;
import hecc.pay.vos.OrderStatisticsVO;
import hecc.pay.vos.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.util.Date;

import static hecc.pay.util.MoneyUtil.toMoney;
import static hecc.pay.util.PageUtil.generatePage;
import static java.util.stream.Collectors.toList;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午8:26 on 2018/3/21.
 */
@RestController
@RequestMapping("/order/")
public class OrderController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    private static final String PAY_RES_CODE = "SUCCESS";

    @Autowired
    private TenantClient tenantClient;
    @Autowired
    private QuickPassOrderRepository orderRepository;
    @Autowired
    private QuickPassTenantRepository tenantRepository;
    @Autowired
    private QuickPassCreditCardRepository creditCardRepository;
    @Autowired
    private PayService payService;

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
        return succeed(routeResponse);
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResponseVO pay(@RequestHeader Long tenantId, Long orderId) {
        QuickPassOrderEntity order = orderRepository.findOne(orderId);
        logger.info("订单orderId=" + order.id + ";交易金额fee=" + order.fee);
        RouterPayResponse payResponse = getPayResponse(
                new RouterRequest(order.id, order.payBankAccount, order.payBankMobile, order.fee,order.thirdNo,
                        order.payUserName, order.payIdCard, order.receiverBankAccount, order.receiverBankMobile,
                        order.receiverBankName, order.withdrawFee, tenantId, order.platform));

        try {
            if (PAY_RES_CODE.equals(payResponse.getResCode())) {
                order.status = OrderStatusEnum.已提交;
                orderRepository.save(order);
                return succeed(payResponse.getData());
            } else {
                order.status = OrderStatusEnum.交易失败;
                orderRepository.save(order);
                return failed(payResponse.getResMsg(), ERROR_CODE_PAY_CODE_FAILED);
            }
        } catch (Exception e) {
            order = orderRepository.findOne(orderId);
            if (OrderStatusEnum.已提交.equals(order.status)) {
                return succeed(payResponse.getData());
            } else if (OrderStatusEnum.交易失败.equals(order.status)) {
                return failed(payResponse.getResMsg(), ERROR_CODE_PAY_CODE_FAILED);
            } else {
                orderRepository.save(order);
                return succeed(payResponse.getData());
            }
        }
    }

    private RouterPayResponse getPayResponse(@RequestBody RouterRequest request) {
        RouterPayResponse payResponse = payService.pay(request);
        return payResponse;
    }


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseVO query(@RequestHeader Long userId, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, OrderStatusEnum status, Integer page) {
        Page<QuickPassOrderEntity> orderList;
        OrderStatisticsVO orderStatisticsVO;
        if (status == null) {
            orderList = orderRepository
                    .findByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndDelIsFalse(
                            userId, startDate, endDate, generatePage(page));
            orderStatisticsVO = orderRepository
                    .calculateByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndDelIsFalse(
                            userId, startDate, endDate);
        } else {
            orderList = orderRepository
                    .findByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(
                            userId, startDate, endDate, status, generatePage(page));
            orderStatisticsVO = orderRepository
                    .calculateByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(
                            userId, startDate, endDate, status);
        }
        return succeed(
                new OrderListVO(orderList.getContent().stream()
                        .map(orderEntity -> new OrderVO(orderEntity))
                        .collect(toList()), orderStatisticsVO));
    }

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public String notify(String bizOrderNumber, Integer tradeAmount) {
        return payService.notify(bizOrderNumber, tradeAmount);
    }

}
