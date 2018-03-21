package hecc.pay.client.route;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:35 on 2018/3/21.
 */
public class RouterPayResponse extends RouterResponse {

    private String data;

    public RouterPayResponse(String resCode, String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
