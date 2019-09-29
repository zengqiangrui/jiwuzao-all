package com.kauuze.major;

import com.kauuze.major.domain.mongo.repository.GoodsRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class TestService {
    //    @Autowired
//    private GoodsRepository goodsRepository;
    public void test() {
    }

    /**
     *
     * @param item
     * @param old 25
     * @param target 12
     * @return
     */
    private String replace1(String item, String old, String target) {
        System.out.println(1);
        return item.replace(old, target);
    }

    /**
     *
     * @param item
     * @param old 21
     * @param target 3
     * @return
     */
    private String replace2(String item, String old, String target) {
        System.out.println(2);
        return item.replace(old, target);
    }

    /**
     *
     * @param item
     * @param old 12
     * @param target 5
     * @return
     */
    private String replace3(String item, String old, String target) {
        System.out.println(3);
        return item.replace(old, target);
    }

    private String swap(String item) {
        return new StringBuffer(item).reverse().toString();
    }

    private String trans(String item) {
        char[] chars = item.toCharArray();
        char[] result = Arrays.copyOf(chars, chars.length + 1);
        result[0] = chars[chars.length - 1];
        System.arraycopy(chars, 0, result, 1, chars.length);
        return String.valueOf(result).substring(0, chars.length);
    }

    /**
     *
     * @param step 6
     * @param item 2152
     * @param target 13
     */
    private void execute(int step, String item, String target) {

        if(item.equals(target)){

        }
    }


    @Test
    public void show() {

    }
}
