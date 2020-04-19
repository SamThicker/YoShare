package com.yo.fileservice.components;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.Upload;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MinioTemplate {

    @Autowired
    private MinioClient client;

    /**
     * 创建bucket
     * */
    public void makeBucket(String bucketName){
        try {
            // 如存储桶不存在，创建之。
            boolean found = client.bucketExists("mybucket");
            if (found) {
                System.out.println("mybucket already exists");
            } else {
                // 创建名为'my-bucketname'的存储桶。
                client.makeBucket("mybucket");
                System.out.println("mybucket is created successfully");
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }

    /**
     * 根据bucketName删除信息
     * */
    @SneakyThrows
    public void removeBucket(String bucketName) {
        client.removeBucket(bucketName);
    }

    /**获取所有bucket*/
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return client.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     * */
    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return client.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据文件前置查询文件
     * */
    @SneakyThrows
    public List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        List<Item> itemList = new ArrayList<>();
        Iterable<Result<Item>> objectsIterator = client.listObjects(bucketName, prefix, recursive);
        while (objectsIterator.iterator().hasNext()) {
            itemList.add(objectsIterator.iterator().next().get());
        }
        return itemList;
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    过期时间 <=7
     * @return url
     */
    @SneakyThrows
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        return client.presignedGetObject(bucketName, objectName, expires);
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        return client.getObject(bucketName, objectName);
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
//    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
//        client.putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
//    }

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param size        大小
     * @param contextType 类型
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) throws Exception {
        PutObjectOptions options = new PutObjectOptions(size, 1024 * 1024 * 10);
        options.setContentType(contextType);
        client.putObject(bucketName, objectName, stream, options);
    }

    /**
     * 获取文件信息
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
     */
    public ObjectStat getObjectInfo(String bucketName, String objectName) throws Exception {
        return client.statObject(bucketName, objectName);
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        client.removeObject(bucketName, objectName);
    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Assert.hasText(endpoint, "Minio url 为空");
//        Assert.hasText(accesskey, "Minio accessKey为空");
//        Assert.hasText(secretkey, "Minio secretKey为空");
//        this.client = new MinioClient(endpoint, accesskey, secretkey);
//    }

    /**
     * 列出未完成上传的文件
     */
    public List<Result<Upload>> listIncompleteUploads(String bucketName, String prefix, boolean recursive){
        List<Result<Upload>> list = new ArrayList<>();
        for (Result<Upload> uploadResult : client.listIncompleteUploads(bucketName, prefix, recursive)) {
            list.add(uploadResult);
        }
        return list;
    }

    /**
     * 获取浏览器上传url
     * */
    public String presignedPutObjectUrl(String bucketName, String objectName, Integer expires) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidResponseException, XmlParserException {
        if (null == expires) {
            return client.presignedPutObject(bucketName, objectName);
        }
        return client.presignedPutObject(bucketName, objectName, expires);
    }

}
