package hecc.pay.controller;

import hecc.pay.entity.QuickPassCodeEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.jpa.QuickPassCodeRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import hecc.pay.service.CodeService;
import hecc.pay.vos.CodeVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * @Auther xuhoujun
 * @Description:  管理员api
 * @Date: Created In 下午10:54 on 2018/3/7.
 */
@RestController
@RequestMapping("/api/admin/quickpass/")
public class AdminController extends BaseController {
    @Autowired
    private QuickPassCodeRepository codeRepository;

    @Autowired
    private QuickPassTenantRepository tenantRepository;
    @Autowired
    private CodeService codeService;

    @ApiOperation("获取code列表")
    @GetMapping("/codes")
    public ResponseVO listCode(@RequestHeader Long tenantId) {
        return succeed(
                codeRepository.findByTenantIdAndDelIsFalse(tenantId)
                        .stream()
                        .map(c -> new CodeVO(c)).collect(Collectors.toList()));
    }

    @ApiOperation("创建码")
    @PostMapping("/code")
    public ResponseVO createCode(@RequestHeader String platform, @RequestHeader Long tenantId, boolean isDefault) {
        QuickPassTenantEntity tenant = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
        if (tenant.code == null) {
            return failed("您不能创建此分享码", ERROR_CODE_CREATE_CODE_FAILED);
        }
       codeService.createCode(platform, isDefault, tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId));
        return succeed(null);
    }

    @ApiOperation("更新码")
    @PostMapping("/code/{code}")
    public ResponseVO updateCode(@RequestHeader Long tenantId, @PathVariable("code") long codeId, boolean isDefault) {
        QuickPassCodeEntity code = codeRepository.findOne(codeId);
        code.isDefault = isDefault;
        codeRepository.saveAndFlush(code);
        if (isDefault) {
            QuickPassTenantEntity tenant = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
            tenant.defaultCode = code;
            tenantRepository.save(tenant);
        }
        return succeed(null);
    }

    @ApiOperation("绑定码")
    @PostMapping("/tenant/bind")
    public ResponseVO bindCode(long codeId, long childId) {
        QuickPassTenantEntity tenant = tenantRepository.findOneByTenantIdAndDelIsFalse(childId);
        QuickPassCodeEntity code = codeRepository.findOne(codeId);
        if (tenant.platform != null && code.platform != null) {
            if (StringUtils.equals(tenant.platform, code.platform)) {
                tenant.code = code;
                tenantRepository.save(tenant);
            }
        }
        return succeed(null);
    }
}
