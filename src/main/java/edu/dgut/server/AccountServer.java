package edu.dgut.server;

import edu.dgut.domain.Account;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface AccountServer {

    //通过用户姓名，获取用户对象
    public Account getAccountByName(String name);

    //通过用户姓名，获取用户对象
    public Account getAccount(@Param("userName") String name, @Param("password")String password);

}
