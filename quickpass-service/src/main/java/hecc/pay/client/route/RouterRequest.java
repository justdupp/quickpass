package hecc.pay.client.route;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author xuhoujun
 * @description: 支付请求参数
 * @date: Created In 下午9:13 on 2018/3/21.
 */
public class RouterRequest {

    /**
     * 订单号
     */
    private Long orderId;
    /**
     * 卡号
     */
    private String accountNumber;
    /**
     * 预留手机号
     */
    private String telNo;
    /**
     * 卡的有效期
     */
    private String expired;
    /**
     * 交易金额
     */
    private Integer tradeAmount;
    /**
     * 订单号
     */
    private String bizOrderNumber;
    /**
     * 商户姓名
     */
    private String merchantName;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 结算卡
     */
    private String bankAccountNumber;
    /**
     * 银行预留手机号
     */
    private String bankAccountTel;
    /**
     * 结算银行名
     */
    private String bankName;

    /**
     * 执行手续费
     */
    private Integer withdrawFee;

    /**
     * 租户Id
     */
    private Long tenantId;
    /**
     * 支付平台
     */
    private String platform;

    public RouterRequest(Long orderId, String accountNumber, String telNo, Integer tradeAmount,
                         String bizOrderNumber, String merchantName, String idCard, String bankAccountNumber,
                         String bankAccountTel, String bankName, Integer withdrawFee, Long tenantId, String platform) {
        this.orderId = orderId;
        this.accountNumber = accountNumber;
        this.telNo = telNo;
        this.expired = "";
        this.tradeAmount = tradeAmount;
        this.bizOrderNumber = bizOrderNumber;
        this.merchantName = merchantName;
        this.idCard = idCard;
        this.bankAccountNumber = bankAccountNumber;
        this.bankAccountTel = bankAccountTel;
        this.bankName = bankName;
        this.withdrawFee = withdrawFee;
        this.tenantId = tenantId;
        this.platform = platform;
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public Integer getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Integer tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getBizOrderNumber() {
        return bizOrderNumber;
    }

    public void setBizOrderNumber(String bizOrderNumber) {
        this.bizOrderNumber = bizOrderNumber;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountTel() {
        return bankAccountTel;
    }

    public void setBankAccountTel(String bankAccountTel) {
        this.bankAccountTel = bankAccountTel;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(Integer withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
