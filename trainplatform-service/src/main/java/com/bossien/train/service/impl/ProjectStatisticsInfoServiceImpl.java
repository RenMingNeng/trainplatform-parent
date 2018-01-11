package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ProjectStatisticsInfoMapper;
import com.bossien.train.domain.ProjectStatisticsInfo;
import com.bossien.train.service.IProjectInfoService;
import com.bossien.train.service.IProjectStatisticsInfoService;
import com.bossien.train.service.IUserService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service("projectStatisticsInfoService")
public class ProjectStatisticsInfoServiceImpl implements IProjectStatisticsInfoService {

    @Autowired
    private ProjectStatisticsInfoMapper projectStatisticsInfoMapper;
   @Autowired
   private IProjectInfoService projectInfoService;
   @Autowired
   private IUserService userService;

    @Override
    public ProjectStatisticsInfo statistics(ProjectStatisticsInfo project_statistics_info) {

        return projectStatisticsInfoMapper.statistics(project_statistics_info );
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {

        return projectStatisticsInfoMapper.selectList(params);
    }

    @Override
    public List<ProjectStatisticsInfo> selectListByProjectId(String projectId) {

        return projectStatisticsInfoMapper.selectListByProjectId(projectId);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return projectStatisticsInfoMapper.selectCount(params);
    }

    @Override
    public ProjectStatisticsInfo selectOne(Map<String, Object> params) {
        return projectStatisticsInfoMapper.selectOne(params);
    }

    @Cacheable(value = "projectStatisticsInfoCache#(60 * 60)", key = "'selectOne'.concat('_').concat(#projectId).concat('_').concat(#userId)")
    @Override
    public ProjectStatisticsInfo selectOne(String projectId, String userId) {
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        params.put("userId", userId);
        return projectStatisticsInfoMapper.selectOne(params);
    }

    @Override
    public Integer update(ProjectStatisticsInfo projectStatisticsInfo) {
        return projectStatisticsInfoMapper.update(projectStatisticsInfo);
    }

    @Override
    public Integer insert(ProjectStatisticsInfo projectStatisticsInfo) {
        return projectStatisticsInfoMapper.insert(projectStatisticsInfo);
    }

    @Override
    public int insertBatch(List<Map<String, Object>> item) {
        return projectStatisticsInfoMapper.insertBatch(item);
    }

    @Override
    public List<Map<String, Object>> selectStatisticsInfoList(Map<String, Object> param) {
        return projectStatisticsInfoMapper.selectStatisticsInfoList(param);
    }

    @Override
    public int delete(Map<String, Object> params) {
        return projectStatisticsInfoMapper.delete(params);
    }
    /**
     * 获取参与培训或者考试人次
     * @param params
     * @return
     */
    @Override
    public int selectJoinTrainOrExamUserCount(Map<String, Object> params){
        //先获取进行中或已结束的并且是含有特定类型项目id
        List<String> projectIds=projectInfoService.selectProjectIds(params);
        if(projectIds.size() == 0){
            return 0 ;
        }
        params.put("projectIds",projectIds);
        //获取该公司下的有效学员ids
        params.put("isValid","1");                                //有效
        List<String> userIds=userService.selectUserIds(params);
        if(userIds.size() == 0){
            return 0;
        }
        params.put("userIds",userIds);
        return projectStatisticsInfoMapper.selectUserCount(params);
    }

    @Override
    public List<Map<String, Object>> selectListMap(Map<String, Object> params) {
        return projectStatisticsInfoMapper.selectListMap(params);
    }

    @Override
    public List<String> selectIdList(Map<String, Object> params) {
        return projectStatisticsInfoMapper.selectIdList(params);
    }

    @Override
    public int updateInfo(Map<String, Object> params) {
        return projectStatisticsInfoMapper.updateInfo(params);
    }

    @Override
    public int selectTrainCount(Map<String, Object> params) {
        return projectStatisticsInfoMapper.selectTrainCount(params);
    }

    @Override
    public List<Map<String, Object>> selectUsers(String projectId) {
        return projectStatisticsInfoMapper.selectUsers(projectId);
    }

    /**
     * 获取培训人数
     * @param params
     * @return
     */
    @Override
    public int selectTrainUserCount(Map<String, Object> params){
        //先获取进行中或已结束的
        List<String> projectIds=projectInfoService.selectProjectIds(params);
        if(projectIds.size()==0){
            return 0;
        }
        params.put("projectIds",projectIds);
        //获取该公司下的有效学员ids
        params.put("isValid","1");                                //有效
        List<String> userIds=userService.selectUserIds(params);
        if(userIds.size() == 0){
            return 0;
        }
        params.put("userIds",userIds);
        params.put("countTrainUser","0");                       //没有学时为0
        params.put("examStatusForTrainUser","1");              //未考试 1
        return projectStatisticsInfoMapper.selectTrainUserCount(params);
    }

    /**
     * 完成培训人次
     * @param params
     * @return
     */
    @Override
    public int selectCompleteTrainUserCount(Map<String, Object> params){
        //先获取进行中或已结束的并且是含有培训的项目id
        List<String> projectIds=projectInfoService.selectProjectIds(params);
        if(projectIds.size()==0){
            return 0;
        }
        params.put("projectIds",projectIds);
        //获取该公司下的有效学员ids
        params.put("isValid","1");                                //有效
        List<String> userIds=userService.selectUserIds(params);
        if(userIds.size() == 0){
            return 0;
        }
        params.put("userIds",userIds);
        params.put("countTrainCompleteYes",60);                       //已修学时大于应修学时
        return projectStatisticsInfoMapper.selectUserCount(params);

    }

    /**
     * 考试合格人次
     * @param params
     * @return
     */
    @Override
    public int selectPassExamUserCount(Map<String, Object> params){
        //先获取进行中或已结束的并且是含有考试的项目id
        List<String> projectIds=projectInfoService.selectProjectIds(params);
        if(projectIds.size()==0){
            return 0;
        }
        params.put("projectIds",projectIds);
        //获取该公司下的有效学员ids
        params.put("isValid","1");                                //有效
        List<String> userIds=userService.selectUserIds(params);
        if(userIds.size() == 0){
            return 0;
        }
        params.put("userIds",userIds);
        params.put("countTrainCompleteYes","");
        params.put("countExamPassYes","2");                         //考试合格
        return projectStatisticsInfoMapper.selectUserCount(params);
    }
    /**
     * 累计学时
     * @param params
     * @return
     */
    @Override
    public double selecttotalClassHour(Map<String, Object> params){
        //先获取进行中或已结束的
        List<String> projectIds=projectInfoService.selectProjectIds(params);
        if(projectIds.size()==0){
            return 0;
        }
        params.put("projectIds",projectIds);
        //获取该公司下的有效学员ids
        List<String> userIds=userService.selectUserIds(params);
        if(userIds.size() == 0){
            return 0;
        }
        params.put("userIds",userIds);
        params.put("isValid","");
        return projectStatisticsInfoMapper.selectTotalClassHour(params);
    }
    /**
     * 年度学时统计
     * @param params
     * @return
     */
    @Override
    public double selecttotalYearClassHour(Map<String, Object> params){
        //先获取进行中或已结束的
        List<String> projectIds=projectInfoService.selectProjectIds(params);
        if(projectIds.size()==0){
            return 0;
        }
        params.put("projectIds",projectIds);
        //获取该公司下的有效学员ids
        List<String> userIds=userService.selectUserIds(params);
        if(userIds.size() == 0){
            return 0;
        }
        params.put("userIds",userIds);
        params.put("isValid","");
        params.put("yearStartTime",Calendar.getInstance().get(Calendar.YEAR)+"-01-01 00:00:00");
        params.put("yearEndTime",Calendar.getInstance().get(Calendar.YEAR)+"-12-31 23:59:59");
       return projectStatisticsInfoMapper.selectTotalClassHour(params);
    }


    @Override
    public void updateBatch(List<Map<String, Object>> param) {
        projectStatisticsInfoMapper.updateBatch(param);
    }
}
