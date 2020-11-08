var prefix = "/report";
var tableGrid_1, tableGrid_2;
var initData = [];
var colNames_1 = ['编号', '名称', '销售金额', '欠款金额'];
var colNames_2 = ['编号', '名称', '销售数量', '销售金额'];
var colModel_1 = colModel_2 = [
    { name:'no', index:'no', editable:false, align: "center", width:40 },
    { name:'name', index:'name', editable:false, sorttype:"text", align: "center", width:60 },
    { name:'amount1', index:'amount1', editable:false, sorttype:"float", align:"right", width:80, formatter:"number" },
    { name:'amount2', index:'amount2', editable:false, sorttype:"float", align:"right", width:80, formatter:"number" }];

var gridConfig_1 = {
    datatype: "local", data: initData, height: 150, rowNum:10, autowidth: true, shrinkToFit: true, colNames: colNames_1, colModel: colModel_1
};

var gridConfig_2 = {
    datatype: "local", data: initData, height: 150, rowNum:10, autowidth: true, shrinkToFit: true, colNames: colNames_2, colModel: colModel_2
};

$(function() {
    $.jgrid.defaults.styleUI = 'Bootstrap';
    tableGrid_1 = $("#table_list_1").jqGrid(gridConfig_1);
    tableGrid_2 = $("#table_list_2").jqGrid(gridConfig_2);

    $('#table_list_1').setGridWidth($('.panel-body').width());
    $('#table_list_2').setGridWidth($('.panel-body').width());

    $(window).bind('resize', function () {
        var width = $('.panel-body').width();
        $('#table_list_1').setGridWidth($('.panel-body').width());
        $('#table_list_2').setGridWidth($('.panel-body').width());
    });

    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        // 获取已激活的标签页的名称
        var activeTab = $(e.target).text();
        // 获取前一个激活的标签页的名称
        var previousTab = $(e.relatedTarget).text();
        console.log(activeTab + '  ' + previousTab)
    });
});

function loadGrid(elem, type) {
    $.ajax({
        url: prefix + "/mainTab",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({ type: type }),
        success: function (r) {
            if (r.code == 0) {
                $('#' + elem).jqGrid('clearGridData');
                $('#' + elem).jqGrid('setGridParam', {data: r.result}).trigger('reloadGrid');
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

