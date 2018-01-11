/**
 * Created by Administrator on 2017-07-31.
 */
function SelectCompany() {
    var _this = this;
    var page;
    var companyIds = [];
    _this.obj = null;
    _this.templateArr = [];
    _this.page_size = 10;

    // init
    _this.init = function(_page) {
        page = _page;
        _this.initTable();
        _this.initEvent();
    }

    _this.initTable = function() {
        var list_url = appPath + "/admin/companyManager/company_list";
        page.init("select_company_form", list_url, "select_company_list", "select_company_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function(dataList){
            var inner = "", item;
            if(dataList){
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
            }else{
                inner += '<tr>';
                inner += '<td colspan="4" >暂无数据</td>';
                inner += '</tr>';
            }
            return inner;
        }

    };

    // 确定(保存项目单位)
    _this.initEvent = function () {
        // 确定
        $("#btn_sure").click(function () {
            var company = _this.templateArr;
            for ( var i = 0; i < company.length; i++) {
                companyIds.push(company[i].intId)
            }
            // 完成单位添加
            $.ajax({
                url : appPath + '/admin/project_create/public/save_public_company',
                dataType : 'json',
                async : false,
                type : 'post',
                data : {
                    "company_project_id" : $("#projectId").val(),
                    "company_ids" : companyIds.toString(),
                    "datBeginTime" : $("#projectId").val(),
                    "datEndTime" : $("#projectId").val(),
                },
                success : function (data) {
                    var result = data.code;
                    if (10000 == result) {
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
                        layer.msg('操作成功',{time:1000});
                        if('1' == $("#type").val()){
                            parent.createPublicProject.initTable_company();
                        }else{
                            parent.updatePublicProject.initTable_company();
                        }
                    } else {
                        layer.alert('操作失敗');
                    }
                }
            });
        });

        //取消
        $("#btn_cancel").click(function () {
            _this.closeDialog();
        });

        //搜索
        $("#submit_search").click(function () {
            _this.initTable();
        });

        //全部
        $("#company_all").click(function () {
            $("#company_name").val("");
            _this.initTable();
        });

        //选择单位
        $('#select_company_list').on('change','input',function(){
            var $this = $(this);
            var flag = $this.prop('checked');
            var id = $this.attr('data-id');
            var index = _this.getIndex(_this.obj, id);
            var template_index = _this.getIndex(_this.templateArr, id);
            if( flag && index != -1  ){

                _this.templateArr.push(_this.obj[index]);

            }else if( !flag && template_index != -1 ){

                _this.templateArr.splice( template_index,1 );

            }
            _this.select_after();
        })
    };

    //关闭对话框
    _this.closeDialog =function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }


    //获取id索引
    _this.getIndex = function(obj, attr){
        var index = -1;
        $.each(obj,function(i, n){
            if( this.intId == attr ){
                index = i;
                return false;
            }
        })
        return index;
    }
    //选择单位后执行
    _this.select_after = function(){
        var tbody = '';
        $.each(_this.templateArr,function(i, n){
            tbody += '<tr >';
            tbody += '<td>' + (i+1) + '</td>';
            tbody += '<td>'+ this.varName + '</td>';
            tbody += '</tr>';
        })
        $('#select_company_list_copy').html( tbody );

    }

    //全选
    _this.checkAll=function () {
        var flag=$("#checkAll").prop("checked");
        $('tbody input[name="companyBox"]').prop("checked",flag);
        if(flag && _this.obj.length>0){
            $.each(_this.obj,function(i, n){
                _this.removeRepart(this);
            })
        }else if( !flag ){
            $.each(_this.obj,function(i, n){
                var id = this.intId;
                var index = _this.getIndex(_this.obj, id);
                var template_index = _this.getIndex(_this.templateArr, id);
                _this.templateArr.splice( template_index,1 );
            })

        }
        _this.select_after();
    }

    //去重
    _this.removeRepart=function (companyData) {
        var flag=true;
        for (var i=0;i<_this.templateArr.length;i++){
            if(_this.templateArr[i].intId==companyData.intId){
                flag=false;
            }
        }
        if(flag){
            _this.templateArr.push(companyData) ;
        }
    }
}

var selectCompany = new SelectCompany();