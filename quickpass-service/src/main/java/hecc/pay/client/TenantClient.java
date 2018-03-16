package hecc.pay.client;

import hecc.pay.client.tenant.TenantEntityVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午4:10 on 2018/3/11.
 */
@FeignClient("tenant-service")
public interface TenantClient {

    @RequestMapping(value = "/tenant/getParent", method = RequestMethod.GET)
    TenantEntityVO getParent(@RequestParam("tenantId") Long tenantId);

    @RequestMapping(value = "/tenant/{tenantId}", method = RequestMethod.GET)
    TenantEntityVO getTenant(@PathVariable("tenantId") Long tenantId);

    @RequestMapping(value = "/tenant", method = RequestMethod.POST)
    void updateTenant(@RequestBody TenantEntityVO tenant);

    @RequestMapping(value = "/tenant/{tenantId}/children", method = RequestMethod.GET)
    List<TenantEntityVO> getChildren(@PathVariable("tenantId") Long tenantId);

    @RequestMapping(value = "/tenant/isTopTenant", method = RequestMethod.GET)
    boolean getIsTopTenant(@RequestParam("tenantId") Long tenantId);

}
