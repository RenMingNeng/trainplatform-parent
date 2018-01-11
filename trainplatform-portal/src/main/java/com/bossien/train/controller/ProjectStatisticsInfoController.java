package com.bossien.train.controller;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.ProjectStatisticsInfo;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IProjectStatisticsInfoService;
import com.bossien.train.service.IUserService;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.MathUtil;
import com.bossien.train.util.ParamsUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017-07-25.
 */
@Controller
@RequestMapping(value="/statistics")
public class ProjectStatisticsInfoController extends BasicController{

    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IUserService userService;

    /**
     * 统计学员在项目中的学习情况
     * @return
     */
    @RequestMapping("/studentStatistics")
    @ResponseBody
    public Object student_tongji(
            @RequestParam(value="project_basicId", required = false, defaultValue = "") String project_basicId,  //项目序号
            @RequestParam(value="project_userId", required = false, defaultValue = "") String project_userId     //用户序号
        ){
        Response<Object> resp = new Response<Object>();
        ProjectStatisticsInfo project_statistics_info = new ProjectStatisticsInfo();
        project_statistics_info.setProjectId(project_basicId);
        project_statistics_info.setUserId(project_userId);
        project_statistics_info = projectStatisticsInfoService.statistics(project_statistics_info);
        resp.setResult(project_statistics_info);
        return resp;
    }

    /**
     * 查询项目用户档案信息
     * @param projectId
     * @param roleId
     * @param trainUserName
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping("/project_statistics_list")
    @ResponseBody
    public Object projectStatisticsList(@RequestParam(value = "projectId", required = false, defaultValue = "") String projectId,  //项目id
        @RequestParam(value = "roleId", required = false, defaultValue = "") String roleId,                    //角色id
        @RequestParam(value = "type", required = false, defaultValue = "") String type,                        //操作类型
        @RequestParam(value = "trainUserName", required = false, defaultValue = "") String trainUserName,      //用户名称
        @RequestParam(value = "examUserName", required = false, defaultValue = "") String examUserName,        //用户名称
        @RequestParam(value = "pageSize", required = true, defaultValue = "1") Integer pageSize,               //每页条数
        @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum                  //页码
    ) {
        Response <Object> response = new Response <Object>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        Map <String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        params.put("roleId", roleId);
        if("1".equals(type)){
            params.put("userName", ParamsUtil.joinLike(trainUserName));
        }else{
            params.put("userName", ParamsUtil.joinLike(examUserName));
        }
        params.put("startNum", (pageNum - 1) * pageSize);
        params.put("endNum", pageSize);
        //项目用户档案信息
        List<Map <String, Object>> result = projectStatisticsInfoService.selectList(params);
        for (Map<String, Object> map: result) {
            // 学员进度（已修学时/应修学时）
            String processPercent = "0%";
            // 已修学时
            double sumIntStudyTime1 = 0.00d;
            // 应修学时
            double sumIntRequirement = 0.00d;
            Map<String, Object> param = new HashedMap();
            param.put("id", map.get("user_id"));
            Map<String, Object> param_ = userService.queryUserInfoById(param);
            if(param_!=null){
                map.put("mobile_no", param_.get("mobile_no"));
            }
            sumIntStudyTime1 = Double.valueOf(map.get("total_studytime").toString());
            sumIntRequirement = Double.valueOf(map.get("requirement_studytime").toString());
            if(sumIntRequirement != 0 && (sumIntStudyTime1 - sumIntRequirement*60)<=0){
                processPercent = MathUtil.percent(sumIntStudyTime1,sumIntRequirement*60);
            }else{
                if(sumIntStudyTime1 != 0){
                    processPercent = "100%";
                }
            }
            map.put("train_process", processPercent);
        }
        //项目用户档案数量
        int count = projectStatisticsInfoService.selectCount(params);
        page = new Page(count, pageNum, pageSize);
        response.setCode(Code.SUCCESS.getCode());
        page.setDataList(result);
        response.setResult(page);
        return response;
    }
}
