package com.bossien.train.controller;

import com.bossien.train.domain.CompanyTrainRole;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.TrainRole;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICompanyTrainRoleService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.service.ITrainRoleService;
import com.bossien.train.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by zhaoli on 2017/7/31.
 */
@Controller
@RequestMapping("admin/trainRole")
public class TrainRoleController extends BasicController {
    private static final Logger logger = LogManager.getLogger(TrainRoleController.class);

    @Autowired
    private ITrainRoleService trainRoleService;
    @Autowired
    private ICompanyTrainRoleService companyTrainRoleService;

    @Autowired
    private ISequenceService sequenceService;
    @Value("${ENTERPRISE_NUMBER}")/*企业自制*/
    private String enterprise_number;
    @Value("${SYSTEM_NUMBER}")/*系统自带*/
    private String getNo_system;
    @Value("${CHRISVAl_ID}")
    private String chrIsValid;/*有效*/
    @Value("${NO_SYSTEM}")
    private String no_system;

    /**
     * 受训角色初始化分页查询
     *
     * @param request
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request) {
        return "admin/basicinfo/train_role_index";
    }

    @RequestMapping("/paging")
    @ResponseBody
    public Object list(HttpServletRequest request, String roleName,
                       @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {

        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.add-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        Response<Object> resp = new Response<Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleName", ParamsUtil.joinLike(roleName));
        /*通过公司id查询数据*/
        List<String> list = companyTrainRoleService.selectByCompanyId(user.getCompanyId());
        params.put("intCompanyId", user.getCompanyId());

        Page<TrainRole> page = new Page<TrainRole>();
        params.put("ids", list);
        if (list.size() <= 0) {
            list.add(no_system);
        }
        int count = trainRoleService.selectCount(params);
        page = new Page(count, pageNum, pageSize);

        params.put("startNum", page.getStartNum());
        params.put("pageSize", page.getPageSize());
        List<TrainRole> list1 = trainRoleService.selectLists(params);
        page.setDataList(list1);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }


    /**
     * 进入新增页面
     *
     * @return
     */
    @RequestMapping(value = "/trainRoleAdd")
    public String addTrainRole() {
        return "admin/basicinfo/train_role_add";
    }


    /**
     * 进入修改页面
     */

    @RequestMapping(value = "/trainRoleModify")
    public ModelAndView trainRoleModify(HttpServletRequest request, @Valid TrainRole trainRole,
                                        @RequestParam(value = "varId") String varId) throws Exception {
        trainRole.setVarId(varId);
        TrainRole trainRoleOne = trainRoleService.selectOne(trainRole);//查询数据
        ModelAndView modelAndView = new ModelAndView("admin/basicinfo/train_role_modify");
        modelAndView.addObject("trainRole", trainRoleOne);
        return modelAndView;
    }

    /**
     * 进入详情页面
     */

    @RequestMapping(value = "/trainRoleInfo")
    public ModelAndView trainRoleInfo(HttpServletRequest request, @Valid TrainRole trainRole,
                                      @RequestParam(value = "varId") String varId) throws Exception {
        trainRole.setVarId(varId);
        TrainRole trainRoleOne = trainRoleService.selectOne(trainRole);//查询数据
        ModelAndView modelAndView = new ModelAndView("admin/basicinfo/train_role_info");
        modelAndView.addObject("trainRole", trainRoleOne);
        return modelAndView;
    }


    /**
     * 新增受训角色
     *
     * @param request
     * @param obj
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(HttpServletRequest request, TrainRole obj) throws ServiceException {
        //验证非空参数
        if (StringUtils.isEmpty(obj.getRoleName())) {
            throw new ServiceException(Code.BAD_REQUEST, "角色名称为空");
        }
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.add-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        //封装参数
        obj.setVarId(sequenceService.generator());
        obj.setCreateUser(user.getId());
        obj.setSource(enterprise_number);//来源默认是企业自制
        obj.setIsValid(chrIsValid);//新增操作默认有效
        Date date = new Date();
        obj.setCreateDate(DateUtils.convertDate2StringTime(date));
        obj.setOperDate(DateUtils.convertDate2StringTime(date));
        obj.setOperUser(user.getId());
        obj.getRoleName().trim();
        trainRoleService.insert(obj);
        CompanyTrainRole record = new CompanyTrainRole();
        record.setIntTrainRoleId(obj.getVarId());
        record.setIntCompanyId(user.getCompanyId());
        companyTrainRoleService.insert(record);
        return new Response();
    }

    /**
     * 修改企业自制培训角色
     *
     * @param request
     * @param
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(HttpServletRequest request,
                         @Valid TrainRole trainRole,
                         @RequestParam(value = "roleName", required = false, defaultValue = "") String roleName,
                         @RequestParam(value = "roleDesc", required = false, defaultValue = "") String roleDesc,
                         @RequestParam(value = "id", required = false, defaultValue = "") String id,
                         @RequestParam(value = "source", required = false, defaultValue = "") String source) throws ServiceException {
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.add-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        //验证非空参数
        if (StringUtils.isEmpty(id)) {

            throw new ServiceException(Code.BAD_REQUEST, "请选择要修改的角色");
        }
        if (source.equals(getNo_system)) {
            throw new ServiceException(Code.BAD_REQUEST, "系统自带的角色不可修改");
        }
        if (StringUtils.isEmpty(roleName)) {
            throw new ServiceException(Code.BAD_REQUEST, "角色名称为空");
        }

        //封装参数
        Date date = new Date();
        trainRole.setOperUser(user.getId());
        trainRole.setOperDate(DateUtils.convertDate2StringTime(date));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleName", roleName);
        params.put("roleDesc", roleDesc);
        params.put("source", enterprise_number);
        params.put("isValid", chrIsValid);
        params.put("operUser", user.getOperUser());
        params.put("operDate", DateUtils.convertDate2StringTime(date));
        params.put("varId", id);
        trainRoleService.update(params);
        return new Response();
    }

    /**
     * 删除企业自制培训角色
     *
     * @param request
     * @param
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(HttpServletRequest request, @RequestParam(value = "idStr", required = true) String idStr) throws ServiceException {
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.delete-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        //验证非空参数
        if (StringUtils.isEmpty(idStr)) {
            throw new ServiceException(Code.BAD_REQUEST, "请选择要删除的角色");
        }
        String[] ids = idStr.replace(" ", "").split(",");
        if (ids.length == 0) {
            throw new ServiceException(Code.BAD_REQUEST, "请选择要删除的主题");
        }
        //封装参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("chrSource", enterprise_number);
        params.put("ids", ids);
        trainRoleService.delete(params);
        params.put("intCompanyId", user.getCompanyId());
        companyTrainRoleService.deleteByAllId(params);
        return new Response();
    }

    /**
     * 导出企业自制培训角色
     *
     * @param request
     * @param
     * @return
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/excel_export", produces = "text/plain;charset=UTF-8")
    public void export(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "roleName", required = false, defaultValue = "") String roleName,
                       @RequestParam(value = "fileName", required = true, defaultValue = "") String fileName,
                       @RequestParam(value = "idStr", required = false) String idStr) throws Exception {
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainSubjectController.excel_export-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        if (null != idStr && !idStr.equals("")) {
            String[] ids = idStr.replace(" ", "").split(",");
            params.put("ids", ids);
        } else {
                  /*通过公司id查询数据*/
            List<String> list = companyTrainRoleService.selectByCompanyId(user.getCompanyId());
            params.put("ids", list);
        }
        params.put("roleName", ParamsUtil.joinLike(roleName));

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", MessageFormat.format("attachment;filename={0}", RequestUtils.getCNParam(request, fileName)));
        trainRoleService.queryExportData(response, params);
    }
    /**
     * 验证企业自制受训是否重复
     *
     * @param request
     * @param
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/verify")
    @ResponseBody
    public Object verify(HttpServletRequest request,
                         @RequestParam(value = "roleName", required = false, defaultValue = "") String name
    ) throws Exception {
        User user = getCurrUser(request);
        if (user == null) {
            logger.error("TrainRoleController.verify-------------用户未登录");
            throw new ServiceException(Code.USER_NOT_LOGIN);
        }
        Response<Object> resp = new Response<Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> list = companyTrainRoleService.selectByCompanyId(user.getCompanyId());
        params.put("ids", list);
        params.put("chrSource", enterprise_number);
        List<TrainRole> trainRoles = trainRoleService.selectVerify(params);
        for (TrainRole trainSubject: trainRoles) {
            if (trainSubject.getRoleName().equals(name)){
                resp.setCode(1);
                return resp;
            }else {
                resp.setCode(Code.SUCCESS.getCode());
            }
        }
        return resp;
    }

}
