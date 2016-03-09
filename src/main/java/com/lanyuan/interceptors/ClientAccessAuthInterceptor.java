package com.lanyuan.interceptors;

import com.lanyuan.util.sign.MapWithStringConvert;
import com.lanyuan.util.sign.RSASignature;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liwanzhong on 2016/3/1.
 */
public class ClientAccessAuthInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest req, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        //todo 进行验证签名(签名通过返回true，否则返回false)
        try{
            req.setCharacterEncoding("ISO-8859-1");
            String encoding = req.getParameter("UTF-8");
            // 获取请求参数中所有的信息
            Map<String, String> reqParam = getAllRequestParam(req);
            // 打印请求报文
            System.out.println("请求参数：====>"+reqParam);

            Map<String, String> valideData = null;
            if (null != reqParam && !reqParam.isEmpty()) {
                Iterator<Map.Entry<String, String>> it = reqParam.entrySet().iterator();
                valideData = new HashMap<String, String>(reqParam.size());
                while (it.hasNext()) {
                    Map.Entry<String, String> e = it.next();
                    String key =  e.getKey();
                    String value =  e.getValue();
//                    value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                    valideData.put(key, value);
                }
            }

            // 验证签名
            if (!validate(valideData)) {
                System.out.println("验证签名结果[失败].");
                return false;
            } else {
                System.out.println("验证签名结果[成功].");
                return true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return false;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle------------------>[ClientAccessAuthInterceptor]");
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {
        System.out.println("afterCompletion------------------>[ClientAccessAuthInterceptor]");
    }

    private static boolean validate(Map<String, String> resData) throws Exception{
        String stringSign = resData.get("signature");
        if(StringUtils.isBlank(stringSign)){
            throw new Exception("无效请求!");
        }
        System.out.println("返回报文中signature=[" + stringSign + "]");
        String stringData = MapWithStringConvert.coverMap2String(resData);
        System.out.println("返回报文中(不含signature域)的stringData=[" + stringData + "]");
        return RSASignature.verify(stringData,stringSign);
    }

    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }
}
