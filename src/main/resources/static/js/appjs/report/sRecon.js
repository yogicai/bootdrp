var prefix = "/report";
var type = $('#type').val();
var tableGrid;
var dataForm;
var start;
var end;
var initData = [];
var colNamesC = ['编号', '客户名称', '年度',  '应收金额', '收款金额', '商品成本', '销售毛利', '欠款金额'];
var colNamesV = ['编号', '供应商名称', '年度',  '应付金额', '付款金额', '', '', '欠款金额'];
var colNames = type == 'CUSTOMER' ? colNamesC : colNamesV;
var colModelC = [
    { name:'instituteId', index:'instituteId', editable:false, align: "center", width:30 },
    { name:'instituteName', index:'instituteName', editable:false, sorttype:"text", align: "center", width:60 },
    { name:'billRegion', index:'billRegion', editable:false, sorttype:"text", align: "center", width:80 },
    { name:'totalAmount', index:'totalAmount', editable:false, sorttype:"float", align: "right", width:80, formatter:"number" },
    { name:'paymentAmount', index:'paymentAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'profitAmount', index:'profitAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'debtAmount', index:'debtAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" }
];
var colModelV = [
    { name:'instituteId', index:'instituteId', editable:false, align: "center", width:30 },
    { name:'instituteName', index:'instituteName', editable:false, sorttype:"text", align: "center", width:60 },
    { name:'billRegion', index:'billRegion', editable:false, sorttype:"text", align: "center", width:80 },
    { name:'totalAmount', index:'totalAmount', editable:false, sorttype:"float", align: "right", width:80, formatter:"number" },
    { name:'paymentAmount', index:'paymentAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:80, align:"right", sorttype:"float", hidden:true, formatter:"number" },
    { name:'profitAmount', index:'profitAmount', editable:false, width:80, align:"right", sorttype:"float", hidden:true, formatter:"number" },
    { name:'debtAmount', index:'debtAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" }
];
var colModel = type == 'CUSTOMER' ? colModelC : colModelV;

var gridConfig = {
        datatype: "local",
        data: initData,
        height: window.innerHeight - 170,
        rowNum:10000,
        autowidth: true,
        shrinkToFit: true,
        footerrow: true,
        colNames: colNames,
        colModel: colModel,
        ondblClickRow: function (rowid, iRow, iCol, e) {
            var rowData = tableGrid.jqGrid('getRowData', rowid);
            var postData = {start: start, end: end};  postData[type == 'CUSTOMER' ? 'consumerId' : 'vendorId'] = rowData.instituteId;
            var postUrl = type == 'CUSTOMER' ? '/se/order' : '/po/order';
            utils.listDataGrid(postUrl, postData);
        }
    };

$(function() {
    if (type == 'CUSTOMER') {
        utils.loadCategory(["CUSTOMER_DATA"], ["instituteId"], [{width:"150px"}]);
    } else {
        utils.loadCategory(["VENDOR_DATA"], ["instituteId"], [{width:"150px"}]);
    }
    dataForm = $("#search");
    load();
});

function load() {

    utils.createDatePicker('datepicker');

    $.jgrid.defaults.styleUI = 'Bootstrap';
    tableGrid = $("#table_list").jqGrid(gridConfig);

    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#table_list').setGridWidth(width);
        $('#table_list').setGridHeight(window.innerHeight - 170);
    });
}

function loadGrid() {
    $.ajax({
        url: prefix + "/sRecon",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(dataForm.serializeObject()),
        success: function (r) {
            if (r.code == 0) {
                var _gridConfig = $.extend({}, gridConfig, {data: r.result});
                $('#table_list').jqGrid('clearGridData');
                $('#table_list').jqGrid('setGridParam', _gridConfig).trigger('reloadGrid');

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
    var recordNum = $("#table_list").jqGrid('getGridParam', 'records');
    var totalAmountTotal=$("#table_list").getCol('totalAmount',false,'sum');
    var paymentAmountTotal=$("#table_list").getCol('paymentAmount',false,'sum');
    var debtAmountTotal=$("#table_list").getCol('debtAmount',false,'sum');
    var costAmountTotal=$("#table_list").getCol('costAmount',false,'sum');
    var profitAmountTotal=$("#table_list").getCol('profitAmount',false,'sum');
    var totalAmountObj = { instituteId: '合计:', instituteName:'数量：' + recordNum, totalAmount: totalAmountTotal, paymentAmount: paymentAmountTotal, debtAmount: debtAmountTotal, costAmount: costAmountTotal, profitAmount: profitAmountTotal };
    // 设置表格合计项金额
    $("#table_list").footerData('set', totalAmountObj);
};

function exportExcel() {
    let queryParam = dataForm.serialize();
    let url = prefix + "/sRecon/export?" + queryParam //下载地址
    utils.download(url ,'SReconResult.xls')
}
