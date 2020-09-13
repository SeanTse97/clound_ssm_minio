package edu.dgut.server.impl;

import edu.dgut.domain.PrivateDO;
import edu.dgut.mapper.PrivateDOMapper;
import edu.dgut.server.PrivateBucketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class PrivateBucketServerImpl implements PrivateBucketServer {

    @Autowired
    private PrivateDOMapper privateDOMapper;

    @Override
    public List<PrivateDO> getByName(String name) {
        List<PrivateDO> list = new LinkedList<>();
        list = privateDOMapper.getByName(name);
        return list;
    }

    @Override
    public int insertData(PrivateDO privateDO) {
        int result = privateDOMapper.insert(privateDO);
        return result;
    }

    @Override
    public List<PrivateDO> getPage(String name, Integer page) {
        return privateDOMapper.getPage(name,(page-1)*9);
    }
}
