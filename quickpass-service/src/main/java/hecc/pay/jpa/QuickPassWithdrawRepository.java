package hecc.pay.jpa;

import hecc.pay.entity.QuickPassWithdrawEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther xuhoujun
 * @Description: 提现JPA
 * @Date: Created In 下午3:57 on 2018/3/18.
 */
public interface QuickPassWithdrawRepository extends JpaRepository<QuickPassWithdrawEntity,Long> {
}
