var prefix = "/po/order";
var dataForm;
var tableGrid;
$(function() {
    load();
    utils.loadEnumTypes(["ORDER_CG_STATUS","AUDIT_STATUS"], ["status","audit"], [{width:"105px"},{width:"105px"}]);
    utils.loadChosenStatic(["billType"], [{width:"105px"}]);
});

function load() {

    recordDate = utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());
    recordDateS= recordDate.data("datepicker").pickers[0];
    recordDateE= recordDate.data("datepicker").pickers[1];

    $.jgrid.defaults.styleUI = 'Bootstrap';

    dataForm  = $('#search');

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/list",
        datatype: "json",
        postData: $.extend({}, dataForm.serializeObject(), {'start' : recordDateS.getDate().format('yyyy-MM-dd'), 'end' : recordDateE.getDate().format('yyyy-MM-dd')}),
        height: window.innerHeight - 180,
        autowidth: true,
        shrinkToFit: false,
        autoScroll: true,
        multiselect: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        colNames: ['单据日期', '编号', '类型', '类型','供应商', '数量', '商品金额', '优惠率', '优惠金额', '采购费用', '已付金额', '优惠后商品金额', '合计金额', '状态', '审核状态', '结算帐户', '备注', '创建时间', '更新时间'],
        colModel: [
            { name:'billDate', index:'billDate', editable:true, width:80, sorttype:"date", formatter:"date", frozen: true },
            { name:'billNo', index:'billNo', editable:true, sorttype:"text", width:170, frozen: true },
            { name:'billType', index:'billType', editable:true, sorttype:"text", width:60, formatter:function (cellValue){return utils.formatEnum(cellValue, 'BILL_TYPE')} },
            { name:'billType', index:'billType', editable:true, sorttype:"text", width:80, hidden:true },
            { name:'vendorName', index:'vendorName', editable:true, sorttype:"text", width:80 },
            { name:'totalQty', index:'totalQty', editable:true, width:60, align:"right", sorttype:"int", formatter:"number" },
            { name:'entryAmount', index:'entryAmount', editable:true, width:80, align:"right", sorttype:"float", formatter:"number" },
            { name:'discountRate', index:'discountRate', editable:true, width:60, align:"right", sorttype:"float", formatter:"number", hidden: true },
            { name:'discountAmount', index:'discountAmount', editable:true, width:70, align:"right", sorttype:"float", formatter:"number" },
            { name:'purchaseFee', index:'purchaseFee', editable:true, width:70, align:"right", sorttype:"float", formatter:"number", hidden: true },
            { name:'paymentAmount', index:'paymentAmount', editable:true, width:80, align:"right", sorttype:"float", formatter:"number" },
            { name:'finalAmount', index:'finalAmount', editable:true, width:90, align:"right", sorttype:"float", formatter:"number" },
            { name:'totalAmount', index:'totalAmount', editable:true, width:80, align:"right", sorttype:"float", formatter:"number" },
            { name:'status', index:'status', editable:true, sorttype:"text", edittype:"select", width:70, formatter:function (cellValue){return utils.formatEnum(cellValue, 'ORDER_CG_STATUS')} },
            { name:'auditStatus', index:'auditStatus', editable:true, sorttype:"text", width:70, formatter:function (cellValue){return utils.formatEnumS(cellValue, 'AUDIT_STATUS')} },
            { name:'settleAccount', index:'settleAccount', editable:true, sorttype:"text", width:80, formatter:function (cellValue){return utils.formatCategory(cellValue, 'ACCOUNT_DATA')} },
            { name:'remark', index:'remark', editable:true, sorttype:"text", width:140 },
            { name:'createTime', index:'createTime', editable:true, width:140 },
            { name:'updateTime', index:'updateTime', editable:true, width:140 }
        ],
        pager: "#pager_list",
        viewrecords: true,
        ondblClickRow: function (rowid, iRow, iCol, e) {
            var row = tableGrid.getRowData(rowid);
            doubleClick('/po/entry?billType=' + row.billType, row.billNo);
        },
        loadComplete: function (data) {
            utils.changeRowCss(tableGrid, "status", "未结款,部分结款");
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
        var width = $('.jqGrid_wrapper').width();
        tableGrid.setGridWidth(width);
        tableGrid.setGridHeight(window.innerHeight - 180);
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
    var postData = $.extend({}, $('#search').serializeObject(), { 'page': inputPage, 'rows': rowNum });
    tableGrid.jqGrid('setGridParam', {postData: $.param(postData)}).trigger("reloadGrid");
}

function reLoad() {
    tableGrid.trigger("reloadGrid", { fromServer: true });
}

function audit(type) {
    var ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length == 0) {
        layer.msg("请选择要" + type == 0 ? "审核" : "反审核" + "的数据");
        return;
    }

    var selectData = new Array();
    $(ids).each(function (index, id){
        var row = tableGrid.jqGrid('getRowData', id);
        selectData.push(row.billNo)
    });

    $.ajax({
        url : prefix+"/audit",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data : JSON.stringify({ 'billNos' : selectData,  'auditStatus' : type == 0 ? 'YES' : 'NO' }),
        success : function(r) {
            if (r.code==0) {
                layer.msg(r.msg);
                reLoad();
            }else{
                layer.msg(r.msg);
            }
        }
    });
}

function remove() {
    var ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }

    var selectData = new Array();
    $(ids).each(function (index, id){
        var row = tableGrid.jqGrid('getRowData', id);
        selectData.push(row.billNo)
    });

    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'billNos': selectData
            },
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function add(dataUrl) {
    triggerMenu(dataUrl);
}

function doubleClick(dataUrl, billNo) {
    triggerMenu(dataUrl, billNo);
}

function triggerMenu(dataUrl, billNo) {
    var dataIndex;
    //触发菜单单击
    window.parent.$(".J_menuItem").each(function (index) {
        if ($(this).attr('href') == dataUrl) {
            window.parent.$(this).trigger('click');
            dataIndex = window.parent.$(this).data('index');
            return false;
        }
    });
    //加载订单数据
    window.parent.$('.J_mainContent .J_iframe').each(function () {
        if ($(this).data('id') == dataUrl) {
            var win = window.parent.$('iframe[name="iframe' + dataIndex +'"]')[0].contentWindow;
            if (win.initOrder) {
                win.initOrder(billNo);
            } else if (win.frameElement) {
                win.frameElement.onload = win.frameElement.onreadystatechange =function () {
                    win.initOrder(billNo);
                }
            }
            return false;
        }
    });
}

function exportExcel() {
    let queryParam = dataForm.serialize();
    let url = prefix + "/export?" + queryParam //下载地址
    utils.download(url ,'POOrderResult.xls');
}