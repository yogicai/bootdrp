let prefix = "/cashier/reconcile/entry";
let tableGrid;
let $tableList;

let colNamesC = ['单据日期', '编号', '店铺', '类型', '销货单位', '结算帐户', '分录备注', '核销金额', '核销折扣', '实收金额', '收款人'];
let colNamesV = ['单据日期', '编号', '店铺', '类型', '购货单位', '结算帐户', '分录备注', '核销金额', '核销折扣', '实付金额', '付款人'];

let colModelC = [
    {name: 'billDate', index: 'billDate', editable: true, width: 80, sorttype: "date", formatter: "date", frozen: true},
    {name: 'billNo', index: 'billNo', editable: true, sorttype: "text", width: 200, frozen: true, title:true},
    {name: 'shopNo', index: 'shopNo', editable: true, sorttype: "text", width: 70, formatter: cellValue => utils.formatType(cellValue, 'data_shop')},
    {name: 'billType', index: 'billType', editable: true, sorttype: "text", width: 70, formatter: cellValue => utils.formatEnum(cellValue, 'BILL_TYPE')},
    {name: 'debtorName', index: 'debtorName', editable: true, sorttype: "text", width: 70, title:true},
    {name: 'settleDOList', index: 'settleDOList', editable: true, width: 90, align: "center", formatter: cellValue => utils.formatListS(cellValue, 'settleName')},
    {name: 'entryDOList', index: 'entryDOList', editable: true, width: 180, align: "center", title: false, formatter: cellValue => utils.formatListSpan(cellValue, 'srcBillNo', 1)},
    {name: 'checkAmount', index: 'checkAmount', editable: true, width: 100, align: "right", sorttype: "float", formatter: "number"},
    {name: 'discountAmount', index: 'discountAmount', editable: true, width: 80, align: "right", sorttype: "float", formatter: "number"},
    {name: 'paymentAmount', index: 'paymentAmount', editable: true, width: 80, align: "right", sorttype: "float", formatter: "number"},
    {name: 'checkName', index: 'checkName', editable: true, sorttype: "text", width: 70},
];
let colModelV = colModelC;

$(function() {
    $tableList = $('#table_list');

    load();
});

function load() {
    let rowData = window.parent.getCurrentRow();
    let billType = rowData['searchObj']['billType'];
    let colNames = billType === 'CW_SK_ORDER' ? colNamesC : colNamesV;
    let colModel = billType === 'CW_SK_ORDER' ? colModelC : colModelV;
    let postData = {
        shopNo: rowData['searchObj']['shopNo'],
        billType: rowData['searchObj']['billType'],
        dateType: rowData['searchObj']['dateType'],
        billRegion: rowData['billDate'],
    };

    tableGrid = $tableList.jqGrid({
        url: prefix + "/page",
        datatype: "json",
        postData: postData,
        colNames: colNames,
        colModel: colModel,
        height: window.innerHeight - 150,
        autowidth: true,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 100,
        rowList: [20, 50, 100, 500, 2000],
        pager: "#pager_list",
        viewrecords: true,
        footerrow: true,
        loadComplete: function (data) {
            collectTotal(data);
        }
    });

    tableGrid.jqGrid('navGrid', '#pager_list', {
        edit: false,
        add: false,
        search: false,
        del: false,
        refresh: true
    }, {
        height: 150,
        reloadAfterSubmit: true
    });

    $(window).bind('resize', function () {
        let width = $('.jqGrid_wrapper').width();
        tableGrid.setGridWidth(width);
        tableGrid.setGridHeight(window.innerHeight - 150);
    });
}

//计算表格合计行数据
function collectTotal(data){
    let paymentAmountTotal=tableGrid.getCol('paymentAmount',false,'sum');
    let checkAmountTotal=tableGrid.getCol('checkAmount',false,'sum');
    let discountAmountTotal=tableGrid.getCol('discountAmount',false,'sum');
    let totalAmountObj = { billNo:'本页合计：', paymentAmount: paymentAmountTotal, checkAmount: checkAmountTotal, discountAmount: discountAmountTotal };
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}
