/**
 * 单位行业树
 * @type {Object}
 */
var businessType = new Object();

//初始化
businessType.init = function () {
    businessType.initTree();
}

businessType.tree_options = {
    id: 'tree',
    url: appPath + '/super/business/companyBusiness_tree',
    setting: {
      view: {
        dblClickExpand: false,
        fontCss: getFontCss
      },
      callback: {
        onClick: function(e,treeId, treeNode){
            businessType.onClick(e,treeId, treeNode);
        }
      }
    },
    zNodes: '',
    rMenu: ''
};
//初始化树
businessType.initTree = function(){
    $.ajax({
      url: businessType.tree_options.url,
      async: false,
      type: 'post',
      data: {},
      success: function(data){
        businessType.tree_options.zNodes = eval(data.result);
        // 加载树
        $.fn.zTree.init($("#"+businessType.tree_options.id), businessType.tree_options.setting, businessType.tree_options.zNodes);
      }
    });
};

//点击
businessType.onClick = function(e,treeId, treeNode) {
    $("#businessId").val(treeNode.id);
    $("#company_business").val(treeNode.name);
    $(".tree_box").hide();
};



/**
 * 单位类型树
 * @type {Object}
 */
var categoryType = new Object();

//初始化
categoryType.init = function () {
  categoryType.initTree();
}

categoryType.tree_options = {
  id: 'tree',
  url: appPath + '/super/category/companyCategory_tree',
  setting: {
    view: {
      dblClickExpand: false,
      fontCss: getFontCss
    },
    callback: {
      onClick: function(e,treeId, treeNode){
        categoryType.onClick(e,treeId, treeNode);
      }
    }
  },
  zNodes: '',
  rMenu: ''
};
//初始化树
categoryType.initTree = function(){
  $.ajax({
    url: categoryType.tree_options.url,
    async: false,
    type: 'post',
    data: {},
    success: function(data){
      categoryType.tree_options.zNodes = eval(data.result);
      // 加载树
      $.fn.zTree.init($("#"+categoryType.tree_options.id), categoryType.tree_options.setting, categoryType.tree_options.zNodes);
    }
  });
};

//点击
categoryType.onClick = function(e,treeId, treeNode) {
  $("#categoryId").val(treeNode.id);
  $("#company_category").val(treeNode.name);
  $(".tree_box").hide();
};
