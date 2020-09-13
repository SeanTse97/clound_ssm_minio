package edu.dgut.server.impl;

import edu.dgut.domain.PublicDO;
import edu.dgut.mapper.PublicDOMapper;
import edu.dgut.server.PublicBucketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PublicBucketServerImpl implements PublicBucketServer {
    @Autowired
    private PublicDOMapper publicDOMapper;

    @Override
    public List<PublicDO> getAll() {
        return publicDOMapper.getAll();
    }

    @Override
    @Transactional
    public int insertData(PublicDO publicDO) {
        return publicDOMapper.insert(publicDO);
    }

    @Override
    @Transactional
    public List<PublicDO> getByPage(Integer id) {
        return publicDOMapper.getByPage((id.intValue()-1)*9);
    }
}
