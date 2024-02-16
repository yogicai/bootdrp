let prefix = "/report";
let type = $('#type').val();
let tableGrid;
let $tableList;
let $dataForm;
let start;
let end;
let loginShopNo = utils.dataCache.loginShopInfo.no

let initData = [];
let colNamesC = ['店铺', '编号', '客户名称', '年度', '应收金额', '收款金额', '商品成本', '销售毛利', '欠款金额'];
let colNamesV = ['店铺', '编号', '供应商名称', '年度', '应付金额', '付款金额', '', '', '欠款金额'];
let colNames = type === 'CUSTOMER' ? colNamesC : colNamesV;
let colModelC = [
    {name: 'shopNo', index: 'shopNo', editable: false, align: "center", width: 40, formatter: cellValue => utils.formatType(cellValue, 'data_shop')},
    { name:'instituteId', index:'instituteId', editable:false, align: "center", width:30 },
    { name:'instituteName', index:'instituteName', editable:false, sorttype:"text", align: "center", width:60 },
    { name:'billRegion', index:'billRegion', editable:false, sorttype:"text", align: "center", width:80 },
    { name:'totalAmount', index:'totalAmount', editable:false, sorttype:"float", align: "right", width:80, formatter:"number" },
    { name:'paymentAmount', index:'paymentAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'profitAmount', index:'profitAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'debtAmount', index:'debtAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" }
];
let colModelV = [
    {name: 'shopNo', index: 'shopNo', editable: false, align: "center", width: 40, formatter: cellValue => utils.formatType(cellValue, 'data_shop')},
    { name:'instituteId', index:'instituteId', editable:false, align: "center", width:30 },
    { name:'instituteName', index:'instituteName', editable:false, sorttype:"text", align: "center", width:60 },
    { name:'billRegion', index:'billRegion', editable:false, sorttype:"text", align: "center", width:80 },
    { name:'totalAmount', index:'totalAmount', editable:false, sorttype:"float", align: "right", width:80, formatter:"number" },
    { name:'paymentAmount', index:'paymentAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:80, align:"right", sorttype:"float", hidden:true, formatter:"number" },
    { name:'profitAmount', index:'profitAmount', editable:false, width:80, align:"right", sorttype:"float", hidden:true, formatter:"number" },
    { name:'debtAmount', index:'debtAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" }
];
let colModel = type === 'CUSTOMER' ? colModelC : colModelV;

let gridConfig = {
    datatype: "local",
    data: initData,
    height: window.innerHeight - 170,
    rowNum: 10000,
    autowidth: true,
    shrinkToFit: true,
    rownumbers: true,
    footerrow: true,
    colNames: colNames,
    colModel: colModel,
    ondblClickRow: function (rowid, iRow, iCol, e) {
        let rowData = tableGrid.jqGrid('getRowData', rowid);
        let postData = {start: start, end: end};
        postData[type === 'CUSTOMER' ? 'consumerId' : 'vendorId'] = rowData.instituteId;
        let postUrl = type === 'CUSTOMER' ? '/se/order' : '/po/order';
        utils.listDataGrid(postUrl, postData);
    }
};

$(function() {
    $dataForm = $('#search');
    $tableList = $('#table_list');

    utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());

    if (type === 'CUSTOMER') {
        utils.loadCategory(["CUSTOMER_DATA"], ["instituteId"], [{width: "120px", liveSearch: true}]);
        utils.loadTypes(["data_shop"], ["shopNo"],
            [{width: "120px", setValue: [loginShopNo], changeOption: {types: ["CUSTOMER_DATA"], elementIds: ["instituteId"]}}]);
    } else {
        utils.loadCategory(["VENDOR_DATA"], ["instituteId"], [{width: "120px", liveSearch: true}]);
        utils.loadTypes(["data_shop"], ["shopNo"],
            [{width: "120px", setValue: [loginShopNo], changeOption: {types: ["VENDOR_DATA"], elementIds: ["instituteId"]}}]);
    }

    load();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $tableList.jqGrid(gridConfig);

    $(window).bind('resize', function () {
        let width = $('.jqGrid_wrapper').width();
        tableGrid.setGridWidth(width);
        tableGrid.setGridHeight(window.innerHeight - 170);
    });
}

function loadGrid() {
    //loading
    $(".loading").show();
    //加载新数据
    $.ajax({
        url: prefix + "/sRecon",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify($dataForm.serializeObject()),
        success: function (r) {
            if (r.code === 0) {
                tableGrid.trigger("reloadGrid", { fromServer: true });
                tableGrid.jqGrid('clearGridData');
                tableGrid.jqGrid('setGridParam', {data: r.result}).trigger('reloadGrid');
                collectTotal();

                $('span[name=toDate]').html("欠款日期: " + r.billRegion);
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
function collectTotal(){
    let recordNum = tableGrid.jqGrid('getGridParam', 'records');
    let totalAmountTotal=tableGrid.getCol('totalAmount',false,'sum');
    let paymentAmountTotal=tableGrid.getCol('paymentAmount',false,'sum');
    let debtAmountTotal=tableGrid.getCol('debtAmount',false,'sum');
    let costAmountTotal=tableGrid.getCol('costAmount',false,'sum');
    let profitAmountTotal=tableGrid.getCol('profitAmount',false,'sum');
    let totalAmountObj = { instituteId: '合计:', instituteName:'数量：' + recordNum, totalAmount: totalAmountTotal, paymentAmount: paymentAmountTotal, debtAmount: debtAmountTotal, costAmount: costAmountTotal, profitAmount: profitAmountTotal };
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}

function exportExcel() {
    let queryParam = $dataForm.serialize();
    let url = prefix + "/sRecon/export?" + queryParam //下载地址
    utils.downloadAjax(url, 'SReconResult.xls')
}
