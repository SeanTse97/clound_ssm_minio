package edu.dgut.server;

import edu.dgut.domain.PublicDO;

import java.util.List;

public interface PublicBucketServer {
    //获取所有的数据内容
    public List<PublicDO> getAll();

    //插入数据信息
    public int insertData(PublicDO publicDO);

    //分页获取信息
    public List<PublicDO> getByPage(Integer id);
}
