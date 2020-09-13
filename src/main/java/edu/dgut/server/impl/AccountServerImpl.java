package edu.dgut.server.impl;

import edu.dgut.domain.Account;
import edu.dgut.mapper.AccountMapper;
import edu.dgut.server.AccountServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServerImpl implements AccountServer {
    @Autowired
    AccountMapper accountMapper;

    @Override
    public Account getAccountByName(String name) {
        return accountMapper.getAccountByName(name);
    }

    @Override
    public Account getAccount(String name, String password) {
        return accountMapper.getAccount(name,password);
    }
}
