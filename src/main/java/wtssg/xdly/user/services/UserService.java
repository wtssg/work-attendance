package wtssg.xdly.user.services;

import wtssg.xdly.user.entity.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface UserService {
    int createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    User findUserByUsername(String username);
}
