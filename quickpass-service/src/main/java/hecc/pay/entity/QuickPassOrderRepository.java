package hecc.pay.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther xuhoujun
 * @Description: 订单jpa
 * @Date: Created In 下午5:00 on 2018/3/4.
 */
public interface QuickPassOrderRepository extends JpaRepository<QuickPassOrderEntity,Long> {
}
