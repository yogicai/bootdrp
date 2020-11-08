var prefix = "/report";
var tableGrid;
var dataForm;
var initData = [];
var colNames = ['商品编号', '商品名称', '条形码',  '单位', '累计入库数', '商品单价', '商品金额', '库存数量', '单位成本', '成本'];
var colModel = [
    { name:'entryId', index:'entryId', editable:false, width:50 },
    { name:'entryName', index:'entryName', editable:false, sorttype:"text", width:150, frozen: true },
    { name:'entryBarcode', index:'entryBarcode', editable:false, sorttype:"text", width:150 },
    { name:'entryUnit', index:'entryUnit', editable:false, sorttype:"float", align: "center", width:30, formatter : function (value,row,index){ return utils.formatType(value, "data_unit")} },
    { name:'qtyTotal', index:'qtyTotal', editable:false, width:70, align:"right", sorttype:"float", formatter:"number" },
    { name:'entryPrice', index:'entryPrice', editable:false, width:70, align:"right", sorttype:"float", formatter:"number" },
    { name:'entryAmount', index:'entryAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
    { name:'inventory', index:'inventory', editable:false, align:"right", sorttype:"float", width:70 },
    { name:'costPrice', index:'costPrice', editable:false, width:70, align:"right", sorttype:"float", formatter:"number" },
    { name:'costAmount', index:'costAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" }];

var rowTemplate = { name:'totalQty', index:'totalQty', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" };
var rowTemplateFun = function (columnName) {  return $.extend({}, rowTemplate, {name:columnName}); };

var groupHeaders = [{startColumnName: 'qtyTotal', numberOfColumns: 6, titleText: '<em>所有仓库</em>'}];
var groupHeaderFun = function (columnName, number, titleText) { return {startColumnName: columnName, numberOfColumns: number, titleText: '<em>'+titleText+'</em>'}; };

var gridConfig = {
        datatype: "local",
        data: initData,
        height: window.innerHeight - 180,
        rowNum:10000,
        autowidth: true,
        shrinkToFit: true,
        footerrow: true,
        colNames: colNames,
        colModel: colModel,
        ondblClickRow: function (rowid, iRow, iCol, e) {
            //TODO
            layer.msg("商品收发明细表还未开发！");
        }
    };

$(function() {
    utils.loadTypes(["data_stock"], ["stock"], [{width:"120px"}]);
    utils.loadCategory(["PRODUCT_DATA","PRODUCT"], ["product","type"], [{width:"150px"},{width:"120px"}]);
    dataForm = $("#search");
    load();
});

function load() {

    utils.createDatePicker('date_1');

    $.jgrid.defaults.styleUI = 'Bootstrap';
    tableGrid = $("#table_list").jqGrid(gridConfig);

    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#table_list').setGridWidth(width);
        $('#table_list').setGridHeight(window.innerHeight - 180);
    });
}

function loadGrid() {
    $.ajax({
        url: prefix + "/pBalance",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(dataForm.serializeObject()),
        success: function (r) {
            if (r.code == 0) {
                var stockList = r.result.stockList;
                var _colNames = colNames.concat();
                var _colModel = colModel.concat();
                var _groupHeaders = groupHeaders.concat();
                var _addColModelName = [];
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
                var _gridConfig = $.extend({}, gridConfig, {height: window.innerHeight - 200    , colNames: _colNames, colModel: _colModel, data: r.result.productInfoList});
                $.jgrid.gridUnload('#table_list');
                $('#table_list').jqGrid( _gridConfig ).trigger('reloadGrid');
                $('#table_list').jqGrid('setGroupHeaders', { useColSpanStyle: true, groupHeaders: _groupHeaders });

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
    var recordNum = $("#table_list").jqGrid('getGridParam', 'records');
    var inventoryTotal=$("#table_list").getCol('inventory',false,'sum');
    var entryAmountTotal=$("#table_list").getCol('entryAmount',false,'sum');
    var totalAmountTotal=$("#table_list").getCol('totalAmount',false,'sum');
    var totalCostTotal=$("#table_list").getCol('costAmount',false,'sum');
    var totalAmountObj = { entryName: '合计:', entryBarcode:'商品数量：' + recordNum, inventory: inventoryTotal, costAmount: totalCostTotal, entryAmount: entryAmountTotal, totalAmount: totalAmountTotal };
    $.each(addColModelName, function (key, val){
        totalAmountObj[val] = $("#table_list").getCol(val,false,'sum');
    });
    // 设置表格合计项金额
    $("#table_list").footerData('set', totalAmountObj);
};