var prefix = "/data/product";
var prefixCategory = "/data/category";
var tableGrid;

$(function() {
    getTreeData();
    load();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/listCost",
        datatype: "json",
        postData: { "productNo": 1 },
        colNames: ['', '商品编号', '采购价', '成本单价', '成本数量', '商品余额', '成本', '成本时间', '类型', '关联单号', '创建时间', '修改时间'],
        colModel: [
            { name:'id', index:'id', editable:false, width:50, hidden:true },
            { name:'productNo', index:'productNo', editable:false, width:60 },
            { name:'entryPrice', index:'entryPrice', editable:false, width:60, formatter:"number" },
            { name:'costPrice', index:'costPrice', editable:false, width:60, formatter:"number" },
            { name:'costQty', index:'costQty', editable:false, width:70, formatter:"number" },
            { name:'costBalance', index:'costBalance', editable:false, width:70, formatter:"number" },
            { name:'costAmount', index:'costAmount', editable:false, width:80, formatter:"number" },
            { name:'costDate', index:'costDate', editable:false, width:100 },
            { name:'costType', index:'costType', editable:false, width:70, formatter:function (cellValue){return utils.formatEnum(cellValue, 'COST_TYPE')} },
            { name:'relateNo', index:'relateNo', editable:false, width:120 },
            { name:'createTime', index:'createTime', editable:false, width:100 },
            { name:'updateTime', index:'updateTime', editable:false,  width:100 }
        ],
        height: window.innerHeight - 120,
        autowidth: true,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 100,
        rowList: [20, 50, 100],
        pager: "#pager_list",
        viewrecords: true
    });

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

    $('#jsTreeDiv').css({"overflow-x":"hidden", "overflow-y":"scroll", "height":window.innerHeight - 60});

    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#table_list').setGridWidth(width);
        $('#table_list').setGridHeight(window.innerHeight - 120);
        $('#jsTreeDiv').css({"overflow-x":"hidden", "overflow-y":"scroll", "height":window.innerHeight - 60});
    });
}

function search(pageBtn) {
    var inputPage = 1;
    var rowNum = tableGrid.getGridParam('rowNum');//获取每页数
    var curPage = tableGrid.getGridParam('page');//获取返回的当前页
    var totalPage = tableGrid.getGridParam('lastpage');//获取总页数
    if (pageBtn == 'first') {
        inputPage = 0;
    } else if (pageBtn == 'last') {
        inputPage = totalPage;
    } else if (pageBtn == 'prev') {
        inputPage = curPage - 1;
    } else if (pageBtn == 'next') {
        inputPage = curPage + 1;
    } else if (pageBtn == 'user') {
        inputPage = $('.ui-pg-input').val();//输入框页数
    } else if (pageBtn == 'records') {
        rowNum = $('.ui-pg-selbox').val();//输入框页数
    }
    inputPage = inputPage > totalPage ? totalPage : inputPage;
    inputPage = inputPage < 1 ? 1 : inputPage;
    var postData = $.extend({}, { "searchName": $('#searchName').val(), 'page': inputPage, 'rows': rowNum });
    tableGrid.jqGrid('setGridParam', {postData: $.param(postData)}).trigger("reloadGrid");
}

//刷新表格数据
function reLoad() {
    tableGrid.trigger("reloadGrid", { fromServer: true });
}

//类目树加载
function getTreeData() {
    $('#jstree').jstree({
        "core": {
            'data' : {
                "url" : prefixCategory + "/productTree",
                "data" : function (node) {
                    return { "id" : node.id, type: "PRODUCT" };
                }
            }
        },
        "plugins" : ["search"]
    });
}

//树搜索框
$("#s").submit(function(e) {
    e.preventDefault();
    $("#jstree").jstree(true).search($("#q").val());
});

$('#jstree').on("changed.jstree", function(e, data) {
    if (data.node.original.leaf) {
        tableGrid.jqGrid('setGridParam', { postData: {"productNo" : data.selected[0]} }).trigger("reloadGrid");
    }
});
