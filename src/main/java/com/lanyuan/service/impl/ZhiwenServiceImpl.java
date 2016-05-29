package com.lanyuan.service.impl;

import com.alibaba.fastjson.JSON;
import com.lanyuan.entity.CustomZhiwenFormMap;
import com.lanyuan.entity.EquipmentFormMap;
import com.lanyuan.mapper.CustomZhiwenMapper;
import com.lanyuan.mapper.EquipmentMapper;
import com.lanyuan.service.IZhiwenService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liwanzhong on 2016/5/9.
 */
@Service
public class ZhiwenServiceImpl implements IZhiwenService {

    private static Logger logger = LoggerFactory.getLogger(ZhiwenServiceImpl.class);

    @Autowired
    private CustomZhiwenMapper customZhiwenMapper;



    @Autowired
    private EquipmentMapper equipmentMapper;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 记录用户的指纹信息
     * @param customId 检测的用户id
     * @param zhiwenCode   指纹码（二进制码的Base64后的字符串）
     * @param filePath  保存的文件路径
     * @param position  指纹位置
     * @param instrument 检测仪器id
     * @throws Exception
     */
    public void record(Long customId,String zhiwenCode, String filePath,Integer position,String instrument) throws Exception {
        logger.info("=========================================记录指纹信息================================");
        //TODO 解析指纹数据为map
        Map zhiwenMap = JSON.parseObject(zhiwenCode,Map.class);
        if(zhiwenMap == null){
            throw new Exception("没有指纹模板数据!");
        }
        // 获取仪器id
        EquipmentFormMap equipmentFormMap = equipmentMapper.findbyFrist("istmt_code",instrument, EquipmentFormMap.class);
        if(equipmentFormMap == null){
            throw new Exception("无效检测");
        }
        Set keySet = zhiwenMap.keySet();
        Iterator iterator = keySet.iterator();

        while (iterator.hasNext()){
            CustomZhiwenFormMap customZhiwenFormMap = new CustomZhiwenFormMap();
            customZhiwenFormMap.put("custom_id",customId);
            Object key = iterator.next();
            customZhiwenFormMap.put("zhiwen_code",zhiwenMap.get(key));
            customZhiwenFormMap.put("zhiwen_path",filePath);
            customZhiwenFormMap.put("insert_time",simpleDateFormat.format(new Date()));
            customZhiwenFormMap.put("finger_position",key);
            customZhiwenFormMap.put("Instrument_id",equipmentFormMap.getLong("id"));

            customZhiwenMapper.addEntity(customZhiwenFormMap);
        }



    }


    /**
     * 通过用户id获取 用户所有指纹
     * @param customId  用户id
     * @return List<String>
     * @throws Exception
     */
    public List<String> queryZhiwenByCustomId(Long customId)throws Exception{
        logger.info("==========================查询用户指纹=====["+customId+"]======================================");
        List<String> zhiwenList = new ArrayList<String>();

        //通过用户id查询指纹列表
        CustomZhiwenFormMap customZhiwenFormMap = new CustomZhiwenFormMap();
        customZhiwenFormMap.put("custom_id",customId);

        List<CustomZhiwenFormMap> customZhiwenFormMapList =  customZhiwenMapper.findList(customZhiwenFormMap);
        if(CollectionUtils.isNotEmpty(customZhiwenFormMapList)&&customZhiwenFormMapList.size()>0){
            for(CustomZhiwenFormMap item:customZhiwenFormMapList){
                zhiwenList.add(item.getStr("zhiwen_code"));
            }
        }
        if(CollectionUtils.isNotEmpty(zhiwenList)&&zhiwenList.size()>0){
            return zhiwenList;
        }
        return null;
    }
}
