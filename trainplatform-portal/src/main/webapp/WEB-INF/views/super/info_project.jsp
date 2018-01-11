<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.bossien.train.util.PropertiesUtils" %>
<%@ page import="com.bossien.train.domain.eum.ProjectTypeEnum" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";

    //含有考试项目的
    String containExamType= PropertiesUtils.getValue("contain_exam_type");
    //含有培训项目的
    String containTrainType=PropertiesUtils.getValue("contain_train_type");
    //含有练习项目的
    String containExerciseType=PropertiesUtils.getValue("contain_exercise_type");
    //单独考试项目
    String examType= ProjectTypeEnum.QuestionType_3.getValue();
%>
<!DOCTYPE html>
<html>
<head>
    <title>公开型项目详情</title>
    <jsp:include page="${path}/common/style" />
</head>
<body>
<jsp:include page="${path}/super/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>

<c:set var="containExamType" value="<%=containExamType%>" ></c:set>
<c:set var="containTrainType" value="<%=containTrainType%>" ></c:set>
<c:set var="containExerciseType" value="<%=containExerciseType%>" ></c:set>
<c:set var="examType" value="<%=examType%>" ></c:set>
<div id="main">
    <div class="area" style="overflow: hidden;">
        <div class="box" style="padding-bottom:10px;margin-bottom:70px;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目基础信息</h3>
                <div class="pull-right search-group">
                    <a href="javascript:;" class="btn btn-info"  id="refresh"><span class="fa fa-refresh"></span> 刷新</a>
                    <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                </div>
            </div>
            <div class="box-body project_info">
                <div class="row">
                <div class="col-12 form-group">
                    <label for="" class="col-1">项目名称：</label>
                    <div class="col-11 info">
                        ${projectInfo.projectName}
                    </div>
                </div>
                <div class="col-12 form-group"  <c:if test="${empty projectInfo.subjectName}">style="display: none"</c:if>>
                    <label for="" class="col-1">培训主题：</label>
                    <div class="col-11 info">${projectInfo.subjectName}</div>
                </div>
                <c:if test="${fn:contains('1457',projectInfo.projectType)}">
                    <div class="col-12 form-group">
                        <label for="" class="col-1">培训时间：</label>
                        <div class="col-11 info">${projectInfo.projectTrainTime}</div>
                    </div>
                </c:if>
                <c:if test="${fn:contains('2467',projectInfo.projectType)}">
                    <div class="col-12 form-group">
                        <label for="" class="col-1">练习时间：</label>
                        <div class="col-11 info">${projectInfo.projectExerciseTime}</div>
                    </div>
                </c:if>
                <c:if test="${fn:contains('3567',projectInfo.projectType )}">
                    <div class="col-12 form-group">
                        <label for="" class="col-1">考试时间：</label>
                        <div class="col-11 info">${projectInfo.projectExamTime}</div>
                    </div>
                </c:if>
            </div>
            </div>
        </div>


        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">培训课程信息列表</h3>
                <form id="course_form">
                <div class="pull-right search-group">
                    <span>
                        <button type="button" class="btn btn-info" id="btn_select_course"><span class="fa fa-mouse-pointer"></span> 选择课程</button>
                        <button type="button"  class="btn btn-info" id="btn_delete_batch_course"><span class="fa fa-trash-o"></span> 批量删除</button>
                    </span>
                    <span class="search">
                        <input type="text" id="course_name" name="course_name" placehodler="请输入课程名称">
                        <button type="button" title="搜索" class="btn" id="btn_search_course">
                            <span class="fa fa-search"></span> 搜索
                        </button>
                    </span>
                    <span>
                        <a href="javascript:;" id="btn_search_all_course" class="btn all">
                            <span class="fa fa-th"></span> 全部
                        </a>
                    </span>
                </div>
                    <input type="hidden" id="course_project_id" name="course_project_id" value="${projectInfo.id}">
                </form>
            </div>
            <div class="box-body">
                        <table class="table">
                            <thead>
                            <tr >
                                <th>序号</th>
                                <th style="width:420px">课程名称</th>
                                <th>课程编号</th>
                                <c:if test="${fn:contains(containTrainType, projectTypeNo)}">
                                    <th>课时</th>
                                    <th>学时要求</th>
                                </c:if>
                                <c:if test="${fn:contains(containExamType, projectTypeNo) || fn:contains(containExerciseType, projectTypeNo) }">
                                    <th>题库总量</th>
                                </c:if>
                                <c:if test="${fn:contains(containExamType, projectTypeNo) }">
                                    <th>必选题量</th>
                                </c:if>
                                <th>查 看</th>
                            </tr>
                            </thead>
                            <%--课程列表--%>
                            <tbody id="course_list">
                            <tr>
                                <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                            </tr>
                            </tbody>
                        </table>
                    <div class="page text-right" style="margin-top:5px;">
                        <ul id="course_page"></ul>
                    </div>
            </div>
        </div>


        <c:if test="${fn:contains('3567',projectInfo.projectType ) and null != examStrategy}">
            <!-- 确定组卷策略开始 -->
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">组卷策略信息</h3>
                </div>
                <div class="box-body">
                    <div>
                        <ul class="clearfix combination" style="border:none;">
                            <li>总分：${examStrategy.totalScore}&nbsp;分</li>
                            <li>合格分：${examStrategy.passScore}&nbsp;分</li>
                            <li>考试学时：${examStrategy.necessaryHour}&nbsp;min</li>
                            <li>考试学时： ${examStrategy.necessaryHour*60}&nbsp;分</li>
                            <li>考试时长：${examStrategy.examDuration}&nbsp;min</li>
                        </ul>
                    </div>
                    <table  class="table">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>题型</th>
                                <%-- <th>总题量</th>--%>
                            <th>考试题量</th>
                            <th>分值</th>
                            <th>总分</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach  items="${examStrategyRows}"   var="row" varStatus="i"  end="0">
                            <tr>
                                <td>${i.index+1}</td>
                                <td>${row.type}</td>
                                <td>${row.intCount}</td>
                                <td>${row.intScore}</td>
                                <td style="border-left:1px solid #ddd" rowspan="${fn:length(examStrategyRows)}">${row.allScore}</td>
                            </tr>
                        </c:forEach>
                        <c:forEach  items="${examStrategyRows}"   var="row" varStatus="i" begin="1" >
                            <tr>
                                <td>${i.index+1}</td>
                                <td>${row.type}</td>
                                <td>${row.intCount}</td>
                                <td>${row.intScore}</td>

                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- 确定组卷策略结束 -->
        </c:if>


        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">培训单位信息列表</h3>
                <form id="company_form">
                <div class="pull-right search-group">
                    <span>
                        <button class="btn btn-info" type="button" id="btn_select_company"><span class="fa fa-mouse-pointer"></span> 选择单位</button>
                        <button class="btn btn-info" type="button" id="btn_delete_batch_company"><span class="fa fa-trash-o"></span> 批量删除</button>
                    </span>
                    <span class="search">
                        <input type="text" id="company_name" name="company_name" placeholder="请输入单位名称">
                        <button type="button" title="搜索" class="btn" id="btn_company_search">
                            <span class="fa fa-search"></span> 搜索
                        </button>
                    </span>
                    <span>
                        <a href="javascript:;" id="btn_company_all" class="btn all">
                            <span class="fa fa-th"></span> 全部
                        </a>
                    </span>
                </div>
                <input type="hidden" id="company_project_id" name="company_project_id" value="${projectInfo.id}">
                </form>
            </div>
            <div class="box-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th width="60">序号</th>
                        <th width="60"><input type="checkbox" name="all"></th>
                        <th>单位名称</th>
                    </tr>
                    </thead>
                    <%--单位列表--%>
                    <tbody id="company_list">
                    <tr>
                        <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                    </tr>
                    </tbody>
                </table>
                <div class="page text-right" style="margin-top:5px;">
                    <ul id="company_page"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="projectId" name="projectId" value="${projectInfo.id}">
<input type="hidden" id="projectStatus" name="projectStatus" value="${projectInfo.projectStatus}">
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectInfo.projectType}">
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=path%>/static/global/js/page2.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/info_publicProject.js"></script>

<script type="text/javascript">
    var page1 = new page1();
    var page2 = new page2();
    infoPublicProject.init(page1,page2);
</script>
</body>
</html>
