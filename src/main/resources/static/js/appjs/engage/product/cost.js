let prefix = "/engage/product/cost";
let tableGrid;

$(function() {
    load();
    utils.loadEnumTypes(["COST_VERSION"], ["version"], [{width:"120px"}]);
    utils.loadCategory(["PRODUCT"], ["productType"], [{width:"180px"}]);
});

function load() {
    $('.input-daterange').datepicker({
        language: "zh-CN",
        todayBtn: "linked",
        autoclose: true,
        todayHighlight: true
    });

    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/list",
        datatype: "json",
        postData: {"version" : "CURRENT"},
        height: window.innerHeight - 170,
        autowidth: true,
        shrinkToFit: true,
        multiselect: true,
        rowNum: 50,
        rowList: [10, 20, 50, 100],
        colNames: ['', '商品编号', '商品名称', '商品类别', '采购价', '成本单价', '成本数量', '商品余额', '成本', '成本时间', '类型', '关联单号', '创建时间', '修改时间'],
        colModel: [
            { name:'id', index:'id', editable:false, width:50, hidden:true },
            { name:'productNo', index:'productNo', editable:false, width:40 },
            { name:'productName', index:'productName', editable:false, width:100 },
            { name:'productType', index:'productType', editable:false, width:45, formatter : function (value,row,index){ return utils.formatCategory(value, "PRODUCT") } },
            { name:'entryPrice', index:'entryPrice', editable:false, width:45, align:"right", formatter:"number" },
            { name:'costPrice', index:'costPrice', editable:false, width:45, align:"right", formatter:"number" },
            { name:'costQty', index:'costQty', editable:false, width:45, align:"right", formatter:"number" },
            { name:'costBalance', index:'costBalance', editable:false, width:50, align:"right", formatter:"number" },
            { name:'costAmount', index:'costAmount', editable:false, width:50, align:"right", formatter:"number" },
            { name:'costDate', index:'costDate', editable:false, width:100 },
            { name:'costType', index:'costType', editable:false, width:60, formatter:function (cellValue){return utils.formatEnum(cellValue, 'COST_TYPE')} },
            { name:'relateNo', index:'relateNo', editable:false, width:100 },
            { name:'remark', index:'remark', editable:false, width:100 },
            { name:'updateTime', index:'updateTime', editable:false,  width:100 }
        ],
        pager: "#pager_list",
        viewrecords: true,
        beforeSelectRow: function (rowid) {
            let selrow = tableGrid.jqGrid('getGridParam','selrow');
            tableGrid.jqGrid('resetSelection');
            return selrow !== rowid;
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
    let postData = $.extend({}, $('#search').serializeObject(), { 'page': inputPage, 'rows': rowNum });
    tableGrid.jqGrid('setGridParam', {postData:  $.param(postData)}).trigger("reloadGrid");
}

function reLoad(type) {
    let postData = $.extend({}, $('#search').serializeObject(), {'page': 1, 'rows': tableGrid.jqGrid('getGridParam', 'rowNum')});
    tableGrid.jqGrid('setGridParam', {postData:  $.param(postData)}).trigger("reloadGrid");
}

function adjust(id) {
    let ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length != 1) {
        layer.msg(ids.length == 0 ? "请选择要修改的记录" : "一次只能修改一条记录");
        return;
    }
    layer.open({
        type : 2,
        title : '成本调整',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '480px' ],
        content : prefix + '/adjust/' + ids[0] // iframe的url
    });
}
