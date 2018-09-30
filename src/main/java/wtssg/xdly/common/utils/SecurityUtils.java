package wtssg.xdly.common.utils;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wt
 * 用于密码的加密和比对
 */
public class SecurityUtils {

    /**
     * MD5加密设置的密码
     * @param password
     * @return
     * 加密过的密码的字符串
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String encryptedPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String result = base64Encoder.encode(md5.digest(password.getBytes("utf-8")));
        return result;
    }

    /**
     * 登录时密码的校验
     * @param inputPwd
     * 输入的登录密码
     * @param dbPwd
     * 数据库中加密过的密码
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static boolean checkPassword(String inputPwd, String dbPwd) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String result = encryptedPassword(inputPwd);
        if (result.equals(dbPwd)) {
            return true;
        }
        return false;
    }
}
