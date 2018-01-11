package com.bossien.framework.event.listener;

import com.bossien.framework.event.Event001;
import com.bossien.framework.event.Event002;
import com.bossien.framework.event.Event003;
import com.bossien.train.domain.CompanyTj;
import com.bossien.train.domain.Department;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ProjectStatusEnum;
import com.bossien.train.interceptor.LoginInterceptor;
import com.bossien.train.listener.SystemContextLoaderListener;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/9/26.
 */
public class EventListener implements ApplicationListener {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    /**
     * publishEvent触发该方法
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof Event001) {        // 学员登陆之前查询当天需要校验项目状态的项目
            Event001 event001 = (Event001) event;
            this.handlerEvent001((Map)event001.getSource());
        } else if(event instanceof Event002) {  // 修改部门名称之后, 发送event更新人员表和人员档案表中的部门名称
            Event002 event002 = (Event002) event;
            this.handlerEvent002((Map)event002.getSource());
        }else if(event instanceof Event003) {  // 管理员登录后, 发送event更新统计表的相关字段
            Event003 event003 = (Event003) event;
            this.handlerEvent003((Map)event003.getSource());
        }
        return;
    }

    private void handlerEvent001(Map<String, Object> params) {
        try {
            // 项目状态更新
            IProjectInfoService projectService = (IProjectInfoService) SystemContextLoaderListener.getBean("projectInfoService");
            Integer count = projectService.checkProjectStatus(params);
            logger.debug(MessageFormat.format("项目状态更新, 个数为{0}", count));

            // 接入sso之后人员档案由登录去触发产生
            String userId = (String) params.get("userId");
            if(StringUtils.isNotEmpty(userId)) {
              IPersonDossierService personDossierService   = (IPersonDossierService) SystemContextLoaderListener.getBean("personDossierService");
                Map<String,Object> personDossienMap = personDossierService.selectOne(params);
                if(null == personDossienMap) {
                    // 新增人员档案表：人员第一次登陆时
                    personDossierService.add(params);
                } else {
                    // 更新人员档案表：档案中学员名称与学员真正名称不一致时
                    String userName = (String) params.get("userName");
                    String dossienUserName = (String) personDossienMap.get("user_name");
                    if(!StringUtils.equals(userName, dossienUserName)) {
                        personDossierService.update(params);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(MessageFormat.format("handlerEvent001 error", ex));
        }
    }

    private void handlerEvent002(Map<String, Object> params) {
        try {
            IUserManagerService userManagerService = (IUserManagerService) SystemContextLoaderListener.getBean("userManagerService");
            // 部门id
            String departmentId = (String) params.get("departmentId");
            // 部门name
            String departmentName = (String) params.get("departmentName");
            String departmentNames = (String) params.get("departmentNames");
            String companyId = (String) params.get("companyId");
            Map<String, Object> paras=new HashMap<String, Object>();
            paras.put("id",departmentId);
            paras.put("isValid", Department.IsValid.TYPE_1.getValue());
            if(StringUtils.isNotEmpty(departmentId)){
                saveInfo(departmentId,companyId,departmentNames,departmentName,userManagerService);
                IDepartmentService departmentService = (IDepartmentService)SystemContextLoaderListener.getBean("departmentService");
                Department department = departmentService.selectOne(paras);
                if(department != null){
                    paras.clear();
                    paras.put("parentId",department.getId());
                    List<Department> deptList = departmentService.selectDepartmentByCompanyId(paras);
                    for (Department department1:deptList) {
                        saveInfo(department1.getId(),companyId,departmentName,department1.getDeptName(),userManagerService);
                    }
                }
            }
            // 人员表user 更新部门名称
            User usr=new User();
            usr.setDepartmentId(departmentId);
            usr.setDepartmentName(departmentName);
            userManagerService.updateDeptName(usr);
            // 项目部门表更新部门名称
            IProjectDepartmentService projectDepartmentService = (IProjectDepartmentService) SystemContextLoaderListener.getBean("projectDepartmentService");
            Map<String, Object> map = new HashedMap();
            map.put("companyId",companyId);
            map.put("deptId",departmentId);
            map.put("deptName",departmentName);
            projectDepartmentService.update(map);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("handlerEvent002 error", ex));
        }

    }

    private void saveInfo(String departmentId, String companyId, String departmentNames, String departmentName, IUserManagerService userManagerService) {
        try {
            // 人员档案表person_dossier_info  更新部门名称
            Map<String, Object> param=new HashMap<String, Object>();
            Map<String, Object> para=new HashMap<String, Object>();
            param.put("deptId",departmentId);
            param.put("isValid", Department.IsValid.TYPE_1.getValue());
            param.put("companyId",companyId);
            param.put("userType",User.UserType.TYPE_3.getValue());
            List<String> ulist = userManagerService.selectUserIds(param);

            IPersonDossierService personDossierService = (IPersonDossierService) SystemContextLoaderListener.getBean("personDossierService");
            IProjectUserService projectUserService = (IProjectUserService)SystemContextLoaderListener.getBean("projectUserService");
            IProjectExerciseOrderService projectExerciseOrderService = (IProjectExerciseOrderService)SystemContextLoaderListener.getBean("projectExerciseOrderService");
            IProjectStatisticsInfoService projectStatisticsInfoService = (IProjectStatisticsInfoService)SystemContextLoaderListener.getBean("projectStatisticsInfoService");
            if(ulist.size()>0){
                if(StringUtils.isNotEmpty(departmentNames)){
                    para.put("deptName",departmentNames+"/"+departmentName);
                }else{
                    para.put("deptName",departmentName);
                }
                para.put("userIds", ulist);
                //修改人员档案部门信息
                personDossierService.updateByUserIds(para);
                //修改项目用户信息
                projectUserService.update(para);
                //修改人员练习详情部门信息
                projectExerciseOrderService.updateInfo(para);
                //修改人员详情部门信息
                projectStatisticsInfoService.updateInfo(para);
            }
        } catch (Exception ex) {
            logger.error(MessageFormat.format("handlerEvent002 error", ex));
        }
    }


    private void handlerEvent003(Map<String, Object> params) {
        try {
            // 项目状态更新
            IProjectInfoService projectService = (IProjectInfoService) SystemContextLoaderListener.getBean("projectInfoService");
            Integer count = projectService.checkProjectStatus(params);
            logger.debug(MessageFormat.format("项目状态更新, 个数为{0}", count));


            // 更新统计表字段
            String companyId = (String) params.get("companyId");
            String userName = (String) params.get("userName");
            if(StringUtils.isNotEmpty(companyId)&&StringUtils.isNotEmpty(userName)) {
                ICompanyTjService companyTjService   = (ICompanyTjService) SystemContextLoaderListener.getBean("companyTjService");
                ICompanyCourseService companyCourseService   = (ICompanyCourseService) SystemContextLoaderListener.getBean("companyCourseService");
                ICourseInfoService courseInfoService   = (ICourseInfoService) SystemContextLoaderListener.getBean("courseInfoService");
                IProjectInfoService projectInfoService   = (IProjectInfoService) SystemContextLoaderListener.getBean("projectInfoService");
                //IUserService userService   = (IUserService) SystemContextLoaderListener.getBean("userService");

                CompanyTj companyTj = companyTjService.selectOne(companyId,userName);

                // 学员数量
                //Integer countUser = userService.selectUserCount(params);
                //课程数量
                Integer countCourse = companyCourseService.selectCompanyCourseCount(params);
                //题目数量
                Integer countQuestion = courseInfoService.selectCourseQuestionCount(params);

                List <String> projectStatus = new ArrayList<String>();
                projectStatus.add(ProjectStatusEnum.ProjectStatus_3.getValue());                                   //进行中
                projectStatus.add(ProjectStatusEnum.ProjectStatus_4.getValue());                                   //已结束
                params.put("projectStatus", projectStatus);
                // 项目数量
                Integer countProject = projectInfoService.selectProjectIdCount(params);



                companyTj.setCountCourse(countCourse.toString());
                companyTj.setCountQuestion(countQuestion.toString());
                companyTj.setCountProject(countProject.toString());
                //companyTj.setCountUser(countUser.toString());
                //最后操作时间、人员
                companyTj.setOperTime(DateUtils.formatDateTime(new Date()));
                companyTj.setOperUser(userName);
                Map<String, Object> companyTjMap = new HashMap<String, Object>();
                MapUtils.putAll(companyTjMap, companyTj);
                //更新数据
                companyTjService.update(companyTjMap);

            }
        } catch (Exception ex) {
            logger.error(MessageFormat.format("handlerEvent001 error", ex));
        }


    }
}
