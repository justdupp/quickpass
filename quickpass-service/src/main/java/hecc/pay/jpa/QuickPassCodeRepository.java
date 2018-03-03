package hecc.pay.jpa;

import hecc.pay.entity.QuickPassCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther xuhoujun
 * @Description: 闪付 -- 码jpa
 * @Date: Created In 上午12:17 on 2018/3/4.
 */
public interface QuickPassCodeRepository extends JpaRepository<QuickPassCodeEntity,Long> {
}
