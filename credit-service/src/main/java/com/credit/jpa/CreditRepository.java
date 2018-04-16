package com.credit.jpa;

import com.credit.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xuhoujun
 * @description: 银行卡 -- jpa
 * @date: Created In 下午10:34 on 2018/4/8.
 */
public interface CreditRepository extends JpaRepository<CreditEntity, Long> {

    /**
     * 获取卡列表
     *
     * @return 码对象列表
     */
    List<CreditEntity> findByDelIsFalse();

    /**
     * 逻辑删除
     *
     * @param id
     * @return 数量
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update CreditEntity c set c.del = true where c.id = ?1")
    int modifyByCreditEntityId(Long id);
}
