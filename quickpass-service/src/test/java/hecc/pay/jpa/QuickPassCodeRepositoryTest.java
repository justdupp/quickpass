package hecc.pay.jpa;


import hecc.pay.entity.QuickPassCodeEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

}
