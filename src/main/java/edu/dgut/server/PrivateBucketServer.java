package edu.dgut.server;

import edu.dgut.domain.PrivateDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrivateBucketServer {

    //获取某个对象所有的数据内容
    public List<PrivateDO> getByName(String name);

    //插入数据信息
    public int insertData(PrivateDO privateDO);

    //获取分页信息
    List<PrivateDO> getPage( String name, Integer page);
}
