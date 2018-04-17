package hecc.pay.util;

import java.math.BigDecimal;

/**
 * @author xuhoujun
 * @description: money 工具类
 * @date: Created In 下午9:45 on 2018/3/19.
 */
public class MoneyUtil {

    /**
     * Integer转String
     *
     * @param money Integer 类型
     * @return
     */
    public static String toMoney(Integer money) {
        return new BigDecimal(money).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN)
                .toPlainString();
    }

    /**
     * Long 转String
     *
     * @param money
     * @return
     */
    public static String toMoney(Long money) {
        return new BigDecimal(money).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN)
                .toPlainString();
    }

    /**
     * String 转Integer
     *
     * @param money
     * @return
     */
    public static Integer toMoney(String money) {
        return new BigDecimal(money).multiply(new BigDecimal(100)).intValue();
    }
}
