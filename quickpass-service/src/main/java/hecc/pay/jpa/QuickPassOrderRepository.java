package hecc.pay.jpa;

import hecc.pay.entity.QuickPassOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther xuhoujun
 * @Description: 订单jpa
 * @Date: Created In 下午8:15 on 2018/3/5.
 */
public interface QuickPassOrderRepository extends JpaRepository<QuickPassOrderEntity, Long> {
}
