package hecc.pay;

import hecc.pay.service.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author xuhoujun
 * @description:
 * @date: Created In 下午9:33 on 2018/3/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitConfigTest {

    @Autowired
    private RabbitSender sender;

    @Test
    public void hello() throws Exception {
        sender.send();
    }
}
