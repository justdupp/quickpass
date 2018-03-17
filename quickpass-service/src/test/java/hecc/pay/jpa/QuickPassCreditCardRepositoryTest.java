import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:41 on 2018/3/16.
 */
@RunWith(Arquillian.class)
public class QuickPassCreditCardRepositoryTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(hecc.pay.jpa.QuickPassCreditCardRepository.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
