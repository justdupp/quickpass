package hecc.pay.client;

import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.service.TenantClientHystric;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 租户feign
 * @Date: Created In 下午4:10 on 2018/3/11.
 */
@FeignClient(value = "tenant-service" ,fallback = TenantClientHystric.class)
public interface TenantClient {

    /**
     * 获取上级租户
     * @param tenantId  租户id
     * @return 租户VO
     */
    @RequestMapping(value = "/tenant/getParent", method = RequestMethod.GET)
    TenantEntityVO getParent(@RequestParam("tenantId") Long tenantId);

    /**
     * 获取租户信息
     * @param tenantId 租户id
     * @return 租户VO
     */
    @RequestMapping(value = "/tenant/{tenantId}", method = RequestMethod.GET)
    TenantEntityVO getTenant(@PathVariable("tenantId") Long tenantId);

    /**
     * 更新租户
     * @param tenant 租户信息
     */
    @RequestMapping(value = "/tenant", method = RequestMethod.POST)
    void updateTenant(@RequestBody TenantEntityVO tenant);

    /**
     * 获取子租户
     * @param tenantId 租户id
     * @return 租户列表
     */
    @RequestMapping(value = "/tenant/{tenantId}/children", method = RequestMethod.GET)
    List<TenantEntityVO> getChildren(@PathVariable("tenantId") Long tenantId);

    /**
     * 是否顶级租户
     * @param tenantId 租户id
     * @return
     */
    @RequestMapping(value = "/tenant/isTopTenant", method = RequestMethod.GET)
    boolean getIsTopTenant(@RequestParam("tenantId") Long tenantId);

}
