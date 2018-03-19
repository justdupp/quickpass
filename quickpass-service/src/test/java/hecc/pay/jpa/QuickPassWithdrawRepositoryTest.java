package hecc.pay.jpa;


import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawTypeEnum;
import hecc.pay.util.PageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:55 on 2018/3/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickPassWithdrawRepositoryTest {

    @Autowired
    private QuickPassWithdrawRepository repository;

    @Test
    public void testFindByTenantTenantIdAndTypeAndDelIsFalse(){
        Integer page  = 1;
        Pageable pageable = PageUtil.generatePage(page);
        Page<QuickPassWithdrawEntity> withdrawEntityPage =
                repository.findByTenantTenantIdAndTypeAndDelIsFalse(3L, WithdrawTypeEnum.快捷,pageable);
        System.out.println(withdrawEntityPage.getNumber());

    }

}
