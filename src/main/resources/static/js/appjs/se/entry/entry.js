let lastSel; //jqGrid最后编辑未保存的行号
let amountOrder; //订单信息对象
let amountEntry; //表格行分录信息对象
let tableGrid;
let $tableList;
let $dataForm;
let $mask;
let $consumerId;
let loginUserId = utils.dataCache.loginUserInfo.userId
let loginShopNo = utils.dataCache.loginShopInfo.no
let initFormData = {billerId: loginUserId, shopNo: loginShopNo, billDate: new Date().format('yyyy-MM-dd')}

let prefix = "/se/entry";
let prefixOrder = "/se/order";
let initData = [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}];

$(function() {
    $mask = $('#mask');
    $dataForm = $('#data_form');
    $consumerId = $('#consumerId');
    $tableList = $('#table_list');

    utils.createDatePicker('date_1');

    utils.loadCategory(["CUSTOMER_DATA", "ACCOUNT_DATA", "USER_DATA"], ["consumerId", "settleAccountTotal", "billerId"],
        [{width: "120px", liveSearch: true, setData: []}, {width: "200px", setIndex: 0}, {width: "120px", setValue: [loginUserId]}]);

    utils.loadTypes(["data_shop"], ["shopNo"],
        [{width: "120px", setValue: [loginShopNo], changeOption: {types: ["CUSTOMER_DATA", "ACCOUNT_DATA"], elementIds: ["consumerId", "settleAccountTotal"]}}]);

    load();
})

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $tableList.jqGrid({
        datatype : "local",
        data: initData,
        height: 'auto',
        rowNum: 1000,
        shrinkToFit: false,
        autoScroll: true,
        rownumbers:true,
        rownumWidth:30,
        footerrow : true,
        editurl: "clientArray",
        colNames: ['','ID','商品名称', '商品ID',  '单位', '仓库', '数量', '单价', '商品金额', '优惠率', '优惠金额',  '采购费用', '合计金额', '备注', '关联销货单'],
        colModel: [
            {
                name: 'act', width: 60, fixed: true, sortable: false, resize: false, formatter: function (cellValue, options, rowObject) {
                    let e = `<a class="btn btn-primary btn-xs" href="#" onclick="addRow('${options.rowId}')"><i class="fa fa-plus"></i></a> `;
                    let d = `<a class="btn btn-warning btn-xs" href="#" onclick="delRow('${options.rowId}')"><i class="fa fa-minus"></i></a> `;
                    return e + d;
                }
            },
            { name:'id', index:'id', editable:false, hidedlg:true, hidden:true},
            { name:'entryName', index:'entryName', editable:true, edittype:'custom', width:300, editoptions: utils.myElementAndValue() },
            { name:'entryId', index:'entryId', editable:false, width:70 },
            { name:'entryUnit', index:'entryUnit', editable:false, width:70 },
            { name:'stockNo', index:'stockNo', editable:true, width:100, edittype:'select', editoptions: utils.formatSelect("data_stock"), formatter: "select" },
            { name:'totalQty', index:'totalQty', editable:true, width:90, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'entryPrice', index:'entryPrice', editable:true, width:100, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'entryAmount', index:'entryAmount', editable:true, width:120, align:"right", editoptions: utils.numberEditOptions(collectRow, {readOnly: true}) },
            { name:'discountRate', index:'discountRate', editable:true, width:90, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'discountAmount', index:'discountAmount', editable:true, width:120, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'purchaseFee', index:'purchaseFee', editable:true, width:120, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'totalAmount', index:'totalAmount', editable:true, width:120, align:"right", editoptions: utils.commonEditOptions({readOnly: true}) },
            { name:'remark', index:'remark', editable:true, width:150, editoptions: utils.commonEditOptions() },
            { name:'requestBillNo', index:'requestBillNo', editable:false, width:200 }
        ],
        beforeSelectRow: function (rowId) {
            if (rowId !== lastSel) {
                tableGrid.jqGrid('saveRow',lastSel); // save row
                lastSel = rowId;
            }
            return true;
        },
        onCellSelect: function(rowId,iCol,contents,event) {
            if (iCol > 1) {
                tableGrid.jqGrid('editRow', rowId, { focusField: iCol });
            }
        },
        gridComplete: function () {
            collectTotal();
        }
    });

    // 设置横向滚动条
    $('.jqGrid_wrapper .ui-jqgrid').attr('style','overflow:auto');

    // 表格失去焦点保存Row数据
    $(document).click(function(e) {
        if(tableGrid.has(e.target).size() < 1 &&　lastSel) {
            tableGrid.jqGrid('saveRow', lastSel);  // save row
            lastSel = undefined;
            collectTotal();
        }
    });

    // 触发订单金额计算事件
    $('[name="discountRateTotal"]').bind('blur', {amountOrderFun: collectValueTotal}, utils.collectAmount);
    $('[name="discountAmountTotal"]').bind('blur', {amountOrderFun: collectValueTotal}, utils.collectAmount);
    $('[name="paymentAmountTotal"]').bind('blur', {amountOrderFun: collectValueTotal}, utils.collectAmount);
    $('[name="expenseFeeTotal"]').bind('blur', {amountOrderFun: collectValueTotal}, utils.collectAmount);

}

amountOrder = {
    // 订单分录汇总金额，数量、商品金额合计、优惠金额合计、采购费用合计、金额合计
    totalObj: {totalQtyTotal: 0, entryAmountTotal: 0, discountAmountTotal: 0, purchaseFeeTotal: 0, totalAmountTotal: 0},
    // 触发更新订单信息的列
    elements: {
        discountRateTotal: ['discountAmountTotal', 'finalAmountTotal', 'paymentAmountTotal', 'debtAccountTotal'],
        discountAmountTotal: ['discountRateTotal', 'finalAmountTotal', 'paymentAmountTotal', 'debtAccountTotal'],
        paymentAmountTotal: ['debtAccountTotal'],
        purchaseFeeTotal: ['debtAccountTotal'],
        expenseFeeTotal: ['debtAccountTotal']
    },
    // 订单各项信息元素（用于手工saveRow时计算）
    valueElements: ['totalQtyTotal', 'entryAmountTotal', 'discountRateTotal','discountAmountTotal', 'finalAmountTotal', 'paymentAmountTotal','purchaseFeeTotal','expenseFeeTotal','debtAccountTotal'],
    // 计算后的订单金额
    valueObj:{totalQtyTotal: 0, entryAmountTotal: 0, discountRateTotal: 0, discountAmountTotal: 0, finalAmountTotal: 0, paymentAmountTotal: 0, purchaseFeeTotal: 0, expenseFeeTotal: 0, debtAccountTotal: 0},
    // 订单金额计算公式
    formula: {
        totalQtyTotal: 'totalObj.totalQtyTotal',
        entryAmountTotal: '(totalObj.entryAmountTotal / 1.0).toFixed(2)',
        discountRateTotal: 'totalObj.totalAmountTotal > 0 ? (valueObj.discountAmountTotal / totalObj.totalAmountTotal * 100.0).toFixed(2) : 0.0',
        discountAmountTotal: 'totalObj.totalAmountTotal > 0 ? (valueObj.discountRateTotal * totalObj.totalAmountTotal / 100.0).toFixed(2) : 0.0',
        finalAmountTotal: '(totalObj.totalAmountTotal - valueObj.discountAmountTotal).toFixed(2)',
        paymentAmountTotal: '(totalObj.totalAmountTotal - valueObj.discountAmountTotal).toFixed(2)',
        debtAccountTotal: '(math.add(totalObj.totalAmountTotal,valueObj.expenseFeeTotal,valueObj.paymentAmountTotal * -1,valueObj.discountAmountTotal * -1)).toFixed(2)',
        purchaseFeeTotal: '(valueObj.purchaseFeeTotal / 1.0).toFixed(2)',
        expenseFeeTotal: '(valueObj.expenseFeeTotal / 1.0).toFixed(2)'
    }
};

function collectValueTotal() {
    let totalQtyTotal = $("#totalQtyTotal").val() || 0;
    let entryAmountTotal = $("#entryAmountTotal").val() || 0;
    let discountAmountTotal = $("#discountAmountTotal").val() || 0;
    let expenseFeeTotal = $("#expenseFeeTotal").val() || 0;
    let purchaseFeeTotal = $("#purchaseFeeTotal").val() || 0;
    let totalAmountTotal = $("#totalAmountTotal").val() || 0;
    let paymentAmountTotal = $("#paymentAmountTotal").val() || 0;
    amountOrder.valueObj = {totalQtyTotal: totalQtyTotal, entryAmountTotal: entryAmountTotal, discountAmountTotal: discountAmountTotal, paymentAmountTotal: paymentAmountTotal, expenseFeeTotal: expenseFeeTotal, purchaseFeeTotal: purchaseFeeTotal, totalAmountTotal: totalAmountTotal};
    return amountOrder;
}

//计算表格合计行数据
function collectTotal(){
    let totalQtyTotal = $tableList.getCol('totalQty', false, 'sum');
    let entryAmountTotal = $tableList.getCol('entryAmount', false, 'sum').toFixed(2);
    let discountAmountTotal = $tableList.getCol('discountAmount', false, 'sum').toFixed(2);
    let purchaseFeeTotal = $tableList.getCol('purchaseFee', false, 'sum').toFixed(2);
    let totalAmountTotal = $tableList.getCol('totalAmount', false, 'sum').toFixed(2);
    amountOrder.totalObj = {totalQtyTotal: totalQtyTotal, entryAmountTotal: entryAmountTotal, discountAmountTotal: discountAmountTotal, purchaseFeeTotal:  purchaseFeeTotal,totalAmountTotal: totalAmountTotal };
    // 全量计算订单各项金额
    utils.collectAmountManual(amountOrder);
    // 设置表格合计项金额
    $tableList.footerData('set', {entryName: '合计:', totalQty: totalQtyTotal, entryAmount: entryAmountTotal, discountAmount: discountAmountTotal, purchaseFee: purchaseFeeTotal, totalAmount: totalAmountTotal});
}

amountEntry = {
    // 订单分录各列值，数量、单价、商品金额、折扣率、折扣金额、采购费用、合计
    totalObj: {totalQty: 0, entryPrice: 0, entryAmount: 0, discountRate: 0, discountAmount: 0, purchaseFee: 0, totalAmount: 0},
    // 金额变化需更新别的金额列
    elements: {
        totalQty: ['entryAmount', 'discountAmount', 'totalAmount'],
        entryPrice: ['entryAmount', 'discountAmount', 'totalAmount'],
        entryAmount: ['entryPrice', 'discountAmount', 'totalAmount'],
        discountRate: ['discountAmount', 'totalAmount'],
        discountAmount: ['discountRate', 'totalAmount'],
        purchaseFee: ['totalAmount']
    },
    // 计算后各列的金额
    valueObj:{entryPrice: 0, entryAmount: 0, discountRate: 0, discountAmount: 0, totalAmount: 0},
    // 各列金额计算公式
    formula: {
        entryPrice: 'totalObj.entryAmount > 0 ? (totalObj.entryAmount / totalObj.totalQty * 1.0).toFixed(2) : 0.0',
        entryAmount: '(totalObj.entryPrice * totalObj.totalQty).toFixed(2)',
        discountRate: 'totalObj.entryAmount > 0 ? (totalObj.discountAmount / totalObj.entryAmount * 100.0).toFixed(2) : 0.0',
        discountAmount: 'totalObj.entryAmount > 0 ? (totalObj.discountRate * totalObj.entryAmount / 100.0).toFixed(2) : 0.0',
        totalAmount: '(totalObj.entryPrice * totalObj.totalQty - totalObj.discountAmount + totalObj.purchaseFee / 1.0).toFixed(2)'
    }
};

//获取编辑状态行数据
function collectRow(_rowId) {
    let rowId = _rowId || $tableList.jqGrid("getGridParam", "selrow");
    let originRow = $("#table_list tr[id=" + (rowId) + "]");
    let entryPrice = originRow.find("[name='entryPrice']").val();
    let totalQty = originRow.find("[name='totalQty']").val();
    let entryAmount = originRow.find("[name='entryAmount']").val();
    let discountRate = originRow.find("[name='discountRate']").val();
    let discountAmount = originRow.find("[name='discountAmount']").val();
    let purchaseFee = originRow.find("[name='purchaseFee']").val();
    amountEntry.totalObj = {entryPrice: entryPrice, totalQty: totalQty, entryAmount: entryAmount, discountRate:  discountRate, discountAmount: discountAmount, purchaseFee: purchaseFee };
    return amountEntry;
}

//增加行
function addRow(rowId) {
    let rowData = {};
    let ids = tableGrid.jqGrid('getDataIDs');
    let maxId = ids.length === 0 ? 1 : Math.max.apply(Math, ids);
    tableGrid.jqGrid('addRowData', maxId + 1, rowData, 'after', rowId);//插入行
}

//删除行
function delRow(rowId) {
    let ids = tableGrid.jqGrid('getDataIDs')
    if (ids.length > 1) {
        tableGrid.jqGrid('delRowData', rowId);
    } else {
        layer.msg('至少保留一个分录',{time:1000});
    }
}

//保存
function save(add) {
    let order = $dataForm.serializeObject();
    let entryArr = tableGrid.jqGrid("getRowData");
    order.entryVOList = [];

    if (_.some([order.shopNo, order.consumerId, order.billerId, order.billDate, order.settleAccountTotal], _.isEmpty) || amountOrder.totalObj.totalQtyTotal <= 0) {
        layer.msg((_.isEmpty(order.shopNo) ? "[所属店铺]" : "") + (_.isEmpty(order.consumerId) ? "[客户信息]" : "") + (_.isEmpty(order.billerId) ? "[销售人员信息]" : "") + (_.isEmpty(order.billDate) ? "[单据日期]" : "") + (_.isEmpty(order.settleAccountTotal) ? "[结算帐户]" : "") + (amountOrder.totalObj.totalQtyTotal <= 0 ? "[合计数量]" : "") + "不能为空！");
        return;
    }

    let entryTotalAmount = 0;
    $.each(entryArr, function (key, val) {
        delete val['act'];
        if (!_.some([val['entryName'], val['totalAmount']], _.isEmpty)) {
            entryTotalAmount = (_.toNumber(entryTotalAmount) + _.toNumber(val.totalAmount)).toFixed(2);
            order.entryVOList.push(val);
        }
    });

    let validateFlag = true;
    $.each(order.entryVOList, function (key, val) {
        if (_.isEmpty(val['totalQty']) || _.isEmpty(val['entryName']) || _.isEmpty(val['stockNo'])) {
            layer.msg((_.isEmpty(val['totalQty']) ? "[数量] " : "") + (_.isEmpty(val['entryName']) ? "[商品]" : "") + (_.isEmpty(val['stockNo']) ? "[仓库]" : "") + "列不能为空！");
            validateFlag = false;
            return false;
        }
    });

    if (_.round((entryTotalAmount - order.finalAmountTotal - order.discountAmountTotal), 2) !== 0) {
        layer.msg("单据商品非空分录金额与总金额不相等！");
        return;
    }

    if (validateFlag) {
        $.ajax({
            url : prefix+"/save",
            type : "post",
            // dataType: "json",
            contentType: "application/json; charset=utf-8",
            data : JSON.stringify(order),
            success : function(r) {
                if (r.code === 0 && add && add === 1) { //保存并新增
                    initBillNo();
                    tableGrid.clearGridData();
                    tableGrid.jqGrid('setGridParam', {data: [{}, {}, {}]}).trigger('reloadGrid');
                    layer.msg(r.msg);
                } else if (r.code === 0) { //新增
                    initBillNo(r.billNo);
                    layer.msg(r.msg);
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }
}

//审核
function audit(type) {
    let billNo = $("#billNo").val();
    if (!_.isEmpty(billNo)) {
        $.ajax({
            url : prefixOrder+"/audit",
            type : "post",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({'billNos': [billNo], 'auditStatus': type === 0 ? 'YES' : 'NO'}),
            success : function(r) {
                if (r.code === 0) {
                    $mask.removeClass('util-has-audit');
                    $mask.addClass('util-has-audit');
                }
                layer.msg(r.msg);
            }
        });
    } else {
        layer.msg('请先保存订单！');
    }
}

//历史单据
function listOrder(dataUrl) {
    let dataIndex;
    //触发菜单单击
    window.parent.$(".J_menuItem").each(function (index) {
        if ($(this).attr('href') === dataUrl) {
            window.parent.$(this).trigger('click');
            dataIndex = window.parent.$(this).data('index');
            return false;
        }
    });
    //重新刷订单列表
    window.parent.$('.J_mainContent .J_iframe').each(function () {
        if ($(this).data('id') === dataUrl) {
            let win = window.parent.$('iframe[name="iframe' + dataIndex + '"]')[0].contentWindow;
            if (win.reLoad) {
                win.reLoad();
            }
            return false;
        }
    });

}

//操作日志
function listLog(rowid) {
    //TODO
    layer.msg('此功能还未实现。。。');
}

//添加订单商品信息弹窗
function add() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1100px', '620px' ],
        content : prefix + '/add' // iframe的url
    });
}

//订单表格插入数据
function insertData(datas) {
    if (_.isEmpty(datas)) return;
    let ids = tableGrid.jqGrid('getDataIDs');
    let maxId = ids.length === 0 ? 1 : Math.max.apply(Math, ids);
    let rowId = tableGrid.jqGrid("getGridParam", "selrow")
    for (let i = 0; i < datas.length; i++) {
        if (i === 0) {
            tableGrid.jqGrid('saveRow', rowId);
            tableGrid.jqGrid('setRowData', rowId, datas[i]);//更新当前行
        } else {
            maxId = maxId + 1;
            tableGrid.jqGrid('addRowData', maxId, datas[i], 'after', rowId);//插入行
            rowId = maxId;
        }
    }
    //选中下一个空行
    ids = tableGrid.jqGrid('getDataIDs');
    $.each(ids, function (key, val) {
        let data = tableGrid.jqGrid("getRowData", val);
        if (_.isEmpty(data['entryName'])) {
            tableGrid.setSelection(val, false);
            return false;
        }
    });
    collectTotal();
}

//选择订单供应商弹窗
function addHead() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1000px', '600px' ],
        content : prefix + '/addHead' // iframe的url
    });
}

//设置供应商下拉框值
function insertHead(data) {
    if (_.isEmpty(data)) return;
    $consumerId.val(data.no).trigger('change');
}

//初始化订单数据
function initOrder(billNo) {
    if (billNo) {
        $.ajax({
            url: prefix + "/get",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: {"billNo": billNo},
            success: function (r) {
                if (r.code === 0) { //保存并新增
                    tableGrid.clearGridData();
                    tableGrid.jqGrid('setGridParam', {datatype : "local", data: r.order.entryVOList}).trigger('reloadGrid');
                    $dataForm.setForm(r.order);
                    $mask.removeClass('util-has-audit');
                    $mask.addClass(r.order && r.order.auditStatus === 'YES' ? 'util-has-audit' : '');
                    initBillNo(r.order.billNo);
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    } else {
        initBillNo();
        tableGrid.clearGridData();
        tableGrid.jqGrid('setGridParam', {data: [{}, {}, {}]}).trigger('reloadGrid');
        $dataForm.resetForm();
        $dataForm.setForm(initFormData);
        $mask.removeClass('util-has-audit');
    }
}

//设置单据号隐藏域值
function initBillNo(iVal) {
    iVal = _.defaultTo(iVal, '');
    $("[name=billNo]").each(function (index, element) {
        if ($(element).prop("tagName") === "SPAN") {
            $(element).html("单据编号: " + iVal);
        } else {
            $(element).val(iVal);
        }
    });
}