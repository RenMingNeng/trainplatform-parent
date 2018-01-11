<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <base href="<%=basePath%>" />
    <meta charset="utf-8"/>
    <title>博晟 | 培训平台-学员-查看记录</title>
    <jsp:include page="${path}/common/style" />
    <style>
        .nav{display: none;}
    </style>
</head>
<body>
<jsp:include page="${path}/student/menu">
    <jsp:param name="menu" value="student"></jsp:param>
</jsp:include>

<div id="main">
    <div class="area">
            <div class="clearfix enter-project-title">
                <div class="pull-left project_title">项目名称：${projectName}--${projectTypeName}</div>
                <div class="pull-right search-group">
                    <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                </div>
            </div>

        <c:if test="${projectType != examType}">
            <!-- 统计 -->
            <c:if test="${fn:contains(containTrain,projectType)}">
                <jsp:include page="${path}/popup/statisticsInfo_3" />
            </c:if>
            <c:if test="${fn:contains(containExercise,projectType)}">
                <jsp:include page="${path}/popup/statisticsInfo_2" />
            </c:if>
            <c:if test="${fn:contains(containTrainAndExercise,projectType)}">
                <jsp:include page="${path}/popup/statisticsInfo_1" />
            </c:if>
           <%-- <div class="box">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">培训记录</h3>

                </div>
                <div class="box-body">
                    <div class="row tooltip">
                        <c:if test="${permissionTrain}">
                            <div class="col-3 form-group" data-info="本项目各个课程应修学时之和。">
                                <label class="col-5"><span>应修学时：</span></label>
                                <div class="col-7 info" id="requirementStudyTime">
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${fn:contains(containTrainAndExercise,projectType)}">
                            <div class="col-3 form-group" data-info="培训学时与答题学时之和。">
                                <label class="col-5"><span>已修学时：</span></label>
                                <div class="col-7 info" id="totalStudyTime">
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${permissionTrain}">
                            <div class="col-3 form-group" data-info="视频学习和文档类资料学习产生的学时记录。">
                                <label class="col-5"><span>培训学时：</span></label>
                                <div class="col-7 info" id="trainStudyTime">
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${permissionExercise}">
                            <div class="col-3 form-group" data-info="通过答题产生的学时信息，答对一题，产生30s学时，答错一题产生3s学时。">
                                <label class="col-5"><span>答题学时：</span></label>
                                <div class="col-7 info" id="answerStudyTime">
                                </div>
                            </div>
                            <div class="col-3 form-group" data-info="各个课程中包含的题量之和。">
                                <label class="col-5"><span>总题量：</span></label>
                                <div class="col-7 info" id="totalQuestion">
                                </div>
                            </div>
                            <div class="col-3 form-group" data-info="已经答过的题目数量，一个题目不管答了多少次，都只记录一题。">
                                <label class="col-5"><span>已答题量：</span></label>
                                <div class="col-7 info" id="yetAnswered">
                                </div>
                            </div>
                            <div class="col-3 form-group" data-info="至少有一次答对的题目数量。">
                                <label class="col-5"><span>答对题量：</span></label>
                                <div class="col-7 info" id="correctAnswered">
                                </div>
                            </div>
                            <c:if test="${fn:contains(containTrainAndExercise,projectType)}">
                                <div class="col-3 form-group" data-info="答对题量与已答题量之比。">
                                    <label  class="col-5"><span>答题正确率：</span></label>
                                    <div class="col-7 info" id="correctRate">
                                    </div>
                                </div>
                            </c:if>
                        </c:if>
                    </div>
                </div>
            </div>--%>

            <!-- 课程信息列表 -->
            <div class="box">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">培训记录列表</h3>
                    <form id="project_record_form">
                        <input type="hidden" id="project_basicId" name="project_basicId" value="${projectId}">   <!--项目id-->
                        <input type="hidden" id="project_userId" name="project_userId" value="${userId}">     <!--用户id-->
                        <input type="hidden" id="finish_status" name="finish_status" value="">     <!--完成状态-->
                        <div class="pull-right search-group">
                            <span class="search">
                                <input type="text" name="course_name" id="name_search" placeholder="请输入课程名称">
                                <button type="button" id="course_search" title="搜索" class="btn" >
                                    <span class="fa fa-search" ></span> 搜索
                                </button>
                            </span>
                            <span>
                                <a href="javascript:;" id="course_all"  class="btn all" ><span class="fa fa-th"></span> 全部</a>
                            </span>
                        </div>
                    </form>
                </div>
                <div class="box-body">
                    <%--<form id="project_record_form">
                        <div class="clearfix search-group">
                            <div class="pull-right">
                                <label>课程名称：</label>
                                <div class="search">
                                    <input type="hidden" id="project_basicId" name="project_basicId" value="${projectId}">   <!--项目id-->
                                    <input type="hidden" id="project_userId" name="project_userId" value="${userId}">     <!--用户id-->
                                    <input type="hidden" id="finish_status" name="finish_status" value="">     <!--完成状态-->
                                    <input type="text" name="course_name" id="name_search" placehodler="请输入课程名称">
                                    <button type="button" title="搜索" >
                                        <span class="fa fa-search" id="course_search"></span>
                                    </button>
                                </div>
                                <a href="javascript:;" id="course_all">全部</a>
                            </div>
                        </div>
                        <div class="status_flag">
                            <dl>
                                &lt;%&ndash;<dt>状态：</dt>&ndash;%&gt;
                                <dd class="clearfix">
                                    <span id="status_1" class="active">全部</span>
                                    <span id="status_2">未完成</span>
                                    <span id="status_3">已完成</span>
                                </dd>
                            </dl>
                        </div>
                    </form>--%>
                        <div class="status_flag">
                            <dl>
                                <!--<dt>状态：</dt>-->
                                <dd class="clearfix">
                                    <span id="status_1" class="active">全部</span>
                                    <span id="status_2">未完成</span>
                                    <span id="status_3">已完成</span>
                                </dd>
                            </dl>
                        </div>
                    <!-- 表格 -->
                    <table class="table pro_msg project_enter_table">
                        <thead>
                        <tr>
                            <th class="pro_num">序号</th>
                            <th class="pro_name">课程名称</th>
                            <c:if test="${permissionTrain}">
                                <th class="pro_time">课时</th>
                                <th class="pro_time">应修学时</th>
                                <c:if test="${fn:contains(containTrainAndExercise,projectType)}">
                                    <th class="pro_time">已修学时</th>
                                </c:if>
                                <th class="pro_times">培训学时</th>
                            </c:if>
                            <c:if test="${permissionExercise}">
                                <th class="pro_states">答题学时</th>
                                <th class="pro_states">题库数量</th>
                                <th class="pro_states">已答题量</th>
                                <th class="pro_states">答对题量</th>
                                <th class="pro_states">答题正确率</th>
                            </c:if>
                            <th class="pro_states">状态</th>
                        </tr>
                        </thead>
                        <tbody id="project_record">
                        </tbody>
                    </table>
                    <%--
                        text-left、text-center、text-right
                        分别让分页版块左、中、右
                      --%>
                    <div class="page text-right" style="margin-top:0;margin-bottom:30px;">
                        <ul id="project_course_page"></ul>
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${permissionExercise}">
            <!-- 练习排行列表 -->
            <div class="box">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">练习排行</h3>
                    <form id="exercise_order_form">
                    <div class="pull-right search-group">
                        <input type="hidden" id="projectId" name="projectId" value="${projectId}">   <!--项目id-->
                        <input type="hidden" id="userId" name="userId" value="${userId}">     <!--用户id-->
                        <input type="hidden" id="orderType" name="orderType" value="">     <!--排序类型-->
                        <input type="hidden" id="operateType" name="operateType" value="student">     <!--操作类型-->
                        <span class="search">
                            <input type="text" id="userName" name="userName" placeholder="请输入人员姓名">
                            <button type="button" id="exercise_search" title="搜索" class="btn">
                                <span class="fa fa-search" ></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a href="javascript:;" class="btn all" id="exercise_all"><span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                    </form>
                </div>
                <div class="box-body">
                    <%--<form id="exercise_order_form">
                        <div class="clearfix search-group">
                           &lt;%&ndash; <div class="pull-left">
                                <a id="sort1" href="javascript:;" onclick="exercise_order.sort('1')"  class="btn btn-info"><span>按答题学时排行</span></a>
                                <a id="sort2" href="javascript:;" onclick="exercise_order.sort('2')"  class="btn"><span>按答题量排行</span></a>
                                <a id="sort3" href="javascript:;" onclick="exercise_order.sort('3')"  class="btn"><span>按答题正确率排行</span></a>
                            </div>&ndash;%&gt;
                            <div class="pull-right">
                                <label>用户名称：</label>
                                <div class="search">
                                    <input type="hidden" id="projectId" name="projectId" value="${projectId}">   <!--项目id-->
                                    <input type="hidden" id="userId" name="userId" value="${userId}">     <!--用户id-->
                                    <input type="hidden" id="orderType" name="orderType" value="">     <!--排序类型-->
                                    <input type="text" id="userName" name="userName" placehodler="请输入人员姓名">
                                    <button type="button" title="搜索" >
                                        <span class="fa fa-search" id="exercise_search"></span>
                                    </button>
                                </div>
                                <a href="javascript:;" id="exercise_all">全部</a>
                            </div>
                        </div>
                    </form>--%>
                    <div class="status_flag">
                        <dl>
                                <%--<dt>状态：</dt>--%>
                            <dd class="clearfix">
                                <span id="sort1" class="active" onclick="exercise_order.sort('1')"  >按答题学时排行</span>
                                <span id="sort2" onclick="exercise_order.sort('2')" >按答题量排行</span>
                                <span id="sort3" onclick="exercise_order.sort('3')">按答题正确率排行</span>
                            </dd>
                        </dl>
                    </div>
                    <!-- 表格 -->
                    <table class="table pro_msg project_enter_table">
                        <thead>
                        <tr>
                            <th class="exercise_num">序号</th>
                            <th class="user_name">姓名</th>
                            <%--<th class="role_name">受训角色</th>--%>
                            <th class="dept_name">部门信息</th>
                            <th class="total_question">总题量</th>
                            <th class="yet_answered">已答题量</th>
                            <th class="not_answered">未答题量</th>
                            <th class="correct_answered">答对题量</th>
                            <th class="fail_answered">答错题量</th>
                            <th class="correct_rate">答题正确率</th>
                            <th class="answer_study_time">答题学时</th>
                        </tr>
                        </thead>
                        <tbody id = user_exercise>

                        </tbody>
                        <tbody id="exercise_order">

                        </tbody>
                    </table>
                        <%--
                            text-left、text-center、text-right
                            分别让分页版块左、中、右
                          --%>
                    <div class="page text-right" style="margin-top:0;margin-bottom:30px;">
                        <ul id="project_exercise_page"></ul>
                    </div>
                </div>
            </div>
        </c:if>
        <!-- 带考试类型就显示考试成绩 -->
        <c:if test="${permissionExam}">
            <!-- 考试信息列表 -->
            <div class="box">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">成绩记录</h3>
                    <form id="exam_score_form">
                        <div class="pull-right search-group">
                            <span>
                                <label>考试类型：</label>
                                <select id="examType" name="examType">
                                    <option value="">全部</option>
                                    <option value="1">模拟考试</option>
                                    <option value="2">正式考试</option>
                                </select>
                            </span>
                            <span>
                                <label>状态：</label>
                                <select id="isPassed" name="isPassed">
                                    <option value="">全部</option>
                                    <option value="1">合格</option>
                                    <option value="2">不合格</option>
                                </select>
                            </span>
                            <span>
                                <input type="hidden" id="examProjectId" name="examProjectId" value="${projectId}">   <!--项目id-->
                                <button type="button" id="exam_search" title="搜索" class="btn" style="vertical-align: top;">
                                    <span class="fa fa-search" ></span> 搜索
                                </button>
                            </span>
                            <span>
                                <a href="javascript:;" class="btn all" id="exam_all"><span class="fa fa-th"></span> 全部</a>
                            </span>
                        </div>
                    </form>
                </div>
                <div class="box-body">
                    <%--<form id="exam_score_form">
                        <div class="clearfix search-group">
                            <div class="pull-left">


                            </div>
                            <div class="pull-right">
                                <div class="search">
                                    <input type="hidden" id="examProjectId" name="examProjectId" value="${projectId}">   <!--项目id-->
                                    <button type="button" title="搜索" >
                                        <span class="fa fa-search" id="exam_search"></span>
                                    </button>
                                </div>
                                <a href="javascript:;" id="exam_all">全部</a>
                            </div>
                        </div>
                    </form>--%>
                    <!-- 表格 -->
                    <table class="table pro_msg project_enter_table">
                        <thead>
                        <tr>
                            <th class="exam_num">序号</th>
                            <th class="exam_score">考试成绩</th>
                            <th class="exam_state">考试状态</th>
                            <th class="exam_type">考试类型</th>
                            <th class="exam_time">考试开始时间</th>
                            <th class="exam_duration">考试时长</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="exam_score">
                        </tbody>
                    </table>
                        <%--
                            text-left、text-center、text-right
                            分别让分页版块左、中、右
                          --%>
                    <div class="page text-right" style="margin-top:0;margin-bottom:30px;">
                        <ul id="exam_score_page"></ul>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>
<!--隐藏字段-->
<input type="hidden" id="projectType" name="projectTypeName" value="${projectType}">     <!--项目类型名称-->

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=path%>/static/global/js/page2.js"></script>
<script src="<%=resourcePath%>static/global/js/student/projectRecord/project_record.js"></script>
<script src="<%=resourcePath%>static/global/js/student/projectRecord/exercise_order.js"></script>
<script src="<%=resourcePath%>static/global/js/student/projectRecord/exam_score.js"></script>
<script>
  var page = new page();
  var page1 = new page1();
  var page2 = new page2();
  var examType = ${examType};
  var projectType = $("#projectType").val();
  var permissionTrain = ${permissionTrain};
  var permissionExercise = ${permissionExercise};
  var permissionExam = ${permissionExam};
  var containTrainAndExercise = ${containTrainAndExercise};
  $(function(){

    var project_basicId = $("#project_basicId").val();
    var project_userId = $("#project_userId").val();
    var basePath = "<%=basePath%>";
    if(projectType != examType){
      project_record.init(basePath, page, projectType, permissionTrain, permissionExercise, containTrainAndExercise);
      project_record.tongji(project_basicId,project_userId);
    }
    if(permissionExercise){
      exercise_order.init(basePath, page1);
    }
    if(permissionExam){
      exam_score.init(basePath, page2);
    }
  })
</script>
</body>
</html>
