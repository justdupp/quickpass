package hecc.pay.util;

import java.math.BigDecimal;

/**
 * @Auther xuhoujun
 * @Description: money 工具类
 * @Date: Created In 下午9:45 on 2018/3/19.
 */
public class MoneyUtil {

    /**
     * Integer转String
     * @param money  Integer 类型
     * @return
     */
    public static String toMoney(Integer money) {
        return new BigDecimal(money).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN)
                .toPlainString();
    }
}
