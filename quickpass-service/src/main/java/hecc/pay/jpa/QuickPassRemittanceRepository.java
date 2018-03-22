package hecc.pay.jpa;

import hecc.pay.entity.QuickPassRemittanceEntity;
import hecc.pay.enumer.RemittanceStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 打款JPA
 * @Date: Created In 下午4:20 on 2018/3/18.
 */
public interface QuickPassRemittanceRepository extends JpaRepository<QuickPassRemittanceEntity,Long> {

    @Modifying
    @Query("update QuickPassRemittanceEntity f set f.status = ?1,f.message = ?2 where f.id = ?3")
    int modifyByQuickPassRemittanceEntityId(RemittanceStatusEnum status, String message, Long id);


    @Query("select f from QuickPassRemittanceEntity f WHERE  f.createDate >= ?1 and f.createDate < ?2 and status = ?3")
    List<QuickPassRemittanceEntity> findByCreateDateGreaterThanEqualAndCreateDateLessAndStatus(
            Date startDate, Date endDate, RemittanceStatusEnum status);

    @Query("select f from QuickPassRemittanceEntity f WHERE  f.createDate >= ?1 and f.createDate < ?2")
    List<QuickPassRemittanceEntity> findByCreateDateGreaterThanEqualAndCreateDateLess(Date startDate, Date endDate);
}
