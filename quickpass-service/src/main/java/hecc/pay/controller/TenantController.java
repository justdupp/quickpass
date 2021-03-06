package hecc.pay.controller;

import hecc.pay.client.TenantClient;
import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassCreditCardEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.jpa.QuickPassCreditCardRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import hecc.pay.service.TenantService;
import hecc.pay.vos.TenantInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xuhoujun
 * @description: 租户控制器
 * @date: Created In 下午9:13 on 2018/3/16.
 */
@RestController
@RequestMapping("/tenant/")
public class TenantController extends BaseController {

    @Autowired
    private TenantClient tenantClient;
    @Autowired
    private TenantService tenantService;

    @Autowired
    private QuickPassCreditCardRepository creditCardRepository;

    @Autowired
    private QuickPassTenantRepository tenantRepository;

    @ApiOperation("获取租户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseVO tenantInfo(@RequestHeader String platform, @RequestHeader Long tenantId) {
        TenantEntityVO tenantEntity = tenantClient.getTenant(tenantId);
        QuickPassTenantEntity tenant = tenantService.getQuickPassTenantEntity(platform, tenantEntity);
        List<QuickPassCreditCardEntity> creditCardList = creditCardRepository.findByTenantTenantIdAndDelIsFalse(tenantId);
        TenantEntityVO parentTenant = new TenantEntityVO();
        if (tenant.code == null) {
            TenantEntityVO parentVO = tenantClient.getTenant(tenantEntity.parentId);
            return failed(
                    String.format("您不能绑定此码，请联系您的上级租户  %s[%s]", parentVO.name, parentVO.mobile),
                    ERROR_CODE_VALID_FAILED);
        }
        if (tenant.code.tenant != null) {
            parentTenant = tenantClient.getTenant(tenant.code.tenant.tenantId);
        }
        return succeed(new TenantInfoVO(parentTenant, tenantEntity, tenant, creditCardList));
    }

    @ApiOperation("租户是否激活")
    @RequestMapping(value = "/isOpen", method = RequestMethod.GET)
    public ResponseVO isOpenTenant(@RequestHeader Long tenantId) {
        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
        return succeed(tenantEntity.active);
    }

    @ApiOperation("删除银行卡")
    @PostMapping("/del/bank/card")
    public ResponseVO delBankAccount(Long id, String bankAccount) {
        QuickPassCreditCardEntity creditCardEntity = creditCardRepository.findFirstByBankAccountAndDelIsFalse(bankAccount);
        if (creditCardEntity != null) {
            if (id.equals(creditCardEntity.id)) {
                creditCardRepository.delete(id);
                return succeed(null);
            }
        }
        return failed("删除失败或已删除,请稍后重试", ERROR_OPERATE_FAILED);
    }
}
