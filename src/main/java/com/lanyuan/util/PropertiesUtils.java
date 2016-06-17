package com.lanyuan.util;

import org.apache.ibatis.io.Resources;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
/**
 * 对属性文件操作的工具类
 * 获取，新增，修改
 * 注意：	以下方法读取属性文件会缓存问题,在修改属性文件时，不起作用，
 *　InputStream in = PropertiesUtils.class.getResourceAsStream("/config.properties");
 *　解决办法：
 *　String savePath = PropertiesUtils.class.getResource("/config.properties").getPath();
 * @author lanyuan
 * 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
public class PropertiesUtils {
	public static final String REPORT_URL_PDF_GEN_MAIN = "REPORT_URL_PDF_GEN_MAIN";
	public static final String REPORT_URL_PDF_GEN_ITEM = "REPORT_URL_PDF_GEN_ITEM";
	public static final String REPORT_PDF_SAVED_PAHT = "REPORT_PDF_SAVED_PAHT";
	public static final String REPORT_URL_PDF_GEN_SYN = "REPORT_URL_PDF_GEN_SYN";


	public static final String REMOTE_WEB_DRIVER_REQ_HTTP = "REMOTE_WEB_DRIVER_REQ_HTTP";



	/**
	 * 获取属性文件的数据 根据key获取值
	 * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param key
	 * @return
	 */
	public static String findPropertiesKey(String key) {

		try {
			Properties prop = getProperties();
			return prop.getProperty(key);
		} catch (Exception e) {
			return "";
		}

	}

	public static void main(String[] args) {
		Properties prop = new Properties();
		InputStream in = PropertiesUtils.class
				.getResourceAsStream("/config.properties");
		try {
			prop.load(in);
			Iterator<Entry<Object, Object>> itr = prop.entrySet().iterator();
			while (itr.hasNext()) {
				Entry<Object, Object> e = (Entry<Object, Object>) itr.next();
				System.err.println((e.getKey().toString() + "" + e.getValue()
						.toString()));
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 返回　Properties
	 * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param
	 * @return
	 */
	public static Properties getProperties(){
		Properties prop = new Properties();
		try {
			Reader reader = Resources.getResourceAsReader("/config.properties");
			prop.load(reader);
		} catch (Exception e) {
			return null;
		}
		return prop;
	}

	public static Properties getjdbcProperties(){
		Properties prop = new Properties();
		try {
			Reader reader = Resources.getResourceAsReader("/jdbc.properties");
			prop.load(reader);
		} catch (Exception e) {
			return null;
		}
		return prop;
	}
	/**
	 * 写入properties信息
	 *
	 * @param key
	 *            名称
	 * @param value
	 *            值
	 */
	public static void modifyProperties(String key, String value) {
		try {
			// 从输入流中读取属性列表（键和元素对）
			Properties prop = getProperties();
			prop.setProperty(key, value);
			String path = PropertiesUtils.class.getResource("/config.properties").getPath();
			FileOutputStream outputFile = new FileOutputStream(path);
			prop.store(outputFile, "modify");
			outputFile.close();
			outputFile.flush();
		} catch (Exception e) {
		}
	}
}
