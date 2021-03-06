package hecc.pay.service;

import hecc.pay.client.TenantClient;
import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.jpa.QuickPassCodeRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuhoujun
 * @description: 租户服务
 * @date: Created In 下午8:37 on 2018/3/16.
 */
@Service
public class TenantService {

    @Autowired
    private QuickPassTenantRepository tenantRepository;

    @Autowired
    private QuickPassCodeRepository codeRepository;

    @Autowired
    private TenantClient tenantClient;

    public QuickPassTenantEntity getQuickPassTenantEntity(String platform, TenantEntityVO tenantEntity) {
        QuickPassTenantEntity tenant = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantEntity.id);
        if (tenantEntity.parentId == null) {
            if (tenant == null) {
                tenant = new QuickPassTenantEntity();
                tenant.tenantId = tenantEntity.id;
                tenant.platform = platform;
                tenant.code = codeRepository.findFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse(platform);
                tenant.active = true;
                tenantRepository.saveAndFlush(tenant);
                TenantEntityVO tenantEntityVO = new TenantEntityVO();
                tenantEntityVO.parentId = tenant.code.tenant == null ? null : tenant.code.tenant.tenantId;
                tenantEntityVO.id = tenant.tenantId;
                tenantClient.updateTenant(tenantEntityVO);
            } else if (tenant.code == null) {
                tenant.code = codeRepository.findFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse(platform);
                tenantRepository.saveAndFlush(tenant);
            }
        } else {
            QuickPassTenantEntity parent = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantEntity.parentId);
            if (parent == null) {
                return new QuickPassTenantEntity();
            } else {
                if (tenant == null) {
                    tenant = new QuickPassTenantEntity();
                    tenant.tenantId = tenantEntity.id;
                    tenant.platform = platform;
                    tenant.code = tenantClient.getIsTopTenant(tenantEntity.parentId) == true ? codeRepository
                            .findFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse(platform)
                            : parent.defaultCode == null ? codeRepository
                            .findFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse(platform)
                            : parent.defaultCode;
                    tenant.active = true;
                    tenantRepository.saveAndFlush(tenant);
                }
            }
        }
        return tenant;
    }
}
