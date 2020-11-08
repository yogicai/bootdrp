var prefix = "/rp/point";
var tableGrid;
var searchType='DETAIL';
$(function() {
    load();
    utils.loadEnumTypes(["POINT_STATUS"], ["status"], [{width:"120px"}]);

    utils.loadCategory(["CUSTOMER_DATA"], ["consumerId"], [{width:"120px"}]);
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
        height: window.innerHeight - 170,
        autowidth: true,
        shrinkToFit: true,
        multiselect: true,
        rowNum: 50,
        rowList: [10, 20, 50, 100],
        colNames: ['客户ID', '客户名称', '积分', '积分来源', '关联单号', '订单金额', '状态', '备注', '日期'],
        colModel: [
            { name:'consumerId', index:'consumerId', editable:true, sorttype:"text", width:60 },
            { name:'consumerName', index:'consumerName', editable:true, sorttype:"text", width:90 },
            { name:'point', index:'point', editable:true, width:80, align:"right", sorttype:"int", formatter:"number" },
            { name:'source', index:'source', editable:true, sorttype:"text", width:80, formatter:function (cellValue){return utils.formatEnum(cellValue, 'POINT_SOURCE')} },
            { name:'relateNo', index:'relateNo', editable:true, sorttype:"text", width:120 },
            { name:'totalAmount', index:'totalAmount', editable:true, sorttype:"text", width:90, formatter:"number" },
            { name:'status', index:'status', editable:true, sorttype:"text", width:90, formatter:function (cellValue){return utils.formatEnum(cellValue, 'POINT_STATUS')} },
            { name:'remark', index:'remark', editable:true, sorttype:"text", width:120 },
            { name:'createTime', index:'createTime', editable:true, width:90 }
        ],
        pager: "#pager_list",
        viewrecords: true,
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
        var width = $('.jqGrid_wrapper').width();
        $('#table_list').setGridWidth(width);
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
    var postData = $.extend({type: searchType}, $('#search').serializeObject(), { 'page': inputPage, 'rows': rowNum });
    tableGrid.jqGrid('setGridParam', {postData:  $.param(postData)}).trigger("reloadGrid");
}

function reLoad(type) {
    searchType = type ? type : searchType;
    var postData = $.extend({type: type}, $('#search').serializeObject(), {'page': 1, 'rows': tableGrid.jqGrid('getGridParam', 'rowNum')});
    tableGrid.jqGrid('setGridParam', {postData:  $.param(postData)}).trigger("reloadGrid");
}

function add() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/add/' // iframe的url
    });
}
function edit(id) {
    if (searchType == 'COLLECT') {
        layer.msg("汇总记录无法修改!");
        return;
    }
    var ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length != 1) {
        layer.msg(ids.length == 0 ? "请选择要修改的记录" : "一次只能修改一条记录");
        return;
    }
    layer.open({
        type : 2,
        title : '编辑',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/edit/' + ids[0] // iframe的url
    });
}

function remove() {
    if (searchType == 'COLLECT') {
        layer.msg("汇总记录无法删除!");
        return;
    }
    var ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm('确定要删除选中的记录？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix+"/remove",
            type : "post",
            data : {
                'ids' : ids
            },
            success : function(r) {
                if (r.code==0) {
                    layer.msg(r.msg);
                    reLoad();
                }else{
                    layer.msg(r.msg);
                }
            }
        });
    })
}