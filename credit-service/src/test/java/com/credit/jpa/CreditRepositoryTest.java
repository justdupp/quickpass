package com.credit.jpa;

import com.credit.entity.CreditEntity;
import com.credit.enumer.CreditBankTypeEnum;
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
 * @Date: Created In 下午9:30 on 2018/4/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditRepositoryTest {

    @Autowired
    private CreditRepository repository;

    @Test
    public void testCreditJpa(){
        CreditEntity entity = new CreditEntity();
        entity.cardName = "招商银行";
        entity.type = CreditBankTypeEnum.激活;
        entity.shortName = "招行";
        entity.bankLogo = "logo";
        entity.bankDetail = "招商银行上海分行张江支行";
        entity.money = "12.25";
        entity.isShow = true;
        entity.platform = "quickpass";
        repository.saveAndFlush(entity);
    }

    @Test
    public void testFindByDelIsFalse(){
        List<CreditEntity> list = repository.findByDelIsFalse();
        System.out.println("列表大小："+ list.size());
        list.forEach(entity->{
            System.out.println(entity.isShow);
            System.out.println(entity.cardName);
            System.out.println(entity.type);
            System.out.println(entity.money);
        });
    }

    @Test
    public void testModifyCredit(){
        int count =  repository.modifyByCreditEntityId(1L);
        CreditEntity entity = repository.findOne(1L);
        System.out.println("修改后是否删除： "+entity.del);
    }
}
