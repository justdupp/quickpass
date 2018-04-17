package hecc.pay.entity;

import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * @author xuhoujun
 * @description: 基础类监听器
 * @date: Created In 上午12:12 on 2018/3/3.
 */
public class BaseEntityListener {
    @PreUpdate
    public void updateUpdateDate(BaseEntity baseEntity) {
        baseEntity.modifyDate = new Date();
    }
}
