package hecc.pay.jpa;

import hecc.pay.entity.QuickPassProfitEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:02 on 2018/3/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickPassProfitRepositoryTest {

    @Autowired
    private QuickPassProfitRepository repository;

    @Test
    public void testProfitJPA(){
        QuickPassProfitEntity profitEntity = new QuickPassProfitEntity();
        profitEntity.profit = 100;
        profitEntity.tenant = null;
        repository.save(profitEntity);
    }


    @Test
    public void testFindByTenantTenantIdAndDelIsFalse(){
        List<QuickPassProfitEntity> profitEntityList = repository.findByTenantTenantIdAndDelIsFalse(1L);
        System.out.println(profitEntityList.size());
    }

    @Test
    public void testCountByOrderIdAndDelIsFalse(){
        System.out.println(" 订单利润数量; "+ repository.countByOrderIdAndDelIsFalse(3L));
    }
}
