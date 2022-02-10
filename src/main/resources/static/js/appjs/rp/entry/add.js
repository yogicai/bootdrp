var prefix = window.parent.billType == "CW_SK_ORDER" ? "/se/order" : "/po/order";
var tableGrid;
var debtorId = window.parent.$("#debtorId").val();
var postData = { "statusNot": ["FINISH_PAY", "ORDER_CANCEL"], "audit": "YES", "consumerId": debtorId, "vendorId": debtorId };
$(function() {
    load();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/list",
        mtype: "POST",
        datatype: "json",
        postData: postData,
        ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
        colNames: ['源单编号', '业务类型', '单据类型', '单据日期', '单据金额', '已核销金额', '未核销金额', '本次核销金额', '备注'],
        colModel: [
            { name:'billNo', index:'billNo', editable:false, sorttype:"text", width:150 },
            { name:'billType', index:'billType', editable:false, sorttype:"text", width:80, formatter:function (cellValue){return utils.formatEnum(cellValue, 'BILL_TYPE')} },
            { name:'billType', index:'billType', editable:false, sorttype:"text", width:80 },
            { name:'billDate', index:'billDate', editable:false, width:100, sorttype:"date", formatter:"date", frozen: true },
            { name:'totalAmount', index:'totalAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
            { name:'paymentAmount', index:'paymentAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
            { name:'unPaymentAmount', index:'unPaymentAmount', editable:false, width:80, align:"right", sorttype:"float", hidden:true, formatter:function (cellValue, option, rowdata){
                return (rowdata['totalAmount'] - rowdata['paymentAmount']).toFixed(2)
            } },
            { name:'checkAmount', index:'checkAmount', editable:false, width:80, align:"right", sorttype:"float", hidden:true, formatter:"number" },
            { name:'remark', index:'remark', editable:false, sorttype:"text", width:90 }
        ],
        height: 350,
        autowidth: true,
        shrinkToFit: true,
        multiselect: true,
        rowNum: 100,
        rowList: [100],
        pager: "#pager_list",
        viewrecords: true,
        footerrow: true,
        serializeGridData: function (postdata) {
            return JSON.stringify(postdata);
        },
        onSelectRow: function (rowid, e) {
            selectTotal();
        },
        loadComplete: function () {
            selectTotal();
        }
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
    tableGrid.jqGrid('setGridParam', {postData: $.extend({}, {"searchText": $('#searchText').val()}, postData) }).trigger("reloadGrid");
}

//添加商品到订单分录
function add() {
    var selectData = new Array();
    var ids = tableGrid.jqGrid('getGridParam', 'selarrrow');
    $(ids).each(function (index, id){
        var row = tableGrid.jqGrid('getRowData', id);
        selectData.push({ "srcBillNo": row.billNo,"srcBillType": row.billType,"srcBillDate": row.billDate,"srcTotalAmount": row.totalAmount,"srcPaymentAmount": row.paymentAmount,"srcUnPaymentAmount": (row.totalAmount - row.paymentAmount).toFixed(2),"checkAmount":(row.totalAmount - row.paymentAmount).toFixed(2) })
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

//计算表格合计行数据
function selectTotal() {
    let totalAmountTotal = 0.0;
    let paymentAmountTotal = 0.0;
    let unPaymentAmountTotal = 0.0;
    let ids = tableGrid.jqGrid('getGridParam', 'selarrrow');
    $(ids).each(function (index, id){
        let row = tableGrid.jqGrid('getRowData', id);
        totalAmountTotal = totalAmountTotal + (row['totalAmount']);
        paymentAmountTotal = paymentAmountTotal + (row['paymentAmount']);
        unPaymentAmountTotal = unPaymentAmountTotal + (row['totalAmount'] - row['paymentAmount']);
    });
    let totalAmountObj = { billDate: "<span style='color: red; font-size: medium' > 本次核销金额：</span>", totalAmount: unPaymentAmountTotal.toFixed(2) };
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}