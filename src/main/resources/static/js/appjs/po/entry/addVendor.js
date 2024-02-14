let prefix = "/data/vendor";
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
        colNames: ['', '供应商编号', '供应商名称', '供应商类别', '联系人', '联系人电话', '供应商地址', '状态'],
        colModel: [
            { name:'rowId', index:'', hidden: true, key: true, frozen : true },
            { name:'no', index:'no', editable:false, key: false, width:70, frozen : true },
            { name:'name', index:'name', editable:false, width:150},
            { name:'type', index:'type', editable:false, width:70, formatter : function (value,row,index){ return utils.formatCategory(value, "VENDOR")}},
            { name:'contacts', index:'contacts', editable:false, width:60 },
            { name:'phone', index:'phone', editable:false, width:80 },
            { name:'address', index:'address', editable:false, width:150 },
            { name:'status', index:'status', editable:false,  width:50, formatter:utils.formatYN }
        ],
        height: 350,
        autowidth: true,
        shrinkToFit: true,
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
        tableGrid.setGridWidth(width);
    });
}

//刷新表格数据
function reLoad() {
    tableGrid.jqGrid('setGridParam', {postData: {"searchText": $searchText.val()}}).trigger("reloadGrid");
}

//添加商品到订单分录
function add() {
    let id = tableGrid.jqGrid('getGridParam', 'selrow');
    let row = tableGrid.jqGrid('getRowData', id);
    window.parent.insertHead(row);
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

//类目树加载
function getTreeData() {
    $.ajax({
        type: "GET",
        data: {"type": "VENDOR"},
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