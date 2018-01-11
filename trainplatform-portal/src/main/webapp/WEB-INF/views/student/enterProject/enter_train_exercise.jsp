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
    <title>博晟 | 培训平台-学员-进入项目</title>
    <jsp:include page="${path}/common/style" />
    <style>
        .nav{display: none;}
    </style>
</head>
<body>

<body>
<jsp:include page="${path}/student/menu">
    <jsp:param name="menu" value="student"></jsp:param>
</jsp:include>
<div id="main">
    <div class="area">
        <%--<div class="clearfix" style="line-height:36px;margin-top:20px;margin-bottom:-10px;">
            <div class="pull-left project_title">项目名称：${projectInfo.projectName}--${projectTypeName}</div>
            <div class="pull-right search-group">
                <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
            </div>
        </div>--%>
        <div class="clearfix enter-project-title">
            <div class="pull-left project_title">项目名称：${projectInfo.projectName}--${projectTypeName}</div>
            <div class="pull-right search-group">
                <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
            </div>
        </div>
        <!-- 统计 -->
            <jsp:include page="${path}/popup/statisticsInfo_1" />
        <%--    <div class="box no-mg-t">

                <div class="box-body">
                    <table class="table-list">
                        <tr>
                            <td rowspan="2" class="no-border-top">
                                <div class="box-header clearfix">
                                    <h3 class="box-title">统计</h3>
                                    <div class="lists">
                                        <ul class="clearfix">
                                            <li title="本项目各个课程应修学时之和。">
                                                <div class="times" id="requirementStudyTime"></div>
                                                <div class="info">应修学时</div>
                                            </li>
                                            <li title="培训学时与答题学时之和。">
                                                <div class="times" id="totalStudyTime"></div>
                                                <div class="info">已修学时</div>
                                            </li>
                                            <li title="培训学时与答题学时之和。">
                                                <div class="times" id="trainStudyTime"></div>
                                                <div class="info">培训学时</div>
                                            </li>
                                            <li title="通过答题产生的学时信息，答对一题，产生30s学时，答错一题产生3s学时。">
                                                <div class="times" id="answerStudyTime"></div>
                                                <div class="info">答题学时</div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </td>
                            <td class="col1 no-border-top">
                                <div class="clearfix" title="各个课程中包含的题量之和。">
                                    <div class="pull-left l">
                                        <div class="icon icon_1"></div>
                                        总题量
                                    </div>
                                    <div class="pull-left r">
                                        <span id="totalQuestion"></span>
                                    </div>
                                </div>
                            </td>
                            <td class="col1 no-border-top" title="至少有一次答对的题目数量。">
                                <div class="pull-left l">
                                    <div class="icon icon_2"></div>
                                    答对题量
                                </div>
                                <div class="pull-left r red">
                                    <span id="correctAnswered"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col1" title="已经答过的题目数量，一个题目不管答了多少次，都只记录一题。">
                                <div class="pull-left l">
                                    <div class="icon icon_1"></div>
                                    已答题量
                                </div>
                                <div class="pull-left r ">
                                    <span id="yetAnswered"></span>
                                </div>
                            </td>
                            <td class="col1" title="答对题量与已答题量之比。">
                                <div class="pull-left l">
                                    <div class="icon icon_2"></div>
                                    答题正确率
                                </div>
                                <div class="pull-left r red">
                                    <span id="correctRate"></span>
                                </div>
                            </td>
                        </tr>
                    </table>

                </div>
            </div>--%>
        <%--<div class="box">

            <div class="box-header clearfix">
                <h3 class="box-title pull-left">统计</h3>
            </div>

            <div class="box-body pd_b_10">
                <div class="row tooltip">

                        <div class="col-3 form-group" data-info="本项目各个课程应修学时之和。">
                            <label class="col-5"><span>应修学时：</span></label>
                            <div class="col-7 info" id="requirementStudyTime">
                            </div>
                        </div>
                        <div class="col-3 form-group" data-info="培训学时与答题学时之和。">
                            <label class="col-5"><span>已修学时：</span></label>
                            <div class="col-7 info" id="totalStudyTime">
                            </div>
                        </div>
                        <div class="col-3 form-group" data-info="视频学习和文档类资料学习产生的学时记录。">
                            <label class="col-5"><span>培训学时：</span></label>
                            <div class="col-7 info" id="trainStudyTime">
                            </div>
                        </div>
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
                            <div class="col-3 form-group" data-info="答对题量与已答题量之比。">
                                <label  class="col-5"><span>答题正确率：</span></label>
                                <div class="col-7 info" id="correctRate">
                                </div>
                            </div>

            </div>
        </div>
        </div>--%>

        <%--在线学习--%>
        <div class="box enter_project">
            <div class="box-header">
                <h3 class="box-title">学习</h3>
            </div>
            <div class="box-body pd_b_10" style="padding-bottom:25px!important;">
                <div id="online_study">
                    <a href="javascript:;" class="exam exam_3 clearfix" onclick="student_enter.enterStudy(null)" style="margin-left:70px;">
                        <span class="icon"></span>
                        <div class="pull-left">在线学习</div>
                    </a>
                </div>
            </div>
        </div>

        <!-- 题库练习列表 -->
        <div class="box" id ="box" >
            <div class="box-header">
                <h3 class="box-title">题库练习</h3>
            </div>

            <div class="clearfix do-exam" >
                <div class="pull-left">
                    <div>
                        <a href="javascript:common.goto_student_wrong('${param.projectId}');" id="wrongCount">我的错题 <span></span> </a>
                    </div>
                    <div>
                        <a href="javascript:common.goto_student_collection('${param.projectId}');" id="collectCount">我的收藏 <span></span> </a>
                    </div>
                </div>
                <div class="pull-right clearfix" id="enter_exercise_1">
                    <div class="pull-left l">
                        <h3>随机练习</h3>
                        <div>
                            <a href="javascript:common.goto_student_random('${param.projectId}');">
                                <span class="icon icon_1"></span> 开始练习
                            </a>
                        </div>
                    </div>
                    <div class="pull-left r">
                        <h3>专项练习</h3>
                        <div>
                            <ul>
                                <li><a href="javascript:common.goto_student_single('${param.projectId}');"><span class="icon  icon_2"></span> 单选题</a></li>
                                <li><a href="javascript:common.goto_student_many('${param.projectId}');"><span class="icon  icon_3"></span> 多选题</a></li>
                                <li><a href="javascript:common.goto_student_judge('${param.projectId}');"><span class="icon  icon_4"></span> 判断题</a></li>
                                <li><a href="javascript:common.goto_student_easyWrongs('${param.projectId}');"><span class="icon  icon_5"></span> 易错题</a></li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div class="pull-right clearfix nav" id="enter_exercise_3">
                    <div class="pull-left l">
                        <h3>随机练习</h3>
                        <div>
                            <a href="javascript:message1()">
                                <span class="icon icon_1"></span> 开始练习
                            </a>
                        </div>
                    </div>
                    <div class="pull-left r">
                        <h3>专项练习</h3>
                        <div>
                            <ul>
                                <li><a href="javascript:message1()"><span class="icon  icon_2"></span> 单选题</a></li>
                                <li><a href="javascript:message1()"><span class="icon  icon_3"></span> 多选题</a></li>
                                <li><a href="javascript:message1()"><span class="icon  icon_4"></span> 判断题</a></li>
                                <li><a href="javascript:message1()"><span class="icon  icon_5"></span> 易错题</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <%--<c:if test="${exercisePer == true}">
            <div class="box-body pd_b_10 enter_exam" style="padding-left:0;">
                <div class="row">
                    <div class="col-6 form-group">
                        <label class="col-3" style="cursor: pointer;width:125px;" onclick="common.goto_student_wrong('${param.projectId}');">我的错题：</label>
                        <div class="col-9 info"><a href="javascript:;"  id="wrongCount"  onclick="common.goto_student_wrong('${param.projectId}');"><span style="color: #188eee;font-size: 18px;">${wrongCount}</span></a></div>
                    </div>
                    <div class="col-6 form-group">
                        <label class="col-3" style="cursor: pointer;" onclick="common.goto_student_collection('${param.projectId}');">我的收藏：</label>
                        <div class="col-9 info"><a href="javascript:;" id="collectCount"  onclick="common.goto_student_collection('${param.projectId}');"><span style="color: #188eee;font-size: 18px;">${collectionCount}</span></a></div>
                    </div>


                        <%--<c:if test="${exercisePer == true}">
                        <div class="col-6 form-group" id="enter_exercise_1">
                            <label class="col-3" style="width:125px;">随机练习：</label>
                            <div class="col-9"><a href="javascript:;" class="btn_a" onclick="common.goto_student_random('${param.projectId}');"><span class="fa fa-long-arrow-left"></span> 开始练习</a></div>
                        </div>
                        <div class="col-6 form-group" id="enter_exercise_2">
                            <label class="col-3">专项练习：</label>
                            <div class="col-9">
                                <a href="javascript:;" class="btn_a" onclick="common.goto_student_single('${param.projectId}');">单选题</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="javascript:;" class="btn_a" onclick="common.goto_student_many('${param.projectId}');">多选题</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="javascript:;" class="btn_a" onclick="common.goto_student_judge('${param.projectId}');">判断题</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="javascript:;" class="btn_a" onclick="common.goto_student_easyWrongs('${param.projectId}');">易错题</a>
                            </div>
                        </div>--%>
                       <%-- </c:if>--%>


                       <%-- <c:if test="${exercisePer == false}">
                            <div class="col-6 form-group nav" id="enter_exercise_3">
                                <label class="col-3" style="width:125px;">随机练习：</label>
                                <div class="col-9"><a href="javascript:;" class="btn_a" onclick="message1()"><span class="fa fa-long-arrow-left"></span> 开始练习</a></div>
                            </div>
                            <div class="col-6 form-group nav" id="enter_exercise_4">
                                <label class="col-3">专项练习：</label>
                                <div class="col-9">
                                    <a href="javascript:;" class="btn_a" onclick="message1()">单选题</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="javascript:;" class="btn_a" onclick="message1()">多选题</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="javascript:;" class="btn_a" onclick="message1()">判断题</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="javascript:;" class="btn_a" onclick="message1()">易错题</a>
                                </div>
                            </div>--%>



                       <%-- </c:if>--%>



        </div>


        <!-- 培训练习课程信息 -->
        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">课程信息</h3>
                <form id="project_course_form">
                    <input type="hidden" id="finish_status" name="finish_status">     <!--课程完成状态: 默认全部，-1 未完成 ，1 已完成-->
                    <input type="hidden" id="project_basicId" name="project_basicId" value="${param.projectId}">   <!--项目id-->
                    <input type="hidden" id="project_userId" name="project_userId" value="${project_userId}">     <!--用户id-->
                    <div class="pull-right search-group">
                        <span class="search">
                            <input type="text" id="course_name" name="course_name" placeholder="请输入课程名称">
                            <button type="button" title="搜索" class="btn" id="course_search">
                                <span class="fa fa-search"></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a href="javascript:;" id="course_all" class="btn all"><span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                </form>
            </div>
            <div class="box-body">
                <div class="status_flag">
                    <dl>
                        <%--<dt>状态:</dt>--%>
                        <dd class="clearfix">
                            <span class="active status" finish_status="" id="btn_all">全部</span>
                            <span class="status" finish_status="-1">未完成</span>
                            <span class="status" finish_status="1">已完成</span>
                        </dd>
                    </dl>
                </div>
                <!-- 表格 -->
                <table class="table pro_msg project_enter_table">
                    <thead>
                    <tr>
                        <th width="60">序号</th>
                        <th class="text-left">课程名称</th>
                        <th width="80">课时</th>
                        <th width="80">应修学时</th>
                        <th width="80">已修学时</th>
                        <th width="120">已修培训学时</th>
                        <th width="50">题库数量</th>
                        <th width="50" >已答题量</th>
                        <th width="50">答对题量</th>
                        <th width="60">答题正确率</th>
                        <th width="60">状态</th>
                        <th width="200">操作</th>
                    </tr>
                    </thead>
                    <tbody id="project_course_list">
                    <tr>
                        <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                    </tr>
                    </tbody>
                </table>
            <div class="page text-right" style="margin-top:0px;">
                <ul id="project_course_page"></ul>
            </div>
            </div>
        </div>

        
    </div>
</div>
<!-- 隐藏a标签 方便跳转 window.open容易被屏蔽 -->
<a href="javascript:void(0);" id="go_exam" target="_blank" style="position: absolute;top: 0;right: 0;display: none;"><span id="span_go_print">跳转考试页面</span></a>
<!--隐藏字段-->
<input type="hidden" id="role_id" name="role_id" value="${role_id}">     <!--角色id-->
<input type="hidden" id="projectId" value="${param.projectId}">

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/student/enterProject/student_enter.js"></script>
<script src="<%=resourcePath%>static/global/js/jquery.blockui.min.js"></script>
<script src="<%=resourcePath%>static/global/js/common.js"></script>

<script>

    function message1() {
        layer.alert('不在时间范围之内！')
    }

    var permission={
        'permissionTrain':${permissionTrain},
        'permissionExercise':${permissionExercise},
        'permissionExam':${permissionExam},
        'trainType':"${trainType}",
        'trainExamType':"${trainExamType}",
        'projectType':"${projectInfo.projectType}",
        'trainPer': "${trainPer}",
        'exercisePer': "${exercisePer}",
        'projectTrainInfo': '${projectBasic.projectTrainInfo}',
        'projectExerciseInfo': '${projectBasic.projectExerciseInfo}',
        'projectExamInfo': '${projectBasic.projectExamInfo}'
    };

    var page = new page();
    student_enter.init(page,permission);

    common.messenger_student_enter_init();


</script>

</html>