package edu.dgut.controller;

import edu.dgut.domain.CommonData;
import edu.dgut.domain.PrivateDO;
import edu.dgut.domain.PublicDO;
import edu.dgut.server.PrivateBucketServer;
import edu.dgut.server.PublicBucketServer;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Controller
@RequestMapping("pic")
public class PictureController {
    @Autowired
    MinioClient minioClient;

    @Autowired
    PublicBucketServer publicBucketServer;

    @Autowired
    PrivateBucketServer privateBucketServer;

    //获取公有文件夹的内容
    @PostMapping("getObjects")
    @ResponseBody
    public CommonData getPublicObjects(HttpServletRequest request){
        CommonData commonData = new CommonData();
        List<String> list = new LinkedList<>();
        try {
            commonData.setUser((String)request.getSession().getAttribute("user"));

            List<PublicDO> pubList = publicBucketServer.getByPage(1);
            for (PublicDO p :pubList) {
                list.add(minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket("public")
                                .object("images/"+p.getPictureName())
                                .expiry(60 * 60 * 24)
                                .build()));
            }
            commonData.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return commonData;
        }
    }

    //获取私有文件夹的内容
    @PostMapping("getPersonalObjects")
    @ResponseBody
    public CommonData getPersonalObjects(HttpServletRequest request){
        CommonData commonData = new CommonData();

        List<String> list = new LinkedList<>();
        String name = (String) request.getSession().getAttribute("user");
        try {
            commonData.setUser(name);
            List<PrivateDO> priList = privateBucketServer.getPage(name, 1);

            for (PrivateDO p :priList) {
                list.add(minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(name)
                                .object("images/"+p.getPictureName())
                                .expiry(60 * 60 * 24)
                                .build()));
            }
            commonData.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return commonData;
        }
    }

    @PostMapping("/uploadPublic")
    @ResponseBody
    public HashMap uploadPublic( @RequestParam(value = "file",required = false) MultipartFile file , HttpServletRequest request){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String bucketName = (String)request.getSession().getAttribute("user");
        String fileName = file.getOriginalFilename();  //获取上传文件的名字
        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        HashMap<String,String>  map = new HashMap<>();
        try {
            // 文件转输入流
            CommonsMultipartFile cFile = (CommonsMultipartFile) file;
            DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
            InputStream inputStream = fileItem.getInputStream();
            String objectName = bucketName + "-" + "file" + "-" + uuid + "." + type;

            minioClient.putObject(
                    PutObjectArgs.builder().bucket("public").object("images/"+objectName).stream(
                            inputStream, -1, 10485760)
                            .contentType("image/" + type)
                            .build());
            PublicDO publicDO = new PublicDO();
            publicDO.setPictureName(objectName);
            publicDO.setCreateTime(new Date(System.currentTimeMillis()));
            publicBucketServer.insertData(publicDO);

        }catch (MinioException e){
            System.out.println("minio error"+e);
            map.put("msg", "上传失败");
        } catch (Exception e){
            System.out.println("other error"+e);
            map.put("msg", "上传失败");
        }finally {

            return map;
        }

    }
    @PostMapping("/uploadPrivate")
    @ResponseBody
    public HashMap uploadPrivate( @RequestParam(value = "file",required = false) MultipartFile file , HttpServletRequest request){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String bucketName = (String)request.getSession().getAttribute("user");
        String fileName = file.getOriginalFilename();  //获取上传文件的名字
        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        HashMap<String,String>  map = new HashMap<>();
        try {
            // 文件转输入流
            CommonsMultipartFile cFile = (CommonsMultipartFile) file;
            DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
            InputStream inputStream = fileItem.getInputStream();

            String objectName = bucketName + "-" + "file" + "-" + uuid + "." + type;

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object("images/"+objectName).stream(
                            inputStream, -1, 10485760)
                            .contentType("image/" + type)
                            .build());
            PrivateDO privateDO = new PrivateDO();
            privateDO.setUserName(bucketName);
            privateDO.setPictureName(objectName);
            privateDO.setcreateTime(new Date(System.currentTimeMillis()));
            privateBucketServer.insertData(privateDO);
        }catch (MinioException e){
            System.out.println("minio error"+e);
            map.put("msg", "上传失败");
        } catch (Exception e){
            System.out.println("other error"+e);
            map.put("msg", "上传失败");
        }finally {
            return map;
        }

    }


    @GetMapping("images")
    public String getpic(@RequestParam("imageUrl") String path,HttpServletResponse response) throws IOException{
        response.setContentType("image/jpeg/jpg/png/gif/bmp/tiff/svg"); // 设置返回内容格式
        path=new String(path.getBytes("ISO-8859-1"),"UTF-8");
        String[] strs = path.split("/");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + new String(path.getBytes("gb2312"),"ISO-8859-1") + "\"");
        InputStream in = null;
        OutputStream os = null;
        try {
            in = minioClient.getObject(
                            GetObjectArgs.builder().bucket(strs[0]).object("images/"+strs[2]).build());
            os = response.getOutputStream();  //创建输出流
            byte[] buf = new byte[16384];
            int bytesRead;
            while ((bytesRead = in.read(buf, 0, buf.length)) >= 0) {
                os.write(buf, 0, bytesRead);
            }

        }catch (MinioException e){
            System.out.println("minio error");
            e.printStackTrace();
        }catch (Exception e){
            System.out.println("other error");
            e.printStackTrace();
        }finally {
            in.close();
            os.flush();
            os.close();
        }

        return null;
    }

    @RequestMapping("getMore")
    @ResponseBody
    public CommonData getMore(@RequestParam("bucketName") String bucketName,@RequestParam("page") int page){
        CommonData commonData = new CommonData();
        List<String> list = new LinkedList<>();
        if(bucketName.equals("public")){
           try{
               List<PublicDO> pubList =  publicBucketServer.getByPage(page);
               for (PublicDO p :pubList) {
                   list.add(minioClient.getPresignedObjectUrl(
                           GetPresignedObjectUrlArgs.builder()
                                   .method(Method.GET)
                                   .bucket("public")
                                   .object("images/"+p.getPictureName())
                                   .expiry(60 * 60 * 24)
                                   .build()));
                   commonData.setData(list);
               }

           } catch (MinioException e) {
               e.printStackTrace();
           } catch (Exception e) {
               e.printStackTrace();
           }
        }else {
            try {
                List<PrivateDO> pubList = privateBucketServer.getPage(bucketName,page);
                for (PrivateDO  p : pubList) {
                    list.add(minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(bucketName)
                                    .object("images/" + p.getPictureName())
                                    .expiry(60 * 60 * 24)
                                    .build()));
                    commonData.setData(list);
                }

            } catch (MinioException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  commonData;
    }
}
