package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bossien.train.dao.tp.*;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.MapUtils;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/8/16.
 */
@Service(value = "companyTjService")
public class CompanyTjServiceImpl implements ICompanyTjService {
    @Autowired
    private CompanyTjMapper companyTjMapper;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProjectStatisticsInfoMapper projectStatisticsInfoMapper;

    @Autowired
    private CompanyProjectMapper companyProjectMapper;

    @Autowired
    private ICompanySuperviseService companySuperviseService;

    @Autowired
    private PersonDossierMapper personDossierMapper;
    @Autowired
    private IUserService userService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ICompanyCourseService companyCourseService;
    @Autowired
    private ICourseInfoService courseInfoService;


    /**
     * 添加
     * @param params
     * @return
     */
    @Override
    public int insert(Map<String,Object> params){
       return companyTjMapper.insert(params);
    }

    /**
     * 修改
     * @param params
     * @return
     */
    @Override
    public int update(Map<String,Object> params){
        return companyTjMapper.update(params);
    }

    /**
     * 查询单条
     * @param params
     * @return
     */
    @Override
    //@Cacheable(value = "cache_company_tj#(60 * 60)", key = "'selectOne'.concat('_').concat(#p0.hashCode())")
   public CompanyTj selectOne(Map<String,Object> params){
       return companyTjMapper.selectOne(params);
   }

    @Override
    public CompanyTj selectOne(String companyId, String userName) {
        if(null == companyId){
            return null;
        }

        Map<String,Object> params = MapUtils.newHashMap();
        params.put("companyId", companyId);
        CompanyTj companyTj = companyTjMapper.selectOne(params);
        if(null != companyTj){

            return companyTj;
        }

        Company company = companyService.selectOne(params);
        companyTj = new CompanyTj(params.get("companyId").toString(), company.getVarName(), userName);
        Map<String, Object> companyTjMap = new HashMap<String, Object>();
        MapUtils.putAll(companyTjMap, companyTj);
        companyTjMapper.insert(companyTjMap);
        return companyTj;
    }

    /**
     * 初始化数据
     * @param
     * @return
     */
    @Override
    public CompanyTj initData(){
        CompanyTj companyTj=new CompanyTj();
        companyTj.setCountUser("0");
        companyTj.setCountTrainUser("0");
        companyTj.setPercentTrainComplete("0");
        companyTj.setCountCourse("0");
        companyTj.setCountQuestion("0");
        companyTj.setCountProject("0");
        companyTj.setTotalClassHour("0.00");
        companyTj.setAveragePersonClassHour("0.00");
        companyTj.setTotalYearClassHour("0.00");
        companyTj.setAverageYearClassHour("0.00");
        companyTj.setCountTrain("0");
        companyTj.setCountTrainCompleteYes("0");
        companyTj.setCountExam("0");
        companyTj.setCountExamPassYes("0");
        return companyTj;
    }

    @Override
    public List<CompanyTj> selectList(Map<String, Object> params) {

        return companyTjMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return companyTjMapper.selectCount(params);
    }

    @Override
    public Map<String, Object> queryForPagination(String rootId, String searchName, String companyName,
                                                  String startTime, String endTime, User user,Integer pageNum, Integer pageSize) {
        //开始、结束时间都为空，或者开始时间为空、结束时间大于当前时间    ---不进行时间搜索
        boolean isNotSearchByTime = (StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime)) ||
                (StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(endTime)
                        && DateUtils.parseDateTime(endTime).getTime() > System.currentTimeMillis());
        if(isNotSearchByTime){
            return selectCompanyTj(rootId, searchName, companyName, user, pageNum, pageSize);
        }else{
            return selectCompanyTjByTime(rootId, searchName, companyName, startTime, endTime, user, pageNum, pageSize);
        }
    }

    /**
     * 包含时间时的统计
     * @param companyId
     * @param companyName
     * @param startTime
     * @param endTime
     * @param dataList
     */
    public Map<String, Object> getDataMethod(String companyId, String companyName, String startTime, String endTime,
                              List<Map<String, Object>> dataList){
        Map<String, Object> rs = new HashedMap();
        Map<String, Object> newParams = new HashMap<String, Object>();
        //根据公司、时间查询项目
        newParams.put("projectStartTime", startTime);
        newParams.put("projectEndTime", endTime);
        newParams.put("companyId", companyId);
        List<String>  projectInfoList = companyProjectMapper.selectProjectIds(newParams);

        newParams = new HashMap<String, Object>();
        newParams.put("companyId", companyId);
        newParams.put("endTime", endTime);
        newParams.put("startTime", startTime);
//        newParams.put("userType", "3");
        newParams.put("isValid", User.IsValid.TYPE_1.getValue());
        //学员数量
        List<String> userIds = userMapper.selectUserIds(newParams);
        Integer countUser = userIds.size();

        //查询出项目中所有的人
        List<Map<String, Object>> projectStatisticsInfos = new ArrayList<Map<String, Object>>();
        for(String projectId: projectInfoList){
            if(StringUtil.isEmpty(projectId)){
                continue;
            }
            //根据项目id查询项目统计
            projectStatisticsInfos.addAll(projectStatisticsInfoMapper.selectUserStudyTimeList(newParams));
        }

        //人员档案
        newParams = new HashMap<String, Object>();
        newParams.put("companyId", companyId);
        newParams.put("userIds", userIds);
        List<Map<String, Object>> personDossiers = new ArrayList<Map<String, Object>>();

        List<List<String>> userIdList = StringUtil.subList(userIds, 1000);
        Iterator<List<String>> it = userIdList.iterator();
        while (it.hasNext()){
            List<String> u = it.next();
            newParams.put("userIds", u);
            personDossiers.addAll(personDossierMapper.selectList(newParams));
        }

        // 培训人数
        Integer countTrainUser = getCountTrainUser(personDossiers);
        // 培训率
        Double percentTrain = 0.0;
        // 总学时
        Double totalClassHour = getTotalClassHour(projectStatisticsInfos, userIds);
        // 人均学时
        Double averagePersonClassHour = 0.00;
        // 培训人次
        Integer countTrain = getCountTrain(personDossiers);
        // 人均培训人次
        Integer averageTrainCount = 0;

        if(countUser != 0){
            percentTrain = Double.parseDouble(new DecimalFormat("0.0").format(countTrainUser * 100.0 / countUser));
            averagePersonClassHour = Double.parseDouble(new DecimalFormat("0.00").format(totalClassHour * 1.00  / countUser));
            averageTrainCount = countTrain / countUser;
        }

        //组装数据
        rs.put("companyId", companyId);
        rs.put("companyName", companyName);
        rs.put("countUser", countUser);
        rs.put("countTrainUser", countTrainUser);
        rs.put("percentTrain", percentTrain);
        rs.put("totalClassHour", totalClassHour);
        rs.put("averagePersonClassHour", averagePersonClassHour);
        rs.put("countTrain", countTrain);
        rs.put("averageTrainCount", averageTrainCount);
        if(null != dataList){
            dataList.add(rs);
        }
        return rs;
    }

    /**
     * 包含时间时的统计(调整)
     * @param companyId
     * @param companyName
     * @param startTime
     * @param endTime
     * @param dataList
     * @return
     */
    public Map<String, Object> getDataMethod2(String companyId, String companyName, String startTime, String endTime,
                                             List<Map<String, Object>> dataList){
        Map<String, Object> rs = new HashedMap();
        Map<String, Object> newParams = new HashMap<String, Object>();
        //根据公司、时间查询项目
        newParams.put("projectStartTime", startTime);
        newParams.put("projectEndTime", endTime);
        newParams.put("companyId", companyId);
        List<String>  projectInfoList = companyProjectMapper.selectProjectIds(newParams);

        newParams = new HashMap<String, Object>();
        newParams.put("companyId", companyId);
        newParams.put("endTime", endTime);
        newParams.put("startTime", startTime);
//        newParams.put("userType", "3");
        newParams.put("isValid", User.IsValid.TYPE_1.getValue());
        //学员数量
        List<String> userIds = userMapper.selectUserIds(newParams);
        Integer countUser = userIds.size();

        //查询出项目中所有的人
        List<Map<String, Object>> projectStatisticsInfos = new ArrayList<Map<String, Object>>();
        for(String projectId: projectInfoList){
            if(StringUtil.isEmpty(projectId)){
                continue;
            }
            //根据项目id查询项目统计
            projectStatisticsInfos.addAll(projectStatisticsInfoMapper.selectUserStudyTimeList(newParams));
        }

        //人员档案
        newParams = new HashMap<String, Object>();
        newParams.put("companyId", companyId);
        newParams.put("userIds", userIds);
        List<Map<String, Object>> personDossiers = new ArrayList<Map<String, Object>>();

        List<List<String>> userIdList = StringUtil.subList(userIds, 1000);
        Iterator<List<String>> it = userIdList.iterator();
        while (it.hasNext()){
            List<String> u = it.next();
            newParams.put("userIds", u);
            personDossiers.addAll(personDossierMapper.selectList(newParams));
        }

        // 培训人数
        Integer countTrainUser = getCountTrainUser(personDossiers);
        // 培训率
        Double percentTrain = 0.0;
        // 总学时
        Double totalClassHour = getTotalClassHour(projectStatisticsInfos, userIds);
        // 人均学时
        Double averagePersonClassHour = 0.00;
        // 培训人次
        Integer countTrain = getCountTrain(personDossiers);
        // 人均培训人次
        Integer averageTrainCount = 0;

        if(countUser != 0){
            percentTrain = Double.parseDouble(new DecimalFormat("0.0").format(countTrainUser * 100.0 / countUser));
            averagePersonClassHour = Double.parseDouble(new DecimalFormat("0.00").format(totalClassHour * 1.00  / countUser));
            averageTrainCount = countTrain / countUser;
        }

        //组装数据
        rs.put("companyId", companyId);
        rs.put("companyName", companyName);
        rs.put("countUser", countUser);
        rs.put("countTrainUser", countTrainUser);
        rs.put("percentTrain", percentTrain);
        rs.put("totalClassHour", totalClassHour);
        rs.put("averagePersonClassHour", averagePersonClassHour);
        rs.put("countTrain", countTrain);
        rs.put("averageTrainCount", averageTrainCount);
        if(null != dataList){
            dataList.add(rs);
        }
        return rs;
    }

    //合并companyTj
    private void sumCompanyTjInfo(CompanyTj companyTj, CompanyTj companyTj1) {
        if(null == companyTj1){
            return;
        }
        if(StringUtil.isEmpty(companyTj.getCountUser())){
            companyTj.setCountUser(companyTj1.getCountUser());
        }else if(!StringUtil.isEmpty(companyTj1.getCountUser())){
            companyTj.setCountUser(Integer.parseInt(companyTj.getCountUser()) + Integer.parseInt(companyTj1.getCountUser()) + "");
        }
        if(StringUtil.isEmpty(companyTj.getCountTrainUser())){
            companyTj.setCountTrainUser(companyTj1.getCountTrainUser());
        }else if(!StringUtil.isEmpty(companyTj1.getCountTrainUser())){
            companyTj.setCountTrainUser(Integer.parseInt(companyTj.getCountTrainUser()) + Integer.parseInt(companyTj1.getCountTrainUser()) + "");
        }
        if(StringUtil.isEmpty(companyTj.getTotalClassHour())){
            companyTj.setTotalClassHour(companyTj1.getTotalClassHour());
        }else if(!StringUtil.isEmpty(companyTj1.getTotalClassHour())){
            companyTj.setTotalClassHour(Double.parseDouble(companyTj.getTotalClassHour()) + Double.parseDouble(companyTj1.getTotalClassHour()) + "");
        }
        if(StringUtil.isEmpty(companyTj.getCountTrain())){
            companyTj.setCountTrain(companyTj1.getCountTrain());
        }else if(!StringUtil.isEmpty(companyTj1.getCountTrain())){
            companyTj.setCountTrain(Integer.parseInt(companyTj.getCountTrain()) + Integer.parseInt(companyTj1.getCountTrain()) + "");
        }
    }

    private void sumCompanyTjInfo(Map<String, Object> data1, Map<String, Object> data2) {
        if(null == data2){
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
                    ? Double.parseDouble(data1.get("totalClassHour").toString())
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
     * 组装数据
     * @param companyTj
     * @param dataList
     */
    public Map<String, Object> resultDataByCompanyTj(CompanyTj companyTj, List<Map<String, Object>> dataList) {
        //学员数量
        Integer countUser = 0;
        // 培训人数
        Integer countTrainUser = 0;
        // 培训率
        Double percentTrain = 0.00;
        // 总学时
        Double totalClassHour = 0.00;
        // 人均学时
        Double averagePersonClassHour = 0.00;
        // 培训人次
        Integer countTrain = 0;
        // 人均培训人次
        Double averageTrainCount = 0.00;

        if(null != companyTj){
            countUser = companyTj.getCountUser() == null ? 0 : Integer.parseInt(companyTj.getCountUser());
            // 培训人数
            countTrainUser = companyTj.getCountTrainUser() == null ? 0 : Integer.parseInt(companyTj.getCountTrainUser());
            // 总学时
            totalClassHour = companyTj.getTotalClassHour() == null ? 0 : Double.parseDouble(companyTj.getTotalClassHour());
            // 培训人次
            countTrain = companyTj.getCountTrain() == null ? 0 : Integer.parseInt(companyTj.getCountTrain());

            if(countUser != 0){
                percentTrain = Double.parseDouble(new DecimalFormat("0.00").format(countTrainUser * 100.0 / countUser));
                averageTrainCount = Double.parseDouble(new DecimalFormat("0.00").format(countTrain * 1.00 / countUser));
                averagePersonClassHour = Double.parseDouble(new DecimalFormat("0.00").format(totalClassHour * 1.00 / countUser));
            }
        }

        //组装数据
        Map<String, Object> rs = new HashedMap();
        rs.put("companyName", companyTj.getCompanyName());
        rs.put("countUser", countUser);
        rs.put("countTrainUser", countTrainUser);
        rs.put("percentTrain", percentTrain);
        rs.put("totalClassHour", totalClassHour);
        rs.put("averagePersonClassHour", averagePersonClassHour);
        rs.put("countTrain", countTrain);
        rs.put("averageTrainCount", averageTrainCount);

        if(null != dataList){
            dataList.add(rs);
        }
        return rs;
    }

    /**
     * 不包含时间查询
     * @param rootId
     * @param searchName
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Map<String, Object> selectCompanyTj(String rootId, String searchName, String companyName, User user,Integer pageNum, Integer pageSize){
        //转换成结果数据
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

        //所有子记录
        Map<String, Object> newParams = new HashMap<String, Object>();
        newParams.put("companyId", user.getCompanyId());
        newParams.put("companyName", ParamsUtil.joinLike(searchName));
        newParams.put("pid", rootId);
        List<CompanySupervise> companySupervises = companySuperviseService.selectList(newParams);

        //保存公司名称
        String[] companyAry = new String[companySupervises.size() + 1];
        CompanyTj childCompanyTj = null;
        int i = 1;
        for(CompanySupervise companySupervise: companySupervises){
            childCompanyTj = getTotalByRootId(companySupervise.getId(), user.getCompanyId(), companySupervise.getCompanyName());

            //保存到输出集合内
            resultDataByCompanyTj(childCompanyTj, dataList);

            //保存公司名称
            companyAry[i++] = companySupervise.getCompanyName();
        }

        //搜索字符串searchName为空
        boolean isAddTotal = StringUtil.isEmpty(searchName) || (!StringUtil.isEmpty(companyName) &&
                companyName.indexOf(searchName) != -1);
        if(isAddTotal){
            //增加总记录---标示是统计记录
            Map<String, Object> total = resultDataByCompanyTj(getTotalByRootId(rootId, rootId, companyName), null);
            total.put("isTop", "1");
            dataList.add(0, total);
            companyAry[0] = companyName;
        }

        //分页
        int count = companySupervises.size() + 1;
        Page<Map<String, Object>> page = new Page(count, pageNum, pageSize);
        Integer endNum = page.getStartNum() + page.getPageSize();
        if(count >= endNum){
            dataList = dataList.subList(page.getStartNum(), endNum);
            companyAry = Arrays.copyOfRange(companyAry, page.getStartNum(), endNum);
        }else{
            dataList = dataList.subList(page.getStartNum(), dataList.size());
            companyAry = Arrays.copyOfRange(companyAry, page.getStartNum(), dataList.size());
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
     * 包含时间的查询
     * @param rootId
     * @param searchName
     * @param startTime
     * @param endTime
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Map<String, Object> selectCompanyTjByTime(String rootId, String searchName, String companyName,
                                      String startTime, String endTime, User user,Integer pageNum, Integer pageSize){
        //转换成结果数据
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //所有子记录
        Map<String, Object> newParams = new HashMap<String, Object>();
        newParams.put("companyId", user.getCompanyId());
        newParams.put("companyName", ParamsUtil.joinLike(searchName));
        newParams.put("pid", rootId);
        List<CompanySupervise> companySupervises = companySuperviseService.selectList(newParams);
        //保存公司名称
        String[] companyAry = new String[companySupervises.size() + 1];
        for(int i = 0; i < companySupervises.size(); i++){
            CompanySupervise companySupervise = companySupervises.get(i);

            //日期未选时直接查统计表
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("companyId", companySupervise.getCompanyId());
            getTotalByRootId(companySupervise.getId(), companySupervise.getCompanyId(), companySupervise.getCompanyName(),
                    startTime, endTime, dataList);

            //保存公司名称
            companyAry[i + 1] = companySupervise.getCompanyName();
        }

        //搜索字符串searchName为空
        boolean isAddTotal = StringUtil.isEmpty(searchName) ||
                (!StringUtil.isEmpty(companyName) && companyName.indexOf(searchName) != -1);
        if(isAddTotal){
            //增加总记录
            Map<String, Object> total = getTotalByRootId(rootId, rootId, companyName, startTime, endTime, null);
            total.put("isTop", "1");
            dataList.add(0, total);
            companyAry[0] = companyName;
        }

        //分页
        int count = companySupervises.size() + 1;
        Page<Map<String, Object>> page = new Page(count, pageNum, pageSize);
        Integer endNum = page.getStartNum() + page.getPageSize();
        if(count >= endNum){
            dataList = dataList.subList(page.getStartNum(), endNum);
            companyAry = Arrays.copyOfRange(companyAry, page.getStartNum(), endNum);
        }else{
            dataList = dataList.subList(page.getStartNum(), dataList.size());
            companyAry = Arrays.copyOfRange(companyAry, page.getStartNum(), dataList.size());
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
     * 根据根节点查询统计总和
     * @param rootId
     * @param companyId
     * @param companyName
     * @return
     */
    public CompanyTj getTotalByRootId(String rootId, String companyId, String companyName){
        //包含自己和所有子节点
        String companyIds = companySuperviseService.getChildCompanyIds(rootId, companyId);
        List<String> companyIdList = Arrays.asList(companyIds.split(","));
        Map<String, Object> params;

        List<List<String>> companyListToList = StringUtil.subList(companyIdList, 100);
        Iterator<List<String>> it = companyListToList.iterator();
        CompanyTj companyTj = new CompanyTj();
        while (it.hasNext()){
            List<String> list = it.next();
            params = new HashedMap();
            params.put("companyIds", list);
            sumCompanyTjInfo(companyTj, companyTjMapper.selectTotal(params));
        }

        companyTj.setCompanyId(companyId);
        companyTj.setCompanyName(companyName);
        return companyTj;
    }

    /**
     * 查询所有子集合
     * @param rootId
     * @param companyId
     * @param rootName
     * @param startTime
     * @param endTime
     * @param dataList
     * @return
     */
    public Map<String, Object> getTotalByRootId(String rootId, String companyId, String rootName, String startTime, String endTime,
                                                List<Map<String, Object>> dataList){
        //包含自己和所有子节点
        String companyIds = companySuperviseService.getChildCompanyIds(rootId, companyId);
        List<String> companyIdList = Arrays.asList(companyIds.split(","));
        Map<String, Object> rs = MapUtils.newHashMap();
        for(String id: companyIdList){
            if(StringUtil.isEmpty(id)){
                continue;
            }
            sumCompanyTjInfo(rs,getDataMethod(id, "", startTime, endTime, null));
        }
        rs.put("companyId", rootId);
        rs.put("companyName", rootName);
        if(null != dataList){
            dataList.add(rs);
        }
        return rs;
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

    //查询统计信息
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

    //培训人数
    public Integer getCountTrainUser(List<Map<String, Object>> personDossiors){
        int result = 0;
        Iterator<Map<String, Object>> it = personDossiors.iterator();

        while (it.hasNext()){
            Map<String, Object> person = it.next();
            if(null == person.get("train_count") ||
                    "".equals(person.get("train_count").toString()) ||
                    "0".equals(person.get("train_count").toString())){
                continue;
            }
            result++;
        }
        return result;
    }

    //获取培训次数
    public Integer getCountTrain(List<Map<String, Object>> personDossiors){
        int result = 0;
        Iterator<Map<String, Object>> it = personDossiors.iterator();
        while (it.hasNext()){
            Map<String, Object> person = it.next();
            if(null == person.get("train_count") ||
                    "".equals(person.get("train_count").toString()) ||
                    "0".equals(person.get("train_count").toString())){
                continue;
            }
            result += Integer.parseInt(person.get("train_count").toString());
        }
        return result;
    }

    //总学时
    public Double getTotalClassHour(List<Map<String, Object>> projectStatisticsInfos, List<String> userIds){
        Long total = 0L;
        for(Map<String, Object> projectStatisticsInfo: projectStatisticsInfos){
            Object userId = projectStatisticsInfo.get("user_id");
            if(null != userIds && !userIds.contains(userId)){
                continue;
            }
            if(null != userId && null != projectStatisticsInfo.get("total_studytime")){
                total += Long.parseLong(projectStatisticsInfo.get("total_studytime").toString());
            }
        }
        return Double.parseDouble(new DecimalFormat("0.00").format(total * 1.00 / (60*60)));
    }

    @Override
    public Map<String, Object> anaylze(Map<String, Object> params) {
        String companyId = (String) params.get("companyId");

        if (StringUtils.isEmpty(companyId)) {
            return null;
        }

        List <String> projectStatus = new ArrayList <String>();
        projectStatus.add("3");                                   //进行中
        projectStatus.add("4");                                   //已结束
        params.put("projectStatus", projectStatus);

        DecimalFormat df = new DecimalFormat("0.00");
        // 学员数量
        Integer countUser = userService.selectUserCount(params);
        //培训人数

        Integer countTrainUser = projectStatisticsInfoService.selectTrainUserCount(params);
        String percentTrainComplete = "0.00";
        if (0 != countUser) {
            //培训率
            percentTrainComplete = df.format(new Double(countTrainUser) / new Double(countUser) * 100);
        }

        //课程数量
        Integer countCourse = companyCourseService.selectCompanyCourseCount(params);
        //题目数量
        Integer countQuestion = courseInfoService.selectCourseQuestionCount(params);
        // 项目数量
        Integer countProject = projectInfoService.selectProjectIdCount(params);


        //参与培训人次
        List <String> projectTypes = new ArrayList <String>();
        projectTypes.add(ProjectTypeEnum.QuestionType_1.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_4.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_5.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_7.getValue());
        params.put("projectTypes", projectTypes);
        Integer countTrain = projectStatisticsInfoService.selectJoinTrainOrExamUserCount(params);


        //参与考试人次
        projectTypes = new ArrayList <String>();
        projectTypes.add(ProjectTypeEnum.QuestionType_3.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_5.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_6.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_7.getValue());
        params.put("projectTypes", projectTypes);
        Integer countExam = projectStatisticsInfoService.selectJoinTrainOrExamUserCount(params);

        //完成培训人次
        Integer countTrainCompleteYes = projectStatisticsInfoService.selectCompleteTrainUserCount(params);
        //考试合格人次
        Integer countExamPassYes = projectStatisticsInfoService.selectPassExamUserCount(params);


        projectTypes = new ArrayList <String>();
        params.put("projectTypes", projectTypes);
        //累计学时
        Double totalClassHour = projectStatisticsInfoService.selecttotalClassHour(params);
        /*totalClassHour = (totalClassHour + study_time) / 3600;

        Double averagePersonClassHour = 0.00;
        if (0 != countUser) {
            //人均学时
            averagePersonClassHour = (double) totalClassHour / countUser;
        }*/

        //年度累计学时
        Double totalYearClassHour = projectStatisticsInfoService.selecttotalYearClassHour(params);
         /*totalYearClassHour = (totalYearClassHour + study_time) / 3600;

        Double averageYearClassHour = 0.00;
        if (0 != countUser) {
            //年度人均学时
            averageYearClassHour = (Double) totalYearClassHour / countUser;
        }*/

        Map <String, Object> param = new HashMap <String, Object>();
        param.put("companyId", companyId);
        param.put("countUser", countUser.toString());
        param.put("countTrainUser", countTrainUser.toString());
        param.put("percentTrainComplete", percentTrainComplete);
        param.put("countCourse", countCourse.toString());
        param.put("countQuestion", countQuestion.toString());
        param.put("countProject", countProject.toString());
        param.put("countTrain", countTrain.toString());
        param.put("countTrainCompleteYes", countTrainCompleteYes.toString());
        param.put("countExam", countExam.toString());
        param.put("countExamPassYes", countExamPassYes.toString());
        param.put("totalClassHour", totalClassHour.toString());
        //param.put("averagePersonClassHour", averagePersonClassHour.toString());
        param.put("totalYearClassHour", totalYearClassHour.toString());
       // param.put("averageYearClassHour", averageYearClassHour.toString());

        return param;
    }


    /**
     * 更新统计（更新人员数量及其相关的统计指标）
     * @param countUser
     * @param companyId
     * @param userName
     */
    @Override
    public void updateCompanyTj(int countUser, String companyId, String userName){
        if(countUser == 0){
            return;
        }
        //公司统计表
        CompanyTj companyTj = this.selectOne(companyId, userName);

        countUser = Integer.parseInt(companyTj.getCountUser()) + countUser;
        if(countUser < 0){
            countUser = 0;
        }
        companyTj.setCountUser(countUser + "");

        //更新统计信息
        Double total = Double.parseDouble(companyTj.getTotalClassHour());
        Double total_year = Double.parseDouble(companyTj.getTotalYearClassHour());

        Double average_person_class_hour = 0.00;
        Double average_year_class_hour = 0.00;
        Double percentTrainComplete = 0.00;

        if(countUser > 0 ){
            //总学时、年度学时
            average_person_class_hour = Double.parseDouble(new DecimalFormat("0.00")
                    .format(total * 1.00 / countUser));
            average_year_class_hour = Double.parseDouble(new DecimalFormat("0.00")
                    .format(total_year * 1.00 / countUser));

            //培训率
            percentTrainComplete = Double.parseDouble(new DecimalFormat("0.00").format(
                    new Double(companyTj.getCountTrainUser()) / new Double(countUser) * 100.00));
        }

        companyTj.setAverageYearClassHour(average_person_class_hour.toString());
        companyTj.setAverageYearClassHour(average_year_class_hour.toString());
        companyTj.setPercentTrainComplete(percentTrainComplete.toString());
        //最后操作时间、人员
        companyTj.setOperTime(DateUtils.formatDateTime(new Date()));
        companyTj.setOperUser(userName);

        Map<String, Object> companyTjMap = new HashMap<String, Object>();
        MapUtils.putAll(companyTjMap, companyTj);
        companyTjMapper.update(companyTjMap);
    }

}
