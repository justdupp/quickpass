package hecc.pay.vos;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 订单列表VO
 * @Date: Created In 下午9:11 on 2018/3/22.
 */
public class OrderListVO {

    public List<OrderVO> list;

    public OrderStatisticsVO total;

    public OrderListVO(List<OrderVO> list, OrderStatisticsVO total) {
        this.list = list;
        this.total = total;
    }
}
