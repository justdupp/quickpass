package hecc.pay.jpa;

import hecc.pay.entity.QuickPassOrderEntity;
import hecc.pay.enumer.OrderStatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static hecc.pay.util.PageUtil.generatePage;
import static org.junit.Assert.*;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午8:57 on 2018/3/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickPassOrderRepositoryTest {

    @Autowired
    private QuickPassOrderRepository repository;

    @Test
    public void testOrderJpa(){
        QuickPassOrderEntity orderEntity = new QuickPassOrderEntity();
        orderEntity.fee = 100;
        orderEntity.payBankAccount = "0014003800120047";
        orderEntity.payBankMobile = "18616687070";
        orderEntity.payBankName = "招商银行";
        orderEntity.payIdCard = "8461357";
        orderEntity.payUserName = "周大宝宝";
        orderEntity.thirdNo = "684631sa31321654";
        orderEntity.realFee = 100;
        orderEntity.tenant = null;
        orderEntity.status = OrderStatusEnum.预下单;
        repository.save(orderEntity);
    }

    @Test
    public void testCountByTenantTenantIdAndStatusAndDelIsFalse(){
        System.out.println("数量："+ repository.countByTenantTenantIdAndStatusAndDelIsFalse(1L,OrderStatusEnum.预下单));
    }

    @Test
    public void testFindByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(){
        Date startDate = new Date();
        Date endDate = new Date();
        String start = "2018-03-27 11:00:00";
        String end = "2018-03-27 12:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<QuickPassOrderEntity> orderEntityList = repository.findByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(startDate,endDate,OrderStatusEnum.预下单);
        //   List<QuickPassOrderEntity> orderEntityList = repository.findByCreateDateGreaterThanEqualAndCreateDateLessAndStatusAndStatusAndDelIsFalse(startDate,endDate,OrderStatusEnum.预下单,OrderStatusEnum.交易失败);
        System.out.println(orderEntityList.size());
        if(orderEntityList!=null && !orderEntityList.isEmpty()){
            orderEntityList.stream().forEach(
                    order->{
                        System.out.println(order.payBankName);
                        System.out.println(order.payUserName);
                        System.out.println(order.status);
                    }
            );
        }else{
            System.out.println("no order data");
        }
    }

    @Test
    public void testFindByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndDelIsFalse(){
        Long tenantId = 1L;
        Date startDate = new Date();
        Date endDate = new Date();
        Integer page =  1;
        String start = "2018-03-27 11:00:00";
        String end = "2018-03-28 12:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //    Page<QuickPassOrderEntity> orderEntityPage = repository.findByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndDelIsFalse(tenantId,startDate,endDate,generatePage(page));
        Page<QuickPassOrderEntity> orderEntityPage = repository.findByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(tenantId,startDate,endDate,OrderStatusEnum.预下单,generatePage(page));
        System.out.println("总页数"+orderEntityPage.getTotalPages());
        System.out.println("总条数"+orderEntityPage.getTotalElements());
        orderEntityPage.getContent().stream().forEach(
                order->{
                    System.out.println(order.status);
                    System.out.println(order.payUserName);
                }
        );
    }

}
