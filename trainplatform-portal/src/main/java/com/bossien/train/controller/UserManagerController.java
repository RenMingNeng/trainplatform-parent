package com.bossien.train.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bossien.train.domain.*;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.*;
import com.bossien.train.util.MD5Utils;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 人员管理
 * Created by Administrator on 2017/7/26.
 */
@Controller
@RequestMapping("admin/user")
public class UserManagerController extends BasicController {

    private static final Logger logger = LogManager.getLogger(UserManagerController.class);

    @Autowired
    private IUserManagerService userManagerService;
    @Autowired
    private IDepartmentService deptService;
    @Autowired
    private ITrainRoleService trainRoleService;
    @Autowired
    private IImportExcelUserService importExcelUserService;
    @Autowired
    private IPersonDossierService personDossierService;
    @Autowired
    private ICompanyTrainRoleService companyTrainRoleService;

    @RequestMapping(value = "/userList")
    public ModelAndView userManagerListPage(HttpServletRequest request){
        logger.info("go to the userManagerListPage method");
        User usr=getCurrUser(request);
        ModelAndView mv = new ModelAndView();
        mv.addObject("companyId",usr.getCompanyId());
        mv.setViewName("admin/userManager/userList");
        logger.info("com.bossien.train.controller.UserManagerController stop");
        return mv;
    }

    @RequestMapping("/queryUserList")
    @ResponseBody
    public Object list(HttpServletRequest request,String userName,String searchType,String departmentId,String companyId,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        Page<User> page = new Page<User>();
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> deptlist=new ArrayList<String>();
        User users=getCurrUser(request);
        params.put("companyId", users.getCompanyId());
        if(pageSize > 1000) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }
        if(!"".equals(userName) && userName!=null) {
            if (!"".equals(searchType) && searchType != null) {
                if ("1".equals(searchType)) {//按姓名搜索
                    params.put("userName", ParamsUtil.joinLike(userName));
                } else {//按账号搜索
                    params.put("userAccount", ParamsUtil.joinLike(userName));
                }
            }
        }
        if(departmentId!=null && !"".equals(departmentId)){
            params.put("departmentId", departmentId);
            deptlist=deptService.getTreeDepartment(params,deptlist);
        }
        List<String> dtlist=new ArrayList<String>();
        Set set=new HashSet();
        for(String deptId:deptlist){
            if(set.add(deptId)){
                dtlist.add(deptId);
            }
        }
        params.put("departmentIdList", dtlist);
        if("-1".equals(departmentId)){//如果查询退厂人员
            params.put("isValid", User.IsValid.TYPE_2.getValue());
        }else{
            params.put("isValid", User.IsValid.TYPE_1.getValue());
        }
        //除去管理员  只查学员(根据需求，现在都查询)
       // params.put("userType", User.UserType.TYPE_3.getValue());
      /*  params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);*/
        Integer count = userManagerService.queryAllUserCount(params);

        page = new Page<User>(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<User> ulist = userManagerService.queryAllUserList(params);
        page.setDataList(ulist);
        resp.setResult(page);
        return resp;
    }

    //用户详情
    @RequestMapping(value = "/userInfo")
    public ModelAndView userInfo(HttpServletRequest request,User user) {
        ModelAndView mv = new ModelAndView();
        Map<String,Object> map=new HashMap<String,Object>();
        List<UserWork> uwkList=new ArrayList<UserWork>();
        user=userManagerService.queryUserInfo(user);
        if(user!=null){
            //查询学员受训角色
            UserTrainRole utr=new UserTrainRole();
            utr.setUserId(user.getId());
            utr.setTrainRoleId(PropertiesUtils.getValue("role_id"));
            List<UserTrainRole> utrList = userManagerService.utrListselectRoleByUserId(utr);
            String roleStr=userManagerService.queryUserTroleNames(utrList);
            if(!StringUtil.isEmpty(roleStr)){
                user.setTroleName(roleStr);
            }else{
                user.setTroleName("");
            }
            UserWork uwork=new UserWork();
            uwork.setUserId(user.getId());
           uwkList = userManagerService.queryUserWorkList(uwork);
        }
        mv.addObject("userInfo",user);
        mv.addObject("workList",uwkList);
        mv.setViewName("admin/userManager/userInfo");
        return mv;
    }

    //去修改页面
    @RequestMapping(value = "/userModify")
    public ModelAndView userModify(HttpServletRequest request,User user) {
        ModelAndView mv = new ModelAndView();
        Map<String,Object> map=new HashMap<String,Object>();
        List<UserWork> uwkList=new ArrayList<UserWork>();
        user=userManagerService.queryUserInfo(user);
        if(user!=null){
            //查询学员受训角色
            UserTrainRole utr=new UserTrainRole();
            utr.setUserId(user.getId());
            List<UserTrainRole> utrList = userManagerService.utrListselectRoleByUserId(utr);
            String roleStr=userManagerService.queryUserTroleNames(utrList);
            if(!StringUtil.isEmpty(roleStr)){
                user.setTroleName(roleStr);
            }else{
                user.setTroleName("");
            }
            UserWork uwork=new UserWork();
            uwork.setUserId(user.getId());
            uwkList = userManagerService.queryUserWorkList(uwork);
        }
        mv.addObject("userInfo",user);
        mv.addObject("workList",uwkList);
        mv.setViewName("admin/userManager/userModify");
        return mv;
    }

    //用户修改
    @RequestMapping("/update")
    @ResponseBody
    public Response<Object> update(HttpServletRequest request,User user,String roleIds) {
        Response<Object> resp = new Response<Object>();
        User usr=getCurrUser(request);
        UserWork uWork=null;
        UserTrainRole usertrole=null;
        // 更新用户信息
        if(user!=null){
            uWork = importExcelUserService.bulidUserWork(usr);
            usertrole=importExcelUserService.bulidUserTrainRole(usr);
            userManagerService.updateUser(user,roleIds,uWork,usertrole);
            resp.setResult(Boolean.TRUE);
            resp.setMessage("修改成功");
        }else{
            // result
            resp.setMessage("修改失败");
            resp.setResult(Boolean.FALSE);

        }
        return resp;
    }


    //查询该公司下所有受训角色
    @RequestMapping("/allTrainRole")
    @ResponseBody
    public Response<Object> selectAllRole(HttpServletRequest request) {
        Response<Object> resp = new Response<Object>();
        User u=getCurrUser(request);
        Map<String,Object> para=new HashMap<String,Object>();
        para.put("companyId",u.getCompanyId());
        List<String> companyTrainRoles=companyTrainRoleService.selectByCompanyId(u.getCompanyId());
        List<Map<String,Object>> trainRoles= new ArrayList <Map<String,Object>>();

        if(companyTrainRoles != null && companyTrainRoles.size()>0){
            para.put("ids", companyTrainRoles);
            //查询公司下所有的的受训色
            trainRoles = trainRoleService.queryTrainRoles(para);
        }

       /* List<Map<String, Object>> rmap = userManagerService.queryAllRoleByParams(para);*/
        JSONArray jsonArray = new JSONArray();
        if(trainRoles.size()>0){
            // result

            for (Map<String,Object> map : trainRoles) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", map.get("varId").toString());
                jsonObject.put("text", map.get("varRoleName").toString());
                jsonArray.add(jsonObject);
            }

        }
        resp.setCode(10000);
        resp.setResult(jsonArray);
        return resp;
    }

    //查询该学员的受训角色
    @RequestMapping("/checkedRole")
    @ResponseBody
    public Response<Object> checkedRole(HttpServletRequest request,String userId) {
        Response<Object> resp = new Response<Object>();
        User user=getCurrUser(request);
        // 查询用户角色
        UserTrainRole utr=new UserTrainRole();
        utr.setUserId(userId);
        List<UserTrainRole> userRoles = userManagerService.queryUserRoles(utr);

        if(userRoles.size()>0){
            // result
            resp.setCode(10000);
            resp.setResult(userRoles);
        }else{
            resp.setMessage("查询失败");
        }

        return resp;
    }


    // 人员-重置密码
    @RequestMapping("/resetPassWord")
    @ResponseBody
    public Response<Object> pwdReset(
            @RequestParam(value = "userId", required = true, defaultValue = "") String userId) {// 人员id
        Response<Object> resp = new Response<Object>();
        if (StringUtils.isEmpty(userId)) {
            resp.setMessage("重置失败");
            return resp;
        }
        User u=new User();
        u.setId(userId);
        u.setUserPasswd(MD5Utils.encoderByMd5With32Bit(PropertiesUtils.getValue("RESET_USER_PASSWORD")));
        int rt=userManagerService.updateUserInfo(u);
        if(rt>0){
            // result
            resp.setCode(10000);
            resp.setResult(PropertiesUtils.getValue("RESET_USER_PASSWORD"));
        }else{
            resp.setMessage("重置失败");
        }

        return resp;
    }

    // 人员转移部门
    @RequestMapping("/changeUserDepartment")
    @ResponseBody
    public Response changeUserDepartment(HttpServletRequest request,
            @RequestParam(value = "companyId", required = true, defaultValue = "") String companyId, // 部门id
            @RequestParam(value = "departmentId", required = true, defaultValue = "") String departmentId, // 部门id
            @RequestParam(value = "userIds", required = true, defaultValue = "") String userIds) { // 人员ids
        Response<Object> resp = new Response<Object>();
        User user=getCurrUser(request);

        userManagerService.changeUserDept(companyId,departmentId,Arrays.asList(userIds.split(",")), user);
        // result
        resp.setResult(Boolean.TRUE);
        resp.setMessage("转移成功");
        return resp;
    }

    // 人员退厂
    @RequestMapping("/changeUserQuit")
    @ResponseBody
    public Response changeUserQuit(HttpServletRequest request,
            @RequestParam(value = "departmentId", required = true, defaultValue = "") String departmentId, // 部门id
            @RequestParam(value = "uids", required = true, defaultValue = "") String uids) { // 人员ids
        Response<Object> resp = new Response<Object>();
        User user=getCurrUser(request);
        userManagerService.changeUserQuit(departmentId,Arrays.asList(uids.split(",")),user);
        // result
        resp.setResult(Boolean.TRUE);
        resp.setMessage("退厂成功");
        return resp;
    }

    // 人员转移选择部门弹窗
    @RequestMapping(value = "/selectDept")
    public ModelAndView selectDeaprtment(
            @RequestParam(value = "companyId", required = false, defaultValue = "") String companyId,	// 单位id
            @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        ModelAndView mv= new ModelAndView();
        mv.addObject("companyId",companyId);
        mv.addObject("userIds", userIds);
        mv.setViewName("admin/userManager/selectDept");
        return mv;
    }

    //删除学员
    @RequestMapping("/delUser")
    @ResponseBody
    public Response<Object> deleteUser(HttpServletRequest request,String userId) {
        User operUser = getCurrUser(request);
        Response<Object> resp = new Response<Object>();
        User user=new User();
        int result = 0;
        if(userId!=null){
            user.setId(userId);
            result = userManagerService.deleteUser(user,operUser);
        }else{
            resp.setMessage("删除失败");
        }
        if(result>0){
            // result
            resp.setCode(10000);
            resp.setMessage("删除成功");
        }else{
            resp.setMessage("删除失败");
        }

        return resp;
    }


    //批量删除学员
    @RequestMapping("/batchDelUser")
    @ResponseBody
    public Response<Object> batchDelUser(HttpServletRequest request,String userIds) {
        User user = getCurrUser(request);
        Response<Object> resp = new Response<Object>();
        int result=0;
        if(userIds!=null){
            result=userManagerService.batchDelUser(Arrays.asList(userIds.split(",")),user);
        }else{
            resp.setMessage("删除失败");
        }

        if(result>0){
            // result
            resp.setCode(10000);
            resp.setMessage("删除成功");
        }else{
            resp.setMessage("删除失败");
        }

        return resp;
    }

    /**
     * 查询学员是否存在学时
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkHours")
    public Object checkDepartmentName(
            @RequestParam(value="userId", required=true, defaultValue="") String userId){
        Response<Object> resp = new Response<Object>();
        Map<String,Object> params=new HashMap<String,Object>();
        if(userId!=null){
            String[] uids=userId.split(",");
            if(uids.length>0){
               for(int i=0;i<uids.length;i++){
                   params.put("userId", uids[i]);
                   Map<String, Object> pd = personDossierService.selectOne(params);
                   if(pd!=null && !pd.isEmpty()){
                       if((long)pd.get("total_studytime")> 0L){   //表示存在学时
                           resp.setResult(Boolean.TRUE);
                           break;
                       }else{
                           resp.setResult(Boolean.FALSE);
                       }
                   }else{
                       resp.setResult(Boolean.FALSE);
                   }
               }
            }
        }

        return resp;
    }
}
