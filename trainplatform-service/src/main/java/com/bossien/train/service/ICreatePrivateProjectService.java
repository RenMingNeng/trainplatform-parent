package com.bossien.train.service;

import com.bossien.train.domain.ProjectBasic;
import com.bossien.train.domain.User;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/26.
 */
public interface ICreatePrivateProjectService {

   /**
    * 保存项目基础信息
    * @param param
    * @param user
    * @return
    */
   ProjectBasic saveProjectBasicInfo(Map<String, Object> param, User user) throws Exception;

    /**
     * 保存项目用户、项目详情、项目课程统计信息
     * @param param
     */
   void saveProjectInfo(Map<String, Object> param, User user) throws Exception;

    /**
     * 保存项目课程信息
     * @param params
     */
    List<String> saveProjectCourseInfo(Map<String, Object> params, User user) throws Exception;

    /**
     * 删除项目课程信息
     * @param param
     */
    void deleteProjectCourse(Map<String, Object> param);

    /**
     * 批量删除项目用户
     * @param param
     */
    void deleteProjectUser(Map<String, Object> param);

    /**
     * 保存项目课程时保存相应的统计信息
     * @param param
     */
    void saveProjectByCourse(Map<String, Object> param);

    /**
     * 保存项目用户时保存相应的统计信息
     * @param param
     */
    void saveProjectByUser(Map<String, Object> param) throws ParseException;

    /**
     * 删除项目课程时修改相应的统计信息
     * @param param
     * @return
     */
    int deleteProjectByCourse(Map<String, Object> param);

    /**
     * 删除项目用户时修改相应的统计信息
     * @param param
     * @return
     */
    int deleteProjectByUser(Map<String, Object> param);

 /**
  * 点发布修改项目信息
  * @param params
  * @return
  * @throws ParseException
  */
 int updateInfo(Map<String, Object> params) throws ParseException;


 /**
  * 修改项目
  * @param user
  * @param params
  */
 void updateProject(User user, Map<String,Object> params);

 /**
  * 消息推送的修改projectInfo和projectDossier表数据
  * @param params
  */
 void update(Map<String, Object> params);

 /**
  * 保存统计信息
  * @param params
  */
 void saveInfo(Map<String, Object> params);


 /**
  * 保存项目用户
  * @param params
  * @throws ParseException
  */
 void insertProjectUser(Map<String, Object> params);

 /**
  * 修改统计信息
  * @param params
  */
 void updateInfoDetail(Map<String, Object> params);

 /**
  * 根据角色保存信息
  * @param params
  */
 void saveByProjectRole(Map<String, Object> params, User user) throws Exception;

 /**
  * 修改项目用户
  * @param params
  */
 void updateProjectUser(Map<String, Object> params);
}
