package hecc.pay.util;

import java.util.UUID;

/**
 * @author xuhoujun
 * @description:
 * @date: Created In 下午9:46 on 2018/3/27.
 */
public class IDUtil {

    /**
     * UUID 去除-
     * @return
     */
   public static String generateID(){
       String s = UUID.randomUUID().toString();
       return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) +
               s.substring(19, 23) + s.substring(24);
   }
}
