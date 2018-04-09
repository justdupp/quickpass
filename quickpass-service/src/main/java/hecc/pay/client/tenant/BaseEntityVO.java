package hecc.pay.client.tenant;

import java.util.Date;

/**
 * @Auther xuhoujun
 * @Description: 基础VO
 * @Date: Created In 下午4:12 on 2018/3/11.
 */
public abstract class BaseEntityVO {

    /**
     * 主键
     */
    public Long id;
    /**
     * 创建时间
     */
    public Date createDate;
    /**
     * 更新时间
     */
    public Date modifyDate;
    /**
     * 乐观锁
     */
    public Integer version;
    /**
     * 逻辑删除
     */
    public Boolean del;
    /**
     * 平台标识
     */
    public String platform;
}
