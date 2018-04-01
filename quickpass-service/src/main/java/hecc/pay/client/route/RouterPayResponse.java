package hecc.pay.client.route;

/**
 * @Auther xuhoujun
 * @Description: 路由支付响应
 * @Date: Created In 下午9:35 on 2018/3/21.
 */
public class RouterPayResponse extends RouterResponse {

    /**
     * 响应data
     */
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
