package hecc.pay.jpa;

import hecc.pay.entity.QuickPassDevelopEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author xuhoujun
 * @description:
 * @date: Created In 下午11:04 on 2018/3/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickPassDevelopRepositoryTest {

    @Autowired
    private QuickPassDevelopRepository repository;

    @Test
    public void testJpa(){
        QuickPassDevelopEntity developEntity = new QuickPassDevelopEntity();
        developEntity.profit = 2000;
        developEntity.idCard = "310101199310011234";
        developEntity.tenant = null;
        repository.save(developEntity);
    }

    @Test
    public void testFindByTenantTenantIdAndDelIsFalse(){
        List<QuickPassDevelopEntity> developEntityList = repository.findByTenantTenantIdAndDelIsFalse(3L);
        System.out.println(developEntityList.size());
    }

    @Test
    public void testCountByIdCardAndDelIsFalse(){
        System.out.println("根据身份证号码获取条数: "+repository.countByIdCardAndDelIsFalse("310101199310011234"));
    }

}
