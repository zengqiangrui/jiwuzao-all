package com.kauuze.major.include.yun;

import com.kauuze.major.include.StringUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class QiniuUtil {
    public static String accesskey;//七牛云AK

    @Value("${qiniu.accesskey}")
    public void setAccesskey(String accesskey2) {
        accesskey = accesskey2;
    }

    public static String secretkey;//七牛云SK

    @Value("${qiniu.secretkey}")
    public void setSecretkey(String secretkey2) {
        secretkey = secretkey2;
    }

    public static String bucket; //七牛云对象存储空间

    @Value("${qiniu.bucket}")
    public void setBucket(String bucket2) {
        bucket = bucket2;
    }

    /**
     * 批量删除七牛云上文件 逗号分隔
     *
     * @param fileNames
     * @return
     */
    public static void delFilesBatch(String fileNames) {
        Configuration cfg = new Configuration(Zone.autoZone());
        Auth auth = Auth.create(accesskey, secretkey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            String[] keyList = fileNames.split(",");
            for (int i = 0; i < keyList.length; i++) {
                String tem = keyList[i];
                keyList[i] = tem.substring(tem.lastIndexOf("/") + 1);
            }
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            bucketManager.batch(batchOperations);
        } catch (QiniuException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取为期一小时的上传凭证,key为文件名。
     *
     * @return
     */
    public static Map<String, String> upToken(String suffix) {
        String key = UUID.randomUUID().toString().replace("-", "") + suffix;
        Auth auth = Auth.create(accesskey, secretkey);
        StringMap policy = new StringMap();
        policy.put("fsizeLimit", 1024 * 1024 * 50);
        String upToken = auth.uploadToken(bucket, key, 3600L, policy);
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("upToken", upToken);
        return map;
    }

    public static boolean delSingle(String url){
        Configuration cfg = new Configuration(Zone.autoZone());
        Auth auth = Auth.create(accesskey, secretkey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, StringUtil.getUrn(url));
        } catch (QiniuException e) {
            e.printStackTrace();
            System.err.println(e.response.toString());
        }
        return true;
    }

}