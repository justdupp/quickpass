package hecc.pay.controller;

import hecc.pay.client.TenantClient;
import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassCreditCardEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.jpa.QuickPassCreditCardRepository;
import hecc.pay.service.TenantService;
import hecc.pay.vos.TenantInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:13 on 2018/3/16.
 */
public class TenantController extends BaseController {

    @Autowired
    private TenantClient tenantClient;
    @Autowired
    private TenantService tenantService;

    @Autowired
    private QuickPassCreditCardRepository creditCardRepository;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseVO merchantInformation(@RequestHeader String platform, @RequestHeader Long tenantId) {
        TenantEntityVO tenantEntity = tenantClient.getTenant(tenantId);
        QuickPassTenantEntity tenant = tenantService.getQuickPassTenantEntity(platform, tenantEntity);
        List<QuickPassCreditCardEntity> creditCardList = creditCardRepository.findByTenantTenantIdAndDelIsFalse(tenantId);
        TenantEntityVO parentUser = new TenantEntityVO();
        if (tenant.code == null) {
            TenantEntityVO parentVO = tenantClient.getTenant(tenantEntity.parent_id);
            return failed(
                    String.format("您不能绑定此码，请联系您的上级租户  %s[%s]", parentVO.name, parentVO.mobile),
                    ERROR_CODE_VALID_FAILED);
        }
        if (tenant.code.tenant != null) {
            parentUser = tenantClient.getTenant(tenant.code.tenant.tenantId);
        }
        return successed(new TenantInfoVO(parentUser, tenantEntity, tenant, creditCardList));
    }
}
