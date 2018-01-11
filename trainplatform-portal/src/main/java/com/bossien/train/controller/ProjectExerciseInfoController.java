package com.bossien.train.controller;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IProjectExerciseOrderService;
import com.bossien.train.util.ParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-25.
 */
@Controller
@RequestMapping(value="/exercise")
public class ProjectExerciseInfoController extends BasicController{

    @Autowired
    private IProjectExerciseOrderService projectExerciseOrderService;

    /**
     * 查看练习排行
     * @param projectId
     * @param modelMap
     * @return
     */
    @RequestMapping("exercise_order")
    public String exercise_order_info(HttpServletRequest request,
                                      @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId , //项目id
                                      @RequestParam(value = "projectTypeName", required = false,defaultValue = "")  String projectTypeName , //项目类型名称
                                      ModelMap modelMap) {
        User user =getCurrUser(request);
        modelMap.addAttribute("userId",user.getId());
        modelMap.addAttribute("projectId",projectId);
        modelMap.addAttribute("projectTypeName",projectTypeName);
        return "student/projectRecord/exercise_order";
    }

    /**
     * 显示项目中用户练习排行记录
     * @param projectId
     * @param orderType
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/user_exercise")
    @ResponseBody
    public Object userExercise(HttpServletRequest request,
                               @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,     //项目Id
                               @RequestParam(value = "orderType", required = false,defaultValue = "1")  String orderType,  //排行类型
                               @RequestParam(value="pageSize", required = false, defaultValue = "1") Integer pageSize,     //每页条数
                               @RequestParam(value="pageNum", required = false, defaultValue = "1") Integer pageNum        //页码
    ) throws ServiceException{
        User user=getCurrUser(request);
        Response<Object> response=new Response <Object>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        //每页条数过大
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST,"pageSize 参数过大");
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("projectId",projectId);
        params.put("orderType",orderType);
        params.put("userId",user.getId());
        params.put("startNum",(pageNum-1)*pageSize);
        params.put("endNum",pageSize);
        //个人练习排行
        List<Map<String, Object>> exerciseOrder = projectExerciseOrderService.selectProjectExerciseOrderList(params);
        response.setCode(Code.SUCCESS.getCode());
        page.setDataList(exerciseOrder);
        response.setResult(page);
        return response;
    }

    /**
     * 显示项目中用户练习排行记录
     * @param projectId
     * @param userName
     * @param orderType
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/showProjectExerciseOrder")
    @ResponseBody
    public Object showProjectExerciseOrder(HttpServletRequest request,
                                           @RequestParam(value = "projectId", required = false,defaultValue = "")  String projectId,        //项目Id
                                           @RequestParam(value = "userName", required = false,defaultValue = "")  String userName,          //学员姓名
                                           @RequestParam(value = "orderType", required = false,defaultValue = "1")  String orderType,       //排行类型
                                           @RequestParam(value = "operateType", required = false,defaultValue = "")  String operateType,    //排行类型
                                           @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,          //每页条数
                                           @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum              //页码
    ) throws ServiceException{
        User user=getCurrUser(request);
        Response<Object> response=new Response <Object>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();

        //每页条数过大
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST,"pageSize 参数过大");
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("projectId",projectId);
        params.put("userName", ParamsUtil.joinLike(userName));
        params.put("orderType",orderType);
        //练习排行次数
        int count = projectExerciseOrderService.selectProjectExerciseOrderCount(params);
        if("student".equals(operateType)){
            params.put("user_id",user.getId());
            params.put("startNum",(pageNum-1)*(pageSize-1));
            params.put("endNum",pageSize-1);
        }else{
            params.put("startNum",(pageNum-1)*pageSize);
            params.put("endNum",pageSize);
        }
        //练习排行记录
        List<Map<String, Object>> exerciseOrder = projectExerciseOrderService.selectProjectExerciseOrderList(params);
        page = new Page(count, pageNum, pageSize);
        response.setCode(Code.SUCCESS.getCode());
        page.setDataList(exerciseOrder);
        response.setResult(page);
        return response;
    }
}
