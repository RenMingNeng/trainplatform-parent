package com.bossien.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bossien.train.dao.tp.CompanySuperviseMapper;
import com.bossien.train.domain.*;
import com.bossien.train.service.*;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Administrator on 2017-07-31.
 */
@Service
public class CompanySuperviseServiceImpl implements ICompanySuperviseService {

    @Autowired
    private CompanySuperviseMapper companySuperviseMapper;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ICompanyTjService companyTjService;
    @Autowired
    private IBaseService<Message> messageBaseService;
    @Autowired
    private IMessageService messageService;

    @Override
    public List<CompanySupervise> initSupervise(String companyId) {
        Map<String, Object> params = new HashedMap();
        params.put("companyId", companyId);
        int count = selectCount(params);
        //没数据时将该公司的所有数据初始化到监管表里
        if(count == 0){
            String ids = companyService.getChildCompanyIds(companyId);
            if(null == ids || ids.equals("")){
                return new ArrayList<CompanySupervise>();
            }

            List<String> idsAry = Arrays.asList(ids.split(","));
            List<Company> companies = companyService.getChildCompanyList(idsAry);

            //Company 转换成 CompanySupervise
            List<CompanySupervise> companySupervises = new ArrayList<CompanySupervise>();
            Map<String, Object> param = new HashedMap();
            for(Company company : companies){
                companySupervises.add(new CompanySupervise(companyId,
                        company.getVarName(),
                        company.getIntId() + "",
                        company.getIntPid() + "",
                        company.getIntOrder()));

//                param = new HashedMap();
//                param.put("companyId", company.getIntId());
//                int length = companyTjService.selectCount(param);
//                if(length > 0){
//                    continue;
//                }
//                param.put("companyName", company.getVarName());
//                param.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                param.put("createUser", "admin");
//                param.put("operTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                param.put("operUser", "admin");
//                companyTjService.insert(param);
            }
            //批量添加
            if(companySupervises.size() > 0){
                companySuperviseMapper.insertBatch(companySupervises);
            }
            return companySupervises;
        }
        return new ArrayList<CompanySupervise>();
    }

    @Override
    public void insert(CompanySupervise companySupervise) {

        companySuperviseMapper.insert(companySupervise);
    }

    @Override
    public void insertBatch(List<CompanySupervise> companySupervises) {

        companySuperviseMapper.insertBatch(companySupervises);
    }

    @Override
    public void update(CompanySupervise companySupervise) {

        companySuperviseMapper.update(companySupervise);
    }

    @Override
    public List<CompanySupervise> selectList(Map<String, Object> params) {

        return companySuperviseMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return companySuperviseMapper.selectCount(params);
    }

    @Override
    public CompanySupervise selectOne(Map<String, Object> params) {

        return companySuperviseMapper.selectOne(params);
    }

    @Override
    public void delete(Map<String, Object> params) {

        companySuperviseMapper.delete(params);
    }

    @Override
    public String getSuperviseTree(String companyId) {
        //初始化判断
        List<CompanySupervise> companySupervises = initSupervise(companyId);
        if(companySupervises.size() < 1){
            Map<String, Object> params = new HashedMap();
            params.put("companyId", companyId);
            companySupervises = selectList(params);
        }

        JSONArray jsonArray = new JSONArray(companySupervises.size());
        for(CompanySupervise companySupervise : companySupervises){
            jsonArray.add(getCompanyTreeNode(companySupervise));
        }

        //生成树
        if(jsonArray.size() > 0){
            return jsonArray.toJSONString();
        }
        return "";
    }

    @Override
    public void sendMessage(User user, String companyName, String pid, String companyIds) {
        Map<String, Object> params = new HashedMap();
        params.put("companyId", user.getCompanyId());
        List<CompanySupervise> companySupervises = companySuperviseMapper.selectList(params);
        for(CompanySupervise companySupervise : companySupervises){
            companyIds = companyIds.replace(companySupervise.getId(), "");
            companyIds = companyIds.replace(",,", ",");
        }
        String[] companyIdArray = companyIds.split(",");
        List<Message> messageList = new ArrayList<Message>();
        for(String companyId : companyIdArray){
            //排除自己
            if(!StringUtil.isEmpty(companyId) && !companyId.equals(user.getCompanyId())){
                String messageTitle = Message.MSG_TPL_TITLE_0001;
                String messageContent = Message.MSG_TPL_CONTENT_0001;
                Message message = messageBaseService.build(user.getUserName(), new Message("",
                                companyId,
                                messageTitle,
                                "",
                                user.getCompanyId(),
                                companyName));
                String yes = "messageId='" + message.getId() + "' pid='" + pid + "'";
                String not = "messageId='" + message.getId() + "'";
                message.setMessageContent(MessageFormat.format(messageContent, companyName, yes, not));
                messageList.add(message);
            }
        }
        if(messageList.size() > 0){
            messageService.sendList(messageList);
        }
    }

    @Override
    public void addSupervise(String messageId, String pid, String isTrue, User user, String companyName) {
        Map<String, Object> params = new HashedMap();
        params.put("id", messageId);
        Message message = messageService.selectOne(params);
        params.put("messageStatus", "2");
        String messageContent = message.getMessageContent().replace("href", "_href");
        messageContent = messageContent.replace("agree_yes", "");
        messageContent = messageContent.replace("agree_no", "");
        params.put("messageContent", messageContent);
        messageService.update(params);

        if(!isTrue.equals("")){//增加监管
            //判断是否已经建立关系
            params = new HashedMap();
            params.put("id", user.getCompanyId());
            params.put("companyId", message.getSendId());
            int count = companySuperviseMapper.selectCount(params);
            if(count > 0){
                return;
            }

            //保存
            params = new HashedMap();
            params.put("id", message.getSendId());
            params.put("pid", pid);
            int orderNum = companySuperviseMapper.selectCount(params);
            CompanySupervise companySupervise = new CompanySupervise(
                    message.getSendId(),
                    companyName,
                    user.getCompanyId(),
                    pid,
                    orderNum + 1
            );
            companySuperviseMapper.insert(companySupervise);
        }
    }

    @Override
    public void upNode(String companyId, String id, String pid) {
        Map<String, Object> params = new HashedMap();
        params.put("companyId", companyId);
        params.put("id", id);
        params.put("pid", pid);
        CompanySupervise curentNode = companySuperviseMapper.selectOne(params);

        Integer crentNum = curentNode.getOrderNum();
        params.clear();
        params.put("companyId", companyId);
        params.put("pid", pid);
        params.put("orderNum", crentNum - 1);
        CompanySupervise preNode = companySuperviseMapper.selectOne(params);

        if(null == preNode){
            return;
        }

        //当前节点上移
        curentNode.setOrderNum(crentNum - 1);
        companySuperviseMapper.update(curentNode);

        //上节点下移
        preNode.setOrderNum(crentNum);
        companySuperviseMapper.update(preNode);
    }

    @Override
    public void downNode(String companyId, String id, String pid) {
        Map<String, Object> params = new HashedMap();
        params.put("companyId", companyId);
        params.put("id", id);
        params.put("pid", pid);
        CompanySupervise curentNode = companySuperviseMapper.selectOne(params);

        Integer crentNum = curentNode.getOrderNum();
        params.clear();
        params.put("companyId", companyId);
        params.put("pid", pid);
        params.put("orderNum", crentNum + 1);
        CompanySupervise nextNode = companySuperviseMapper.selectOne(params);

        if(null == nextNode){
            return;
        }

        //当前节点下移
        curentNode.setOrderNum(crentNum + 1);
        companySuperviseMapper.update(curentNode);

        //上节点上移
        nextNode.setOrderNum(crentNum);
        companySuperviseMapper.update(nextNode);
    }

    @Override
    public void upgrade(String companyId, String id, String pid, Integer orderNum) {
        //父节点
        Map<String, Object> params = new HashedMap();
        params.put("companyId", companyId);
        params.put("id", pid);
        CompanySupervise pidNode = companySuperviseMapper.selectOne(params);
        //当前节点
        params = new HashedMap();
        params.put("companyId", companyId);
        params.put("id", id);
        CompanySupervise nowNode = companySuperviseMapper.selectOne(params);

        //它下面的节点上移操作
        params = new HashedMap();
        params.put("companyId", companyId);
        params.put("pid", nowNode.getPid());
        params.put("order_num", nowNode.getOrderNum());
        companySuperviseMapper.upNodes(params);

        //它上级节点的下面节点下移
        params = new HashedMap();
        params.put("companyId", companyId);
        params.put("pid", pidNode.getPid());
        params.put("order_num", pidNode.getOrderNum());
        companySuperviseMapper.downNodes(params);

        //当前节点升级
        nowNode.setPid(pidNode.getPid());
        nowNode.setOrderNum(pidNode.getOrderNum()+1);
        companySuperviseMapper.update(nowNode);
    }

    @Override
    public void degrade(String companyId, String id, String pid, Integer orderNum) {
        //上级节点
        Map<String, Object> params = new HashedMap();
        params.put("companyId", companyId);
        params.put("orderNum", orderNum-1);
        params.put("pid", pid);
        CompanySupervise preNode = companySuperviseMapper.selectOne(params);
        params = new HashedMap();
        params.put("companyId", companyId);
        params.put("pid", preNode.getId());
        Integer count = companySuperviseMapper.selectCount(params);
        //当前节点
        params = new HashedMap();
        params.put("companyId", companyId);
        params.put("id", id);
        CompanySupervise nowNode = companySuperviseMapper.selectOne(params);

        //它下面的节点上移操作
        params = new HashedMap();
        params.put("companyId", companyId);
        params.put("pid", nowNode.getPid());
        params.put("order_num", nowNode.getOrderNum());
        companySuperviseMapper.upNodes(params);

        //当前节点降级
        nowNode.setPid(preNode.getId());
        nowNode.setOrderNum(count);
        companySuperviseMapper.update(nowNode);
    }

    @Override
    public String highchartsgetJson(String companyId) {
        String ids = companyService.getChildCompanyIds(companyId);
        if(null == ids || ids.equals("")){
            return "";
        }

        Map<String, Object> params = new HashedMap();
        params.put("companyIds", Arrays.asList(ids.split(",")));
        List<CompanyTj> companyTjs = companyTjService.selectList(params);
        JSONArray jsonArray = new JSONArray();

        //人均学时、总学时、培训人次
        Map<String, Object> data = new HashedMap();
        for(CompanyTj companyTj : companyTjs){
            data = new HashedMap();
            data.put("name", companyTj.getCompanyName());

            List<Double> list = new ArrayList<Double>();
            list.add(Double.parseDouble(companyTj.getAveragePersonClassHour()));
            list.add(Double.parseDouble(companyTj.getTotalClassHour()));
            list.add(Double.parseDouble(companyTj.getCountTrain()));
            data.put("data", list);

            jsonArray.add(data);
        }
        return jsonArray.toJSONString();
    }

    @Override
    public String getChildCompanyIds(String rootId, String companyId) {
        Map<String, Object> params = new HashedMap();
        params.put("companyId", companyId);
        params.put("rootId", rootId);
        String companyIds = companySuperviseMapper.getChildCompanyIds(params);
        return companyIds == null ? "" : companyIds.replace("$,", "");
    }

    //组建成树节点
    public Map<String, Object> getCompanyTreeNode(CompanySupervise companySupervise){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", companySupervise.getId());
        map.put("name", companySupervise.getCompanyName());
        map.put("open", false);
        map.put("pId", companySupervise.getPid());
        if(companySupervise.getId().equals(companySupervise.getCompanyId())){
            map.put("open", true);
            map.put("pId", "0");
        }
        map.put("orderNum", companySupervise.getOrderNum());
        return map;
    }
}
