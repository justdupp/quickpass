package hecc.pay.client.tenant;

/**
 * @Auther xuhoujun
 * @Description: 租户VO
 * @Date: Created In 下午4:13 on 2018/3/11.
 */
public class TenantEntityVO extends BaseEntityVO {

    /**
     * 关联上级租户
     */
    public Long parent_id;
    /**
     * 用户姓名
     */
    public String name;
    /**
     * 用户手机号
     */
    public String mobile;
    /**
     * 商户编码
     */
    public String merchantCode;
    /**
     * 收款卡号
     */
    public String recieverBankAccount;
    /**
     * 收款卡银行名称
     */
    public String recieverBankName;
    /**
     * 身份证号
     */
    public String idCard;
    /**
     * 银行卡是否已鉴权通过
     */
    public boolean bankCardHasPassed;
    /**
     * 身份证正面照
     */
    public String idCardFontPic;
    /**
     * 身份证反面照
     */
    public String idCardBackPic;
    /**
     * 登录姓名
     */
    public String userName;
    /**
     * 微信openid
     */
    public String openid;
    /**
     * 登录密码
     */
    public String password;
}
