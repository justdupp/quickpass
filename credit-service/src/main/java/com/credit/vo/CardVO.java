package com.credit.vo;

import com.credit.entity.CreditEntity;
import com.credit.enumer.CreditBankTypeEnum;

/**
 * @Auther xuhoujun
 * @Description: 银行卡VO
 * @Date: Created In 下午9:15 on 2018/4/9.
 */
public class CardVO {

    /**
     * id
     */
    public Long id;
    /**
     * 银行名称
     */
    public String name;
    /**
     * 卡类型
     */
    public CreditBankTypeEnum type;
    /**
     * 银行简称
     */
    public String shortName;
    /**
     * 银行logo
     */
    public String bankLogo;
    /**
     * 银行全称
     */
    public String bankDetail;
    /**
     * 金额
     */
    public String money;
    /**
     * 是否显示
     */
    public Boolean show;

    public CardVO(CreditEntity entity) {
        this.id = entity.id;
        this.name = entity.cardName;
        this.type = entity.type;
        this.shortName = entity.shortName;
        this.bankLogo = entity.bankLogo;
        this.bankDetail = entity.bankDetail;
        this.money = entity.money;
        this.show = entity.isShow;
    }
}
