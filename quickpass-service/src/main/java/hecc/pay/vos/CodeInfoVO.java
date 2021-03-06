package hecc.pay.vos;

import hecc.pay.entity.QuickPassCodeEntity;

/**
 * @author xuhoujun
 * @description: 码信息VO
 * @date: Created In 下午8:50 on 2018/3/16.
 */
public class CodeInfoVO {
    public CodeVO currentCode;
    public CodeVO newCode;

    public CodeInfoVO(QuickPassCodeEntity currentCode, QuickPassCodeEntity newCode) {
        this.newCode = new CodeVO(newCode);
        this.currentCode = new CodeVO(currentCode);
    }
}
