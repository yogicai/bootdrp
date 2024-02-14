let prefix = "/engage/product/cost";
let tableGrid;
let $dataForm;
let $tableList;
let currentRow = {};

$(function() {
    $dataForm = $('#search');
    $tableList = $("#table_list");

    utils.loadTypes(["data_shop"], ["shopNo"], [{width: "100px"}]);
    utils.loadEnumTypes(["COST_VERSION", "COST_TYPE"], ["version", "costType"], [{width:"120px"}, {width:"120px"}]);
    utils.loadCategory(["PRODUCT"], ["productType"], [{width:"120px"}]);

    load();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $tableList.jqGrid({
        url: prefix + "/list",
        datatype: "json",
        postData: {"version" : "CURRENT"},
        height: window.innerHeight - 170,
        autowidth: true,
        shrinkToFit: true,
        multiselect: true, //自带多选
        multiboxonly: true, //变成单选
        rownumbers: true,
        rowNum: 50,
        rowList: [10, 20, 50, 100],
        colNames: ['', '店铺', '商品编号', '商品名称', '商品类别', '采购价', '成本单价', '库存数量', '库存变更', '库存余额', '成本', '成本时间', '类型', '关联单号', '备注', '修改时间'],
        colModel: [
            { name:'id', index:'id', editable:false, width:50, hidden:true },
            {name: 'shopNo', index: 'shopNo', editable: false, align: "center", width: 70, formatter: cellValue => utils.formatType(cellValue, 'data_shop')},
            {name: 'productNo', index: 'productNo', editable: false, width: 70},
            { name:'productName', index:'productName', editable:false, width:160 },
            { name:'productType', index:'productType', editable:false, width:70, formatter : function (value,row,index){ return utils.formatCategory(value, "PRODUCT") } },
            { name:'entryPrice', index:'entryPrice', editable:false, width:70, align:"right", formatter:"number" },
            { name:'costPrice', index:'costPrice', editable:false, width:70, align:"right", formatter:"number" },
            { name:'costQty', index:'costQty', editable:false, width:70, align:"right", formatter:"number" },
            { name:'entryQty', index:'entryQty', editable:false, width:70, align:"right", formatter:"number" },
            { name:'costBalance', index:'costBalance', editable:false, width:80, align:"right", formatter:"number" },
            { name:'costAmount', index:'costAmount', editable:false, width:80, align:"right", formatter:"number" },
            { name:'costDate', index:'costDate', editable:false, width:140, hidden: true },
            { name:'costType', index:'costType', editable:false, width:80, formatter:function (cellValue){return utils.formatEnum(cellValue, 'COST_TYPE')}, unformat: function (cellValue) { return utils.unformatEnum(cellValue, 'COST_TYPE') } },
            { name:'relateNo', index:'relateNo', editable:false, width:180 },
            { name:'remark', index:'remark', editable:false, width:130, formatter:function (cellValue){return utils.changeRowCellCss(cellValue, '反审核')} },
            { name:'updateTime', index:'updateTime', editable:false, width:140 }
        ],
        pager: "#pager_list",
        viewrecords: true,
        beforeSelectRow: function (rowid) {

        },
        ondblClickRow: function (rowid, iRow, iCol, e) {
            currentRow = tableGrid.jqGrid("getRowData", rowid);
            searchCost(currentRow);
        },
        loadComplete: function (data) {
            utils.changeRowCss(tableGrid, "costType", "MANUAL");
            utils.changeRowCss(tableGrid, "remark", "反审核");
        },
        onPaging:search
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
        $('#table_list').setGridWidth(width);
    });
}

function search(pageBtn) {
    let inputPage = 1;
    let rowNum = tableGrid.getGridParam('rowNum');//获取每页数
    let curPage = tableGrid.getGridParam('page');//获取返回的当前页
    let totalPage = tableGrid.getGridParam('lastpage');//获取总页数
    if (pageBtn === 'first') {
        inputPage = 0;
    } else if (pageBtn === 'last') {
        inputPage = totalPage;
    } else if (pageBtn === 'prev') {
        inputPage = curPage - 1;
    } else if (pageBtn === 'next') {
        inputPage = curPage + 1;
    } else if (pageBtn === 'user') {
        inputPage = $('.ui-pg-input').val();//输入框页数
    } else if (pageBtn === 'records') {
        rowNum = $('.ui-pg-selbox').val();//输入框页数
    }
    inputPage = inputPage > totalPage ? totalPage : inputPage;
    inputPage = inputPage < 1 ? 1 : inputPage;
    let postData = $.extend({}, $dataForm.serializeObject(), {'page': inputPage, 'rows': rowNum});
    tableGrid.jqGrid('setGridParam', {postData:  $.param(postData)}).trigger("reloadGrid");
}

function reLoad(type) {
    let postData = $.extend({}, $dataForm.serializeObject(), {'page': 1, 'rows': tableGrid.jqGrid('getGridParam', 'rowNum')});
    tableGrid.jqGrid('setGridParam', {postData:  $.param(postData)}).trigger("reloadGrid");
}

function adjust() {
    //未启用复选框（即单选），用selrow获取行号；启用复选框，用selarrrow获取行号数组
    let rowid = tableGrid.jqGrid("getGridParam", "selrow");
    if (!rowid) {
        layer.msg("请选择要修改的记录");
        return;
    }
    layer.open({
        type : 2,
        title : '成本调整',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '480px' ],
        content : prefix + '/adjust/' + rowid // iframe的url
    });
}

function searchCost(rowData) {
    layer.open({
        type : 2,
        title : '商品成本',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1300px', '650px' ],
        content: prefix + '/productCostB' // iframe的url
    });
}

function getCurrentRow() {
    return currentRow || {};
}

function exportExcel() {
    let queryParam = $dataForm.serialize();
    let url = prefix + "/export?" + queryParam //下载地址
    utils.downloadAjax(url, 'ProductCostResult.xls')
}
