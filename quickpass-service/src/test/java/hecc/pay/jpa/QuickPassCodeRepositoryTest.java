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
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午8:36 on 2018/3/5.
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
        List<QuickPassCodeEntity> codeEntityList = codeRepository.findByTenantIdAndDelIsFalse(3L);
        System.out.println("码对象列表的大小: "+codeEntityList.size());
        codeEntityList.stream().forEach(
                c-> {
                    System.out.println(c.code);
                    System.out.println(c.platform);
                    System.out.println(c.tenant.createDate);
                }
        );
    }

}
