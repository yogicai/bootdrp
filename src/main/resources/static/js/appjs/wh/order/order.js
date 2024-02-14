let prefix = "/wh/order";
let tableGrid;
let $dataForm;
let $tableList;

let colNames_RK = ['单据日期', '编号', '店铺', '类型', '业务类型', '供应商', '入库数量', '入库金额', '审核状态', '备注', '创建时间', '修改时间'];
let colNames_CK = ['单据日期', '编号', '店铺', '类型', '业务类型', '供应商', '出库数量', '出库成本', '审核状态', '备注', '创建时间', '修改时间'];
let billType = $('#billType').val();
let colNames = billType === 'WH_RK_ORDER' ? colNames_RK : colNames_CK;
let dataUrl = billType === 'WH_RK_ORDER' ? '/wh/entry?billType=WH_RK_ORDER' : '/wh/entry?billType=WH_CK_ORDER';
let data_type = billType === 'WH_RK_ORDER' ? 'data_wh_rk' : 'data_wh_ck';

$(function() {
    $dataForm = $('#search');
    $tableList = $("#table_list");

    utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());
    if (billType === 'WH_RK_ORDER') {
        utils.loadTypes(["data_wh_rk"], ["serviceType"], [{width: "100px"}]);
    } else {
        utils.loadTypes(["data_wh_ck"], ["serviceType"], [{width: "100px"}]);
    }
    utils.loadTypes(["data_shop"], ["shopNo"], [{width: "100px", noneSelectedText: '店铺', multiple: true}]);
    utils.loadEnumTypes(["AUDIT_STATUS"], ["auditStatus"], [{width: "100px"}]);

    load();
});
function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $tableList.jqGrid({
        url: prefix + "/list",
        datatype: "json",
        postData: $.extend({}, $dataForm.serializeObject(), {"billType": billType}),
        height: window.innerHeight - 180,
        autowidth: true,
        shrinkToFit: false,
        autoScroll: true,
        multiselect: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        colNames: colNames,
        colModel: [
            {name: 'billDate', index: 'billDate', editable: true, width: 80, sorttype: "date", formatter: "date", frozen: true},
            {name: 'billNo', index: 'billNo', editable: true, sorttype: "text", width: 200, frozen: true},
            {name: 'shopNo', index: 'shopNo', editable: true, sorttype: "text", width: 70, formatter: cellValue => utils.formatType(cellValue, 'data_shop')},
            {name: 'billType', index: 'billType', editable: true, sorttype: "text", width: 80, formatter: cellValue => utils.formatEnum(cellValue, 'BILL_TYPE')},
            {name: 'serviceType', index: 'serviceType', editable: true, sorttype: "text", width: 80, formatter: cellValue => utils.formatType(cellValue, data_type)},
            {name: 'debtorName', index: 'debtorName', editable: true, sorttype: "text", width: 90},
            {name: 'totalQty', index: 'totalQty', editable: true, width: 80, align: "right", sorttype: "float", formatter: "number"},
            {name: 'entryAmount', index: 'entryAmount', editable: true, width: 100, align: "right", sorttype: "float", formatter: "number"},
            {name: 'auditStatus', index: 'auditStatus', editable: true, sorttype: "text", width: 70, formatter: cellValue => utils.formatEnumS(cellValue, 'AUDIT_STATUS')},
            {name: 'remark', index: 'remark', editable: true, sorttype: "text", width: 140},
            {name: 'createTime', index: 'createTime', editable: true, width: 140},
            {name: 'updateTime', index: 'updateTime', editable: true, width: 140}
        ],
        pager: "#pager_list",
        viewrecords: true,
        ondblClickRow: function (rowid, iRow, iCol, e) {
            let row = tableGrid.getRowData(rowid);
            doubleClick(dataUrl, row.billNo);
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
        tableGrid.setGridWidth(width);
        tableGrid.setGridHeight(window.innerHeight - 180);
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

function reLoad() {
    tableGrid.trigger("reloadGrid", { fromServer: true });
}

function audit(type) {
    let ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length === 0) {
        layer.msg("请选择要" + type === 0 ? "审核" : "反审核" + "的数据");
        return;
    }

    let selectData = [];
    $(ids).each(function (index, id){
        let row = tableGrid.jqGrid('getRowData', id);
        selectData.push(row.billNo)
    });

    $.ajax({
        url : prefix+"/audit",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data : JSON.stringify({ 'billNos' : selectData,  'auditStatus' : type === 0 ? 'YES' : 'NO' }),
        success : function(r) {
            if (r.code===0) {
                layer.msg(r.msg);
                reLoad();
            }else{
                layer.msg(r.msg);
            }
        }
    });
}

function remove() {
    let ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length === 0) {
        layer.msg("请选择要删除的数据");
        return;
    }

    let selectData = [];
    $(ids).each(function (index, id){
        let row = tableGrid.jqGrid('getRowData', id);
        selectData.push(row.billNo)
    });

    layer.confirm('确定要删除选中的记录？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix+"/remove",
            type : "post",
            data: {'billNos': selectData},
            success : function(r) {
                if (r.code===0) {
                    layer.msg(r.msg);
                    reLoad();
                }else{
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function add() {
    triggerMenu(dataUrl);
}

function doubleClick(dataUrl, billNo) {
    triggerMenu(dataUrl, billNo);
}

function triggerMenu(dataUrl, billNo) {
    let dataIndex;
    //触发菜单单击
    window.parent.$(".J_menuItem").each(function (index) {
        if ($(this).attr('href') === dataUrl) {
            window.parent.$(this).trigger('click');
            dataIndex = window.parent.$(this).data('index');
            return false;
        }
    });
    //加载订单数据
    window.parent.$('.J_mainContent .J_iframe').each(function () {
        if ($(this).data('id') === dataUrl) {
            let win = window.parent.$('iframe[name="iframe' + dataIndex +'"]')[0].contentWindow;
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
    let queryParam = $dataForm.serialize();
    let url = prefix + "/export?" + queryParam //下载地址
    utils.downloadAjax(url, 'WHOrderResult.xls');
}