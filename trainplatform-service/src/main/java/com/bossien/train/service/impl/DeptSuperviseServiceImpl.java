package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bossien.train.dao.tp.*;
import com.bossien.train.domain.*;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.IDeptSuperviseService;
import com.bossien.train.util.MapUtils;
import com.bossien.train.util.MathUtil;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by A on 2017/11/27.
 */

@Service
public class DeptSuperviseServiceImpl implements IDeptSuperviseService {
    /**
     * 自学学时表
     */
    private final String collectionName_study_self = "study_self";

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PersonDossierMapper personDossierMapper;
    @Autowired
    private ProjectStatisticsInfoMapper projectStatisticsInfoMapper;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private MongoOperations mongoTemplate;

    @Override
    public Map<String, Object> queryForPagination(String companyId, String deptId, String deptName, String search,
                                                  Integer pageNum, Integer pageSize) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //第一天增加一个统计总和的行
        if(pageNum.equals(1) && StringUtils.contains(deptName, search)){
            pageSize = pageSize-1;
            //当前节点
            Map<String, Object> total = getTotal(companyId, deptId, deptName);
            total.put("isTop", "1");
            dataList.add(total);
        }

        //分页开始位
        int startNum = (pageNum - 1) * pageSize;

        //公司查询
        Map<String, Object> params = MapUtils.newHashMap();
        String[] companyAry = new String[0];
        int companyCount = getTotalInfoByCompanyId(companyId, search, startNum, pageSize, companyAry, dataList);

        //部门查询
        int deptCount = getTotalInfoByDeptId(deptId, search, pageSize, startNum, companyCount, companyAry, dataList);

        //返回结果
        Page<Map<String, Object>> page = new Page(companyCount + deptCount, pageNum, pageSize);
        JSONArray jsonArray = new JSONArray();
        getSuperviseInfo(jsonArray, dataList);
        page.setDataList(dataList);

        Map<String, Object> result = new HashedMap();
        result.put("page", page);
        result.put("jsonArray", jsonArray);
        result.put("companyAry", companyAry);
        return result;
    }

    /**
     * 根据公司id查询数据
     * @param companyId
     * @param companyName
     * @return
     */
    public Map<String, Object> getTotalInfoByCompanyId(String companyId, String companyName){
        if(StringUtil.isEmpty(companyId)){
            return MapUtils.newHashMap();
        }

        String companyIds = companyService.getChildCompanyIds(companyId);
        Map<String, Object> params = MapUtils.newHashMap();
        params.put("companyIds", Arrays.asList(companyIds.split(",")));
        List<Map<String, Object>> personDossierList = personDossierMapper.selectList(params);
        if(null == personDossierList || personDossierList.size() < 1){
            return makeInfo(companyId, companyName, 0, 0, 0.0, 0);
        }
        return totalPersonDossier(companyId, companyName, personDossierList.size(), personDossierList);
    }

    /**
     * 查询公司列表
     * @param companyId
     * @param search
     * @param startNum
     * @param pageSize
     * @param companyAry
     * @param dataList
     * @return
     */
    public int getTotalInfoByCompanyId(String companyId, String search, int startNum, int pageSize,
                                       String[] companyAry, List<Map<String, Object>> dataList){
        Map<String, Object> params = MapUtils.newHashMap();
        int companyCount = 0;
        if(StringUtils.isNotEmpty(companyId)){
            //先查子公司集合
            params.put("intPid", companyId);
            params.put("search", ParamsUtil.joinLike(search));
            companyCount = companyService.selectCount(params);
            if(startNum < companyCount){
                //查询记录
                params.put("startNum", startNum);
                params.put("endNum", pageSize);
                List<Map<String, Object>> companyList = companyService.selectList(params);
                for(Map<String, Object> company: companyList){
                    if(null != company.get("intId") && null != company.get("varName")){
                        dataList.add(getTotalInfoByCompanyId(company.get("intId").toString(), company.get("varName").toString()));
                        MathUtil.modifyArrayData(companyAry, company.get("varName").toString());
                    }
                }
            }
        }
        return companyCount;
    }

    /**
     *  增加部门列表
     * @param deptId
     * @param search
     * @param companyAry
     * @param dataList
     * @param pageSize
     * @param startNum
     * @param companyCount
     * @return
     */
    public int getTotalInfoByDeptId(String deptId, String search, int pageSize, int startNum, int companyCount,
                                    String[] companyAry, List<Map<String, Object>> dataList){
        Map<String, Object> params = MapUtils.newHashMap();
        int deptCount = 0;
        if(StringUtils.isNotEmpty(deptId)){
            //查询一级部门集合
            params = MapUtils.newHashMap();
            params.put("parentId", deptId);
            params.put("isValid", Department.IsValid.TYPE_1.getValue());
            params.put("search", ParamsUtil.joinLike(search));
            deptCount = departmentMapper.selectCount(params);
            //添加到总的记录中
            if(dataList.size() < pageSize && deptCount > 0){
                params.put("startNum", companyCount >= startNum ? 0 : (startNum - companyCount - 1));
                params.put("endNum", companyCount > startNum ? (pageSize + startNum - companyCount) : pageSize);
                List<Department> departmentList = departmentMapper.selectList(params);
                for(Department department: departmentList){
                    if(null != department.getId() && null != department.getDeptName()){
                        dataList.add(getTotalInfoByDeptId(department.getId(), department.getDeptName()));
                        MathUtil.modifyArrayData(companyAry, department.getDeptName());
                    }
                }
            }
        }
        return deptCount;
    }

    /**
     * 根据部门id查询数据
     * @param deptId
     * @param deptName
     * @return
     */
    public Map<String, Object> getTotalInfoByDeptId(String deptId, String deptName){
        if(StringUtil.isEmpty(deptId)){
            return MapUtils.newHashMap();
        }

        String deptIds = departmentMapper.getChildById(deptId);
        Map<String, Object> params = MapUtils.newHashMap();
        params.put("departmentIdList", Arrays.asList(deptIds.split(",")));
        List<Map<String, Object>> personDossierList = personDossierMapper.selectList(params);
        if(null == personDossierList || personDossierList.size() < 1){
            return makeInfo(deptId, deptName, 0, 0, 0.0, 0);
        }
        return totalPersonDossier(deptId, deptName, personDossierList.size(), personDossierList);
    }

    /**
     * 求出总的统计
     * @param companyId
     * @param deptId
     * @param name
     * @return
     */
    public Map<String, Object> getTotal(String companyId, String deptId, String name){
        Map<String, Object> currentMap = null;
        //公司查询
        if(StringUtils.isNotEmpty(companyId)){
            currentMap = getTotalInfoByCompanyId(companyId, name);
        }

        //部门查询
        if(null == currentMap && StringUtils.isNotEmpty(deptId)){
            currentMap = getTotalInfoByDeptId(deptId, name);
        }
        return currentMap;
    }
    /*--------------------------------------------no_time_end-----------------------------------------------------*/

    /*--------------------------------------------time_start-----------------------------------------------------*/

    @Override
    public Map<String, Object> queryForPagination(String companyId, String deptId, String deptName, String search,
                                                  String startTime, String endTime, Integer pageNum, Integer pageSize) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //第一天增加一个统计总和的行
        if(pageNum.equals(1) && StringUtils.contains(deptName, search)){
            pageSize = pageSize-1;
            Map<String, Object> total = getTotalByTime(companyId, deptId, deptName, startTime, endTime);
            total.put("isTop", "1");
            //当前节点
            dataList.add(total);
        }

        //分页开始位
        int startNum = (pageNum - 1) * pageSize;

        //公司查询
        Map<String, Object> params = MapUtils.newHashMap();
        String[] companyAry = new String[0];
        int companyCount = getTotalInfoByCompanyIdByTime(companyId, search, startNum, pageSize, startTime, endTime, companyAry, dataList);

        //部门查询
        int deptCount = getTotalInfoByDeptIdByTime(deptId, search, pageSize, startNum, companyCount, startTime, endTime, companyAry, dataList);

        //返回结果
        Page<Map<String, Object>> page = new Page(companyCount + deptCount, pageNum, pageSize);
        JSONArray jsonArray = new JSONArray();
        getSuperviseInfo(jsonArray, dataList);
        page.setDataList(dataList);

        Map<String, Object> result = new HashedMap();
        result.put("page", page);
        result.put("jsonArray", jsonArray);
        result.put("companyAry", companyAry);
        return result;
    }

    /**
     * 查询公司列表
     * @param companyId
     * @param search
     * @param startNum
     * @param pageSize
     * @param companyAry
     * @param dataList
     * @return
     */
    public int getTotalInfoByCompanyIdByTime(String companyId, String search, int startNum, int pageSize,
                                             String startTime, String endTime, String[] companyAry, List<Map<String, Object>> dataList){
        Map<String, Object> params = MapUtils.newHashMap();
        int companyCount = 0;
        if(StringUtils.isNotEmpty(companyId)){
            //先查子公司集合
            params.put("intPid", companyId);
            params.put("search", ParamsUtil.joinLike(search));
            companyCount = companyService.selectCount(params);
            if(startNum < companyCount){
                //查询记录
                params.put("startNum", startNum);
                params.put("endNum", pageSize);
                List<Map<String, Object>> companyList = companyService.selectList(params);
                for(Map<String, Object> company: companyList){
                    if(null != company.get("intId") && null != company.get("varName")){
                        dataList.add(getTotalByCompanyId(company.get("intId").toString(), company.get("varName").toString(), startTime, endTime));
                        MathUtil.modifyArrayData(companyAry, company.get("varName").toString());
                    }
                }
            }
        }
        return companyCount;
    }

    /**
     *  增加部门列表
     * @param deptId
     * @param search
     * @param companyAry
     * @param dataList
     * @param pageSize
     * @param startNum
     * @param companyCount
     * @return
     */
    public int getTotalInfoByDeptIdByTime(String deptId, String search, int pageSize, int startNum, int companyCount,
                         String startTime, String endTime, String[] companyAry, List<Map<String, Object>> dataList){
        Map<String, Object> params = MapUtils.newHashMap();
        int deptCount = 0;
        if(StringUtils.isNotEmpty(deptId)){
            //查询一级部门集合
            params = MapUtils.newHashMap();
            params.put("parentId", deptId);
            params.put("isValid", Department.IsValid.TYPE_1.getValue());
            params.put("search", ParamsUtil.joinLike(search));
            deptCount = departmentMapper.selectCount(params);
            //添加到总的记录中
            if(dataList.size() < pageSize && deptCount > 0){
                params.put("startNum", companyCount >= startNum ? 0 : (startNum - companyCount - 1));
                params.put("endNum", companyCount > startNum ? (pageSize + startNum - companyCount) : pageSize);
                List<Department> departmentList = departmentMapper.selectList(params);
                for(Department department: departmentList){
                    if(null != department.getId() && null != department.getDeptName()){
                        dataList.add(getTotalByDeptId(department.getId(), department.getDeptName(), startTime, endTime));
                        MathUtil.modifyArrayData(companyAry, department.getDeptName());
                    }
                }
            }
        }
        return deptCount;
    }

    /**
     * 根据时间查询部门id的统计
     * @param deptId
     * @param deptName
     * @param startTime
     * @param endTime
     * @return
     */
    public Map<String, Object> getTotalByDeptId(String deptId, String deptName, String startTime, String endTime){
        //子公司集合
        String deptIds = departmentMapper.getChildById(deptId);

        //查询用户
        Map<String, Object> params = MapUtils.newHashMap();
        params.put("deptIds", Arrays.asList(deptIds.split(",")));
        params.put("endTime", endTime);
        params.put("isValid", User.IsValid.TYPE_1.getValue());
        List<String> users = userMapper.selectUserIds(params);
        Integer countUser = users.size();
        if(users.size() < 1){
            return makeInfo(deptId, deptName, 0, 0, 0.0, 0);
        }

        /*统计培训、练习、考试中的记录*/
        params.put("userIds", users);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
         /*查询项目人员统计表  -----培训和练习项目*/
        List<ProjectStatisticsInfo> projectStatisticsList = new ArrayList<ProjectStatisticsInfo>();
        //培训练习
        projectStatisticsList.addAll(projectStatisticsInfoMapper.selectStatisticsListByDeptSupervise(params));
        //考试项目
        projectStatisticsList.addAll(projectStatisticsInfoMapper.selectExamPaperInfoByDeptSupervise(params));

        return makeTotalByProjectStatistics("", deptId, deptName, countUser, startTime, endTime, projectStatisticsList);
    }

    /**
     * 根据时间查询公司id的统计
     * @param companyId
     * @param companyName
     * @param startTime
     * @param endTime
     * @return
     */
    public Map<String, Object> getTotalByCompanyId(String companyId, String companyName, String startTime, String endTime){
        //子公司集合
        String companyIds = companyService.getChildCompanyIds(companyId);

        //查询用户
        Map<String, Object> params = MapUtils.newHashMap();
        params.put("companyIds", Arrays.asList(companyIds.split(",")));
        params.put("endTime", endTime);
        params.put("isValid", User.IsValid.TYPE_1.getValue());
        List<String> users = userMapper.selectUserIds(params);
        Integer countUser = users.size();
        if(users.size() < 1){
            return makeInfo(companyId, companyName, 0, 0, 0.0, 0);
        }

        /*统计培训、练习、考试中的记录*/
        params.put("companyIds", Arrays.asList(companyIds.split(",")));
        params.put("startTime", startTime);
        params.put("endTime", endTime);
         /*查询项目人员统计表  -----培训和练习项目*/
        List<ProjectStatisticsInfo> projectStatisticsList = new ArrayList<ProjectStatisticsInfo>();
        //培训练习
        projectStatisticsList.addAll(projectStatisticsInfoMapper.selectStatisticsListByDeptSupervise(params));
        //考试项目
        projectStatisticsList.addAll(projectStatisticsInfoMapper.selectExamPaperInfoByDeptSupervise(params));

        return makeTotalByProjectStatistics(companyId, "", companyName, countUser, startTime, endTime, projectStatisticsList);
    }

    /**
     * 根据项目人员统计表统计
     * @param companyId
     * @param companyName
     * @param countUser
     * @param projectStatisticsList
     * @return
     */
    public Map<String, Object> makeTotalByProjectStatistics(String companyId, String deptId, String companyName,
                           int countUser, String startTime, String endTime, List<ProjectStatisticsInfo> projectStatisticsList){
        // 培训人数
        Integer countTrainUser = 0;
        // 总学时
        Double totalClassHour = 0.0;
        Long totalClassHourLong = 0L;
        // 培训人次
        Integer countTrain = 0;

        /*分类统计*/
        Iterator<ProjectStatisticsInfo> projectStatisticsInfoIterator = projectStatisticsList.iterator();
        Map<String, List<ProjectStatisticsInfo>> countTrainMap = MapUtils.newHashMap();
        Map<String, List<ProjectStatisticsInfo>> countTrainUserMap = MapUtils.newHashMap();
        List<ProjectStatisticsInfo> projectStatisticsInfoList;
        while (projectStatisticsInfoIterator.hasNext()){
            ProjectStatisticsInfo projectStatisticsInfo = projectStatisticsInfoIterator.next();
            if(null == projectStatisticsInfo){
                continue;
            }
            //总学时
            Long totalStudyTime = projectStatisticsInfo.getTotalStudyTime();

            //为空时是考试项目
            boolean timeBtn = null == totalStudyTime || totalStudyTime == 0;
            boolean examBtn = null == projectStatisticsInfo.getExamNo() || projectStatisticsInfo.getExamNo().equals("");
            if(timeBtn && examBtn){
                //学时为0时不继续
                continue;
            }

            //初始化
            if(null == totalStudyTime){
                totalStudyTime = 0L;
            }

            //统计学时
            totalClassHourLong += totalStudyTime;
//            totalClassHour = Double.parseDouble(new DecimalFormat("0.00").format(totalClassHourLong * 1.00  / 3600));

            // 培训人次
            projectStatisticsInfoList = new ArrayList<ProjectStatisticsInfo>();
            String key = projectStatisticsInfo.getProjectId() + projectStatisticsInfo.getUserId();
            if(null != countTrainMap.get(key)){
                projectStatisticsInfoList = countTrainMap.get(key);
            }
            projectStatisticsInfoList.add(projectStatisticsInfo);
            countTrainMap.put(key, projectStatisticsInfoList);

            // 培训人数
            projectStatisticsInfoList = new ArrayList<ProjectStatisticsInfo>();
            key = projectStatisticsInfo.getUserId();
            if(null != countTrainUserMap.get(key)){
                projectStatisticsInfoList = countTrainUserMap.get(key);
            }
            projectStatisticsInfoList.add(projectStatisticsInfo);
            countTrainUserMap.put(key, projectStatisticsInfoList);
        }

        //增加自学学时
        totalClassHourLong += getStudySelfTotal(companyId, deptId, startTime, endTime);
        totalClassHour = Double.parseDouble(new DecimalFormat("0.00").format(totalClassHourLong * 1.00  / 3600));

        // 培训人次
        countTrain = countTrainMap.size();
        // 培训人数
        countTrainUser = countTrainUserMap.size();

        return makeInfo(companyId, companyName, countUser, countTrainUser, totalClassHour, countTrain);
    }

    /**
     * 求出总的统计
     * @param companyId
     * @param deptId
     * @param name
     * @return
     */
    public Map<String, Object> getTotalByTime(String companyId, String deptId, String name, String startTime, String endTime){
        Map<String, Object> currentMap = null;
        //公司查询
        if(StringUtils.isNotEmpty(companyId)){
            currentMap = getTotalByCompanyId(companyId, name, startTime, endTime);
        }

        //部门查询
        if(null == currentMap && StringUtils.isNotEmpty(deptId)){
            currentMap = getTotalByDeptId(deptId, name, startTime, endTime);
        }
        return currentMap;
    }

    /**
     * 查询总自学学时
     * @param companyId
     * @param deptId
     * @return
     */
    public int getStudySelfTotal(String companyId, String deptId, String startTime, String endTime){
        //查询用户集合
        Map<String, Object> params = MapUtils.newConcurrentHashMap();
        params.put("companyId", companyId);
        params.put("deptId", deptId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<String> userIds = userMapper.selectUserIds(params);
        if(null == userIds || userIds.size() < 1){
            return 0;
        }
        //查询mongo中的数据
        Criteria criteria = Criteria.where("user_id").in(userIds);
        if(StringUtils.isNotEmpty(startTime)){
            criteria = new Criteria().andOperator(criteria, Criteria.where("create_time").gt(startTime));
        }
        if(StringUtils.isNotEmpty(endTime)){
            criteria = new Criteria().andOperator(criteria, Criteria.where("create_time").lt(endTime));
        }
        List<StudySelf> studySelfList = mongoTemplate.find(Query.query(criteria), StudySelf.class, collectionName_study_self);
        //统计
        if(null != studySelfList && studySelfList.size() > 0){
            int totalTime = 0;
            for(StudySelf studySelf: studySelfList){
                totalTime += studySelf.getStudy_time();
            }
            return totalTime;
        }
        return 0;
    }

    /*------------------------------------------time_end-----------------------------------------------------*/


    @Override
    public Map<String, Object> queryForPagination(String deptId, String deptName, String search, String companyName,
                                                  User user, Integer pageNum, Integer pageSize,
                                                  Map<String, String> deptMap, Map<String, Map<String, Object>> deptTotalInfoMap) {

        return selectDetpTj(deptId, deptName, search, companyName, user, pageNum, pageSize, deptMap, deptTotalInfoMap);
    }

    @Override
    public Map<String, Object> queryForPagination(String companyId, String companyName, String search, User user, Integer pageNum, Integer pageSize) {
        //转换成结果数据
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

        Map<String, Object> params = MapUtils.newConcurrentHashMap();
        params.put("intPid", companyId);
        params.put("search", ParamsUtil.joinLike(search));
        int count = companyService.selectCount(params);
        Page<Map<String, Object>> page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> companyChildList = companyService.selectList(params);
        String[] companyAry = new String[0];
        for(Map<String, Object> company: companyChildList){
            if(null != company.get("intId") && null != company.get("varName")){
                dataList.add(getTotalInfoByCompanyId(company.get("intId").toString(), company.get("varName").toString()));
                MathUtil.modifyArrayData(companyAry, company.get("varName").toString());
            }
        }

        JSONArray jsonArray = new JSONArray();
        getSuperviseInfo(jsonArray, dataList);
        page.setDataList(dataList);

        //返回结果
        Map<String, Object> result = new HashedMap();
        result.put("page", page);
        result.put("jsonArray", jsonArray);
        result.put("companyAry", companyAry);
        return result;
    }

    @Override
    public Map<String, String> getChildsByDeptPid(String companyId, String deptId, String deptName, String startTime, String endTime, Map<String, Map<String, Object>> deptTotalInfoMap) {
        Map<String, Object> params = MapUtils.newConcurrentHashMap();
        Map<String, String> result = MapUtils.newConcurrentHashMap();
        params.put("companyId", companyId);
        List<Department> departments = departmentMapper.selectDepartmentByCompanyId(params);
        Map<String, List<Department>> listMap = listToMap(departments);
        //查询所有的子部门
        String childList = recursion(listMap, deptId);
        for(Department department: departments){
            //deptId等于公司id时，查询全部
            if(childList.indexOf(department.getId()) != -1){
                result.put(department.getId(), recursion(listMap, department.getId()));

                if(!StringUtil.isEmpty(startTime) || !StringUtil.isEmpty(endTime)){
//                    deptTotalInfoMap.put(department.getId(), selectDetpCountByDeptId(department.getId(), department.getDeptName(),
//                            companyId, startTime, endTime));
                }else{
                    deptTotalInfoMap.put(department.getId(), selectDetpCountByDeptId(department.getId(), department.getDeptName(), companyId));
                }
            }
        }

        //增加根节点
        if(companyId.equals(deptId)){
            deptTotalInfoMap.put(companyId, selectDetpCountByDeptId(deptId, deptName, companyId));
            return result;
        }
        return result;
    }

    /**
     * 统计部门集合
     * @param deptId
     * @param deptName
     * @param search
     * @param companyName
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Map<String, Object> selectDetpTj(String deptId, String deptName, String search, String companyName,
                                            User user, Integer pageNum, Integer pageSize, Map<String, String> deptMap,
                                            Map<String, Map<String, Object>> deptTotalInfoMap){
        //转换成结果数据
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> total = new HashMap<String, Object>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isValid", Department.IsValid.TYPE_1.getValue());
        params.put("companyId", user.getCompanyId());

        //根节点
        Department department;
        Map<String, Object> yourSelfMap;
        if(deptId.equals(user.getCompanyId())){
            department = new Department(user.getCompanyId(), companyName, user.getCompanyId());
        }else{
            department = new Department(deptId, deptName, user.getCompanyId());
        }

        //本节点
        yourSelfMap = getTotalInfoByDeptIds(deptTotalInfoMap, department.getId(), deptId, deptName);

        //查询子节点
        params.put("parentId", deptId);
        params.put("deptName", ParamsUtil.joinLike(search));
        List<Department> departments = departmentMapper.selectDepartmentByCompanyId(params);
        Iterator<Department> it = departments.iterator();
        Map<String, Object> rs;
        String[] companyAry = new String[departments.size() + 1];
        int chunk = 1;
        String childs = "";
        String id = "";
        while (it.hasNext()){
            Department d = it.next();
            childs = deptMap.get(d.getId());
            rs = getTotalInfoByDeptIds(deptTotalInfoMap, childs, d.getId(), d.getDeptName());

            //加到list中
            dataList.add(rs);
            //加到总的记录中
            sumDeptTjInfo(total, rs);
            //加入名称
            companyAry[chunk++] = d.getDeptName();
        }

        //分页
        int count = departments.size();
        Page<Map<String, Object>> page = new Page(count, pageNum, pageSize);
        Integer endNum = page.getStartNum() + page.getPageSize();
        if(count >= endNum){
            dataList = dataList.subList(page.getStartNum(), endNum);
        }else{
            dataList = dataList.subList(page.getStartNum(), dataList.size());
        }

        //搜索字段为空，或者本节点包含搜索字段时添加
        if(StringUtil.isEmpty(search) || deptName.indexOf(search) != -1){
            //dataList加入总节点
            total.put("companyId", deptId);
            total.put("companyName", department.getDeptName());
            sumDeptTjInfo(total, yourSelfMap);
            dataList.add(0, total);
            companyAry[0] = department.getDeptName();
        }

        JSONArray jsonArray = new JSONArray();
        getSuperviseInfo(jsonArray, dataList);
        page.setDataList(dataList);
        //返回结果
        Map<String, Object> result = new HashedMap();
        result.put("page", page);
        result.put("jsonArray", jsonArray);
        result.put("companyAry", companyAry);
        return result;
    }

    /**
     * 合并数据 根据deptIds合并数据，deptId是deptIds的pid
     * @param deptTotalInfoMap
     * @param deptIds
     * @param deptId
     * @param deptName
     * @return
     */
    public Map<String, Object> getTotalInfoByDeptIds(Map<String, Map<String, Object>> deptTotalInfoMap,
                                                     String deptIds, String deptId, String deptName){
        if(StringUtil.isEmpty(deptIds)){
            return MapUtils.newHashMap();
        }

        Map<String, Object> total = MapUtils.newHashMap();
        String[] ids = deptIds.split(",");
        for (String id: ids){
            if(StringUtil.isEmpty(id)){
                continue;
            }
            Map<String, Object> map = deptTotalInfoMap.get(id);
            if(null != map){
                sumDeptTjInfo(total, map);
            }
        }

        total.put("companyId", deptId);
        total.put("companyName", deptName);
        return total;
    }

    /**
     * 单个部门的统计信息
     * @param deptId
     * @param deptName
     * @return
     */
    public Map<String, Object> selectDetpCountByDeptId(String deptId, String deptName, String companyId){
        //查询用户
        Map<String, Object> params = MapUtils.newHashMap();
        params.put("deptId", deptId);
        params.put("companyId", companyId);
        params.put("isValid", User.IsValid.TYPE_1.getValue());
        List<String> users;
        if(StringUtils.isEmpty(deptId)){
            users = userMapper.selectUserIdsByDefaultDept("", companyId);
        }else {
            params.put("deptId", deptId);
            params.put("companyId", companyId);
            params.put("isValid", User.IsValid.TYPE_1.getValue());
            users = userMapper.selectUserIds(params);
        }

        Integer countUser = users.size();
        if(users.size() < 1){
            return makeInfo(deptId, deptName, countUser, 0, 0.0, 0);
        }

        //人员档案
        List<Map<String, Object>> personDossiers = new ArrayList<Map<String, Object>>();

        /*查询人员档案*/
        params = new HashMap<String, Object>();
        params.put("userIds", users);
        List<List<String>> userIdList = StringUtil.subList(users, 1000);
        Iterator<List<String>> it = userIdList.iterator();
        while (it.hasNext()){
            List<String> u = it.next();
            params.put("userIds", u);
            personDossiers.addAll(personDossierMapper.selectList(params));
        }

        return totalPersonDossier(deptId, deptName, countUser, personDossiers);
    }

    /**
     * 根据人员档案统计
     * @param deptId
     * @param deptName
     * @param countUser
     * @param personDossiers
     * @return
     */
    public Map<String, Object> totalPersonDossier(String deptId, String deptName, Integer countUser,
                                                  List<Map<String, Object>> personDossiers){
        // 培训人数
        Integer countTrainUser = 0;
        // 总学时
        Double totalClassHour = 0.0;
        Long totalClassHourLong = 0L;
        // 培训人次
        Integer countTrain = 0;
        /*计算培训人数、培训人次*/
        Iterator<Map<String, Object>> personIt = personDossiers.iterator();
        while (personIt.hasNext()){
            Map<String, Object> person = personIt.next();
            //总学时
            totalClassHourLong += Long.parseLong(person.get("total_studytime").toString());
            totalClassHour = Double.parseDouble(new DecimalFormat("0.00").format(totalClassHourLong * 1.00  / 3600));

            if(null == person.get("train_count") ||
                    "".equals(person.get("train_count").toString()) ||
                    "0".equals(person.get("train_count").toString())){
                continue;
            }else{
                // 培训人数
                countTrainUser++;
                // 培训人次
                countTrain += Integer.parseInt(person.get("train_count").toString());
            }
        }
        return makeInfo(deptId, deptName, countUser, countTrainUser, totalClassHour, countTrain);
    }

    /*组装数据*/
    public Map<String, Object> makeInfo(String rootId, String rootName, int countUser,
                                        int countTrainUser, Double totalClassHour, int countTrain){
        // 培训率
        Double percentTrain = 0.0;
        // 人均学时
        Double averagePersonClassHour = 0.00;
        // 人均培训人次
        Integer averageTrainCount = 0;

        if(countUser != 0){
            percentTrain = Double.parseDouble(new DecimalFormat("0.00").format(countTrainUser * 100.00 / countUser));
            averagePersonClassHour = Double.parseDouble(new DecimalFormat("0.00").format(totalClassHour * 1.00  / countUser));
            averageTrainCount = countTrain / countUser;
        }

        //组装数据
        Map<String, Object> rs = new HashedMap();
        rs.put("companyId", rootId);
        rs.put("companyName", rootName);
        rs.put("countUser", countUser);
        rs.put("countTrainUser", countTrainUser);
        rs.put("percentTrain", percentTrain);
        rs.put("totalClassHour", totalClassHour);
        rs.put("averagePersonClassHour", averagePersonClassHour);
        rs.put("countTrain", countTrain);
        rs.put("averageTrainCount", averageTrainCount);
        return rs;
    }

    private void sumDeptTjInfo(Map<String, Object> data1, Map<String, Object> data2) {
        if(null == data2 || data2.size() < 1){
            return;
        }

        Integer countUser = 0;
        // 培训人数
        Integer countTrainUser = 0;
        // 总学时
        Double totalClassHour = 0.0;
        // 培训人次
        Integer countTrain = 0;

        Double percentTrain = 0.0;
        Double averageTrainCount = 0.0;
        Double averagePersonClassHour = 0.0;

        if(StringUtil.isEmpty(data1.get("countUser"))){
            countUser = data2.get("countUser") == null ? 0 : Integer.parseInt(data2.get("countUser").toString());
        }else{
            countUser = data2.get("countUser") == null
                    ? Integer.parseInt(data1.get("countUser").toString())
                    : Integer.parseInt(data1.get("countUser").toString()) +
                        (data2.get("countUser") == null ? 0 : Integer.parseInt(data2.get("countUser").toString()));
        }
        if(StringUtil.isEmpty(data1.get("countTrainUser"))){
            countTrainUser = data2.get("countTrainUser") == null ? 0 : Integer.parseInt(data2.get("countTrainUser").toString());
        }else{
            countTrainUser = data2.get("countTrainUser") == null
                    ? Integer.parseInt(data1.get("countTrainUser").toString())
                    : Integer.parseInt(data1.get("countTrainUser").toString()) +
                        (data2.get("countTrainUser") == null ? 0 : Integer.parseInt(data2.get("countTrainUser").toString()));
        }
        if(StringUtil.isEmpty(data1.get("totalClassHour"))){
            totalClassHour = data2.get("totalClassHour") == null ? 0 : Double.parseDouble(data2.get("totalClassHour").toString());
        }else{
            totalClassHour = data2.get("totalClassHour") == null
                    ? Integer.parseInt(data1.get("totalClassHour").toString())
                    : Double.parseDouble(data1.get("totalClassHour").toString()) +
                        (data2.get("totalClassHour") == null ? 0 : Double.parseDouble(data2.get("totalClassHour").toString()));
        }
        if(StringUtil.isEmpty(data1.get("countTrain"))){
            countTrain = data2.get("countTrain") == null ? 0 : Integer.parseInt(data2.get("countTrain").toString());
        }else{
            countTrain = data2.get("countTrain") == null
                    ? Integer.parseInt(data1.get("countTrain").toString())
                    : Integer.parseInt(data1.get("countTrain").toString()) +
                        (data2.get("countTrain") == null ? 0 : Integer.parseInt(data2.get("countTrain").toString()));
        }

        if(countUser != 0){
            percentTrain = Double.parseDouble(new DecimalFormat("0.0").format(countTrainUser * 100.0 / countUser));
            averageTrainCount = Double.parseDouble(new DecimalFormat("0.0").format(countTrain * 1.0 / countUser));
            averagePersonClassHour = Double.parseDouble(new DecimalFormat("0.0").format(totalClassHour * 1.0 / countUser));
        }

        data1.put("countUser", countUser);
        data1.put("countTrainUser", countTrainUser);
        data1.put("percentTrain", percentTrain);
        data1.put("totalClassHour", totalClassHour);
        data1.put("averagePersonClassHour", averagePersonClassHour);
        data1.put("countTrain", countTrain);
        data1.put("averageTrainCount", averageTrainCount);
    }

    /**
     * 组装成报表能识别的集合
     * @param jsonArray
     * @param list
     */
    public void getSuperviseInfo(JSONArray jsonArray, List<Map<String, Object>> list){
        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        List<List<Object>> rjxsList = new LinkedList<List<Object>>();
        List<List<Object>> zxsTolist = new LinkedList<List<Object>>();
        List<List<Object>> pxrcTolist = new LinkedList<List<Object>>();
        makeSupervise(rjxsList, zxsTolist, pxrcTolist, list);

        String xjxs = "人均学时";
        Map<String, Object> xjxsMap = new HashedMap();
        xjxsMap.put("name", xjxs);
        xjxsMap.put("data", rjxsList);
        result.add(xjxsMap);

        String zxs = "总学时";
        Map<String, Object> zxsMap = new HashedMap();
        zxsMap.put("name", zxs);
        zxsMap.put("data", zxsTolist);
        result.add(zxsMap);

        String pxrc = "培训人次";
        Map<String, Object> pxrcMap = new HashedMap();
        pxrcMap.put("name", pxrc);
        pxrcMap.put("data", pxrcTolist);
        result.add(pxrcMap);
        for(Map<String, Object> map: result){
            jsonArray.add(map);
        }
    }

    //组装成报表能识别的
    public void makeSupervise(List<List<Object>> rjxsList, List<List<Object>> zxsTolist,
                              List<List<Object>> pxrcTolist, List<Map<String, Object>> list){
        for(int i = 0; i < list.size(); i++){
            Map<String, Object> map = list.get(i);
            Object companyName = map.get("companyName");
            Object averagePersonClassHour = map.get("averagePersonClassHour");
            Object totalClassHour = map.get("totalClassHour");
            Object countTrain = map.get("countTrain");

            List<Object> data = new ArrayList<Object>();
            data.add(companyName);
            data.add(averagePersonClassHour);
            rjxsList.add(data);

            data = new ArrayList<Object>();
            data.add(companyName);
            data.add(totalClassHour);
            zxsTolist.add(data);

            data = new ArrayList<Object>();
            data.add(companyName);
            data.add(countTrain);
            pxrcTolist.add(data);
        }
    }

    //递归
    public String recursion(Map<String, List<Department>> listMap, String deptId){
        String result = "";
        if(null == listMap.get(deptId)){

            return deptId;
        }

        //添加自己
        result += deptId;

        List<Department> departments = listMap.get(deptId);
        for(Department department: departments){
            //添加该节点
            String dt = department.getId();
            //添加该节点的所有子节点
            String rs = recursion(listMap, dt);
            result += rs.equals("") ? "" : "," + rs;
        }
        return result;
    }

    public Map<String, List<Department>> listToMap(List<Department> list) {
        Map<String, List<Department>> mlist = new HashMap<String, List<Department>>();
        for (Department department : list) {
            Object pid = department.getParentId();
            if (null != pid) {
                List<Department> types = new ArrayList<Department>();
                if (null != mlist.get(pid.toString())) {
                    types = mlist.get(pid.toString());
                }
                types.add(department);
                mlist.put(pid.toString(), types);
            }
        }
        return mlist;
    }
}
