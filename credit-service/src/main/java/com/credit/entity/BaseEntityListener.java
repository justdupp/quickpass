package com.credit.entity;

import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * @Auther xuhoujun
 * @Description: 基础类监听器
 * @Date: Created In 上午12:12 on 2018/3/3.
 */
public class BaseEntityListener {

    @PreUpdate
    public void updateModifyDate(BaseEntity baseEntity) {
        baseEntity.modifyDate = new Date();
    }
}
