var prefix = "/data/product";
var prefixCategory = "/data/category";
var tableGrid;
$(function() {
    getTreeData();
    load();
    bindEvent();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/listJQ",
        datatype: "json",
        postData: { "status": 1 },
        colNames: ['', '商品编号', '商品名称', '库存', '条形码', '类别', '品牌', '单位', '采购价', '零售价', '仓库', '仓库编号', '状态'],
        colModel: [
            { name:'rowId', index:'', hidden: true, key: true, frozen : true },
            { name:'no', index:'no', editable:false, key: false, width:70, frozen : true },
            { name:'name', index:'name', editable:false, width:150},
            { name:'costQty', index:'costQty', editable:false, width:70, align:"right", formatter:"number" },
            { name:'barCode', index:'barCode', editable:false, width:150 },
            { name:'type', index:'type', editable:false, width:60, formatter : function (value,row,index){ return utils.formatCategory(value, "PRODUCT")} },
            { name:'brand', index:'brand', editable:false, width:60, formatter : function (value,row,index){ return utils.formatType(value, "data_brand")} },
            { name:'unit', index:'unit', editable:false, width:40, formatter : function (value,row,index){ return utils.formatType(value, "data_unit")} },
            { name:'purchasePrice', index:'purchasePrice', editable:false, width:80, align:"right", formatter:"number" },
            { name:'salePrice', index:'salePrice', editable:false, width:80, align:"right", formatter:"number" },
            { name:'stockNo', index:'stockNo', editable:false, width:80, formatter : function (value,row,index){ return utils.formatType(value, "data_stock")} },
            { name:'stockNo', index:'stockNo', editable:false, width:80, hidden:true },
            { name:'status', index:'status', editable:false, width:50, formatter:utils.formatYN }
        ],
        height: window.innerHeight - 180,
        autowidth: true,
        shrinkToFit: true,
        multiselect: true,
        rowNum: 100,
        rowList: [100],
        pager: "#pager_list",
        viewrecords: true
    });
    // Setup buttons
    tableGrid.jqGrid('navGrid', '#pager_list', {
        edit: false,
        add: false,
        search: false,
        del: false,
        refresh: true
    }, {
        height: 200,
        reloadAfterSubmit: true
    });

    // Add responsive to jqGrid
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#table_list').setGridWidth(width);
        $('#table_list').setGridHeight(window.innerHeight - 180);
    });
}

//刷新表格数据
function reLoad() {
    tableGrid.jqGrid('setGridParam', {postData: {"searchText": $('#searchText').val()}}).trigger("reloadGrid");
}

//添加商品到订单分录
function add() {
    var selectData = new Array();
    var ids = tableGrid.jqGrid('getGridParam', 'selarrrow');
    $(ids).each(function (index, id){
        var row = tableGrid.jqGrid('getRowData', id);
        selectData.push({ "entryName": row.name,"entryId": row.no,"entryUnit": row.unit,"totalQty": 1,"entryPrice": row.purchasePrice,"stockNo": row.stockNo,"entryAmount":row.purchasePrice,"totalAmount":row.purchasePrice })
    });
    if (selectData.length > 0) {
        window.parent.insertData(selectData);
        tableGrid.jqGrid('resetSelection');
    }
}

//添加商品到订单分录并关闭窗口
function addAndClose() {
    add();
    cancel();
}

//关闭窗口
function cancel() {
    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
    parent.layer.close(index);
}

//新加商品
function addProduct() {
    //TODO
    layer.msg("此功能还未实现。。。");
}

//类目树加载
function getTreeData() {
    $.ajax({
        type : "GET",
        data: { "type": "PRODUCT" },
        url : prefixCategory + "/tree",
        success : function(tree) {
            loadTree(tree);
        }
    });
}
function loadTree(tree) {
    $('#jstree').jstree({
        "core": { 'data' : tree },
        "plugins": [ "search" ]
    });
    $('#jstree').jstree().open_all();
}

$('#jstree').on("changed.jstree", function(e, data) {
    if (data.selected == -1) {
        tableGrid.jqGrid('setGridParam', {postData: {"type": ""} }).trigger("reloadGrid");
    } else {
        tableGrid.jqGrid('setGridParam', { postData: {"type" : data.selected[0]} }).trigger("reloadGrid");
    }
});

//绑定事件
function bindEvent() {
    var timeoutID;
    $('#searchText').bind('keyup', function () {
        clearTimeout(timeoutID);
        timeoutID= window.setTimeout(function(){
            reLoad();
        }, 1000);
    });
}