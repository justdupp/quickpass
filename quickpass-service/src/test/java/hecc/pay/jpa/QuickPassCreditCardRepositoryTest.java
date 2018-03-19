package hecc.pay.jpa;

import hecc.pay.entity.QuickPassCreditCardEntity;
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
 * @Date: Created In 下午9:41 on 2018/3/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickPassCreditCardRepositoryTest {

    @Autowired
    private QuickPassCreditCardRepository creditCardRepository;

    @Test
    public void testCreditCardJPA(){
        QuickPassCreditCardEntity creditCardEntity = new QuickPassCreditCardEntity();
        creditCardEntity.bankAccount = "6215";
        creditCardEntity.platform = "quickpass";
        creditCardEntity.bankName = "建行";
        creditCardEntity.tenant = null;
        creditCardRepository.saveAndFlush(creditCardEntity);
    }

    @Test
    public void testFindByTenantTenantIdAndDelIsFalse(){
        List<QuickPassCreditCardEntity> list =  creditCardRepository.findByTenantTenantIdAndDelIsFalse(1L);
        list.stream().forEach(
                credit->{
                    System.out.println(credit.bankName);
                    System.out.println(credit.tenant.version);
                }
        );
    }

    @Test
    public void testFindFirstByBankAccountAndDelIsFalse(){
        QuickPassCreditCardEntity creditCardEntity = creditCardRepository.findFirstByBankAccountAndDelIsFalse("6215");
        System.out.println(creditCardEntity.bankName);
    }

}
