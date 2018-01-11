package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.*;
import com.bossien.train.domain.*;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.IDepartmentService;
import com.bossien.train.service.IPersonDossierService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by A on 2017/7/25.
 */

@Service(value="personDossierService")
public class PersonDossierServiceImpl implements IPersonDossierService {

    private static final String pId = PropertiesUtils.getValue("p_id");

    @Autowired
    private PersonDossierMapper personDossierMapper;

    @Autowired
    private ProjectStatisticsInfoMapper projectStatisticsInfoMapper;

    @Autowired
    private ExamScoreMapper examScoreMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private UserTrainRoleMapper userTrainRoleMapper;
    @Autowired
    private IDepartmentService departmentService;

    @Override
    public void insert(PersonDossier personDossier) {

        personDossierMapper.insert(personDossier);
    }

    @Override
    public int insertBatch(List<PersonDossier> personDossiers) {

        return personDossierMapper.insertBatch(personDossiers);
    }

    @Override
    public void update(Map<String, Object> params) {

        personDossierMapper.update(params);
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {

        return personDossierMapper.selectList(params);
    }

    @Override
    public Page<Map<String, Object>> queryForPagination(Map<String, Object> params,
                                                        Integer pageNum, Integer pageSize, User user) {
        //para.put("isValid", User.IsValid.TYPE_1.getValue());
        //params.put("userType", User.UserType.TYPE_3.getValue());
        //查询所有人的档案  包含管理员可能存在学员角色
        Integer count =userMapper.selectUserCount(params);
        Page<Map<String, Object>> page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> ulist = userMapper.selectUserByDeptId(params);
        List<String> userlist=new ArrayList<String>();
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        if(ulist.size()>0 && !ulist.isEmpty()){
            for(Map<String, Object> umap:ulist){
                if(!umap.isEmpty() && umap.get("userId")!=null){
                    if(umap.get("userId").toString()!=null){
                        userlist.add(umap.get("userId").toString());
                    }
                }
            }
            params.put("userIds",userlist);
            params.remove("startNum");
            params.remove("endNum");
            listMap = personDossierMapper.selectList(params);
        }
        page.setDataList(listMap);
        return page;
    }

    @Override
    public Page<Map<String, Object>> queryPersonDossierForPagination(Map<String, Object> params, Integer pageNum, Integer pageSize, User user) {
        //查询所有人的档案  包含管理员可能存在学员角色
        Integer count = personDossierMapper.selectCount(params);
        Page<Map<String, Object>> page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        listMap = personDossierMapper.selectList(params);
        page.setDataList(listMap);
        return page;
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return personDossierMapper.selectCount(params);
    }

    @Override
    public Map<String, Object> selectOne(Map<String, Object> params) {

        return personDossierMapper.selectOne(params);
    }

    @Override
    public void export(Map<String, Object> params, OutputStream output) throws Exception{
        //人员档案
        List<Map<String, Object>> personDossiers = personDossierMapper.selectList(params);
        //未查询到人员档案时推出
        if(null == personDossiers && personDossiers.size() < 1){
            return;
        }

        //个人学时统计表
        List<Map<String, Object>> stratisticsInfos = projectStatisticsInfoMapper.selectList(params);
        Map<String, List<Map<String, Object>>> stratisticsInfoMap = listToMap(stratisticsInfos, "user_id");

        //成绩
        params.put("examType", "2");//考试类型，2正式考试
        List<ExamScore> examScores = examScoreMapper.selectMastScoreByProjectAndUser(params);
        Map<String, List<ExamScore>> examScoreMap = listObjToMap(examScores, "userId");

        //用户详情
//        List<User> users = userMapper.queryAllUserList(params);
//        Map<String, List<User>> userMap = listToMap(users, "id");

        export(personDossiers, stratisticsInfoMap, examScoreMap, output);
    }

    @Override
    public Map<String, Object> selectRank(String userId, String companyId) {
        Map<String, Object> params = new HashedMap();
        params.put("userId", userId);
        params.put("companyId", companyId);
        params.put("isValid", Department.IsValid.TYPE_1.getValue());
        //查询本公司的排行
        Map<String, Object> result = personDossierMapper.selectRank(params);
        Company company = companyService.selectOne(params);
        if (company != null) {
            if(StringUtils.isNotEmpty(company.getVarName())){
                result.put("company_name", company.getVarName());
            }
        }
       /* Map<String, Object> companyMap = companyService.getCompanyInfo(companyId);
        if(companyMap != null){
            String parentId = companyMap.get("intpid").toString();
            if(!pId.equals(parentId)){
                String rank = getRank(userId,parentId);
                Map<String, Object> companyMap_ = companyService.getCompanyInfo(parentId);
                if(result != null){
                    result.put("prevName",companyMap_.get("varName"));
                    result.put("prevRank",rank);
                }
                if(companyMap_ != null){
                    String parentId_ = companyMap_.get("intpid").toString();
                    if(!pId.equals(parentId_)){
                        String rank_ = getRank(userId,parentId_);
                        Map<String, Object> companyMap_1 = companyService.getCompanyInfo(parentId_);
                        result.put("pentName",companyMap_1.get("varName"));
                        result.put("pentRank",rank_);
                    }
                }
            }
        }*/
        return result;
    }

    @Override
    public int updateByUserIds(Map<String, Object> params) {
        return personDossierMapper.updateByUserIds(params);
    }
    public  PersonDossier build(){
        PersonDossier personDossier = new PersonDossier();
        personDossier.setId(sequenceService.generator());
        personDossier.setTotalStudytime(0);
        personDossier.setYearStudytime(0);
        personDossier.setTrainCount(0);
        personDossier.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        personDossier.setOperTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return personDossier;
    }

    @Override
    public void add(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String companyId = (String) params.get("companyId");
        String userName = (String) params.get("userName");
        String deptId = (String) params.get("deptId");
        String deptName = "";

        //查询公司名称
        Company company = companyService.selectOne(params);
        String  companyName = company !=null?company.getVarName():"默认单位";


        //查询用户所在的部门名字
        if(StringUtils.isEmpty(deptId)) {
            deptId = companyId;
        }else{
            Map<String,Object> map = new HashMap<>();
            map.put("id",deptId);
            map.put("isValid",Department.IsValid.TYPE_1.getValue());
            //查询部门信息
            Department department = departmentService.selectOne(map);
            if(department != null){
                map.put("id",department.getParentId());
                deptName = department.getDeptName();
            }
        }

        //查询用户角色
        String roleId = "-1";
        String roleName = "默认角色";
        UserTrainRole userTrainRole = userTrainRoleMapper.selectOne(new UserTrainRole(userId));
        if(userTrainRole != null && StringUtils.isNotEmpty(userTrainRole.getTrainRoleId())){
            roleId = userTrainRole.getTrainRoleId();
            roleName = userTrainRole.getRoleName();
        }


        PersonDossier personDossier = this.build();

        personDossier.setUserId(userId);
        personDossier.setUserName(userName);
        personDossier.setCompanyId(companyId);
        personDossier.setCompanyName(companyName);
        personDossier.setRoleId(roleId);
        personDossier.setDeptId(companyId);
        personDossier.setRoleName(roleName);
        personDossier.setDeptName(deptName);
        personDossier.setCreateUser(userName);
        personDossier.setOperUser(userName);

        //插入
        personDossierMapper.insert(personDossier);
     }

    private String getRank(String userId, String parentId) {
        Map<String, Object> params = new HashedMap();
        List<String> companyIds = new ArrayList<>();
        params.put("userId", userId);
        Map<String, Object> datas = null;
        String ids = companyService.getChildCompanyIds(parentId);
        if(StringUtils.isNotEmpty(ids)){
            companyIds.clear();
            companyIds = Arrays.asList(ids.split(","));
            params.put("companyIds", companyIds);
            datas = personDossierMapper.selectRank(params);
        }
        if(datas != null){
            return datas.get("rownum").toString();
        }
        return "0";
    }

    public void exportFile(String userName, Map<String, Object> personDossier, List<Map<String, Object>> trainLMap,
                           List<ExamScore> examLMap, File archive) throws Exception{
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(userName);
        sheet.setColumnWidth(0, 10*256);
        sheet.setColumnWidth(1, 30*256);
        sheet.setColumnWidth(2, 30*256);
        sheet.setColumnWidth(3, 30*256);
        sheet.setColumnWidth(4, 40*256);
        sheet.setColumnWidth(5, 15*256);
        sheet.setColumnWidth(6, 15*256);
        sheet.setColumnWidth(7, 15*256);
        sheet.setColumnWidth(8, 15*256);
        sheet.setColumnWidth(9, 15*256);
        sheet.setColumnWidth(10, 15*256);
        // 居中样式
        XSSFCellStyle centerStyle = wb.createCellStyle();centerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);centerStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 居右样式
        XSSFCellStyle rightStyle = wb.createCellStyle();rightStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);rightStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        XSSFRow row = null;
        XSSFCell cell = null;
        // 行索引
        int rowIndex = 0;
        // 标题-合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));

        // 标题-创建
        String sheetHeader = MessageFormat.format("{0}安全生产培训档案", userName);
        row = sheet.createRow(rowIndex);row.setHeight((short)725);
        cell = row.createCell(0);
        cell.setCellValue(sheetHeader);
        cell.setCellStyle(centerStyle);

        rowIndex++;

        // 制表时间-合并单元格
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 10));
        // 制表时间-创建
        String createSheetTime = MessageFormat.format("制表时间: {0} ", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        row = sheet.createRow(rowIndex);row.setHeight((short)525);
        cell = row.createCell(0);
        cell.setCellValue(createSheetTime);
        cell.setCellStyle(rightStyle);

        rowIndex++;

        // 人员概况-合并单元格
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 10));
        // 人员概况-创建
        row = sheet.createRow(rowIndex);row.setHeight((short)525);
        cell = row.createCell(0);
        cell.setCellValue("人员概况");
        cell.setCellStyle(centerStyle);

        rowIndex++;

        // 人员概况-表头-创建
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 4, 5));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 7));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 8, 10));
        row = sheet.createRow(rowIndex);row.setHeight((short)525);
        String[] userInfoHeader = {"姓名", "部门/班组", "登记时间", "年度学时（时）", "累计学时（时）"};
        for(int i=0; i<userInfoHeader.length; i++){
            cell = row.createCell(i*2);
            cell.setCellValue(userInfoHeader[i]);
            cell.setCellStyle(centerStyle);
        }

        rowIndex++;

        // 人员概况-内容-创建
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 4, 5));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 7));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 8, 10));
        row = sheet.createRow(rowIndex);row.setHeight((short)525);
        cell = row.createCell(0);// 姓名
        cell.setCellValue(userName);
        cell.setCellStyle(centerStyle);
        cell = row.createCell(1*2);// 部门/班组
        cell.setCellValue(personDossier.get("dept_name").toString());//department
        cell.setCellStyle(centerStyle);
        cell = row.createCell(2*2);// 受训角色
//            cell.setCellValue(personDossier.get("role_name").toString());//roleNames
//            cell.setCellStyle(centerStyle);
//            cell = row.createCell(3*2);// 登记时间
        cell.setCellValue(personDossier.get("create_time").toString().substring(0, 19));
        cell.setCellStyle(centerStyle);
        cell = row.createCell(3*2);// 年度学时（时）
        long yearStudytime = Long.parseLong(personDossier.get("year_studytime").toString());
        cell.setCellValue(DateUtils.getHourStr(yearStudytime));
        cell.setCellStyle(centerStyle);
        cell = row.createCell(4*2);// 总学时（时）
        long totalStudytime = Long.parseLong(personDossier.get("total_studytime").toString());
        cell.setCellValue(DateUtils.getHourStr(totalStudytime));
        cell.setCellStyle(centerStyle);

        rowIndex++;
        // 考试记录-标题-合并单元格
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 10));
        rowIndex++;

        // 培训记录-合并单元格
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 10));
        row = sheet.createRow(rowIndex);row.setHeight((short)525);
        cell = row.createCell(0);
        cell.setCellValue("培训记录");
        cell.setCellStyle(centerStyle);

        rowIndex++;

        // 培训记录-表头-创建
        row = sheet.createRow(rowIndex);row.setHeight((short)525);
        String[] trainHeader = {"序号", "项目名称", "培训时间", "练习时间", "考试时间", "已修学时（分）", "答题学时（分）", "已答题量", "正确率（%）", "是否完成", "备注"};
        for(int i=0; i<trainHeader.length; i++){
            cell = row.createCell(i);
            cell.setCellValue(trainHeader[i]);
            cell.setCellStyle(centerStyle);
        }

        // 培训记录-内容-创建
        int trainRecordIndex = 0;	// 培训记录序号
        String datBeginTime_train = "";	// 培训开始时间
        String datEndTime_train = "";		// 培训结束时间
        long sumIntStudyTime = 0L;	// 已修学时
        long sumIntRequirement = 0L;// 应修学时
        // 培训的培训学时
        BigDecimal sumIntStudyTime1 = new BigDecimal(0.00d);
        // 练习的答题学时
        BigDecimal sumIntStudyTime2 = new BigDecimal(0.00d);
        // 考试的答题学时
        BigDecimal sumIntStudyTime3 = new BigDecimal(0.00d);
        // 练习项目id
        String varExerciseId = "";
        // 考试项目id
        String varExamId = "";

        Map<String, ProjectInfo> projectInfoMap = new HashedMap();
        if(null != trainLMap && trainLMap.size() > 0){
            for(Map<String, Object> map : trainLMap){
                //项目详情
                Object projectId = map.get("project_id");
                if(null == projectId){
                    continue;
                }
                ProjectInfo projectInfo = projectInfoMap.get(projectId.toString());
                if(null == projectInfo){
                    projectInfo = projectInfoMapper.selectProjectInfoById(projectId.toString());
                    projectInfoMap.put(projectId.toString(), projectInfo);
                }

                if(null == projectInfo){
                    continue;
                }

                //String[] trainHeader = {"序号", "项目名称", "培训时间", "练习时间", "考试时间", "已修学时（分）", "答题学时（分）", "已答题量", "正确率（%）", "是否完成", "备注"};

                rowIndex++;
                row = sheet.createRow(rowIndex);row.setHeight((short)525);
                trainRecordIndex++;
                // 序号
                cell = row.createCell(0);
                cell.setCellValue(trainRecordIndex);
                cell.setCellStyle(centerStyle);
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                // 培训名称
                String varTrainName = projectInfo.getProjectName();
                cell = row.createCell(1);
                cell.setCellValue(varTrainName);
                cell.setCellStyle(centerStyle);
                // 培训时间
                String train_time = projectInfo.getProjectTrainTime();
                cell = row.createCell(2);
                cell.setCellValue(train_time);
                cell.setCellStyle(centerStyle);
                //练习时间
                String exerciseTime = projectInfo.getProjectExerciseTime();
                cell = row.createCell(3);
                cell.setCellValue(exerciseTime);
                cell.setCellStyle(centerStyle);
                //考试时间
                String examTime = projectInfo.getProjectExamTime();
                cell = row.createCell(4);
                cell.setCellValue(examTime);
                cell.setCellStyle(centerStyle);
                //已修学时（分）
                cell = row.createCell(5);
                long year_studytime = Long.parseLong(map.get("total_studytime").toString());
                cell.setCellValue(DateUtils.getMinDouble(year_studytime));
                cell.setCellStyle(centerStyle);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                //答题学时
                cell = row.createCell(6);
                long answer_studytime = Long.parseLong(map.get("answer_studytime").toString());
                cell.setCellValue(DateUtils.getMinDouble(answer_studytime));
                cell.setCellStyle(centerStyle);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                //已答题量
                cell = row.createCell(7);
                long yet_answered = Long.parseLong(map.get("yet_answered").toString());
                cell.setCellValue(yet_answered);
                cell.setCellStyle(centerStyle);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                //正确率
                cell = row.createCell(8);
                cell.setCellValue(Double.parseDouble(map.get("correct_rate").toString()));
                cell.setCellStyle(centerStyle);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                // 是否完成
                Object train_status = map.get("train_status");
                cell = row.createCell(9);
                cell.setCellValue(train_status == null || !train_status.toString().equals("2")  ? "未完成" : "已完成");
                cell.setCellStyle(centerStyle);
                // 备注
                cell = row.createCell(10);
                cell.setCellValue("");
                cell.setCellStyle(centerStyle);
            }
        }

        rowIndex++;
        // 考试记录-标题-合并单元格
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 10));
        rowIndex++;

        // 考试记录-标题-合并单元格
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 10));
        row = sheet.createRow(rowIndex);row.setHeight((short)525);
        cell = row.createCell(0);
        cell.setCellValue("考试记录");
        cell.setCellStyle(centerStyle);

        rowIndex++;


        // 考试记录-表头-创建
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 4, 5));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 7));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 8, 9));
        row = sheet.createRow(rowIndex);row.setHeight((short)525);
        String[] examHeader = {"序号", "考试名称", "考试时间", "考试成绩", "是否合格", "备注"};
        for(int i=0; i<examHeader.length; i++){
            cell = row.createCell(i*2);
            cell.setCellValue(examHeader[i]);
            cell.setCellStyle(centerStyle);
        }

        // 考试记录-内容-创建
        int examRecordIndex = 0;		// 考试记录序号
        String varExaminationName = "";	// 考试名称
        String datBeginTime_exam = "";	// 考试开始时间
        String datEndTime_exam = "";	// 考试结束时间
        Integer intScore = 0;				// 考试成绩
        String chrIsPassed = "";		// 是否合格
        if(null != examLMap && examLMap.size() > 0){
            for(ExamScore examScore : examLMap){
                //项目详情
                String projectId = examScore.getProjectId();
                ProjectInfo projectInfo = projectInfoMap.get(projectId);
                if(null == projectInfo){
                    projectInfo = projectInfoMapper.selectProjectInfoById(projectId);
                    projectInfoMap.put(projectId, projectInfo);
                }

                if(null == projectInfo){
                    continue;
                }

                rowIndex++;
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 3));
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 4, 5));
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 7));
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 8, 9));
                row = sheet.createRow(rowIndex);row.setHeight((short)525);
                examRecordIndex++;
                // 序号
                cell = row.createCell(0);
                cell.setCellValue(examRecordIndex);
                cell.setCellStyle(centerStyle);
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                // 考试名称
                varExaminationName = projectInfo.getProjectName();
                cell = row.createCell(1*2);
                cell.setCellValue(varExaminationName);
                cell.setCellStyle(centerStyle);
                // 考试时间
                String exam_time = examScore.getExamTime();
                Double examDuration = examScore.getExamDuration() * 60 * 1000;
                String end_time = DateUtils.formatDateTime(DateUtils.parseDateTime(exam_time).getTime() + examDuration.longValue());
                cell = row.createCell(2*2);
                cell.setCellValue(StringUtils.substring(exam_time, 0, 16) + " 至 " + StringUtils.substring(end_time, 0, 16));
                cell.setCellStyle(centerStyle);
                // 考试成绩
                intScore = examScore.getScore();
                cell = row.createCell(3*2);
                cell.setCellValue(intScore);
                cell.setCellStyle(centerStyle);
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                // 是否合格
                chrIsPassed = examScore.getIsPassed();
                cell = row.createCell(4*2);
                cell.setCellValue("1".equals(chrIsPassed)?"合格":"不合格");
                cell.setCellStyle(centerStyle);
                // 备注
                cell = row.createCell(5*2);
                cell.setCellValue("");
                cell.setCellStyle(centerStyle);
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(archive);
        // 数据写入文件
        wb.write(fileOutputStream);
        fileOutputStream.close();
    }

    public void export(List<Map<String, Object>> personDossiers, Map<String, List<Map<String, Object>>> stratisticsInfoMap,
                       Map<String, List<ExamScore>> examScoreMap, OutputStream output) throws Exception{
        List<File> archives = new ArrayList<File>();
        File archive = null;
        for(Map<String, Object> personDossier : personDossiers){
            String userName = null == personDossier.get("user_name") ? "" : personDossier.get("user_name").toString();

            Object userId = personDossier.get("user_id");
            if(null == userId){
                return;
            }

            //创建文件
            archive = this.produceArchive(userName);
            //写入流
            exportFile(userName, personDossier, stratisticsInfoMap.get(userId.toString()),
                    examScoreMap.get(userId.toString()), archive);
            //保存
            archives.add(archive);
        }

        // 档案打包zip
        String fileName = System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID() + ".zip";
        ZipUtil zc = new ZipUtil(fileName);
        zc.compress(archives); // 压缩多个文件
        // 下载zip
        File file = zc.getZipFile();
        // 删除临时文件
        for(File f : archives){
            if(f.exists()&&f.isFile()) {
                f.delete();
            }
        }
        ExcelUtil.copyLarge(new FileInputStream(file), output);
    }

    // 产生学员的档案文件
    private File produceArchive(String userName) throws IOException {
        File tempFile = new File(ExcelUtil.TP_EXCEL_ARCHIVE_TEMP_PATH);
        if(!tempFile.exists()){
            tempFile.mkdirs();
        }
        if(StringUtils.isEmpty(userName)) {
            return File.createTempFile("temp", ".xlsx", tempFile);
        }
        // 创建
        return File.createTempFile(userName + "-" + "temp", ".xlsx", tempFile);
    }

    /**
     * 将list根据columnName分组
     * @param t
     * @param columnName
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> Map<String, List<T>> listObjToMap(List<T> t, String columnName) throws Exception{
        Map<String, List<T>> result = new HashedMap();
        for(T tt : t){
            Field[] fields = tt.getClass().getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                if(field.getName().equals(columnName)){
                    Object value = field.get(tt);
                    List<T> data = new ArrayList<>();
                    if(null == value){
                         break;
                    }
                    if(null != result.get(value.toString())){
                        data = result.get(value.toString());
                    }
                    data.add(tt);
                    result.put(value.toString(), data);
                }
            }
        }
        return result;
    }

    public Map<String, List<Map<String, Object>>> listToMap(List<Map<String, Object>> t, String columnName) throws Exception{
        Map<String, List<Map<String, Object>>> result = new HashedMap();
        for(Map<String, Object> tt : t){
            Object value = tt.get(columnName);
            if(null == value){
                continue;
            }
            List<Map<String, Object>> data = new ArrayList<>();
            if(null != result.get(value.toString())){
                data = result.get(value.toString());
            }
            data.add(tt);
            result.put(value.toString(), data);
        }
        return result;
    }

    /**
     * 变更单位时修改个人档案中的单位相关字段
     * @param params
     * @return
     */
    @Override
    public int updateCompanyId(Map<String, Object> params){
        return personDossierMapper.updateCompanyId(params);
    }

    @Override
    public PersonDossier selectStudySelf(Map<String, Object> params) {
        return personDossierMapper.selectStudySelf(params);
    }
}
