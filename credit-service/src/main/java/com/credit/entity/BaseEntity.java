package hecc.pay.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther xuhoujun
 * @Description: 基础类
 * @Date: Created In 上午12:11 on 2018/3/3.
 */
@EntityListeners(BaseEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue
    public Long id;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date createDate = new Date();

    /**
     * 修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date modifyDate = new Date();

    /**
     * 版本号--乐观锁
     */
    @Version
    public Integer version = 0;

    /**
     * 逻辑删除
     */
    public Boolean del = false;

    /**
     * 平台标识
     */
    @Column(nullable = false)
    public String platform;
}
