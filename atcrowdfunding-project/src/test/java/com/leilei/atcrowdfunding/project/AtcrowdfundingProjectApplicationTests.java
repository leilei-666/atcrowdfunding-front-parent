package com.leilei.atcrowdfunding.project;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.leilei.atcrowdfunding.project.component.OssTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtcrowdfundingProjectApplicationTests {
    @Autowired
    private OssTemplate ossTemplate;
    @Test
    public void testOss() throws IOException {
        FileInputStream inputStream=new FileInputStream("C:\\Users\\31429\\Desktop\\3.jpg");
        byte[] bytes=new byte[inputStream.available()];
         inputStream.read(bytes);
        String upload = ossTemplate.upload(bytes, "2.jpg");
        System.out.println(upload);
    }
    @Test
    public void contextLoads() throws IOException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAIoXvOSc0R64nb";
        String accessKeySecret = "oESo635FZfjnWq9OFDCcqpMxeT9jSj";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\31429\\Desktop\\jqq.jpg");
        byte[] content = content = new byte[inputStream.available()];
        inputStream.read(content);
        //第一个参数桶的名字，第二个参数文件的名字
        ossClient.putObject("jqq21", "jqq.jpg", new ByteArrayInputStream(content));

// 关闭OSSClient。
        ossClient.shutdown();
    }

}
