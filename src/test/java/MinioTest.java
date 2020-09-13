import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MinioTest {

    @Test
    public void testMinio() throws IOException, InvalidKeyException, NoSuchAlgorithmException{
        //MinioClient minioClient = new MinioClient("http://119.23.237.101", "02K29ZK9I6RTJXV6321S", "tpWuRq61Fep7seRdI9fYbazABBPyd1yiSDRdXyI4");
        try {
            MinioClient minioClient = MinioClient.builder()
                            .endpoint("http://119.23.237.101",9000,false)
                            .credentials("02K29ZK9I6RTJXV6321S", "tpWuRq61Fep7seRdI9fYbazABBPyd1yiSDRdXyI4")
                            .build();
            List<Bucket> bucketList = minioClient.listBuckets();
            for (Bucket bucket : bucketList) {
                System.out.println(bucket.creationDate() + ", " + bucket.name());
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }
    @Test
    public void testDoload() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        MinioClient minioClient = new MinioClient("http://119.23.237.101", 9000,"02K29ZK9I6RTJXV6321S","tpWuRq61Fep7seRdI9fYbazABBPyd1yiSDRdXyI4");
        InputStream stream =
                minioClient.getObject(
                        GetObjectArgs.builder().bucket("public").object("images/xiaoxin-file-af7d01109861447cab25d825251d1c90.jpg").build());

        // Read the input stream and print to the console till EOF.
        byte[] buf = new byte[16384];
        int bytesRead;
        FileOutputStream downloadFile = new FileOutputStream(new File("D://1.jpg"));
        while ((bytesRead = stream.read(buf, 0, buf.length)) >= 0) {
            downloadFile.write(buf, 0, bytesRead);
            downloadFile.flush();
        }

        // Close the input stream.
        downloadFile.close();
        stream.close();
    }
}
