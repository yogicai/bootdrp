var prefix = "/report";
var tableGrid;
var dataForm;
var start;
var end;
var initData = [];
var colNames = ['商品编号', '商品名称', '单位', '均价', '销售开单量', '商品数量', '商品成本', '销售金额', '销售毛利'];
var colModel = [
    { name:'entryId', index:'entryId', editable:false, align: "center", width:30 },
    { name:'entryName', index:'entryName', editable:false, sorttype:"text", align: "center", width:60 },
    { name:'entryUnit', index:'entryUnit', editable:false, sorttype:"text", align: "center", width:30 },
    { name:'entryPrice', index:'entryPrice', editable:false, sorttype:"text", align: "right", width:60, formatter:"number" },
    { name:'billCount', index:'billCount', editable:false, sorttype:"text", align: "right", width:60, formatter:"number" },
    { name:'totalQty', index:'totalQty', editable:false, sorttype:"float", align: "right", width:80, formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'entryAmount', index:'entryAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'billProfit', index:'billProfit', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" }    ];

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

        }
    };

$(function() {
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
        url: prefix + "/saleProduct",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(dataForm.serializeObject()),
        success: function (r) {
            if (r.code == 0) {
                var _gridConfig = $.extend({}, gridConfig, {data: r.result});
                $('#table_list').jqGrid('clearGridData');
                $('#table_list').jqGrid('setGridParam', _gridConfig).trigger('reloadGrid');

                collectTotal();

                dataForm.setForm(r);
                // start = r.start; end = r.end;
                $('span[name=toDate]').html("单据日期: " + r.billRegion);
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
    var billCountTotal = $("#table_list").getCol('billCount', false,'sum');
    var costAmountTotal=$("#table_list").getCol('costAmount',false,'sum');
    var entryAmountTotal=$("#table_list").getCol('entryAmount',false,'sum');
    var billProfitTotal=$("#table_list").getCol('billProfit',false,'sum');
    var totalAmountObj = { entryId: '合计:', entryName:'数量：' + recordNum, billCount: billCountTotal, costAmount: costAmountTotal, entryAmount: entryAmountTotal, billProfit: billProfitTotal };
    // 设置表格合计项金额
    $("#table_list").footerData('set', totalAmountObj);
};