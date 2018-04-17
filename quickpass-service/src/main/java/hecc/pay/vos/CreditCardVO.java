package hecc.pay.vos;

import hecc.pay.entity.QuickPassCreditCardEntity;

/**
 * @author xuhoujun
 * @description: 银行卡VO
 * @date: Created In 下午9:27 on 2018/3/16.
 */
public class CreditCardVO {
    public String bankAccountNumber;
    public String bankName;
    public String mobile;
    public String bankId;

    public CreditCardVO(QuickPassCreditCardEntity creditCard) {
        this.bankAccountNumber = creditCard.bankAccount;
        this.bankName = creditCard.bankName;
        this.mobile = creditCard.mobile;
        this.bankName = creditCard.bankName;
        this.bankId = creditCard.id + "";
    }
}
