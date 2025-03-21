let prefix = "/cashier";
let tableGrid;
let $tableList;
let $dataForm;
let start;
let end;
let loginShopNo = utils.dataCache.loginShopInfo.no

$(function() {
    $dataForm = $("#search");
    $tableList = $('#table_list');

    utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());
    utils.loadTypes(["data_shop"], ["shopNo"], [{width: "120px", setValue: [loginShopNo]}]);
    utils.loadEnumTypes(["DATE_TYPE"], ["dateType"], [{width: "120px", setValue: ['DAY']}]);

    load();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $tableList.jqGrid({
        url: prefix + "/reconcile/page",
        datatype: "json",
        postData: $dataForm.serializeObject(),
        height: window.innerHeight - 210,
        autowidth: true,
        shrinkToFit: true,
        multiselect: false, //自带多选
        multiboxonly: true, //变成单选
        rownumbers: true,
        rowNum: 50,
        rowList: [10, 20, 50, 100],
        colNames: ['', '单据日期', '单据编号', '单据数量', '核销金额', '收款金额', '到账金额'],
        colModel: [
            { name:'id', index:'id', editable:false, width:50, hidden:true },
            { name:'billDate', index:'billDate', editable:false, sorttype:"text", align: "center", width:60 },
            { name:'billNo', index:'billNo', editable:false, sorttype:"text", align: "center", width:80, formatter: cellValue => utils.formatSubstr(cellValue, 23)},
            { name:'billCount', index:'billCount', editable:false, width:40, align:"right", sorttype:"float", formatter:"number" },
            { name:'checkAmount', index:'checkAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
            { name:'paymentAmount', index:'paymentAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" },
            { name:'payAmount', index:'payAmount', editable:false, width:80, align:"right", sorttype:"float", formatter:"number" }
        ],
        pager: "#pager_list",
        viewrecords: true,
        footerrow: true,
        ondblClickRow: function (rowid, iRow, iCol, e) {
            currentRow = tableGrid.jqGrid("getRowData", rowid);
        },
        loadComplete: function (data) {
            collectTotal(data);
        },
        onPaging:search
    });

    // Setup buttons
    tableGrid.jqGrid('navGrid', '#pager_list', {
        edit: false,
        add: false,
        search: false,
        del: false,
        refresh: true
    }, {
        height: 200,
        reloadAfterSubmit: true
    });

    $(window).bind('resize', function () {
        let width = $('.jqGrid_wrapper').width();
        tableGrid.setGridWidth(width);
        tableGrid.setGridHeight(window.innerHeight - 210);
    });
}

// 所有合计
function collectTotal(data){
    let billCountTotal = data['extra']['billCountTotal'];
    let checkAmountTotal = data['extra']['checkAmountTotal'];
    let paymentAmountTotal = data['extra']['paymentAmountTotal'];
    let payAmountTotal = data['extra']['payAmountTotal'];
    let totalAmountObj = {
        billNo: '全部汇总：',
        billCount: billCountTotal,
        checkAmount: checkAmountTotal,
        paymentAmount: paymentAmountTotal,
        payAmount: payAmountTotal
    };
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}

function search(pageBtn) {
    let inputPage = 1;
    let rowNum = tableGrid.getGridParam('rowNum');//获取每页数
    let curPage = tableGrid.getGridParam('page');//获取返回的当前页
    let totalPage = tableGrid.getGridParam('lastpage');//获取总页数
    if (pageBtn === 'first') {
        inputPage = 0;
    } else if (pageBtn === 'last') {
        inputPage = totalPage;
    } else if (pageBtn === 'prev') {
        inputPage = curPage - 1;
    } else if (pageBtn === 'next') {
        inputPage = curPage + 1;
    } else if (pageBtn === 'user') {
        inputPage = $('.ui-pg-input').val();//输入框页数
    } else if (pageBtn === 'records') {
        rowNum = $('.ui-pg-selbox').val();//输入框页数
    }
    inputPage = inputPage > totalPage ? totalPage : inputPage;
    inputPage = inputPage < 1 ? 1 : inputPage;
    let postData = $.extend({}, $dataForm.serializeObject(), {'page': inputPage, 'rows': rowNum});

    Object.keys(postData).forEach((element, index, array) => {
        if (Array.isArray(postData[element])) {
            postData[element] = postData[element].join();
        }
    });

    tableGrid.jqGrid('setGridParam', {postData:  $.param(postData)}).trigger("reloadGrid");
}

function exportExcel() {
    let queryParam = $dataForm.serialize();
    let url = prefix + "/reconcile/export?" + queryParam
    utils.downloadAjax(url ,'ReconcileExport.xls')
}