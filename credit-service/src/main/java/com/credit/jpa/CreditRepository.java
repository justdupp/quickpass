package com.credit.jpa;

import com.credit.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午10:34 on 2018/4/8.
 */
public interface CreditRepository extends JpaRepository<CreditEntity, Long> {

}
