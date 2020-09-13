package edu.dgut.controller;

import edu.dgut.domain.Account;
import edu.dgut.domain.RecordDO;
import edu.dgut.mapper.RecordDOMapper;
import edu.dgut.server.AccountServer;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountServer accountServer;

    @Autowired
    MinioClient minioClient;

    @Autowired
    RecordDOMapper recordDOMapper;

    @PostMapping("/login")
    public String checkAccount(@RequestParam("userName") String name, @RequestParam("password") String password,
                               HttpServletRequest request){
        Account account = accountServer.getAccount(name, password);
        if(account == null){
            return "redirect:/login";
        }
        // 设置会话状态
        request.getSession().setAttribute("user", account.getUserName());
        try{
            // Check if the bucket already exists.
            boolean isExist =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if(!isExist) {
                // Make a new bucket  to hold a zip file of photos.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            }
        }catch (MinioException e){
            System.out.println("Minio Error occurred: " + e);
        }catch (Exception e){
            System.out.println("Error occurred: " + e);
        }

        return "redirect:/showPic";
    }

    //获取图片

    //申请设备同步
    @PostMapping("/reqSync")
    @ResponseBody
    public HashMap<String,String> reqSyncthing(@RequestParam(value = "devId",required = true) String devId,@RequestParam(value = "devName",required = true) String devName,HttpServletRequest request){
        HashMap<String,String> hashMap = new HashMap<>();
        if (devId.equals("") || devName.equals("")){
            hashMap.put("msg", "申请失败，请输入相关信息！");
            return hashMap;
        }
        RecordDO recordDO = new RecordDO();
        recordDO.setDevid(devId);
        recordDO.setDevname(devName);
        recordDO.setUsername((String)request.getSession().getAttribute("user"));
        recordDO.setFinish("F");
        int result = recordDOMapper.insert(recordDO);
        if(result == 1){
            hashMap.put("msg", "申请成功，请耐心等待！");
        }else{
            hashMap.put("msg", "申请失败，请稍后重试！");
        }
        return hashMap;
    }


    //退出登陆
    @RequestMapping("/loginOut")
    public String loginOutSys(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "redirect:/login";
    }
}
