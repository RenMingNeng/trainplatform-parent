/**
 * 创建公开型项目第三步
 */
function Step_3() {
    var _this = this;
    var page;
    var projectId = $("#id").val();                 // 项目编号
    var projectTypeNo = $("#projectTypeNo").val();  // 项目类型编号
    _this.page_size = 10;
    _this.index = -1;

    _this.init = function (_page) {
        page = _page;
        _this.initEvent();
        _this.initTable_company();
    }

    _this.initEvent = function () {
        // 弹窗-选择培训单位
        $("#btn_select_company").click(function() {
            layer.open({
                type : 2,
                title : '选择培训单位',
                shadeClose : false, // true点击遮罩层关闭
                shade : 0.3, // 遮罩层背景
                area : [ '1200px', '80%' ], // 弹出层大小
                scrollbar : false, // false隐藏滑动块
                content : [appPath+ '/popup/selectTrainCompany?projectId='+$("#id").val()+"&type=1",'yes' ]
            });
        });

        // 批量删除单位
        $("#btn_delete_batch_company").click(function() {
            var $checkboxs = $('#company_list input:checked');
            var company_ids = [];
            $.each($checkboxs,function(i,n){
                var $this = $(this);
                company_ids.push( $this.data('id') );
            })
            console.log(company_ids)
            if(company_ids.length < 1){
                layer.alert("至少选择一条数据",{icon:7})
            }else{
                layer.confirm('确定删除？', {
                    icon : 3,
                    btn: ['确定','取消']        //按钮
                }, function(){
                    $.ajax({
                        url : appPath + '/super/project_create/public/delete_project_company',
                        dataType : 'json',
                        async : false,
                        type : 'post',
                        data : {
                            'projectId' : $("#projectId").val(),
                            'companyIds' : company_ids.join(',') 		    //要删除的单位id
                        },
                        success : function(data) {
                            var result = data.code;
                            if (10000 == result) {
                                layer.alert('操作成功', {icon : 1},function (index) {
                                    layer.close(index);
                                    //刷新课程列表
                                    _this.initTable_company();
                                })
                            } else {
                                layer.alert('操作失敗', {icon : 2});
                            }
                        }
                    });
                }, function(){
                    //取消
                });
            }

        });

        // 搜索单位
        $("#btn_company_search").click(function() {
            _this.initTable_company();
        });

        // 全部单位
        $("#btn_company_all").click(function() {
            $("#company_name").val("");
            _this.initTable_company();
        });
        // 回到首页
        $("#index").click(function() {
            window.location.href = appPath+"/super/project_create/public/create_project?step="+'0';
        });
        // 点击上一步
        $("#step_2").click(function() {
            var param = "?projectId="+projectId+"&projectTypeNo="+projectTypeNo+"&step="+'2';
            window.location.href = appPath+"/super/project_create/public/create_project"+param;
        });

        // 点击下一步
        $("#step_4").click(function() {
            var param = "?projectId="+projectId+"&projectTypeNo="+projectTypeNo+"&step="+'4';
            window.location.href = appPath+"/super/project_create/public/create_project"+param;
        });
    }

    //单位列表
    _this.initTable_company = function() {
        var list_url = appPath + "/super/project_create/public/company_list";
        page.init("company_form", list_url, "company_list", "company_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function(dataList){
            var inner = "", item;
            _this.obj = dataList;
            var len = dataList.length;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td class="pro_num">' + (i+1) + '</td>';
                inner += '<td><input type=\"checkbox\" name="companyBox" data-id="' + item['intId'] + '"></td>';
                inner += '<td class="pro_name">'+ item['varName'] + '</td>';
                inner += '</tr>';
            }
            return inner;
        }

    };

    //判断字符串是否包含某字符
    _this.isContains = function(str,substr){
        return new RegExp(substr).test(str);
    }

    //关闭对话框
    _this.closeDialog =function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }

}

var step_3 = new Step_3();