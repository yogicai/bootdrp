let prefix = "/report/sRecon/entry";
let tableGrid;
let $tableList;

let colNamesC = ['店铺', '编号', '客户名称', '单据日期', '销售单编号', '状态', '应收金额', '收款金额', '商品成本', '销售毛利', '欠款金额'];
let colNamesV = ['店铺', '编号', '供应商名称', '单据日期', '采购单编号', '状态', '应付金额', '付款金额', '', '', '欠款金额'];

let colModelC = [
    { name:'shopNo', index: 'shopNo', editable: false, sortable: true, align: "center", formatter: cellValue => utils.formatType(cellValue, 'data_shop') },
    { name:'instituteId', index:'instituteId', editable:false, align: "center", hidden: true },
    { name:'instituteName', index:'instituteName', editable:false, sortable: true, sorttype: "text", align: "center", formatter: cellValue => utils.formatSubstr(cellValue, 8) },
    { name:'billRegion', index:'billRegion', editable:false, sortable: true, sorttype: "text", align: "center", formatter:"date" },
    { name:'billNo', index:'billNo', editable:false, sortable: true, sorttype: "text", align: "center", width:200, formatter: cellValue => utils.formatSubstr(cellValue) },
    { name:'status', index: 'status', editable: false,  align:"center", sortable: true, sorttype: "text", width: 70, formatter: cellValue => utils.formatEnum(cellValue, 'ORDER_CG_STATUS') },
    { name:'totalAmount', index:'totalAmount', editable:false, sortable: true, sorttype:"float", align: "right", width:120, formatter:"number" },
    { name:'paymentAmount', index:'paymentAmount', editable:false, width:120, align:"right", sortable: true, sorttype:"float", formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:120, align:"right", sortable: true, sorttype:"float", formatter:"number" },
    { name:'profitAmount', index:'profitAmount', editable:false, width:120, align:"right", sortable: true, sorttype:"float", formatter:"number" },
    { name:'debtAmount', index:'debtAmount', editable:false, width:120, align:"right", sortable: true, sorttype:"float", formatter:"number" },
];
let colModelV = [
    { name: 'shopNo', index: 'shopNo', editable: false, sortable: true, align: "center", formatter: cellValue => utils.formatType(cellValue, 'data_shop') },
    { name:'instituteId', index:'instituteId', editable:false, align: "center", hidden: true },
    { name:'instituteName', index:'instituteName', editable:false, sortable: true, sorttype: "text", align: "center", formatter: cellValue => utils.formatSubstr(cellValue) },
    { name:'billRegion', index:'billRegion', editable:false, sortable: true, sorttype: "text", align: "center", formatter:"date" },
    { name:'billNo', index:'billNo', editable:false, sortable: true, sorttype: "text", align: "center", width:200, formatter: cellValue => utils.formatSubstr(cellValue) },
    { name: 'status', index: 'status', editable: false,  sortable: true, align:"center", sorttype: "text", width: 70, formatter: cellValue => utils.formatEnum(cellValue, 'ORDER_CG_STATUS') },
    { name:'totalAmount', index:'totalAmount', editable:false, sortable: true, sorttype:"float", align: "right", width:120, formatter:"number" },
    { name:'paymentAmount', index:'paymentAmount', editable:false, width:120, align:"right", sortable: true, sorttype:"float", formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:120, align:"right", sortable: true, sorttype:"float", hidden:true, formatter:"number" },
    { name:'profitAmount', index:'profitAmount', editable:false, width:120, align:"right", sortable: true, sorttype:"float", hidden:true, formatter:"number" },
    { name:'debtAmount', index:'debtAmount', editable:false, width:120, align:"right", sortable: true, sorttype:"float", formatter:"number" },
];

$(function() {
    $tableList = $('#table_list');

    load();
});

function load() {
    let rowData = window.parent.getCurrentRow();
    let instituteId = rowData['searchObj']['instituteId'];
    let dimensionType = rowData['searchObj']['dimensionType'];
    let instituteIdRow = dimensionType === 'USER' ? rowData['instituteId'] : null;
    let colNames = rowData['type'] === 'CUSTOMER' ? colNamesC : colNamesV;
    let colModel = rowData['type'] === 'CUSTOMER' ? colModelC : colModelV;
    let postData = {
        type: rowData['type'],
        shopNo: rowData['shopNo'],
        billRegion: rowData['billRegion'],
        dimensionType: dimensionType,
        instituteId: _.isEmpty(instituteId) ? instituteIdRow : instituteId,
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
            utils.changeRowCss(tableGrid, "status", "未结款,部分结款");
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
    let totalAmountTotal=tableGrid.getCol('totalAmount',false,'sum');
    let paymentAmountTotal=tableGrid.getCol('paymentAmount',false,'sum');
    let debtAmountTotal=tableGrid.getCol('debtAmount',false,'sum');
    let costAmountTotal=tableGrid.getCol('costAmount',false,'sum');
    let profitAmountTotal=tableGrid.getCol('profitAmount',false,'sum');
    let totalAmountObj = { instituteName:'本页合计：', totalAmount: totalAmountTotal, paymentAmount: paymentAmountTotal, debtAmount: debtAmountTotal, costAmount: costAmountTotal, profitAmount: profitAmountTotal };
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}
