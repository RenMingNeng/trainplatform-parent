package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bossien.train.dao.ap.CompanyTrainRoleMapper;
import com.bossien.train.dao.ap.TrainRoleMapper;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.TrainRole;
import com.bossien.train.domain.dto.TrainRoleDTO;
import com.bossien.train.domain.dto.TrainRoleMessage;
import com.bossien.train.service.ICompanyTrainRoleService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.service.ITrainRoleService;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.ExcelUtil;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * Created by A on 2017/7/25.
 */

@Service
public class TrainRoleServiceImpl implements ITrainRoleService {

    @Autowired
    private TrainRoleMapper trainRoleMapper;
    @Autowired
    private CompanyTrainRoleMapper companyTrainRoleMapper;
    private String system_number = PropertiesUtils.getValue("SYSTEM_NUMBER");//获取配置文件
    private String company_Id = PropertiesUtils.getValue("COMPANY_Id");//获取配置文件
    private static final Logger logger = LogManager.getLogger(TrainRoleServiceImpl.class);
    private String chrIsValid = PropertiesUtils.getValue("CHRISVAl_ID");//获取配置文件
    @Autowired
    private ISequenceService iSequenceService;
    @Autowired
    private ICompanyTrainRoleService companyTrainRoleService;

    @Override
    public void insert(TrainRole params) {

        trainRoleMapper.insert(params);
    }

    @Override
    public void insertMessage(TrainRoleMessage trainRoleMessage) {
        TrainRole trainRole = new TrainRole();
        trainRole.setVarId(trainRoleMessage.getId());
        trainRole.setIsValid(trainRoleMessage.getIsValid());
        trainRole.setRoleName(trainRoleMessage.getRoleName());
        trainRole.setRoleDesc(trainRoleMessage.getRoleDesc());
        trainRole.setSource(trainRoleMessage.getSource());
        trainRole.setCompanyId(trainRoleMessage.getCompanyId());
        trainRole.setOperUser(trainRoleMessage.getOperUser());
        trainRole.setOperDate(trainRoleMessage.getOperDate());
        trainRole.setCreateUser(trainRoleMessage.getCreateUser());
        trainRole.setCreateDate(trainRoleMessage.getCreateDate());
        trainRoleMapper.insert(trainRole);
    }

    @Override
    public void update(Map<String, Object> params) {
        trainRoleMapper.update(params);
    }

    @Override
    public TrainRole selectByRole(Map<String, Object> params) {
        return trainRoleMapper.selectByRole(params);
    }

    @Override
    public TrainRole selectById(Map<String, Object> params) {
        return trainRoleMapper.selectById(params);
    }

    @Override
    public void updateMessage(TrainRoleMessage trainRoleMessage) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("varId", trainRoleMessage.getId());
        params.put("roleName", trainRoleMessage.getRoleName());
        params.put("roleDesc", trainRoleMessage.getRoleDesc());
        params.put("source", system_number);
        params.put("companyId", trainRoleMessage.getCompanyId());
        params.put("isValid", trainRoleMessage.getIsValid());
        params.put("createDate", trainRoleMessage.getCreateDate());
        params.put("createUser", trainRoleMessage.getCreateUser());
        params.put("operUser", trainRoleMessage.getOperUser());
        params.put("operDate", trainRoleMessage.getOperDate());
        trainRoleMapper.update(params);//授权角色表中
    }

    @Override
    public int insertBatch(List<Map<String,Object>> list) {
        return trainRoleMapper.insertBatch(list);
    }

    @Override
    public int updateBatch(List<Map<String,Object>> list) {
        return trainRoleMapper.updateBatch(list);
    }


    @Override
    public void delete(Map<String, Object> params) {
        trainRoleMapper.delete(params);
    }


    @Override
    public Page<TrainRole> selectList(Map<String, Object> params) {
        try {
            Page<TrainRole> page = new Page<TrainRole>(trainRoleMapper.selectCount(params),
                    Integer.parseInt(params.get("pageNum").toString()),
                    Integer.parseInt(params.get("pageSize").toString()));
            params.put("startNum", page.getStartNum());
            List<TrainRole> list = trainRoleMapper.selectList(params);
            page.setDataList(list);
            return page;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*导出功能*/
    @Override
    public void queryExportData(HttpServletResponse response, Map<String, Object> params) {


        // 导出的excel数据集
        List<Map<String, Object>> lMap = trainRoleMapper.selectByParams(params);
        for (Map<String, Object> map : lMap) {
            if (map.get("chrSource").equals("1")) {
                map.put("chrSource", "系统自带");
            } else {
                map.put("chrSource", "企业自制");
            }
        }

        // 文件头部
        Map<String, Object> headerMap = this.getHeader();
        // 生成文件
        ExcelUtil.exportExcelFile(response, lMap, headerMap);
    }

    @Override
    public List<TrainRole> selectLists(Map<String, Object> params) {
        return trainRoleMapper.selectList(params);
    }

    // 获取导出excel头部
    public Map<String, Object> getHeader() {
        Map<String, Object> header = new LinkedHashMap<String, Object>();
        // 姓名
        header.put("varRoleName", "受训角色");
        header.put("chrSource", "受训角色来源");
        return header;
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return trainRoleMapper.selectCount(params);
    }

    @Override
    public List<Map<String, Object>> queryTrainRoles(Map<String, Object> params) {

        return trainRoleMapper.selectByParams(params);
    }

    @Override
    public List<TrainRole> selectVerify(Map<String, Object> params) {
        return trainRoleMapper.selectVerify(params);
    }

    @Override
    public TrainRole selectOne(TrainRole params) {

        return trainRoleMapper.selectOne(params);
    }

    /**
     * 通过companyId查询受训角色
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> selectTrainRoles(Map<String, Object> params) {
        return trainRoleMapper.selectTrainRoles(params);
    }

    /**
     * 构造受训角色树
     *
     * @param roles
     * @return
     */
    @Override
    public List<Map<String, Object>> ConstructRoleTreeNodes(String roles) {
        Class<Map> clazz = Map.class;
        List<Map> roleList = JsonUtils.joinToList(roles, clazz);

        List<Map<String, Object>> treeNodes = new ArrayList<Map<String, Object>>();
        int index = 0;
        // TP_TrainRole 没有父子关系 手动构造根节点
        Map<String, Object> rootNode = new HashMap<String, Object>();
        rootNode.put("id", String.valueOf(index + 1));
        rootNode.put("name", "受训角色");
        rootNode.put("open", true);
        rootNode.put("pid", String.valueOf(index));
        rootNode.put("roleId", "");// 页面需要-受训角色id

        List<Map<String, Object>> childrenNodes = new ArrayList<Map<String, Object>>();
        Map<String, Object> childrenNode = null;
        for (Map map : roleList) {
            index++;
            childrenNode = new HashMap<String, Object>();
            childrenNode.put("id", String.valueOf(index + 1));
            childrenNode.put("name", map.get("text").toString());
            childrenNode.put("open", false);
            childrenNode.put("pid", (String) rootNode.get("id"));
            childrenNode.put("roleId", map.get("id").toString());// 页面需要-受训角色id
            childrenNodes.add(childrenNode);
        }
        rootNode.put("children", childrenNodes);
        treeNodes.add(rootNode);
        return treeNodes;
    }

    /**
     * 将受训角色数据组装到json数组，便于前端select2插件展示
     *
     * @param trainRoles
     * @return
     */
    public Object assembleTrainRoleData(List<TrainRole> trainRoles) {
        JSONArray jsonArray = new JSONArray();
        for (TrainRole trainRole : trainRoles) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", trainRole.getVarId());
            jsonObject.put("text", trainRole.getRoleName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 数据同步方法目的：两个监听器接受数据放入一张表保持一致性是一致性
     *
     * @param dataObj
     */
    public synchronized void TrainRoleSynchronized(List<Map<String, String>> dataObj, TrainRoleDTO dto) {
        TrainRole trainRole = new TrainRole();
        if (null == dataObj) {//为空说明先进入的是基础数据不是关联数据
            for (TrainRoleMessage trainRoleMessage : dto.getDataObj()) {
                /*先查看是否有数据*/
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("varRoleId", trainRoleMessage.getId());
                params.put("varRoleName", company_Id);
                TrainRole selectByRole = trainRoleMapper.selectByRole(params);
                if (null != selectByRole) {//说明有数据需要跟新
                    trainRoleMessage.setSource(system_number);
                    trainRoleMessage.setId(selectByRole.getVarId());
                    Map<String, Object> paramsx = new HashMap<String, Object>();
                    paramsx.put("varId", trainRoleMessage.getId());
                    paramsx.put("roleName", trainRoleMessage.getRoleName());
                    paramsx.put("roleDesc", trainRoleMessage.getRoleDesc());
                    paramsx.put("source", system_number);
                    paramsx.put("companyId", trainRoleMessage.getCompanyId());
                    paramsx.put("isValid", trainRoleMessage.getIsValid());
                    paramsx.put("createDate", trainRoleMessage.getCreateDate());
                    paramsx.put("createUser", trainRoleMessage.getCreateUser());
                    paramsx.put("operUser", trainRoleMessage.getOperUser());
                    paramsx.put("operDate", trainRoleMessage.getOperDate());
                    trainRoleMapper.update(paramsx);//授权角色表中
                    logger.debug("======TopicRoleListener===授权角色有关联值更新成功========");
                } else {//说明没有值直接添加
                    trainRoleMessage.setCompanyId(company_Id);
                    trainRole.setVarId(iSequenceService.generator());
                    trainRole.setRoleId(trainRoleMessage.getId());
                    trainRole.setIsValid(trainRoleMessage.getIsValid());
                    trainRole.setRoleName(trainRoleMessage.getRoleName());
                    trainRole.setRoleDesc(trainRoleMessage.getRoleDesc());
                    trainRole.setSource(trainRoleMessage.getSource());
                    trainRole.setCompanyId(trainRoleMessage.getCompanyId());
                    trainRole.setOperUser(trainRoleMessage.getOperUser());
                    trainRole.setOperDate(trainRoleMessage.getOperDate());
                    trainRole.setCreateUser(trainRoleMessage.getCreateUser());
                    trainRole.setCreateDate(trainRoleMessage.getCreateDate());
                    trainRoleMapper.insert(trainRole);
                    logger.debug("======TopicRoleListener===授权角色没有关联数据增加成功========");
                }
            }
        } else {/*说明关联信息先进入*/
            for (Map<String, String> objectMap : dataObj) {//添加数据
                String companyId = objectMap.get("companyId");
                String id = objectMap.get("id");
                Map<String, Object> params = new HashMap<>();
                params.put("intCompanyId", company_Id);/*定义一个假数据公司id方便操作*/
                params.put("varRoleId", id);
                TrainRole selectByRole = trainRoleMapper.selectByRole(params);
                if (null != selectByRole) {//说明单位id为空
                    Map<String, Object> parames = new HashMap<>();
                    parames.put("companyId", companyId);
                    parames.put("varId", selectByRole.getVarId());//主键
                    parames.put("chrSource", system_number);//系统自带
                    trainRoleMapper.update(parames);
                    logger.debug("======TopicCompanyTrainRoleListener===受训角色跟新了单位id========");
                } else {
                    trainRole.setVarId(iSequenceService.generator());
                    trainRole.setRoleId(id);
                    trainRole.setCompanyId(companyId);
                    trainRole.setSource(system_number);
                    trainRole.setRoleName(company_Id);//给假名字方便查找
                    trainRole.setIsValid(chrIsValid);
                    trainRole.setCreateUser(chrIsValid);
                    trainRole.setOperUser(chrIsValid);
                    Date date = new Date();
                    trainRole.setCreateDate(DateUtils.convertDate2StringTime(date));
                    trainRole.setOperDate(DateUtils.convertDate2StringTime(date));
                    trainRoleMapper.insert(trainRole);
                    logger.debug("======TopicCompanyTrainRoleListener===受训角色表中暂时没有数据直接加入关联关系========");
                }
                logger.debug("======TopicCompanyTrainRoleListener===受训角色关系增加成功========");
            }
        }
    }

}
