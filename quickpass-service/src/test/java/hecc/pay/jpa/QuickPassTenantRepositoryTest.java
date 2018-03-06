package hecc.pay.jpa;

import hecc.pay.entity.QuickPassTenantEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午8:57 on 2018/3/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickPassTenantRepositoryTest {
    @Autowired
    private QuickPassTenantRepository tenantRepository;

    @Autowired
    private QuickPassCodeRepository codeRepository;

    @Test
    public void testTenantJPA(){
        QuickPassTenantEntity tenantEntity = new QuickPassTenantEntity();
        tenantEntity.tenantId = 1L;
        tenantEntity.active = false;
        tenantEntity.code= null;
        tenantEntity.defaultCode = null;
        tenantRepository.saveAndFlush(tenantEntity);
    }

    @Test
    public void testFindOneByTenantIdAndDelIsFalse(){
        QuickPassTenantEntity tenantEntity = tenantRepository.findOneByTenantIdAndDelIsFalse(1L);
        System.out.println(tenantEntity.code.platform);
        System.out.println(tenantEntity.active);
    }

    @Test
    public void testCountByCodeId(){
        long conut = tenantRepository.countByCodeId(1);
        System.out.println("code 的数量: " + conut);
    }
}
