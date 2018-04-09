package hecc.pay.controller;

import hecc.pay.client.TenantClient;
import hecc.pay.entity.*;
import hecc.pay.enumer.OrderStatusEnum;
import hecc.pay.enumer.RemittanceStatusEnum;
import hecc.pay.enumer.WithdrawStatusEnum;
import hecc.pay.enumer.WithdrawTypeEnum;
import hecc.pay.jpa.*;
import hecc.pay.service.CodeService;
import hecc.pay.service.PayService;
import hecc.pay.vos.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static hecc.pay.util.MoneyUtil.toMoney;
import static java.util.stream.Collectors.toList;

/**
 * @Auther xuhoujun
 * @Description: 内部控制器
 * @Date: Created In 上午12:07 on 2018/3/8.
 */
@RestController
@RequestMapping("/domestic/")
public class DomesticController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(DomesticController.class);
    @Autowired
    private QuickPassCodeRepository codeRepository;
    @Autowired
    private QuickPassTenantRepository tenantRepository;
    @Autowired
    private CodeService codeService;
    @Autowired
    private QuickPassWithdrawRepository withdrawRepository;
    @Autowired
    private QuickPassRemittanceRepository remittanceRepository;
    @Autowired
    private TenantClient tenantClient;
    @Autowired
    private QuickPassOrderRepository orderRepository;
    @Autowired
    private PayService payService;

    @ApiOperation("绑码")
    @PostMapping("/code/bind")
    public CodeVO bindCode(String code, Long currentTenantId) {
        QuickPassCodeEntity codeEntity = codeRepository.findOneByCodeAndDelIsFalse(code);
        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(currentTenantId);
        if (tenantEntity == null) {
            tenantEntity = new QuickPassTenantEntity();
        }
        tenantEntity.tenantId = currentTenantId;
        tenantEntity.platform = codeEntity.platform;
        tenantEntity.code = codeEntity;
        tenantEntity.active = true;
        tenantRepository.save(tenantEntity);
        return new CodeVO(codeEntity);
    }

    @ApiOperation("设置默认码")
    @PostMapping("/code/default")
    public CodeVO setDefaultCode(String platform) {
        QuickPassCodeEntity defaultCode = codeRepository.findFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse(platform);
        if (defaultCode != null) {
            return null;
        } else {
            defaultCode = new QuickPassCodeEntity();
            defaultCode.platform = platform;
            defaultCode.isDefault = true;
            codeRepository.saveAndFlush(defaultCode);
            defaultCode.code = "quickPass " + DigestUtils.sha1Hex(defaultCode.id + "");
            codeRepository.save(defaultCode);
            return new CodeVO(defaultCode);
        }
    }

    @ApiOperation("获取默认码")
    @GetMapping("/code/fetchDefault")
    public List<CodeVO> fetchDefaultCodes() {
        return codeRepository.findByIsDefaultIsTrueAndDelIsFalse()
                .stream()
                .map(c -> new CodeVO(c)).collect(toList());
    }

    @ApiOperation("设置top code")
    @PostMapping("/code/top")
    public void setTopCode(Long tenantId) {
        QuickPassCodeEntity topCode = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId).code;
        topCode.code = "quickPass " + DigestUtils.sha1Hex(topCode.id + "");
        codeRepository.save(topCode);
    }

    @ApiOperation("设置默认租户")
    @PostMapping("/tenant/default")
    public void setDefaultTenant(Long tenantId) {
        QuickPassTenantEntity tenant = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
        QuickPassCodeEntity defaultCode = codeRepository.findFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse(tenant.platform);
        defaultCode.tenant = tenant;
        codeRepository.saveAndFlush(defaultCode);
        QuickPassCodeEntity topCode = new QuickPassCodeEntity();
        topCode.platform = tenant.platform;
        codeRepository.saveAndFlush(topCode);
        tenant.code = topCode;
        tenantRepository.save(tenant);
    }

    @ApiOperation("创建租户")
    @RequestMapping(value = "/code", method = RequestMethod.POST)
    public ResponseVO createCode(Long tenantId) {
        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
        QuickPassCodeEntity code = codeService.createCode(tenantEntity.platform, null, tenantEntity);
        return succeed(code.code);
    }

    @ApiOperation("更新code租户")
    @RequestMapping(value = "/code/{code}/owner", method = RequestMethod.POST)
    public ResponseVO modifyCodeOwner(Long newOwnerId, @PathVariable("code") String code) {
        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(newOwnerId);
        QuickPassCodeEntity codeEntity = codeRepository.findOneByCodeAndDelIsFalse(code);
        codeEntity.tenant = tenantEntity;
        codeRepository.save(codeEntity);
        return succeed(code);
    }

    @ApiOperation("根据租户获取code")
    @GetMapping("/tenant/{tenantId}/code")
    public CodeVO getCodeByTenantId(@PathVariable("userId") Long tenantId) {
        QuickPassTenantEntity tenant = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
        return tenant == null ? new CodeVO() : new CodeVO(tenant.code);
    }

    @ApiOperation("根据租户获取code列表")
    @GetMapping("/tenant/{tenantId}/codes")
    public List<CodeVO> getCodeListByTenantId(@PathVariable("tenantId") Long tenantId) {
        return codeRepository.findByTenantTenantIdAndDelIsFalse(tenantId)
                .stream().map(co -> new CodeVO(co)).collect(toList());
    }

    @ApiOperation("是否当前租户")
    @GetMapping("/tenant/{tenantId}/isNormalTenant")
    public boolean isCurrentTenantUseDefaultCode(@PathVariable("tenantId") Long tenantId) {
        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
        if (tenantEntity == null) {
            return true;
        } else {
            QuickPassCodeEntity defaultCode = codeRepository.findFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse(tenantEntity.platform);
            return tenantEntity.code.id.equals(defaultCode.id);
        }
    }

    @RequestMapping(value = "/profits", method = RequestMethod.GET)
    public List<WithdrawEntityVO> profitList(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                             @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                             @RequestParam("status") String status, @RequestParam("type") String type) {
        List<QuickPassWithdrawEntity> withdrawEntityList;
        if (WithdrawStatusEnum.提现成功.equals(status)) {
            withdrawEntityList = withdrawRepository
                    .findByTypeAndCreateDateGreaterThanEqualAndCreateDateLessAndStatus(
                            "快捷支付".equals(type) ? WithdrawTypeEnum.快捷支付 : WithdrawTypeEnum.拉新, startDate, endDate,
                            WithdrawStatusEnum.提现成功);
        } else if (WithdrawStatusEnum.提现失败.equals(status)) {
            withdrawEntityList = withdrawRepository
                    .findByTypeAndCreateDateGreaterThanEqualAndCreateDateLessAndStatus(
                            "快捷支付".equals(type) ? WithdrawTypeEnum.快捷支付 : WithdrawTypeEnum.拉新, startDate, endDate,
                            WithdrawStatusEnum.提现失败);
        } else if (WithdrawStatusEnum.已提交.equals(status)) {
            withdrawEntityList = withdrawRepository
                    .findByTypeAndCreateDateGreaterThanEqualAndCreateDateLessAndStatus(
                            "快捷支付".equals(type) ? WithdrawTypeEnum.快捷支付 : WithdrawTypeEnum.拉新, startDate, endDate,
                            WithdrawStatusEnum.已提交);
        } else {
            withdrawEntityList = withdrawRepository
                    .findByTypeAndCreateDateGreaterThanEqualAndCreateDateLess(
                            "快捷支付".equals(type) ? WithdrawTypeEnum.快捷支付 : WithdrawTypeEnum.拉新, startDate, endDate);
        }
        return withdrawEntityList.stream()
                .map(c -> new WithdrawEntityVO(c))
                .collect(toList());
    }

    @RequestMapping(value = "/update/profit", method = RequestMethod.POST)
    public ResponseVO updateProfit(Long withdrawId, String message, boolean suc) {
        if (suc) {
            withdrawRepository.modifyByQuickPassWithdrawEntityId(WithdrawStatusEnum.提现成功, null, withdrawId);
        } else {
            withdrawRepository.modifyByQuickPassWithdrawEntityId(WithdrawStatusEnum.提现失败, message, withdrawId);
        }
        return succeed(null);
    }

    @RequestMapping(value = "/cashBack", method = RequestMethod.GET)
    public Collection<RemittanceVO> cashBack(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam("status") String status) {
        List<QuickPassRemittanceEntity> remittanceEntityList;
        if (RemittanceStatusEnum.打款成功.equals(status)) {
            remittanceEntityList = remittanceRepository.findByCreateDateGreaterThanEqualAndCreateDateLessAndStatus(startDate, endDate,
                            RemittanceStatusEnum.打款成功);
        } else if (RemittanceStatusEnum.打款失败.equals(status)) {
            remittanceEntityList = remittanceRepository.findByCreateDateGreaterThanEqualAndCreateDateLessAndStatus(startDate, endDate,
                            RemittanceStatusEnum.打款失败);
        } else if (RemittanceStatusEnum.未打款.equals(status)) {
            remittanceEntityList = remittanceRepository.findByCreateDateGreaterThanEqualAndCreateDateLessAndStatus(startDate, endDate,
                            RemittanceStatusEnum.未打款);
        } else {
            remittanceEntityList = remittanceRepository.findByCreateDateGreaterThanEqualAndCreateDateLess(startDate, endDate);
        }
        return remittanceEntityList.stream()
                .map(c -> new RemittanceVO(tenantClient.getTenant(c.order.tenant.tenantId), c))
                .collect(toList());
    }

    @RequestMapping(value = "/update/cashBack", method = RequestMethod.POST)
    public ResponseVO updateCashBack(Long remittanceId, String message, boolean suc) {
        if (suc) {
            remittanceRepository.modifyByQuickPassRemittanceEntityId(RemittanceStatusEnum.打款成功, null, remittanceId);
        } else {
            remittanceRepository.modifyByQuickPassRemittanceEntityId(RemittanceStatusEnum.打款失败, message, remittanceId);
        }
        return succeed(null);
    }

    @RequestMapping(value = "/update/develop", method = RequestMethod.POST)
    public ResponseVO updateDevelop(Long withdrawId, String message, boolean suc) {
        if (suc) {
            withdrawRepository.modifyByQuickPassWithdrawEntityId(WithdrawStatusEnum.提现成功, null, withdrawId);
        } else {
            withdrawRepository.modifyByQuickPassWithdrawEntityId(WithdrawStatusEnum.提现失败, message, withdrawId);
        }
        return succeed(null);
    }

    @GetMapping("/find/order")
    public OrderVO findOrder(String id) {
        QuickPassOrderEntity orderEntity = orderRepository.findOne(Long.parseLong(id));
        return new OrderVO(orderEntity);
    }

    @RequestMapping(value = "/query/order", method = RequestMethod.GET)
    public void queryOrder(@RequestParam("id") Long id) {
        QuickPassOrderEntity order = orderRepository.findOne(id);
        if (OrderStatusEnum.交易成功.equals(order.status) || OrderStatusEnum.交易失败.equals(order.status)) {
            return;
        }
        RabbitMqMessageVO mqMessageVO = payService.queryOrder(id);
        logger.info("查询返回信息orderId=" + mqMessageVO.orderId + "状态返回status" + mqMessageVO.status);
        if (mqMessageVO != null) {
            QuickPassOrderEntity orderEntity = orderRepository.findOne(id);
            if (OrderStatusEnum.已提交.equals(mqMessageVO.status + "")) {
                orderEntity.status = OrderStatusEnum.已提交;
            } else if (OrderStatusEnum.交易成功.equals(mqMessageVO.status + "")) {
                orderEntity.status = OrderStatusEnum.交易成功;
            } else {
                orderEntity.status = OrderStatusEnum.交易失败;
            }
            orderEntity.modifyDate = mqMessageVO.finishTime;
            orderRepository.saveAndFlush(orderEntity);
            if (OrderStatusEnum.交易成功.equals(mqMessageVO.status + "")) {
                payService.setAsyncTasks(orderEntity.id);
            }
        }
    }

    @RequestMapping(value = "/find/orderList", method = RequestMethod.GET)
    public List<OrderVO> findOrderList(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        List<QuickPassOrderEntity> orderEntityList = orderRepository
                .findByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(
                        startDate, endDate, OrderStatusEnum.已提交);
        return orderEntityList.stream().map(c -> new OrderVO(c))
                .collect(toList());
    }

    @RequestMapping(value = "/update/order", method = RequestMethod.POST)
    public void modifyOrderStatusFail(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<QuickPassOrderEntity> orderEntityList = orderRepository
                .findByCreateDateGreaterThanEqualAndCreateDateLessAndStatusAndStatusAndDelIsFalse(
                        startDate, endDate, OrderStatusEnum.预下单, OrderStatusEnum.已提交);
        logger.info("查询到" + DateFormatUtils.format(startDate, "yyyy-MM-dd") + "到" + DateFormatUtils
                .format(endDate, "yyyy-MM-dd") + "时间段预下单和已提交的订单条数为size=" + orderEntityList.size());
        if (orderEntityList != null && !orderEntityList.isEmpty()) {
            for (QuickPassOrderEntity order : orderEntityList) {
                QuickPassOrderEntity orderEntity = orderRepository.findOne(order.id);
                orderEntity.status = OrderStatusEnum.交易失败;
                orderEntity.modifyDate = new Date();
                orderRepository.saveAndFlush(orderEntity);
            }
        }
    }

    @RequestMapping(value = "/update/openid", method = RequestMethod.POST)
    public void updateOpenid(Long id, String platform) {
        try {
            QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(id);
            if (tenantEntity == null) {
            } else {
                tenantEntity.platform = platform;
                tenantRepository.save(tenantEntity);
                List<QuickPassCodeEntity> codeEntityList = codeRepository.findByTenantTenantIdAndDelIsFalse(id);
                for (QuickPassCodeEntity codeEntity : codeEntityList) {
                    codeEntity.platform = platform;
                    codeRepository.save(codeEntity);
                }
            }
        } catch (Exception e) {
            logger.error("修改商户platform异常" + e);
            throw new RuntimeException();
        }
    }

    @RequestMapping(value = "/code/{code}/info", method = RequestMethod.POST)
    public ResponseVO modifyCode(@PathVariable("code") String code,  String withdrawFee) {
        QuickPassCodeEntity codeEntity = codeRepository.findOneByCodeAndDelIsFalse(code);
        codeEntity.withdrawFee = toMoney(withdrawFee);
        codeRepository.save(codeEntity);
        return succeed(code);
    }


}
