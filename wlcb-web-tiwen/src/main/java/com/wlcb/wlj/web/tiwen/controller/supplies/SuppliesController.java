package com.wlcb.wlj.web.tiwen.controller.supplies;

import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.common.service.tiwen.SuppliesService;
import com.wlcb.wlj.module.common.utils.BeanUtil;
import com.wlcb.wlj.module.common.utils.ReturnJsonUtil;
import com.wlcb.wlj.module.common.utils.cache.LoginTokenCache;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.tiwen.TblSupplies;
import com.wlcb.wlj.module.dbs.entity.tiwen.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName TiwenController
 * @Description TODO 体温调用入口Controller
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("supplies")
public class SuppliesController {

    private static final Logger logger = LoggerFactory.getLogger(SuppliesController.class);

    @Resource
    private SuppliesService suppliesService;

    @RequestMapping(value = "/list",method = RequestMethod.POST,produces="application/json")
    public ResponseData list(User user ,TblSupplies supplies, HttpServletRequest request, HttpServletResponse response){

        PageBean<Map<String, Object>> pageBean = suppliesService.listPage(user,supplies);

        return ReturnJsonUtil.printJson(200,"查询成功",pageBean,true);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST,produces="application/json")
    public ResponseData add(User user, TblSupplies supplies, HttpServletRequest request, HttpServletResponse response){

        ResponseData responseData = BeanUtil.allFieldIsNULL(user,
                "openid",
                "name",
                "phone",
                "idcard");
        if (responseData.getCode() == 406){
            return responseData;
        }

        responseData = BeanUtil.allFieldIsNULL(supplies,
                "openid",
                "bayonetNumber",
                "bayonetName",
                "supplies",
                "quxian",
                "jiedao",
                "shequ",
                "xiaoqu");
        if (responseData.getCode() == 406){
            return responseData;
        }

        Integer count = suppliesService.add(user,supplies);

        if (count > 0){
            return ReturnJsonUtil.printJson(201,"新增成功",true);
        }else {
            return ReturnJsonUtil.printJson(400,"新增失败",false);
        }
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT,produces="application/json")
    public ResponseData update(TblSupplies supplies, HttpServletRequest request, HttpServletResponse response){

        ResponseData responseData = BeanUtil.allFieldIsNULL(supplies,"id");
        if (responseData.getCode() == 406){
            return responseData;
        }

        Integer count = suppliesService.update(supplies);

        if (count > 0){
            return ReturnJsonUtil.printJson(201,"更新成功",true);
        }else {
            return ReturnJsonUtil.printJson(400,"更新失败",false);
        }
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE,produces="application/json")
    public ResponseData delete(String id, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(id)){
            return ReturnJsonUtil.printJson(406,"id不可为空",false);
        }

        Integer count = suppliesService.delete(id);

        if (count > 0){
            return ReturnJsonUtil.printJson(201,"删除成功",true);
        }else {
            return ReturnJsonUtil.printJson(400,"删除失败",false);
        }
    }

}
