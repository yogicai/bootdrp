let prefix = "/cashier/journal";
let tableGrid1;
let tableGrid2;
let tableGrid3;

let dataForm;

let $tableList1;
let $tableList2;
let $tableList3;

let initData = [];

let groupHeaders1 = [{startColumnName: 'account', numberOfColumns: 13, titleText: '<b>账户现金流统计</b>'}];
let groupHeaders2 = [{startColumnName: 'year', numberOfColumns: 13, titleText: '<b>年度现金流统计</b>'}];
let groupHeaders3 = [{startColumnName: 'year', numberOfColumns: 13, titleText: '<b>经营业绩情况</b>'}];

let colNames1 = ['账户', '打款', '经销商', '调货', '调账', '店内调账', '父母', '个人消费', '换现金', '贷款综合', '运营费用', '支付工资', '营销费用'];
let colNames2 = ['年份', '打款', '经销商', '调货', '调账', '店内调账', '父母', '个人消费', '换现金', '贷款综合', '运营费用', '支付工资', '营销费用'];
let colNames3 = ['年份', '营业额', '收款', '成本', '欠款', '毛利', '毛利率', '父母', '运营费用', '工资', '营销费用', '净利润', '净利润率', '已实现净利润', '已支付工资'];

let colModel1 = [
    {name: 'account', index: 'account', editable: false, width: 90, align: "center"},
    {name: 'remit', index: 'remit', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'agency', index: 'agency', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'exchange', index: 'exchange', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'adjust', index: 'adjust', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'innerAdjust', index: 'innerAdjust', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'parent', index: 'parent', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'consume', index: 'consume', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'forCash', index: 'forCash', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'loans', index: 'loans', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'operation', index: 'operation', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'salary', index: 'salary', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'market', index: 'market', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"}
];
let colModel2 = [
    {name: 'year', index: 'year', editable: false, width: 90, align: "center"},
    {name: 'remit', index: 'remit', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'agency', index: 'agency', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'exchange', index: 'exchange', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'adjust', index: 'adjust', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'innerAdjust', index: 'innerAdjust', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'parent', index: 'parent', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'consume', index: 'consume', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'forCash', index: 'forCash', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'loans', index: 'loans', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'operation', index: 'operation', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'salary', index: 'salary', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'market', index: 'market', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"}
];
let colModel3 = [
    {name: 'year', index: 'year', editable: false, width: 90, align: "center"},
    {name: 'totalAmount', index: 'totalAmount', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'paymentAmount', index: 'paymentAmount', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'costAmount', index: 'costAmount', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'debtAmount', index: 'debtAmount', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'profitAmount', index: 'profitAmount', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'profitRate', index: 'profitRate', editable: true, sorttype: "float", width: 90, align: "right", formatter: "currency", formatoptions: {suffix: ""}},
    {name: 'parentFee', index: 'parentFee', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'operationFee', index: 'operationFee', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'salaryFee', index: 'salaryFee', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'marketFee', index: 'marketFee', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'netProfit', index: 'netProfit', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number"},
    {name: 'netProfitRate', index: 'netProfitRate', editable: true, sorttype: "float", width: 90, align: "right", formatter: "currency", formatoptions: {suffix: ""}},
    {name: 'netProfitRealized', index: 'netProfitRealized', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number", hidden: true },
    {name: 'salaryRealized', index: 'salaryRealized', editable: true, sorttype: "float", width: 90, align: "right", formatter: "number", hidden: true }
];

let gridConfig = {
    datatype: "local",
    data: initData,
    height: 50,
    rowNum: 10000,
    autowidth: true,
    shrinkToFit: true,
    rownumbers: true,
    footerrow: true,
    colNames: [],
    colModel: []
};

let gridConfig1 = $.extend({}, gridConfig, {height: 100, colNames: colNames1, colModel: colModel1});
let gridConfig2 = $.extend({}, gridConfig, {height: 200, colNames: colNames2, colModel: colModel2});
let gridConfig3 = $.extend({}, gridConfig, {height: 200, colNames: colNames3, colModel: colModel3});

$(function () {
    dataForm = $("#search");
    $tableList1 = $("#table_list1");
    $tableList2 = $("#table_list2");
    $tableList3 = $("#table_list3");

    utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());

    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid1 = $tableList1.jqGrid(gridConfig1);
    tableGrid1.jqGrid('setGroupHeaders', {useColSpanStyle: true, groupHeaders: groupHeaders1});

    tableGrid2 = $tableList2.jqGrid(gridConfig2);
    tableGrid2.jqGrid('setGroupHeaders', {useColSpanStyle: true, groupHeaders: groupHeaders2});

    tableGrid3 = $tableList3.jqGrid(gridConfig3);
    tableGrid3.jqGrid('setGroupHeaders', {useColSpanStyle: true, groupHeaders: groupHeaders3});

    $(window).bind('resize', function () {
        let width = $('.jqGrid_wrapper').width();
        tableGrid1.setGridWidth(width);
        tableGrid2.setGridWidth(width);
        tableGrid3.setGridWidth(width);
    });
});

function loadGrid() {
    //loading
    $(".loading").show();
    //加载新数据
    $.ajax({
        url: prefix + "/general",
        type: "post",
        data: dataForm.serializeObject(),
        datatype: "json",
        success: function (r) {
            if (r.code === 0) {
                tableGrid1.clearGridData();
                tableGrid1.jqGrid('setGridParam', {datatype: 'local', data: r.data['flowAccountStatList']}).trigger("reloadGrid");

                tableGrid2.clearGridData();
                tableGrid2.jqGrid('setGridParam', {datatype: 'local', data: r.data['flowAccountYearList']}).trigger("reloadGrid");

                tableGrid3.clearGridData();
                tableGrid3.jqGrid('setGridParam', {datatype: 'local', data: r.data['operateYearList']}).trigger("reloadGrid");

                collectTotal1();
                collectTotal2();
                collectTotal3();

                $('#toDate').html("单据日期: " + r.data['startYear'] + '_' + r.data['endYear']);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function search() {
    loadGrid();
}

//计算表格合计行数据
function collectTotal1() {
    let remitTotal = $tableList1.getCol('remit', false, 'sum');
    let agencyTotal = $tableList1.getCol('agency', false, 'sum');
    let exchangeTotal = $tableList1.getCol('exchange', false, 'sum');
    let adjustTotal = $tableList1.getCol('adjust', false, 'sum');
    let innerAdjustTotal = $tableList1.getCol('innerAdjust', false, 'sum');
    let parentTotal = $tableList1.getCol('parent', false, 'sum');
    let consumeTotal = $tableList1.getCol('consume', false, 'sum');
    let forCashTotal = $tableList1.getCol('forCash', false, 'sum');
    let loansTotal = $tableList1.getCol('loans', false, 'sum');
    let operationTotal = $tableList1.getCol('operation', false, 'sum');
    let salaryTotal = $tableList1.getCol('balance', false, 'sum');
    let marketTotal = $tableList1.getCol('market', false, 'sum');
    let totalAmountObj = {
        account: '合计:',
        remit: remitTotal,
        agency: agencyTotal,
        exchange: exchangeTotal,
        adjust: adjustTotal,
        innerAdjust: innerAdjustTotal,
        parent: parentTotal,
        consume: consumeTotal,
        forCash: forCashTotal,
        loans: loansTotal,
        operation: operationTotal,
        salary: salaryTotal,
        market: marketTotal
    };
    // 设置表格合计项金额
    $tableList1.footerData('set', totalAmountObj);
}

function collectTotal2() {
    let remitTotal = $tableList2.getCol('remit', false, 'sum');
    let agencyTotal = $tableList2.getCol('agency', false, 'sum');
    let exchangeTotal = $tableList2.getCol('exchange', false, 'sum');
    let adjustTotal = $tableList2.getCol('adjust', false, 'sum');
    let innerAdjustTotal = $tableList2.getCol('innerAdjust', false, 'sum');
    let parentTotal = $tableList2.getCol('parent', false, 'sum');
    let consumeTotal = $tableList2.getCol('consume', false, 'sum');
    let forCashTotal = $tableList2.getCol('forCash', false, 'sum');
    let loansTotal = $tableList2.getCol('loans', false, 'sum');
    let operationTotal = $tableList2.getCol('operation', false, 'sum');
    let salaryTotal = $tableList2.getCol('balance', false, 'sum');
    let marketTotal = $tableList2.getCol('market', false, 'sum');
    let totalAmountObj = {
        year: '合计:',
        remit: remitTotal,
        agency: agencyTotal,
        exchange: exchangeTotal,
        adjust: adjustTotal,
        innerAdjust: innerAdjustTotal,
        parent: parentTotal,
        consume: consumeTotal,
        forCash: forCashTotal,
        loans: loansTotal,
        operation: operationTotal,
        salary: salaryTotal,
        market: marketTotal
    };
    // 设置表格合计项金额
    $tableList2.footerData('set', totalAmountObj);
}

function collectTotal3() {
    let totalAmountTotal = $tableList3.getCol('totalAmount', false, 'sum');
    let paymentAmountTotal = $tableList3.getCol('paymentAmount', false, 'sum');
    let costAmountTotal = $tableList3.getCol('costAmount', false, 'sum');
    let debtAmountTotal = $tableList3.getCol('debtAmount', false, 'sum');
    let profitAmountTotal = $tableList3.getCol('profitAmount', false, 'sum');
    let profitRateTotal = (profitAmountTotal / totalAmountTotal).toFixed(2);
    let parentFeeTotal = $tableList3.getCol('parentFee', false, 'sum');
    let operationFeeTotal = $tableList3.getCol('operationFee', false, 'sum');
    let salaryFeeTotal = $tableList3.getCol('salaryFee', false, 'sum');
    let marketFeeTotal = $tableList3.getCol('marketFee', false, 'sum');
    let netProfitTotal = $tableList3.getCol('netProfit', false, 'sum');
    let netProfitRateTotal = (netProfitTotal / totalAmountTotal).toFixed(2);
    let netProfitRealized = $tableList3.getCol('netProfitRealized', false, 'sum');
    let salaryRealized = $tableList3.getCol('salaryRealized', false, 'sum');
    let totalAmountObj = {
        year: '合计:',
        totalAmount: totalAmountTotal,
        paymentAmount: paymentAmountTotal,
        costAmount: costAmountTotal,
        debtAmount: debtAmountTotal,
        profitAmount: profitAmountTotal,
        profitRate: profitRateTotal,
        parentFee: parentFeeTotal,
        operationFee: operationFeeTotal,
        salaryFee: salaryFeeTotal,
        marketFee: marketFeeTotal,
        netProfit: netProfitTotal,
        netProfitRate: netProfitRateTotal
    };
    // 设置表格合计项金额
    $tableList3.footerData('set', totalAmountObj);

    $('#metric').html(`已实现净利润：${utils.priceFormat(netProfitRealized)} 应收账款：${utils.priceFormat(debtAmountTotal)} 已结算工资：${utils.priceFormat(salaryRealized)} 未结算工资：${utils.priceFormat(salaryFeeTotal - salaryRealized)}`);
}

function exportExcel() {
    let queryParam = dataForm.serialize();
    let url = prefix + "/general/export?" + queryParam //下载地址
    utils.downloadAjax(url, 'OperateStat.xlsx');
}
