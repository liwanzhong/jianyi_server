package com.lanyuan.util.sign;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2016/3/2.
 */
public class SignConfigLoader {

    public  String FILE_NAME = "sign.properties";


    private  String private_key_pkcs8;

    private  String public_key;

    private  String encoding;


    private static SignConfigLoader ourInstance = new SignConfigLoader();

    private static Properties properties;



    public static Properties getProperties() {
        return properties;
    }

    private SignConfigLoader() {
        if(properties==null){
            InputStream in=null;
            try {
                in = SignConfigLoader.class.getClassLoader().getResourceAsStream(FILE_NAME);
                if(in != null) {
                    this.properties = new Properties();
                    try {
                        this.properties.load(in);
                    } catch (IOException var12) {
                        throw var12;
                    }
                }
            } catch (IOException var13) {
                var13.printStackTrace();
            } finally {
                if(in != null) {
                    try {
                        in.close();
                    } catch (IOException var11) {
                        var11.printStackTrace();
                    }
                }

            }
        }
    }
}
