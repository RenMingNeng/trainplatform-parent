<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<div class="box no-mg-t">

    <table class="table-list">
        <tr>
            <td class="no-border-top">
                <div class="box-header clearfix">
                    <h3 class="box-title" style="margin-top:0;">统计</h3>
                    <div class="lists">
                        <ul class="clearfix">
                            <li title="本项目各个课程应修学时之和。">
                                <div class="times" id="requirementStudyTime"></div>
                                <div class="info">答题学时</div>
                            </li>
                            <li title="视频学习和文档类资料学习产生的学时记录。">
                                <div class="times" id="trainStudyTime"></div>
                                <div class="info">总题量</div>
                            </li>
                            <%-- <li title="已经答过的题目数量，一个题目不管答了多少次，都只记录一题。">
                                 <div class="times question-num" id="yetAnswered"></div>
                                 <div class="info">已答题量</div>
                             </li>
                             <li title="至少有一次答对的题目数量。">
                                 <div class="times question-num" id="correctAnswered"></div>
                                 <div class="info">答对题量</div>
                             </li>--%>
                        </ul>
                    </div>
                </div>
            </td>
            <td width="30%" style="border-top:none;"></td>
        </tr>

    </table>
</div>

