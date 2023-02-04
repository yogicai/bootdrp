let prefix = "/report";
let type = $('#type').val();
let tableGrid;
let dataForm;
let start;
let end;
let initData = [];
let colNamesC = ['编号', '客户名称', '年度',  '应收金额', '收款金额', '商品成本', '销售毛利', '欠款金额'];
let colNamesV = ['编号', '供应商名称', '年度',  '应付金额', '付款金额', '', '', '欠款金额'];
let colNames = type === 'CUSTOMER' ? colNamesC : colNamesV;
let colModelC = [
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
    if (type === 'CUSTOMER') {
        utils.loadCategory(["CUSTOMER_DATA"], ["instituteId"], [{width:"150px"}]);
    } else {
        utils.loadCategory(["VENDOR_DATA"], ["instituteId"], [{width:"150px"}]);
    }
    dataForm = $("#search");
    load();
});

function load() {

    utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());

    $.jgrid.defaults.styleUI = 'Bootstrap';
    tableGrid = $("#table_list").jqGrid(gridConfig);

    $(window).bind('resize', function () {
        let width = $('.jqGrid_wrapper').width();
        tableGrid.setGridWidth(width);
        tableGrid.setGridHeight(window.innerHeight - 170);
    });
}

function loadGrid() {
    //消空数据
    tableGrid.jqGrid('clearGridData');
    //加载新数据
    $.ajax({
        url: prefix + "/sRecon",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(dataForm.serializeObject()),
        success: function (r) {
            if (r.code === 0) {
                let _gridConfig = $.extend({}, gridConfig, {data: r.result});
                $.jgrid.gridUnload('#table_list');
                tableGrid = $('#table_list').jqGrid( _gridConfig );
                tableGrid.trigger("reloadGrid", { fromServer: true });
                // tableGrid.jqGrid('clearGridData');
                // tableGrid.jqGrid('setGridParam', _gridConfig).trigger('reloadGrid');

                collectTotal();

                start = r.start; end = r.end;
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
};

function exportExcel() {
    let queryParam = dataForm.serialize();
    let url = prefix + "/sRecon/export?" + queryParam //下载地址
    utils.download(url ,'SReconResult.xls')
}
