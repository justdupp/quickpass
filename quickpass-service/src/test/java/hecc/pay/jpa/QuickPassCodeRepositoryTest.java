package hecc.pay.jpa;


import hecc.pay.entity.QuickPassCodeEntity;
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
 * @date: Created In 下午8:36 on 2018/3/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickPassCodeRepositoryTest {
    @Autowired
    private QuickPassCodeRepository codeRepository;

    @Test
    public void testCodeJPA(){
        QuickPassCodeEntity code = new QuickPassCodeEntity();
        code.code = "11031";
        code.platform = "quickpass";
        code.isDefault = true;
        code.tenant = null;
        codeRepository.saveAndFlush(code);
    }

    @Test
    public void testFindByTenantIdAndDelIsFalse(){
        List<QuickPassCodeEntity> codeEntityList = codeRepository.findByTenantTenantIdAndDelIsFalse(3L);
        System.out.println("码对象列表的大小: "+codeEntityList.size());
        codeEntityList.stream().forEach(
                c-> {
                    System.out.println(c.code);
                    System.out.println(c.platform);
                    System.out.println(c.tenant.createDate);
                }
        );
    }

    @Test
    public void testFindOneByCodeAndDelIsFalse(){
     /*   QuickPassCodeEntity entity = codeRepository.findOneByCodeAndDelIsFalse("11031");
        System.out.println(entity.platform);
        System.out.println("一觉游仙好梦，任它竹冷松寒");*/
        QuickPassCodeEntity entity = codeRepository.findOneByCodeAndDelIsFalse("11031");
        System.out.println("第一次查询：" + entity.platform);

        QuickPassCodeEntity entity2 = codeRepository.findOneByCodeAndDelIsFalse("11031");
        System.out.println("第二次查询：" + entity2.platform);

        entity.platform = "ali";
        codeRepository.save(entity);
        QuickPassCodeEntity entity3 = codeRepository.findOneByCodeAndDelIsFalse("11031");
        System.out.println("第三次查询：" + entity3.platform);
    }

    @Test
    public void testFindFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse(){
        QuickPassCodeEntity codeEntity = codeRepository.findFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse("quickpass");
        System.out.println(codeEntity.code);
    }

    @Test
    public void testFindByIsDefaultIsTrueAndDelIsFalse(){
        List<QuickPassCodeEntity> codeEntities = codeRepository.findByIsDefaultIsTrueAndDelIsFalse();
        System.out.println("默认码列表大小：" + codeEntities.size());
        System.out.println("天下事，古今谈，风流河山");
    }

}
