let prefix = "/report";
let type = $('#type').val();
let tableGrid;
let $tableList;
let $dataForm;
let loginShopNo = utils.dataCache.loginShopInfo.no
let currentRow = {};

let initData = [];
let colNamesC = ['店铺', '编号', '客户名称', '单据日期', '销售单编号', '数量', '应收金额', '收款金额', '商品成本', '销售毛利', '欠款金额'];
let colNamesV = ['店铺', '编号', '供应商名称', '单据日期', '采购单编号', '数量', '应付金额', '付款金额', '', '', '欠款金额'];
let colNames = type === 'CUSTOMER' ? colNamesC : colNamesV;
let colModelC = [
    { name: 'shopNo', index: 'shopNo', editable: false, align: "center", formatter: cellValue => utils.formatType(cellValue, 'data_shop') },
    { name:'instituteId', index:'instituteId', editable:false, align: "center", hidden: true },
    { name:'instituteName', index:'instituteName', editable:false, sorttype:"text", align: "center", formatter: cellValue => utils.formatSubstr(cellValue, 8) },
    { name:'billRegion', index:'billRegion', editable:false, sorttype:"text", align: "center" },
    { name:'billNo', index:'billNo', editable:false, sorttype:"text", align: "center", width:180, formatter: cellValue => utils.formatSubstr(cellValue) },
    { name:'billCount', index:'billCount', editable:false, sorttype:"text", align: "center", width:80 },
    { name:'totalAmount', index:'totalAmount', editable:false, sorttype:"float", align: "right", width:120, formatter:"number" },
    { name:'paymentAmount', index:'paymentAmount', editable:false, width:120, align:"right", sorttype:"float", formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:120, align:"right", sorttype:"float", formatter:"number" },
    { name:'profitAmount', index:'profitAmount', editable:false, width:120, align:"right", sorttype:"float", formatter:"number" },
    { name:'debtAmount', index:'debtAmount', editable:false, width:120, align:"right", sorttype:"float", formatter:"number" }
];
let colModelV = [
    { name: 'shopNo', index: 'shopNo', editable: false, align: "center", formatter: cellValue => utils.formatType(cellValue, 'data_shop') },
    { name:'instituteId', index:'instituteId', editable:false, align: "center", hidden: true },
    { name:'instituteName', index:'instituteName', editable:false, sorttype:"text", align: "center", formatter: cellValue => utils.formatSubstr(cellValue) },
    { name:'billRegion', index:'billRegion', editable:false, sorttype:"text", align: "center" },
    { name:'billNo', index:'billNo', editable:false, sorttype:"text", align: "center", width:180, formatter: cellValue => utils.formatSubstr(cellValue) },
    { name:'billCount', index:'billCount', editable:false, sorttype:"text", align: "center", width:80 },
    { name:'totalAmount', index:'totalAmount', editable:false, sorttype:"float", align: "right", width:120, formatter:"number" },
    { name:'paymentAmount', index:'paymentAmount', editable:false, width:120, align:"right", sorttype:"float", formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:120, align:"right", sorttype:"float", hidden:true, formatter:"number" },
    { name:'profitAmount', index:'profitAmount', editable:false, width:120, align:"right", sorttype:"float", hidden:true, formatter:"number" },
    { name:'debtAmount', index:'debtAmount', editable:false, width:120, align:"right", sorttype:"float", formatter:"number" }
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
        let rowData = tableGrid.jqGrid('getLocalRow', rowid);
        currentRow = $.extend(rowData, { type : type})
        searchEntryBalance(currentRow);
    }
};

$(function() {
    $dataForm = $('#search');
    $tableList = $('#table_list');

    utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());
    utils.loadChosenStatic(["dimensionType"], [{width: "120px", setValue: ['USER']}]);


    if (type === 'CUSTOMER') {
        utils.loadCategory(["CUSTOMER_DATA"], ["instituteId"], [{width: "120px", liveSearch: true, setData: []}]);
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
        datatype: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify($dataForm.serializeObject()),
        success: function (r) {
            if (r.code === 0) {
                tableGrid.trigger("reloadGrid", { fromServer: true });
                tableGrid.jqGrid('clearGridData');
                tableGrid.jqGrid('setGridParam', {data: r.data.itemList}).trigger('reloadGrid');
                collectTotal();

                $('span[name=toDate]').html("欠款日期: " + r.data.billRegion);
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
    let billCountTotal=tableGrid.getCol('billCount',false,'sum');
    let totalAmountTotal=tableGrid.getCol('totalAmount',false,'sum');
    let paymentAmountTotal=tableGrid.getCol('paymentAmount',false,'sum');
    let debtAmountTotal=tableGrid.getCol('debtAmount',false,'sum');
    let costAmountTotal=tableGrid.getCol('costAmount',false,'sum');
    let profitAmountTotal=tableGrid.getCol('profitAmount',false,'sum');
    let totalAmountObj = { instituteId: '合计:', instituteName:'数量：' + recordNum, billCount: billCountTotal, totalAmount: totalAmountTotal, paymentAmount: paymentAmountTotal, debtAmount: debtAmountTotal, costAmount: costAmountTotal, profitAmount: profitAmountTotal };
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}

function exportExcel() {
    let queryParam = $dataForm.serialize();
    let url = prefix + "/sRecon/export?" + queryParam //下载地址
    utils.downloadAjax(url, 'SReconResult.xls')
}

function searchEntryBalance(rowData) {
    layer.open({
        type : 2,
        title : '单据明细',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1300px', '650px' ],
        content : prefix + '/sRecon/entry' // iframe的url
    });
}

function getCurrentRow() {
    return $.extend(currentRow || {}, {"searchObj": $dataForm.serializeObject()});
}