package com.lzz.tools;

import com.lzz.tools.helper.RsaUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

/**
 * Created by beny.lin on 2017/8/4.
 */
public class RsaUtilsTest {

    public static void main(String[] args) throws Exception {
        //Java生成公钥私钥

        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
        //初始化密钥生成器

        /**
         * 密钥长度，DH算法的默认密钥长度是1024
         * 密钥长度必须是64的倍数，在512到65536位之间
         * */
        keyPairGenerator.initialize(1024);
        //生成密钥对
        KeyPair keyPair=keyPairGenerator.generateKeyPair();
        //甲方公钥
        RSAPublicKey publicKey=(RSAPublicKey) keyPair.getPublic();
        System.out.println("系数："+ publicKey.getModulus()+"  加密指数："+publicKey.getPublicExponent());
        //甲方私钥
        RSAPrivateKey privateKey=(RSAPrivateKey) keyPair.getPrivate();
        System.out.println("系数："+ privateKey.getModulus()+"  解密指数："+privateKey.getPrivateExponent());

        System.out.println("公钥："+ Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("私钥："+ Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        //加密字符串
        String message = "我是要加密的内容";
        String messageEn = RsaUtils.encrypt(message, Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println(message + "\n加密后的字符串为:" + messageEn);
        String messageDe = RsaUtils.decrypt(messageEn, Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("还原后的字符串为:" + messageDe);

    }

}
