package hecc.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import hecc.pay.client.route.RouterPayResponse;
import hecc.pay.client.route.RouterRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static hecc.pay.util.MD5Util.MD5;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午10:53 on 2018/3/21.
 */
@Service
public class PayService {

    private Logger logger = LoggerFactory.getLogger(PayService.class);
    public final static int CONNECT_TIMEOUT = 10;
    public final static int READ_TIMEOUT = 10;
    public final static int WRITE_TIMEOUT = 10;

    private String version;

    private String key;

    private String serverUrl;

    private String mid;

    public RouterPayResponse pay(RouterRequest request) {
        JSONObject obj = new JSONObject();
        String method = "quickPass";
        obj.put("encryptId", mid);                                        //默认的字符串mid
        obj.put("version", version);                                      //默认值为1
        obj.put("method", method);                                        //请求的支付方式
        obj.put("dateStamp", Calendar.getInstance().getTimeInMillis());   //时间戳
        obj.put("mid", mid);                                              //mid
        obj.put("accountNumber", request.getAccountNumber());             //卡号
        obj.put("bankAccountTel", request.getBankAccountTel());           //预留手机号
        obj.put("expired", request.getExpired());                         //卡的有效期
        obj.put("tradeAmount", new BigDecimal(request.getTradeAmount())
                .divide(new BigDecimal(100)).doubleValue());         //交易金额
        obj.put("bizOrderNumber", request.getBizOrderNumber());           //订单号
        obj.put("merchantName", request.getMerchantName());               //商户姓名
        obj.put("idCard", request.getIdCard());                           //身份证号
        obj.put("bankAccountNumber", request.getBankAccountNumber());     //结算卡
        obj.put("bankAccountTel", request.getBankAccountTel());           //银行预留手机号
        obj.put("bankName", request.getBankName());                       //结算银行名称

        JSONObject content = new JSONObject();
        content.put("content", JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
        content.put("key", key);
        String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
        String sign = MD5(signStr);
        content.remove("key");
        content.put("sign", sign);
        logger.info("支付前，请求参数=" + JSON.toJSONString(content));
        Builder okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JSON.toJSONString(content));
        Request req = new Request.Builder().addHeader("Content-Type", "text/plain;charset=UTF-8")
                .addHeader("dataType", "json").url(serverUrl + method).post(requestBody).build();
        RouterPayResponse res = new RouterPayResponse();
        try {
            Response response = okHttpClient.build().newCall(req).execute();
            String result = response.body().string();
            logger.info("接收到支付的返回res --" + result);
            Map<String, String> resMap = new LinkedHashMap<>();
            resMap.put("key", key);
            resMap.put("result", JSONObject.parseObject(result).getString("result"));
            signStr = JSON.toJSONString(resMap, SerializerFeature.WriteMapNullValue);
            sign = MD5(signStr);
            /**
             * 验签
             */
            if (!sign.equals(JSONObject.parseObject(result).getString("sign"))) {
                res.setResCode("ERROR");
                res.setResMsg("本地签名异常");
                res.setData("");
                logger.info("本地签名异常sign=" + sign + "----返回值签名sign=" + JSONObject.parseObject(result).getString("sign"));
            } else {
                JSONObject resultJSON = JSONObject
                        .parseObject(JSONObject.parseObject(result).getString("result"));
                if ("000000".equals(resultJSON.getString("code"))) {
                    String status = resultJSON.getJSONObject("data").getString("status");
                    res.setData(status);
                    res.setResMsg(resultJSON.getString("message"));
                    res.setResCode("SUCCESS");
                } else {
                    res.setData("");
                    res.setResMsg(resultJSON.getString("message"));
                    res.setResCode("ERROR");
                }
            }
        } catch (Exception e) {
            logger.error("支付异常" + e);
            res.setData("");
            res.setResMsg("支付失败");
            res.setResCode("ERROR");
        }
        return res;
    }
}
