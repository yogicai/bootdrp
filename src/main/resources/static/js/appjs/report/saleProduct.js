let prefix = "/report";
let tableGrid;
let $tableList;
let $dataForm;
let start;
let end;
let loginShopNo = utils.dataCache.loginShopInfo.no

let initData = [];
let colNames = ['店铺', '商品编号', '商品名称', '单位', '均价', '销售开单量', '商品数量', '商品成本', '销售金额', '销售毛利'];
let colModel = [
    {name: 'shopNo', index: 'shopNo', editable: false, align: "center", width: 40, formatter: cellValue => utils.formatType(cellValue, 'data_shop')},
    { name:'entryId', index:'entryId', editable:false, align: "center", width:30 },
    { name:'entryName', index:'entryName', editable:false, sorttype:"text", align: "center", width:60 },
    { name:'entryUnit', index:'entryUnit', editable:false, sorttype:"text", align: "center", width:30 },
    { name:'entryPrice', index:'entryPrice', editable:false, sorttype:"text", align: "right", width:60, formatter:"number" },
    { name:'billCount', index:'billCount', editable:false, sorttype:"text", align: "right", width:60, formatter:"number" },
    { name:'totalQty', index:'totalQty', editable:false, sorttype:"float", align: "right", width:80, formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'entryAmount', index:'entryAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'billProfit', index:'billProfit', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" }    ];

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

    }
};

$(function() {
    $dataForm = $("#search");
    $tableList = $('#table_list');

    utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());
    utils.loadTypes(["data_shop"], ["shopNo"], [{width: "120px", setValue: [loginShopNo]}]);

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
        url: prefix + "/saleProduct",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify($dataForm.serializeObject()),
        success: function (r) {
            if (r.code === 0) {
                tableGrid.jqGrid('clearGridData');
                tableGrid.jqGrid('setGridParam', {data: r.result}).trigger('reloadGrid');
                collectTotal();

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
    let recordNum = tableGrid.jqGrid('getGridParam', 'records');
    let billCountTotal = tableGrid.getCol('billCount', false,'sum');
    let costAmountTotal=tableGrid.getCol('costAmount',false,'sum');
    let entryAmountTotal=tableGrid.getCol('entryAmount',false,'sum');
    let billProfitTotal=tableGrid.getCol('billProfit',false,'sum');
    let totalAmountObj = { entryId: '合计:', entryName:'数量：' + recordNum, billCount: billCountTotal, costAmount: costAmountTotal, entryAmount: entryAmountTotal, billProfit: billProfitTotal };
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}