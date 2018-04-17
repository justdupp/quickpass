package hecc.pay.client.route;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author xuhoujun
 * @description: 支付响应
 * @date: Created In 下午9:28 on 2018/3/21.
 */
public class RouterResponse {

    /**
     * 响应code
     */
    protected String resCode;

    /**
     * 响应msg
     */
    protected String resMsg;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
