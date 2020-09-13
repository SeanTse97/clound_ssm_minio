package edu.dgut.util;

import edu.dgut.config.MinioConfig;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component
public class MinioUtil {

    @Autowired
    MinioClient minioClient;

    /**
     *   获取minio 里面的bucket
     */
    public List<Bucket> getBuckets() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        List<Bucket> bucketList = minioClient.listBuckets();
        return bucketList;
    }


}
