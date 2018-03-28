package hecc.pay.service;

import hecc.pay.client.TenantClient;
import hecc.pay.client.tenant.TenantEntityVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 上午12:40 on 2018/3/29.
 */
@Component
public class TenantClientHystric implements TenantClient {

    @Override
    public String sayHiFromClientOne(String name) {
        return "error"+ name;
    }

    @Override
    public TenantEntityVO getParent(Long tenantId) {
        TenantEntityVO vo = new TenantEntityVO();
        vo.userName = "sorry";
        return vo;
    }

    @Override
    public TenantEntityVO getTenant(Long tenantId) {
        return null;
    }

    @Override
    public void updateTenant(TenantEntityVO tenant) {

    }

    @Override
    public List<TenantEntityVO> getChildren(Long tenantId) {
        return null;
    }

    @Override
    public boolean getIsTopTenant(Long tenantId) {
        return false;
    }
}
