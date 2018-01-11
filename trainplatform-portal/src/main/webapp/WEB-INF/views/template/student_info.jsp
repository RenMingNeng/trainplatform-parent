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
            <div class="pull-left">
                <div class="box-title"><span><a href="">我的项目&nbsp;<i class="fa fa-angle-right"></i></a></span></div>
            </div>
            <div class="pull-right">
                <div class="box-toolbar"><a href="javascript:window.location.reload();" title="刷新"><i class="fa fa-refresh"></i></a><a href="javascript:history.go(-1);" title="返回"><i class="fa fa-arrow-left"></i></a></div>
            </div>
        </div>

        <!-- 项目基础信息 -->
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">项目基础信息</h3>
            </div>
            <div class="box-body project_info">
                <div class="row">
                    <div class="col-12 form-group">
                        <label for="" class="col-1">培训主题：</label>
                        <div class="col-11 info">5108分24秒</div>
                    </div>
                    <div class="col-12 form-group">
                        <label for="" class="col-1">项目名称：</label>
                        <div class="col-11 info">
                            2017年全国水利安全生产知识网络竞赛
                            2017年全国水利安全生产知识网络竞赛
                            2017年全国水利安全生产知识网络竞赛
                            2017年全国水利安全生产知识网络竞赛
                        </div>
                    </div>
                    <div class="col-12 form-group">
                        <label for="" class="col-1">受训角色：</label>
                        <div class="col-11 info">默认角色</div>
                    </div>
                    <div class="col-12 form-group">
                        <label for="" class="col-1">培训时间：</label>
                        <div class="col-11 info">2017-06-01 至 2017-12-31</div>
                    </div>
                    <div class="col-12 form-group">
                        <label for="" class="col-1">练习时间：</label>
                        <div class="col-11 info">2017-06-01 至 2017-12-31</div>
                    </div>
                    <div class="col-12 form-group">
                        <label for="" class="col-1">受训单位：</label>
                        <div class="col-11 info">5108分24秒</div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 课程信息 -->
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">课程信息</h3>
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
                </form>
                <table class="table pro_msg project_info_table">
                    <thead>
                    <tr>
                        <th class="pro_num">序号</th>
                        <th class="pro_info_num">课程编号</th>
                        <th class="pro_name">课程名称</th>
                        <th class="pro_time">课时</th>
                        <th class="pro_requirst_time">学时要求</th>
                        <th class="pro_course_num">题库数量</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="pro_num">1</td>
                        <td class="pro_info_num">编号89757</td>
                        <td class="pro_name">
									<span class="text-orange" title="混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程">		混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程
									</span>
                        </td>

                        <td class="pro_time">25分0秒</td>
                        <td class="pro_requirst_time">25分0秒</td>
                        <td class="pro_course_num">25分0秒</td>
                    </tr>
                    <tr>
                        <td class="pro_num">1</td>
                        <td class="pro_info_num">编号89757</td>
                        <td class="pro_name">
									<span class="text-orange" title="混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程">		混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程
									</span>
                        </td>

                        <td class="pro_time">25分0秒</td>
                        <td class="pro_requirst_time">25分0秒</td>
                        <td class="pro_course_num">25分0秒</td>
                    </tr>
                    <tr>
                        <td class="pro_num">1</td>
                        <td class="pro_info_num">编号89757</td>
                        <td class="pro_name">
									<span class="text-orange" title="混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程">		混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程
									</span>
                        </td>

                        <td class="pro_time">25分0秒</td>
                        <td class="pro_requirst_time">25分0秒</td>
                        <td class="pro_course_num">25分0秒</td>
                    </tr>
                    <tr>
                        <td class="pro_num">1</td>
                        <td class="pro_info_num">编号89757</td>
                        <td class="pro_name">
									<span class="text-orange" title="混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程">		混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程
									</span>
                        </td>

                        <td class="pro_time">25分0秒</td>
                        <td class="pro_requirst_time">25分0秒</td>
                        <td class="pro_course_num">25分0秒</td>
                    </tr>
                    <tr>
                        <td class="pro_num">1</td>
                        <td class="pro_info_num">编号89757</td>
                        <td class="pro_name">
									<span class="text-orange" title="混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程">		混凝土机械安全操作规程混凝土机械安全操作规程混凝土机械安全操作规程
									</span>
                        </td>

                        <td class="pro_time">25分0秒</td>
                        <td class="pro_requirst_time">25分0秒</td>
                        <td class="pro_course_num">25分0秒</td>
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