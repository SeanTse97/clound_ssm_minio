package edu.dgut.mapper;

import edu.dgut.domain.Account;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMapper {

    //通过用户姓名，获取用户对象
    public Account getAccountByName(String name);

    //通过用户姓名，获取用户对象
    public Account getAccount(@Param("name") String name, @Param("password")String password);

}
