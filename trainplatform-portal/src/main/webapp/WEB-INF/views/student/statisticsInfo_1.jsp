<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="box no-mg-t">
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
                                <li title="视频学习和文档类资料学习产生的学时记录。">
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
</div>

