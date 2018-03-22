package hecc.pay.controller;

import hecc.pay.entity.QuickPassCodeEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawStatusEnum;
import hecc.pay.enumer.WithdrawTypeEnum;
import hecc.pay.jpa.QuickPassCodeRepository;
import hecc.pay.jpa.QuickPassRemittanceRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import hecc.pay.jpa.QuickPassWithdrawRepository;
import hecc.pay.service.CodeService;
import hecc.pay.vos.CodeVO;
import hecc.pay.vos.WithdrawEntityVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther xuhoujun
 * @Description: 内部控制器
 * @Date: Created In 上午12:07 on 2018/3/8.
 */
@RestController
@RequestMapping("/domestic/")
public class DomesticController extends BaseController {

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
            defaultCode.code = "quickpass" + DigestUtils.sha1Hex(defaultCode.id + "");
            codeRepository.save(defaultCode);
            return new CodeVO(defaultCode);
        }
    }

    @ApiOperation("获取默认码")
    @GetMapping("/code/fetchDefault")
    public List<CodeVO> fetchDefaultCodes() {
        return codeRepository.findByIsDefaultIsTrueAndDelIsFalse()
                .stream()
                .map(c -> new CodeVO(c)).collect(Collectors.toList());
    }

    @ApiOperation("设置top code")
    @PostMapping("/code/top")
    public void setTopCode(Long tenantId) {
        QuickPassCodeEntity topCode = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId).code;
        topCode.code = "quickpass " + DigestUtils.sha1Hex(topCode.id + "");
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
        return successed(code.code);
    }

    @ApiOperation("更新code租户")
    @RequestMapping(value = "/code/{code}/owner", method = RequestMethod.POST)
    public ResponseVO modifyCodeOwner(Long newOwnerId, @PathVariable("code") String code) {
        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(newOwnerId);
        QuickPassCodeEntity codeEntity = codeRepository.findOneByCodeAndDelIsFalse(code);
        codeEntity.tenant = tenantEntity;
        codeRepository.save(codeEntity);
        return successed(code);
    }

    @ApiOperation("根据租户获取code")
    @GetMapping("/tenant/{tenantId}/code")
    public CodeVO getCostCodeByTenantId(@PathVariable("userId") Long tenantId) {
        QuickPassTenantEntity tenant = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
        return tenant == null ? new CodeVO() : new CodeVO(tenant.code);
    }

    @ApiOperation("根据租户获取code列表")
    @GetMapping("/tenant/{tenantId}/codes")
    public List<CodeVO> getCodeListByUserId(@PathVariable("tenantId") Long tenantId) {
        return codeRepository.findByTenantIdAndDelIsFalse(tenantId)
                .stream().map(co -> new CodeVO(co)).collect(Collectors.toList());
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
        if ("提现成功".equals(status)) {
            withdrawEntityList = withdrawRepository
                    .findByTypeAndCreateDateGreaterThanEqualAndCreateDateLessAndStatus(
                            "快捷支付".equals(type) ? WithdrawTypeEnum.快捷支付 : WithdrawTypeEnum.拉新, startDate, endDate,
                            WithdrawStatusEnum.提现成功);
        } else if ("提现失败".equals(status)) {
            withdrawEntityList = withdrawRepository
                    .findByTypeAndCreateDateGreaterThanEqualAndCreateDateLessAndStatus(
                            "快捷支付".equals(type) ? WithdrawTypeEnum.快捷支付 : WithdrawTypeEnum.拉新, startDate, endDate,
                            WithdrawStatusEnum.提现失败);
        } else if ("已提交".equals(status)) {
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
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/update/profit", method = RequestMethod.POST)
    public ResponseVO updateProfit(Long withdrawId, String message, boolean suc) {
        if (suc) {
            withdrawRepository.modifyByQuickPassWithdrawEntityId(WithdrawStatusEnum.提现成功, null, withdrawId);
        } else {
            withdrawRepository.modifyByQuickPassWithdrawEntityId(WithdrawStatusEnum.提现失败, message, withdrawId);
        }
        return successed(null);
    }


}
