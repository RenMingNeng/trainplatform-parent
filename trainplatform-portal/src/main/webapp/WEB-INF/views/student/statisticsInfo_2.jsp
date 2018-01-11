<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="box no-mg-t">
    <table class="table-list">
        <tr>
            <td rowspan="2" class="no-border-top">
                <div class="box-header clearfix">
                    <h3 class="box-title" style="margin-top:0;">统计</h3>
                    <div class="lists">
                        <ul class="clearfix">
                            <li title="通过答题产生的学时信息，答对一题，产生30s学时，答错一题产生3s学时。">
                                <div class="times" id="answerStudyTime"></div>
                                <div class="info">答题学时</div>
                            </li>
                            <li title="各个课程中包含的题量之和。">
                                <div class="times question-num" ><b id="totalQuestion"></b></div>
                                <div class="info">总题量</div>
                            </li>
                            <li title="已经答过的题目数量，一个题目不管答了多少次，都只记录一题。">
                                <div class="times question-num"><b id="yetAnswered"></b></div>
                                <div class="info">已答题量</div>
                            </li>
                            <li title="至少有一次答对的题目数量。">
                                <div class="times question-num" ><b id="correctAnswered"></b></div>
                                <div class="info">答对题量</div>
                            </li>
                        </ul>
                    </div>
                </div>
            </td>
        </tr>
    </table>
</div>

