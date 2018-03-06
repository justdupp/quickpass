package hecc.pay.vos;

import hecc.pay.entity.QuickPassCodeEntity;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @Auther xuhoujun
 * @Description:  码返回对象
 * @Date: Created In 下午9:57 on 2018/3/6.
 */
public class CodeVO {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String createDate;
    public String code;
    public Boolean isDefault;
    public Long parentId;


    public CodeVO(QuickPassCodeEntity codeEntity, QuickPassCodeEntity defaultCode) {
        this.createDate = DateFormatUtils.format(codeEntity.createDate, DEFAULT_DATE_TIME_FORMAT);
        this.code = codeEntity.code;
        this.isDefault = defaultCode == null ? false : codeEntity.id.equals(defaultCode.id);
        this.parentId = codeEntity.tenant == null ? null : codeEntity.tenant.tenantId;
    }

    public CodeVO(QuickPassCodeEntity codeEntity) {
        this(codeEntity, null);
    }

}
