package com.wlcb.wlj.module.common.service.sync.impl;

import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.service.sync.SyncService;
import com.wlcb.wlj.module.common.utils.CsvUtil;
import com.wlcb.wlj.module.common.utils.ReturnJsonUtil;
import com.wlcb.wlj.module.common.utils.SplitList;
import com.wlcb.wlj.module.dbs.dao.sync.SyncMapper;
import com.wlcb.wlj.module.dbs.entity.sync.ImportInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Service("syncService")
public class SyncServiceImpl implements SyncService {

    private static final Logger logger = LoggerFactory.getLogger(SyncServiceImpl.class);

    @Autowired
    private SyncMapper syncMapper;

    @Override
    public ResponseData sync(ImportInfo importInfo){

        try {


            List<String> listFileds = new ArrayList<>(Arrays.asList(importInfo.getFileds().split(",")));

            InputStream inputStream = importInfo.getFile().getInputStream();


            List<Map<String,Object>> list = CsvUtil.readCSVFileData(inputStream,listFileds,importInfo.getStartColumn(),importInfo.getEndColumn(),importInfo.getStartRow());


            List<List<Map<String,Object>>> phoneSplit = SplitList.splitList(list,importInfo.getLimit());


            int counts = 0;

            for (List<Map<String, Object>> listMap : phoneSplit) {
                int count = syncMapper.insterList(listMap,listFileds,importInfo.getTableName());
                counts = counts+count;
            }

            logger.info("导入成功：共{}行数据，成功{}行数据",list.size(),counts);

            return ReturnJsonUtil.printJson(0,"导入成功：共"+list.size()+"行数据，成功"+counts+"行数据",true);
        } catch (Exception e) {
            logger.error("读取文件内容失败：{}",e.getMessage());
            return ReturnJsonUtil.printJson(-1,"文件导入失败",false);
        }

    }

}
