package hecc.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializerFeature;
import hecc.pay.client.route.RouterPayResponse;
import hecc.pay.client.route.RouterRequest;
import hecc.pay.entity.QuickPassOrderEntity;
import hecc.pay.enumer.OrderStatusEnum;
import hecc.pay.enumer.ResultOrderStatusEnum;
import hecc.pay.jpa.QuickPassOrderRepository;
import hecc.pay.vos.RabbitMqMessageVO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static hecc.pay.util.MD5Util.MD5;

/**
 * @Auther xuhoujun
 * @Description: 支付服务
 * @Date: Created In 下午10:53 on 2018/3/21.
 */
@Service
public class PayService {

    private Logger logger = LoggerFactory.getLogger(PayService.class);
    public final static int CONNECT_TIMEOUT = 10;
    public final static int READ_TIMEOUT = 10;
    public final static int WRITE_TIMEOUT = 10;

    @Autowired
    AsyncTask asyncTask;

    @Autowired
    private QuickPassOrderRepository orderRepository;

    @Value("${quickpass-pay-service.api-version}")
    private String version;

    @Value("${quickpass-pay-service.key}")
    private String key;

    @Value("${quickpass-pay-service.server-url}")
    private String serverUrl;

    @Value("${quickpass-pay-service.mid}")
    private String mid;

    @Value("${quickpass-pay-service.notify-url}")
    private String notifyUrl;

    public RouterPayResponse pay(RouterRequest request) {
        JSONObject obj = new JSONObject();
        String method = "quickPass";
        /**
         * 默认字符串
         */
        obj.put("mid", mid);
        /**
         * 版本号
         */
        obj.put("version", version);
        /**
         * 请求支付方式
         */
        obj.put("method", method);
        /**
         * 时间戳
         */
        obj.put("dateStamp", Calendar.getInstance().getTimeInMillis());
        /**
         * 卡号
         */
        obj.put("accountNumber", request.getAccountNumber());
        /**
         * 银行预留手机号
         */
        obj.put("bankAccountTel", request.getBankAccountTel());
        /**
         * 银行卡有效期
         */
        obj.put("expired", request.getExpired());
        /**
         * 交易金额
         */
        obj.put("tradeAmount", new BigDecimal(request.getTradeAmount()).divide(new BigDecimal(100)).doubleValue());
        /**
         * 订单号
         */
        obj.put("bizOrderNumber", request.getBizOrderNumber());
        /**
         * 商户名称
         */
        obj.put("merchantName", request.getMerchantName());
        /**
         * 身份证号码
         */
        obj.put("idCard", request.getIdCard());
        /**
         * 结算银行名称
         */
        obj.put("bankName", request.getBankName());
        obj.put("notifyUrl", notifyUrl);
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

    public String notify(String bizOrderNumber, Integer tradeAmount) {
        QuickPassOrderEntity order = orderRepository.findOne(Long.parseLong(bizOrderNumber));
        if ("交易成功".equals(order.status)) {
            return "success";
        }
        order.status = OrderStatusEnum.交易成功;
        order.finishDate = new Date();
        order.thirdNo = bizOrderNumber + "";
        order.realFee = tradeAmount;
        try {
            orderRepository.save(order);
        } catch (Exception e) {
        }
        if (OrderStatusEnum.交易成功.equals(order.status)) {
            new Thread(() -> setAsyncTasks(order.id)).start();
        }
        return "success";
    }

    public void setAsyncTasks(Long id) {
        asyncTask.asyncProfit(id);
        asyncTask.asyncDevelop(id);
    }


    /**
     * 查询接口
     */
    public RabbitMqMessageVO queryOrder(Long bizOrderNumber) {
        JSONObject resJson = new JSONObject();
        String method = "quickPassQuery";
        JSONObject content = new JSONObject();
        resJson.put("encryptId", mid);
        resJson.put("apiVersion", version);
        resJson.put("queryDate", Calendar.getInstance().getTimeInMillis());
        resJson.put("method", method);
        resJson.put("mid", mid);
        resJson.put("bizOrderNumber", bizOrderNumber);

        content.put("content", JSONObject.toJSONString(resJson, SerializerFeature.WriteMapNullValue));
        content.put("key", key);
        String signStr = JSON.toJSONString(content, SerializerFeature.WriteMapNullValue);
        String sign = MD5(signStr);
        content.remove("key");
        content.put("sign", sign);
        /*--------------------------请求查询接口-----------------------------*/
        logger.info("请求支付前参数" + JSON.toJSONString(content));
        Builder okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okhttp3.RequestBody requestBody = okhttp3.RequestBody
                .create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(content));
        Request req = new Request.Builder()
                .addHeader("Content-Type", "text/plain;charset=UTF-8")
                .addHeader("dataType", "json")
                .url(serverUrl + method).post(requestBody).build();
        try {
            Response response = okHttpClient.build().newCall(req).execute();
            String result = response.body().string();
            logger.info("查询返回result=" + result);
            JSONObject data = JSONObject.parseObject(JSONPath.read(result, "result") + "");
            String resQuery = data.getString("data");
            if (!"000000".equals(data.getString("code"))) {
                logger.info("查询状态异常data=" + data);
                return new RabbitMqMessageVO(bizOrderNumber + "", ResultOrderStatusEnum.已提交, new Date());
            } else {
                Map<String, String> resMap = new LinkedHashMap<>();
                resMap.put("key", key);
                resMap.put("result", JSONPath.read(result, "result") + "");
                signStr = JSON.toJSONString(resMap, SerializerFeature.WriteMapNullValue);
                sign = MD5(signStr);
                if (!sign.equals(JSONPath.read(result, "sign") + "")) {
                    logger.info("本地签名异常sign=" + sign + "----返回值签名sign=" + JSONPath.read(result, "sign") + "");
                    return new RabbitMqMessageVO(bizOrderNumber + "", ResultOrderStatusEnum.交易失败, new Date());
                } else {
                    if ("success".equals(JSONPath.read(resQuery, "status"))) {
                        return new RabbitMqMessageVO(JSONPath.read(resQuery, "bizOrderNumber") + "",
                                ResultOrderStatusEnum.交易成功, new Date());
                    } else if ("commit".equals(JSONPath.read(resQuery, "status"))) {
                        return new RabbitMqMessageVO(JSONPath.read(resQuery, "bizOrderNumber") + "",
                                ResultOrderStatusEnum.已提交, new Date());
                    } else {
                        return new RabbitMqMessageVO(bizOrderNumber + "", ResultOrderStatusEnum.交易失败, new Date());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("调用查询接口异常" + e);
            return new RabbitMqMessageVO(bizOrderNumber + "", ResultOrderStatusEnum.已提交, new Date());
        }

    }

}
