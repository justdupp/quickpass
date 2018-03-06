package hecc.pay.controller;

import hecc.pay.entity.QuickPassCodeEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.jpa.QuickPassCodeRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import hecc.pay.service.CodeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther xuhoujun
 * @Description: 闪付 -- 码控制器
 * @Date: Created In 下午11:55 on 2018/3/2.
 */
@RestController
@RequestMapping("/api/code")
public class CodeController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(CodeController.class);

    @Autowired
    private CodeService codeService;
    @Autowired
    private QuickPassTenantRepository tenantRepository;

    @ApiOperation("新增码操作")
    @RequestMapping(value = "/createCode", method = RequestMethod.POST)
    public ResponseVO createCode(@RequestHeader String platform, @RequestHeader Long tenantId) {
        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
        if (tenantEntity.code == null) {
            return failed("您不能创建此码", ERROR_CODE_CREATE_CODE_FAILED);
        }
        QuickPassCodeEntity code = codeService.createCode(platform, null, tenantEntity);
        return succeed(code.code);
    }

}
