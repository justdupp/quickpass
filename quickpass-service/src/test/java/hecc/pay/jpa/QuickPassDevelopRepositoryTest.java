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
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午11:04 on 2018/3/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickPassDevelopRepositoryTest {

    @Autowired
    private QuickPassDevelopRepository repository;

    @Test
    public void testFindByTenantTenantIdAndDelIsFalse(){
        List<QuickPassDevelopEntity> developEntityList = repository.findByTenantTenantIdAndDelIsFalse(3L);
        System.out.println(developEntityList.size());
    }

}
