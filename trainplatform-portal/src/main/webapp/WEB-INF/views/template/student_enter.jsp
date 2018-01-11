<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>博晟 | 培训平台-学员-项目详情</title>
    <jsp:include page="${path}/common/style" />
</head>
<body>
<jsp:include page="${path}/student/menu" />
<div id="main">
    <div class="area">
        <div id="addr">
            <a href="javascript:;">首页</a> &gt; <a href="javascript:;">项目详情</a>
        </div>
        <!-- 统计 -->
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">统计</h3>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-3 form-group">
                        <label for="" class="col-5">应修学时：</label>
                        <div class="col-7">5108分24秒</div>
                    </div>
                    <div class="col-3 form-group">
                        <label for="" class="col-5">已修学时：</label>
                        <div class="col-7">5108分24秒</div>
                    </div>
                    <div class="col-3 form-group">
                        <label for="" class="col-5">培训学时：</label>
                        <div class="col-7">5108分24秒</div>
                    </div>
                    <div class="col-3 form-group">
                        <label for="" class="col-5">答题学时：</label>
                        <div class="col-7">5108分24秒</div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-3 form-group">
                        <label for="" class="col-5">总题量：</label>
                        <div class="col-7">5108分24秒</div>
                    </div>
                    <div class="col-3 form-group">
                        <label for="" class="col-5">已答题量：</label>
                        <div class="col-7">97</div>
                    </div>
                    <div class="col-3 form-group">
                        <label for="" class="col-5">答对题量：</label>
                        <div class="col-7">34</div>
                    </div>
                    <div class="col-3 form-group">
                        <label for="" class="col-5">答题正确率：</label>
                        <div class="col-7">35.05%</div>
                    </div>
                </div>

            </div>
        </div>
        <!-- 题库练习列表 -->
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">题库练习列表</h3>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-6 form-group">
                        <label for="" class="col-3">我的错题：</label>
                        <div class="col-9"><a href="javascript:;">38</a></div>
                    </div>
                    <div class="col-6 form-group">
                        <label for="" class="col-3">我的收藏：</label>
                        <div class="col-9"><a href="javascript:;">38</a></div>
                    </div>
                    <div class="col-6 form-group">
                        <label for="" class="col-3">随机练习：</label>
                        <div class="col-9"><a href="javascript:;">开始练习</a></div>
                    </div>
                    <div class="col-6 form-group">
                        <label for="" class="col-3">专项练习：</label>
                        <div class="col-9">
                            <a href="javascript:;">单选题</a>&nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:;">多选题</a>&nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:;">判断题</a>&nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:;">易错题</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 考试 -->
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">考试</h3>
            </div>
            <div class="box-body">
                <div class="row">
                    <div class="col-6 form-group">
                        <label for="" class="col-3">考试类型：</label>
                        <div class="col-9">
                            <a href="javascript:;">正式考试</a>&nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:;">模拟考试</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 题库练习列表 -->
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">培训练习课程信息</h3>
            </div>
            <div class="box-body">
                <form>
                    <div class="clearfix search-group">
                        <div class="pull-right">
                            <label>课程名称</label>
                            <div class="search">
                                <input type="text" placehodler="请输入关键字">
                                <button type="button" title="搜索">
                                    <span class="fa fa-search"></span>
                                </button>
                            </div>
                            <a href="javascript:;">全部</a>
                        </div>
                    </div>
                    <div class="status_flag">
                        <dl>
                            <dt>状态：</dt>
                            <dd class="clearfix">
                                <span class="active">全部</span>
                                <span>未开始</span>
                                <span>进行中</span>
                                <span>已结束</span>
                            </dd>
                        </dl>
                    </div>
                </form>
                <!-- 表格 -->
                <table class="table pro_msg project_enter_table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>课程名称</th>
                        <th>课时</th>
                        <th>应修学时</th>
                        <th>已修学时</th>
                        <th>已修培训学时</th>
                        <th>题库数量</th>
                        <th>已答题量</th>
                        <th>答对题量</th>
                        <th>答题正确率</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1</td>
                        <td>
                            <div class="text-orange tooltip" data-length="20" >
                                凝土机械凝土机械凝土机械凝土机械凝土机械凝土机械凝土机械凝土机械凝土机械
                            </div>
                        </td>
                        <td>999分59秒</td>
                        <td>25分0秒</td>
                        <td>25分0秒</td>
                        <td>25分0秒</td>
                        <td>213</td>
                        <td>213</td>
                        <td>23</td>
                        <td>11</td>
                        <td>47%</td>
                        <td>
                            <a href="javascript:;" class="link">进入学习</a>
                            <a href="javascript:;" class="link">进入答题</a>
                        </td>
                    </tr>
                    <tr>
                        <td class="pro_num">1</td>
                        <td class="pro_name">
                            <div class="text-orange" title="混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程">
                                凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程
                            </div>
                        </td>
                        <td class="pro_time">999分59秒</td>
                        <td class="pro_time">25分0秒</td>
                        <td class="pro_time">25分0秒</td>
                        <td class="pro_times">25分0秒</td>
                        <td class="pro_states">213</td>
                        <td class="pro_states">213</td>
                        <td class="pro_states">23</td>
                        <td class="pro_states">11</td>
                        <td class="pro_states">47%</td>
                        <td class="pro_handler">
                            <a href="javascript:;" class="link">进入学习</a>
                            <a href="javascript:;" class="link">进入答题</a>
                        </td>
                    </tr>
                    <tr>
                        <td class="pro_num">1</td>
                        <td class="pro_name">
                            <div class="text-orange" title="混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程">
                                凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程
                            </div>
                        </td>
                        <td class="pro_time">999分59秒</td>
                        <td class="pro_time">25分0秒</td>
                        <td class="pro_time">25分0秒</td>
                        <td class="pro_times">25分0秒</td>
                        <td class="pro_states">213</td>
                        <td class="pro_states">213</td>
                        <td class="pro_states">23</td>
                        <td class="pro_states">11</td>
                        <td class="pro_states">47%</td>
                        <td class="pro_handler">
                            <a href="javascript:;" class="link">进入学习</a>
                            <a href="javascript:;" class="link">进入答题</a>
                        </td>
                    </tr>
                    <tr>
                        <td class="pro_num">1</td>
                        <td class="pro_name">
                            <div class="text-orange" title="混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程">
                                凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程
                            </div>
                        </td>
                        <td class="pro_time">999分59秒</td>
                        <td class="pro_time">25分0秒</td>
                        <td class="pro_time">25分0秒</td>
                        <td class="pro_times">25分0秒</td>
                        <td class="pro_states">213</td>
                        <td class="pro_states">213</td>
                        <td class="pro_states">23</td>
                        <td class="pro_states">11</td>
                        <td class="pro_states">47%</td>
                        <td class="pro_handler">
                            <a href="javascript:;" class="link">进入学习</a>
                            <a href="javascript:;" class="link">进入答题</a>
                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
</html>