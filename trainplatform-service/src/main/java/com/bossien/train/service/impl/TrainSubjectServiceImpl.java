package com.bossien.train.service.impl;


import com.bossien.train.dao.ap.SubjectCourseMapper;
import com.bossien.train.dao.ap.TrainSubjectMapper;
import com.bossien.train.domain.CompanyTrainSubject;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.TrainSubject;
import com.bossien.train.domain.dto.TrainSubjectDTO;
import com.bossien.train.domain.dto.TrainSubjectMessage;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.StringUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zhaoli on 2017/8/2.
 */

@Service
public class TrainSubjectServiceImpl implements ITrainSubjectService {

    private static final Logger logger = LogManager.getLogger(TrainSubjectServiceImpl.class);

    @Autowired
    private TrainSubjectMapper trainSubjectMapper;
    @Autowired
    private ICompanyTrainSubjectService companyTrainSubjectService;
    @Autowired
    private SubjectCourseMapper subjectCourseMapper;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private ISequenceService iSequenceService;
    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    private String company_Id = PropertiesUtils.getValue("COMPANY_Id");//获取配置文件
    private String system_number = PropertiesUtils.getValue("SYSTEM_NUMBER");//获取配置文件
    private String chrIsValid = PropertiesUtils.getValue("CHRISVAl_ID");//获取配置文件

    @Override
    public List<TrainSubject> selectList(Map<String, Object> params) {
        return trainSubjectMapper.selectList(params);
    }

    @Override
    public List<TrainSubject> selectVerify(Map<String, Object> params) {
        return trainSubjectMapper.selectVerify(params);
    }

    @Override
    public void insertMessage(TrainSubjectMessage trainSubjectMessage) {
        TrainSubject trainSubject = new TrainSubject();
        trainSubject.setVarId(trainSubjectMessage.getId());
        trainSubject.setSubjectName(trainSubjectMessage.getSubjectName());
        trainSubject.setSubjectDesc(trainSubjectMessage.getSubjectDesc());
        trainSubject.setSource(trainSubjectMessage.getSource());
        trainSubject.setIsValid(trainSubjectMessage.getIsValid());
        trainSubject.setCreateUser(trainSubjectMessage.getCreateUser());
        trainSubject.setOperUser(trainSubjectMessage.getOperUser());
        trainSubject.setCreateDate(trainSubjectMessage.getCreateDate());
        trainSubject.setOperDate(trainSubjectMessage.getOperDate());
        trainSubject.setLogo(trainSubjectMessage.getfId());
        trainSubject.setOrder(trainSubjectMessage.getOrder());
        List<String> stringList = subjectCourseMapper.selectCourseIds(trainSubjectMessage.getId());
        trainSubject.setCourseCount(String.valueOf(stringList.size()));
        trainSubjectMapper.insert(trainSubject);//将数据保存在主题表中
    }


    @Override
    public void insert(TrainSubject dto) {
        trainSubjectMapper.insert(dto);
    }

    @Override
    public int insertBatch(List<TrainSubject> list) {
        return trainSubjectMapper.insertBatch(list);
    }

    @Override
    public int updateBatch(List<TrainSubject> list) {
        return trainSubjectMapper.updateBatch(list);
    }

    @Override
    public void updateMessage(TrainSubjectMessage trainSubjectMessage) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("subjectName", trainSubjectMessage.getSubjectName());
        params.put("subjectDesc", trainSubjectMessage.getSubjectDesc());
        params.put("isValid", trainSubjectMessage.getIsValid());
        params.put("varCreateUser", trainSubjectMessage.getCreateUser());
        params.put("datCreateDate", trainSubjectMessage.getCreateDate());
        params.put("chrSource", trainSubjectMessage.getSource());
        params.put("datOperDate", trainSubjectMessage.getOperDate());
        params.put("operUser", trainSubjectMessage.getOperUser());
        params.put("varId", trainSubjectMessage.getId());
        params.put("logo", trainSubjectMessage.getfId());
        params.put("order", trainSubjectMessage.getOrder());
        List<String> stringList = subjectCourseMapper.selectCourseIds(trainSubjectMessage.getId());
        params.put("courseCount", String.valueOf(stringList.size()));
        trainSubjectMapper.updateMessage(params);
    }

    @Override
    public void update(Map<String, Object> params) {
        trainSubjectMapper.update(params);
    }


    /*
    *
    * 删除企业自制*/
    @Override
    public void deleteBySourse(Map<String, Object> params) {
        trainSubjectMapper.deleteBySourse(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return trainSubjectMapper.selectCount(params);
    }

    @Override
    public TrainSubject selectOne(TrainSubject params) {

        return trainSubjectMapper.selectOne(params);
    }

    /**
     * 根据companyId查询受训主题
     *
     * @param params
     * @return
     */
    @Override
    public List<TrainSubject> selectByCompanyId(Map<String, Object> params) {
        String companyId = params.get("companyId").toString();
        List<TrainSubject> list;
        if (StringUtil.isEmpty(companyId)) {
            // 查询全部主题
            list = trainSubjectMapper.selectAll();
        } else {
            // 查询当前单位下的所有主题id
            List<String> subject_ids = companyTrainSubjectService.selectByCompanyId(companyId);
            // 查询公司主题
            list = trainSubjectMapper.selectSubjectByIds(subject_ids);
        }
        return list;
    }


    @Override
    public void deleteByCodes(String[] codes) {
        trainSubjectMapper.deleteByCodes(codes);
    }

    @Override
    public List<TrainSubject> selectHotSubject(Map<String, Object> params) {
        String company_id = params.get("company_id").toString();
        //查询当前单位下的所有项目id
        List<String> project_ids = companyProjectService.selectProjectIdsByCompanyId(company_id);
        //根据项目id获取主题id
        List<String> subject_ids = projectBasicService.selectSubjectIdByProjectId(project_ids);
        //查询所有项目中的主题
        List<TrainSubject> list = trainSubjectMapper.selectSubjectByIds(subject_ids);
        return list;
    }

    @Override
    public Integer select_count(Map<String, Object> params) {
        return trainSubjectMapper.select_count(params);
    }

    @Override
    public List<Map<String, Object>> select_list(Map<String, Object> params) {
        return trainSubjectMapper.select_list(params);
    }

    @Override
    public TrainSubject selectSubject(Map<String, Object> params) {
        return trainSubjectMapper.selectSubject(params);
    }

    @Override
    public TrainSubject selectById(Map<String, Object> params) {
        return trainSubjectMapper.selectById(params);
    }


    /**
     * 数据同步方法目的：两个监听器接受数据放入一张表保持一致性是一致性
     *
     * @param dataObj
     */
    @Override
    public synchronized void TarinSubjcetSynchronized(List<Map<String, String>> dataObj, TrainSubjectDTO dto) {
        TrainSubject trainSubject = new TrainSubject();
        if (null == dataObj && null != dto) {//基础数据先到，关联数据还没到
            logger.debug("============基础数据先到，关联数据还没到=====================");
            for (TrainSubjectMessage trainSubjectMessage : dto.getDataObj()) {
                Map<String, Object> param = new HashMap<>();
                param.put("varSubjectId", trainSubjectMessage.getId());
                param.put("varSubjectName", company_Id);//假数据查找
                TrainSubject selectSubject = trainSubjectMapper.selectSubject(param);
                if (null == selectSubject) {//基础数据先到 关联数据还没有到
                    trainSubjectMessage.setCompanyId(company_Id);
                    trainSubject.setVarId(iSequenceService.generator());
                    trainSubject.setSubjectName(trainSubjectMessage.getSubjectName());
                    trainSubject.setSubjectDesc(trainSubjectMessage.getSubjectDesc());
                    trainSubject.setSource(trainSubjectMessage.getSource());
                    trainSubject.setIsValid(trainSubjectMessage.getIsValid());
                    trainSubject.setCreateUser(trainSubjectMessage.getCreateUser());
                    trainSubject.setOperUser(trainSubjectMessage.getOperUser());
                    trainSubject.setCreateDate(trainSubjectMessage.getCreateDate());
                    trainSubject.setOperDate(trainSubjectMessage.getOperDate());
                    trainSubjectMapper.insert(trainSubject);//将数据保存在主题表中
                    logger.debug("======TopicQuestionListener===培训主题增加成功但是没有关联关系========");
                } else {//关联数据已经保存进去了
                    trainSubjectMessage.setSource(system_number);
                    trainSubjectMessage.setId(trainSubject.getVarId());
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("subjectName", trainSubjectMessage.getSubjectName());
                    params.put("subjectDesc", trainSubjectMessage.getSubjectDesc());
                    params.put("isValid", trainSubjectMessage.getIsValid());
                    params.put("operUser", trainSubjectMessage.getOperUser());
                    params.put("chrSource", trainSubjectMessage.getSource());
                    params.put("datOperDate", trainSubjectMessage.getOperDate());
                    params.put("varId", trainSubjectMessage.getId());
                    trainSubjectMapper.updateMessage(params);
                    logger.debug("======TopicQuestionListener===培训主题更新成功========");
                }
            }
        } else if (null != dataObj && null == dto) {//关联数据先到，基础数据没到
            logger.debug("======关联数据先到，基础数据没到========");
            for (Map<String, String> objectMap : dataObj) {//添加数据
                String companyId = objectMap.get("companyId");
                String id = objectMap.get("id");
                Map<String, Object> params = new HashMap<>();
                params.put("intCompanyId", company_Id);/*定义一个假数据公司id方便操作*/
                params.put("varSubjectId", id);
                TrainSubject selectSubject = trainSubjectMapper.selectSubject(params);//查询主题
                if (null != selectSubject) {//说明单位id为空
                    Map<String, Object> parames = new HashMap<>();
                    parames.put("intCompanyId", companyId);
                    parames.put("varId", selectSubject.getVarId());//主键
                    parames.put("chrSource", system_number);//系统自带
                    trainSubjectMapper.update(parames);
                    logger.debug("======TopicCompanyTrainSubjectListener===培训主题跟新了单位id========");
                } else {
                    trainSubject.setVarId(iSequenceService.generator());
                    trainSubject.setSource(system_number);
                    trainSubject.setSubjectName(company_Id);
                    trainSubject.setIsValid(chrIsValid);
                    trainSubject.setCreateUser(chrIsValid);
                    trainSubject.setOperUser(chrIsValid);
                    Date date = new Date();
                    trainSubject.setCreateDate(DateUtils.convertDate2StringTime(date));
                    trainSubject.setOperDate(DateUtils.convertDate2StringTime(date));
                    trainSubjectMapper.insert(trainSubject);
                    logger.debug("======TopicCompanyTrainSubjectListener===培训主题表中暂时没有数据直接加入关联关系========");
                }
                logger.debug("======TopicCompanyTrainSubjectListener===培训关联关系增加成功========");
            }


        }




        /*ccc*/


    }


}
