package hecc.pay.jpa;

import hecc.pay.entity.QuickPassRemittanceEntity;
import hecc.pay.enumer.WithdrawStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @Auther xuhoujun
 * @Description: 打款JPA
 * @Date: Created In 下午4:20 on 2018/3/18.
 */
public interface QuickPassRemittanceRepository extends JpaRepository<QuickPassRemittanceEntity,Long> {

    @Modifying
    @Query("update QuickPassRemittanceEntity f set f.status = ?1,f.message = ?2 where f.id = ?3")
    int modifyByQuickPassRemittanceEntityId(WithdrawStatusEnum status, String message, Long id);
}
