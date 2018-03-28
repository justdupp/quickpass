package hecc.pay.service;

import hecc.pay.entity.QuickPassCodeEntity;
import hecc.pay.entity.QuickPassTenantEntity;
import hecc.pay.jpa.QuickPassCodeRepository;
import hecc.pay.jpa.QuickPassTenantRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static hecc.pay.util.IDUtil.generateID;

/**
 * @Auther xuhoujun
 * @Description: 码服务
 * @Date: Created In 下午9:28 on 2018/3/6.
 */
@Service
public class CodeService {
    @Autowired
    private QuickPassCodeRepository codeRepository;

    @Autowired
    private QuickPassTenantRepository userRepository;

    public QuickPassCodeEntity createCode(String platform, Boolean isDefault, QuickPassTenantEntity tenant) {
        QuickPassCodeEntity code = new QuickPassCodeEntity();
        code.platform = platform;
        code.tenant = tenant;
        codeRepository.saveAndFlush(code);
        code.code = "quickPass" + generateID();
        codeRepository.save(code);
        if (BooleanUtils.isTrue(isDefault)) {
            tenant.defaultCode = code;
            userRepository.save(tenant);
        }
        return code;
    }
}
