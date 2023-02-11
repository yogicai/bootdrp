let prefix = "/engage/product";
let tableGrid;

$(function() {
    load();
});

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    let rowData = window.parent.getCurrentRow();

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/balanceEntry/list",
        datatype: "json",
        postData: { "entryId": rowData['entryId'], "end": rowData['searchObj']['toDate'] },
        colNames: ['单据日期', '编号', '类型', '商品编号', '商品名称', '单位', '商品单价', '成本单价', '商品数量', '创建时间', '修改时间'],
        colModel: [
            { name:'billDate', index:'billDate', editable:true, width:80, sorttype:"date", formatter:"date", frozen: true },
            { name:'billNo', index:'billNo', editable:true, sorttype:"text", width:150, frozen: true },
            { name: 'billType', index: 'billType', editable: true, sorttype: "text", width: 60, formatter: function (cellValue) { return utils.formatEnum(cellValue, 'BILL_TYPE') }, unformat: function (cellValue) { return utils.unformatEnum(cellValue, 'BILL_TYPE') } },
            { name:'entryId', index:'entryId', editable:false, width:60 },
            { name:'entryName', index:'entryName', editable:false, width:80 },
            { name:'entryUnit', index:'entryUnit', editable:false, width:40 },
            { name:'entryPrice', index:'entryPrice', editable:false, width:80, align:"right", formatter:formatBalance },
            { name:'costPrice', index:'costPrice', editable:false, width:80, align:"right", formatter:formatBalance },
            { name:'totalQty', index:'totalQty', editable:false, width:80, align:"right", formatter:formatBalance},
            { name:'createTime', index:'createTime', editable:false, width:100 },
            { name:'updateTime', index:'updateTime', editable:false,  width:100 }
        ],
        height: window.innerHeight - 150,
        autowidth: true,
        shrinkToFit: true,
        rownumbers: true,
        rowNum: 50,
        rowList: [20, 50, 100, 200],
        pager: "#pager_list",
        viewrecords: true,
        footerrow: true,
        sortname: "billType",
        loadComplete: function (data) {
            collectTotal(data);
            utils.changeRowCss(tableGrid, "billType", "CG_ORDER,WH_RK_ORDER");
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
    //库存数量
    let incomeQty = data['extra']['incomeQty'];
    let outcomeQty = data['extra']['outcomeQty'];
    let balanceQty = data['extra']['balanceQty'];
    let totalAmountObj = {
        entryPrice: `采购:${utils.priceFormat(incomeQty)}`,
        costPrice: `销售:${utils.priceFormat(outcomeQty)}`,
        totalQty: `<span style="color:blue;font-weight:bold">库存:${utils.priceFormat(balanceQty)}</span>`
    };
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}

/**
 * @param cellvalue 当前cell的值
 * @param options   该cell的options设置，包括{rowId, colModel,pos,gid}
 * @param rowObject 当前cell所在row的值，是一个对象
 * @returns {*|string}
 */
let billTypeSet = new Set().add('CG_ORDER').add('WH_RK_ORDER');
function formatBalance(cellvalue, options, rowObject) {
    if (billTypeSet.has(rowObject['billType'])) {
        return '<span style="color:blue;font-weight:bold">' + utils.priceFormat(cellvalue) + '</span>';
    } else if (isFinite(cellvalue)) {
        return utils.priceFormat(cellvalue);
    } else {
        return cellvalue;
    }
}

