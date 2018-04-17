package hecc.pay.vos;

import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassRemittanceEntity;
import hecc.pay.jpa.QuickPassRemittanceRepository;

import java.text.SimpleDateFormat;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @author xuhoujun
 * @description: 打款VO
 * @date: Created In 上午12:32 on 2018/3/23.
 */
public class RemittanceVO {

    public Long id;
    public String receiverUserName;
    public String idCard;
    public String receiverBankAccount;
    public String receiverBankMobile;
    public String receiverBankName;
    public String createDate;
    public String modifyDate;
    public String status;
    public String message;
    public String orderFee;
    public Long orderId;


    public RemittanceVO(TenantEntityVO tenant, QuickPassRemittanceEntity remittanceEntity) {
        this.receiverUserName = tenant.name;
        this.receiverBankAccount = tenant.receiverBankAccount;
        this.receiverBankMobile = tenant.mobile;
        this.idCard = tenant.idCard;
        this.receiverBankName = tenant.receiverBankName;
        this.createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm-dd")
                .format(remittanceEntity.createDate);
        this.modifyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm-dd")
                .format(remittanceEntity.modifyDate);
        this.id = remittanceEntity.id;
        this.message = remittanceEntity.message;
        this.status = remittanceEntity.status + "";
        this.orderFee = toMoney(remittanceEntity.orderFee != null ? remittanceEntity.orderFee : 0);
        this.orderId = remittanceEntity.order.id;
    }

}
