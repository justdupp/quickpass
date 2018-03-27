package hecc.pay.jpa;

import hecc.pay.entity.QuickPassRemittanceEntity;
import hecc.pay.enumer.RemittanceStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 打款JPA
 * @Date: Created In 下午4:20 on 2018/3/18.
 */
public interface QuickPassRemittanceRepository extends JpaRepository<QuickPassRemittanceEntity, Long> {

    /**
     * 根据打款id修改打款状态、打款状态描述
     *
     * @param status  打款状态
     * @param message 款状态描述
     * @param id      打款id
     * @return 数量
     */
    @Modifying
    @Transactional
    @Query("update QuickPassRemittanceEntity f set f.status = ?1,f.message = ?2 where f.id = ?3")
    int modifyByQuickPassRemittanceEntityId(RemittanceStatusEnum status, String message, Long id);

    /**
     * 根据开始时间、结束时间、打款状态获取打款列表
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param status    打款状态
     * @return 打款列表
     */
    @Query("select f from QuickPassRemittanceEntity f WHERE  f.createDate >= ?1 and f.createDate < ?2 and status = ?3")
    List<QuickPassRemittanceEntity> findByCreateDateGreaterThanEqualAndCreateDateLessAndStatus(
            Date startDate, Date endDate, RemittanceStatusEnum status);

    /**
     * 根据开始时间、结束时间获取打款列表
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 打款列表
     */
    @Query("select f from QuickPassRemittanceEntity f WHERE  f.createDate >= ?1 and f.createDate < ?2")
    List<QuickPassRemittanceEntity> findByCreateDateGreaterThanEqualAndCreateDateLess(Date startDate, Date endDate);
}
