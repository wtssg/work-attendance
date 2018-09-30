package wtssg.xdly.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wtssg.xdly.common.utils.SecurityUtils;
import wtssg.xdly.user.dao.UserMapper;
import wtssg.xdly.user.entity.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public int createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        user.setPassword(SecurityUtils.encryptedPassword(user.getPassword()));
        return userMapper.insert(user);
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        return user;
    }
}
