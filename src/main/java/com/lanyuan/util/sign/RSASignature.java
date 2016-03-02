package com.lanyuan.util.sign;

import com.lanyuan.util.Base64;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;



/**
 * RSA签名验签类
 */
public class RSASignature{

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String private_key_pkcs8= SignConfigLoader.getProperties().getProperty("sign.private_key_pkcs8");

    private static final String public_key= SignConfigLoader.getProperties().getProperty("sign.public_key");

    private static final String encoding = SignConfigLoader.getProperties().getProperty("sign.encoding");

    private static String privateKeyString = null;

    private static String publicKeyString = null;


    static {
        try {
            loadPrivateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            loadPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载私钥
     * @throws Exception
     */
    public static void loadPrivateKey() throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(private_key_pkcs8));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            privateKeyString = sb.toString();
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }


    /**
     * 加载公钥
     * @throws Exception
     */
    public static void loadPublicKey() throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(public_key));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            publicKeyString = sb.toString();
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }


    /**
     * 签名
     * @param content
     * @return
     */
    public static String sign(String content){
        try{
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec( Base64.decode(privateKeyString) );
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update( content.getBytes(encoding));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 验签
     * @param content
     * @param sign
     * @return
     */
    public static boolean verify(String content, String sign){
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKeyString);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature .getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update( content.getBytes(encoding) );

            return signature.verify( Base64.decode(sign) );

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
