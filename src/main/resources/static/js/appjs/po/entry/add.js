let prefix = "/data/product";
let tableGrid;
let $tableList;
let $searchText;
let $jsTree;

let shopNo = window.parent.$('#shopNo').val();

$(function() {
    $searchText = $('#searchText');
    $tableList = $('#table_list');
    $jsTree = $('#jstree');

    getTreeData();
    load();
    bindEvent();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $tableList.jqGrid({
        url: prefix + "/listJQ",
        datatype: "json",
        postData: {status: 1, shopNo: shopNo},
        colNames: ['', '商品编号', '商品名称', '库存', '成本价', '采购价', '零售价', '条形码', '类别', '品牌', '单位', '仓库', '仓库编号', '状态'],
        colModel: [
            { name:'rowId', index:'', hidden: true, key: true, frozen : true },
            { name:'no', index:'no', editable:false, key: false, width:70, frozen : true },
            { name:'name', index:'name', editable:false, width:150},
            { name:'costQty', index:'costQty', editable:false, width:70, align:"right", formatter:"number" },
            { name:'costPrice', index:'costPrice', editable:false, width:80, align:"right", formatter:"number" },
            { name:'purchasePrice', index:'purchasePrice', editable:false, width:80, align:"right", formatter:"number"},
            { name:'salePrice', index:'salePrice', editable:false, width:80, align:"right", formatter:"number" },
            { name:'barCode', index:'barCode', editable:false, width:150, hidden: true},
            { name:'type', index:'type', editable:false, width:60, formatter : function (value,row,index){ return utils.formatCategory(value, "PRODUCT")} },
            { name:'brand', index:'brand', editable:false, width:60, formatter : function (value,row,index){ return utils.formatType(value, "data_brand")} },
            { name:'unit', index:'unit', editable:false, width:40, formatter : function (value,row,index){ return utils.formatType(value, "data_unit")} },
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
        let width = $('.jqGrid_wrapper').width();
        $tableList.setGridWidth(width);
        $tableList.setGridHeight(window.innerHeight - 180);
    });
}

//刷新表格数据
function reLoad() {
    tableGrid.jqGrid('setGridParam', {postData: {"searchText": $('#searchText').val()}}).trigger("reloadGrid");
}

//添加商品到订单分录
function add() {
    let selectData = [];
    let ids = tableGrid.jqGrid('getGridParam', 'selarrrow');
    $(ids).each(function (index, id){
        let row = tableGrid.jqGrid('getRowData', id);
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
    let index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
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
        type: "GET",
        data: {"type": "PRODUCT"},
        url: "/data/category/tree",
        success: function (tree) {
            loadTree(tree);
        }
    });
}
function loadTree(tree) {
    $jsTree.jstree({
        "core": { 'data' : tree },
        "plugins": [ "search" ]
    });

    $jsTree.on("changed.jstree", function (e, data) {
        if (_.isEmpty(data.selected) || data.selected[0] === '-1') {
            tableGrid.jqGrid('setGridParam', {postData: {"type": ""}}).trigger("reloadGrid");
        } else {
            tableGrid.jqGrid('setGridParam', {postData: {"type": data.selected[0]}}).trigger("reloadGrid");
        }
    });

    $jsTree.jstree().open_all();
}

//绑定事件
function bindEvent() {
    let timeoutID;
    $searchText.bind('keyup', function () {
        clearTimeout(timeoutID);
        timeoutID= window.setTimeout(function(){
            reLoad();
        }, 1000);
    });
}