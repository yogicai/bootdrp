let prefix = "/engage/product";
let tableGrid;
let $tableList;
let $dataForm;

let initData = [];
let currentRow = {};
let colNames = ['店铺', '商品编号', '商品名称', '条形码', '单位', '累计入库数量', '入库单价', '入库金额', '库存数量', '库存单价', '库存金额'];
let colModel = [
    {name: 'shopNo', index: 'shopNo', editable: false, align: "center", width: 50, formatter: cellValue => utils.formatType(cellValue, 'data_shop')},
    {name: 'entryId', index: 'entryId', editable: false, width: 50},
    {name: 'entryName', index: 'entryName', editable: false, sorttype: "text", width: 150, frozen: true},
    {name: 'entryBarcode', index: 'entryBarcode', editable: false, sorttype: "text", width: 60},
    {name: 'entryUnit', index: 'entryUnit', editable: false, sorttype: "float", align: "center", width: 30, formatter: (value, row, index) => utils.formatType(value, "data_unit")},
    {name: 'qtyTotal', index: 'qtyTotal', editable: false, width: 70, align: "right", sorttype: "float", formatter: "number"},
    {name: 'entryPrice', index: 'entryPrice', editable: false, width: 70, align: "right", sorttype: "float", formatter: "number"},
    {name: 'entryAmount', index: 'entryAmount', editable: false, width: 80, align: "right", sorttype: "float", formatter: "number"},
    {name: 'inventory', index: 'inventory', editable: false, align: "right", sorttype: "float", width: 70},
    {name: 'costPrice', index: 'costPrice', editable: false, width: 70, align: "right", sorttype: "float", formatter: "number"},
    {name: 'costAmount', index: 'costAmount', editable: false, width: 80, align: "right", sorttype: "float", formatter: "number"}];

let rowTemplate = { name:'totalQty', index:'totalQty', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" };
let rowTemplateFun = function (columnName) {  return $.extend({}, rowTemplate, {name:columnName}); };

let groupHeaders = [{startColumnName: 'qtyTotal', numberOfColumns: 6, titleText: '<em>所有仓库</em>'}];
let groupHeaderFun = function (columnName, number, titleText) { return {startColumnName: columnName, numberOfColumns: number, titleText: '<em>'+titleText+'</em>'}; };

let gridConfig = {
    datatype: "local",
    data: initData,
    height: window.innerHeight - 180,
    rowNum: 10000,
    autowidth: true,
    shrinkToFit: true,
    rownumbers: true,
    footerrow: true,
    colNames: colNames,
    colModel: colModel,
    beforeSelectRow: function (rowid, e) {

    },
    ondblClickRow: function (rowid, iRow, iCol, e) {
        currentRow = tableGrid.jqGrid("getRowData", rowid);
        searchEntryBalance(currentRow);
    }
};

$(function() {
    $dataForm = $('#search');
    $tableList = $("#table_list");

    utils.createDatePicker('date_1', {}, new Date());
    utils.loadTypes(["data_stock", "data_shop"], ["stock", "shopNo"], [{width: "120px"}, {width: "120px"}]);
    utils.loadCategory(["PRODUCT_DATA", "PRODUCT"], ["productNo", "type"], [{width: "120px", liveSearch: true}, {width: "120px"}]);

    load();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $tableList.jqGrid(gridConfig);

    $(window).bind('resize', function () {
        let width = $('.jqGrid_wrapper').width();
        tableGrid.setGridWidth(width);
        tableGrid.setGridHeight(window.innerHeight - 180);
    });
}

function loadGrid() {
    //loading
    $(".loading").show();
    //加载新数据
    $.ajax({
        url: prefix + "/balance/list",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify($dataForm.serializeObject()),
        success: function (r) {
            if (r.code === 0) {
                let stockList = r.result.stockList;
                let _colNames = colNames.concat();
                let _colModel = colModel.concat();
                let _groupHeaders = groupHeaders.concat();
                let _addColModelName = [];
                $.each(stockList, function (key, val) {
                    _colNames.push("库存数量");
                    _colModel.push(rowTemplateFun("totalQty".concat(key)));
                    _groupHeaders.push(groupHeaderFun("totalQty".concat(key), 1, val))
                    _addColModelName.push("totalQty".concat(key));
                });
                $.each(r.result.productInfoList, function (key, val) {
                    $.each(val.stockInfoList, function (keyS, valS) {
                        val["totalQty".concat(keyS)] = valS.totalQty;
                    });
                });
                let _gridConfig = $.extend({}, gridConfig, {height: window.innerHeight - 200, colNames: _colNames, colModel: _colModel, data: r.result.productInfoList});
                $.jgrid.gridUnload('#table_list');
                tableGrid = $('#table_list').jqGrid( _gridConfig );
                tableGrid.trigger("reloadGrid", { fromServer: true });
                tableGrid.jqGrid('setGroupHeaders', { useColSpanStyle: true, groupHeaders: _groupHeaders });

                collectTotal(_addColModelName);

                $('span[name=toDate]').html("库存日期: " + r.result.toDate);
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
function collectTotal(addColModelName){
    let recordNum = tableGrid.jqGrid('getGridParam', 'records');
    let inventoryTotal=tableGrid.getCol('inventory',false,'sum');
    let entryAmountTotal=tableGrid.getCol('entryAmount',false,'sum');
    let totalAmountTotal=tableGrid.getCol('totalAmount',false,'sum');
    let totalCostTotal=tableGrid.getCol('costAmount',false,'sum');
    let totalAmountObj = { entryName: '合计:', entryBarcode:'商品数量：' + recordNum, inventory: inventoryTotal, costAmount: totalCostTotal, entryAmount: entryAmountTotal, totalAmount: totalAmountTotal };
    $.each(addColModelName, function (key, val){
        totalAmountObj[val] = tableGrid.getCol(val,false,'sum');
    });
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}

function searchEntryBalance(rowData) {
    layer.open({
        type : 2,
        title : '库存余额',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1300px', '650px' ],
        content : prefix + '/balanceEntry' // iframe的url
    });
}

function getCurrentRow() {
    return $.extend(currentRow || {}, {"searchObj": $dataForm.serializeObject()});
}

function exportExcel() {
    let queryParam = $dataForm.serialize();
    let url = prefix + "/balance/export?" + queryParam //下载地址
    utils.downloadAjax(url, 'ProductBalanceResult.xls');
}
