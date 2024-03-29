import com.jiwuzao.common.domain.enumType.ReceiptEnum;
import com.jiwuzao.common.vo.common.AppUpdateVO;
import com.kauuze.major.MajorApplication;
import com.kauuze.major.config.contain.ExpressTempDemo;
import com.kauuze.major.domain.mysql.repository.ReceiptRepository;
import com.kauuze.major.service.ShopCartService;
import com.kauuze.major.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MajorApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ShopCartTest{
    @Autowired
    ShopCartService shopCartService;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private SystemService systemService;

    @Test
    public void testReceipt(){
//        receiptRepository.findAll().forEach(System.out::println);
        receiptRepository.findByType(ReceiptEnum.COMPANY).forEach(System.out::println);
    }

    @Test
    public void addItem(){
//        shopCartService.addItem(4, "5d2bf4862197fd2c5cfd4af6",
//                "5d2bf4862197fd2c5cfd4af9", 2);
//        System.out.println(shopCartService.getItems(4).toString());
//        List<String> cidList= new ArrayList<>();
//        cidList.add("5d2ec4776a4d8045020e2b7c");
//        cidList.add("5d2ec4ed6a4d80458fc159d2");
//        cidList.add("5d2ec5e26a4d804649c28348");
//        cidList.add("5d2ec62a6a4d8046ce4697b2");
//        shopCartService.delItems(cidList);
    }

    @Test
    public void getVersion(){
        AppUpdateVO update = systemService.getUpdate();
        System.out.println(update);
    }
}