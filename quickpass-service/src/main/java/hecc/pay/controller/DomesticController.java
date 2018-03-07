package hecc.pay.controller;

import hecc.pay.entity.QuickPassCodeEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.jpa.QuickPassCodeRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import hecc.pay.vos.CodeVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 上午12:07 on 2018/3/8.
 */
@RestController
@RequestMapping("/domestic/")
public class DomesticController extends BaseController {

    @Autowired
    private QuickPassCodeRepository codeRepository;
    @Autowired
    private QuickPassTenantRepository tenantRepository;

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

}
