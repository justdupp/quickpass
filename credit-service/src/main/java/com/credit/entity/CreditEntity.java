package com.credit.entity;

import com.credit.enumer.CreditBankTypeEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @author xuhoujun
 * @description: 银行卡对象
 * @date: Created In 下午10:33 on 2018/4/8.
 */
@Entity
@Table(name = "credit")
public class CreditEntity extends BaseEntity {

    /**
     * 卡名称
     */
    public String cardName;
    /**
     * 卡类型
     */
    @Enumerated(EnumType.STRING)
    public CreditBankTypeEnum type;
    /**
     * 卡简称
     */
    public String shortName;
    /**
     * 卡logo
     */
    public String bankLogo;
    /**
     * 卡全称
     */
    public String bankDetail;
    /**
     * 金额
     */
    public String money;
    /**
     * 是否显示
     */
    public Boolean isShow;
}
