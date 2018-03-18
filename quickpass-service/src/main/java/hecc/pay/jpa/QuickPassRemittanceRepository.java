package hecc.pay.jpa;

import hecc.pay.entity.QuickPassRemittanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther xuhoujun
 * @Description: 打款JPA
 * @Date: Created In 下午4:20 on 2018/3/18.
 */
public interface QuickPassRemittanceRepository extends JpaRepository<QuickPassRemittanceEntity,Long> {
}
