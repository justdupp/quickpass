package hecc.pay.controller;

import hecc.pay.entity.QuickPassCodeEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.jpa.QuickPassCodeRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import hecc.pay.service.CodeService;
import hecc.pay.vos.CodeVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private QuickPassCodeRepository codeRepository;

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

    @ApiOperation("获取码列表")
    @RequestMapping(value = "/codeList", method = RequestMethod.GET)
    public ResponseVO codeList(@RequestHeader Long tenantId){
        List<QuickPassCodeEntity> codeList = codeRepository.findByTenantIdAndDelIsFalse(tenantId);
        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(tenantId);
        return succeed(codeList.stream().filter(c -> BooleanUtils.isNotTrue(c.isDefault))
                .map(c -> new CodeVO(c, tenantEntity.defaultCode))
                .collect(Collectors.toList()));
    }


    @ApiOperation("删除码操作")
    @PostMapping("/code/{code}/del")
    public ResponseVO delCode(@PathVariable("code") String code) {
        QuickPassCodeEntity codeEntity = codeRepository.findOneByCodeAndDelIsFalse(code);
        if (codeEntity == null) {
            return failed("码不存在或已删除", 1);
        }
        if (tenantRepository.countByCodeId(codeEntity.id) > 0) {
            return failed("您的码已经有下级在使用,不能删除", 1);
        } else {
            codeEntity.del = true;
            codeRepository.save(codeEntity);
            return succeed(null);
        }
    }

}
