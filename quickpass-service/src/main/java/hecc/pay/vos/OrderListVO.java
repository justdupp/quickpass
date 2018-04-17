package hecc.pay.vos;

import java.util.List;

/**
 * @author xuhoujun
 * @description: 订单列表VO
 * @date: Created In 下午9:11 on 2018/3/22.
 */
public class OrderListVO {

    public List<OrderVO> list;

    public OrderStatisticsVO total;

    public OrderListVO(List<OrderVO> list, OrderStatisticsVO total) {
        this.list = list;
        this.total = total;
    }
}
