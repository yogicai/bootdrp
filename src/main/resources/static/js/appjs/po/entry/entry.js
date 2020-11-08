var lastSel; //jqGrid最后编辑未保存的行号
var amountOrder; //订单信息对象
var amountEntry; //表格行分录信息对象
var tableGrid;
var dataForm;
var prefix = "/po/entry";
var prefixOrder = "/po/order";
var initData = [{},{},{}];
var mask;

$(function() {
	load();
    dataForm  = $('#data_form');
    mask = $('#mask');
    utils.loadCategory(["VENDOR_DATA","ACCOUNT_DATA"], ["vendorId","settleAccountTotal"], [{width:"300px"},{width:"185px"}]);
});

function load() {

    utils.createDatePicker('date_1');

    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        datatype : "local",
        data: initData,
        height: 'auto',
        rowNum:100,
        shrinkToFit: false,
        autoScroll: true,
        rownumbers:true,
        rownumWidth:30,
        footerrow : true,
        editurl: "clientArray",
        colNames: ['','ID','商品名称', '商品ID',  '单位', '仓库', '数量', '单价', '商品金额', '优惠率', '优惠金额',  '采购费用', '合计金额', '备注', '关联购货单'],
        colModel: [
            { name: 'act', width:60, fixed:true, sortable:false, resize:false, formatter : function(cellValue, options, rowObject) {
                var e = '<a class="btn btn-primary btn-xs" href="#" mce_href="#" onclick="addRow(\'' + options.rowId + '\')"><i class="fa  fa-plus"></i></a> ';
                var d = '<a class="btn btn-warning btn-xs" href="#" mce_href="#" onclick="delRow(\''+ options.rowId + '\')"><i class="fa fa-minus"></i></a> ';
                return e + d ;
            }},
            { name:'id', index:'id', editable:false, hidedlg:true, hidden:true},
            { name:'entryName', index:'entryName', editable:true, edittype:'custom', width:300, editoptions: utils.myElementAndValue() },
            { name:'entryId', index:'entryId', editable:false, width:70 },
            { name:'entryUnit', index:'entryUnit', editable:false, width:70 },
            { name:'stockNo', index:'stockNo', editable:true, width:100, edittype:'select', editoptions: utils.formatSelect("data_stock"), formatter: "select" },
            { name:'totalQty', index:'totalQty', editable:true, width:90, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'entryPrice', index:'entryPrice', editable:true, width:100, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'entryAmount', index:'entryAmount', editable:true, width:120, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'discountRate', index:'discountRate', editable:true, width:90, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'discountAmount', index:'discountAmount', editable:true, width:120, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'purchaseFee', index:'purchaseFee', editable:true, width:120, align:"right", editoptions: utils.numberEditOptions(collectRow) },
            { name:'totalAmount', index:'totalAmount', editable:true, width:120, align:"right", editoptions: utils.commonEditOptions() },
            { name:'remark', index:'remark', editable:true, width:150, editoptions: utils.commonEditOptions() },
            { name:'requestBillNo', index:'requestBillNo', editable:false, width:200 }
        ],
        beforeSelectRow: function(rowid) {
            if (rowid !== lastSel) {
                tableGrid.jqGrid('saveRow',lastSel); // save row
                lastSel = rowid;
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

    // 自适应表格宽度
    // $(window).bind('resize', function () {
    //     var width = $('.jqGrid_wrapper').width();
    //     tableGrid.setGridWidth(width);
    // });

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
    $('[name="discountRateTotal"]').bind('blur', {amountOrder: collectValueTotal()}, utils.collectAmount);
    $('[name="discountAmountTotal"]').bind('blur', {amountOrder: collectValueTotal()}, utils.collectAmount);
    $('[name="paymentAmountTotal"]').bind('blur', {amountOrder: collectValueTotal()}, utils.collectAmount);
    $('[name="purchaseFeeTotal"]').bind('blur', {amountOrder: collectValueTotal()}, utils.collectAmount);

}

amountOrder = {
    // 订单分录汇总金额，数量、商品金额合计、优惠金额合计、采购费用合计、金额合计
    totalObj: {totalQtyTotal: 0, entryAmountTotal: 0, discountAmountTotal: 0, purchaseFeeTotal: 0, totalAmountTotal: 0},
    // 触发更新订单信息的列
    elements: {
        discountRateTotal: ['discountAmountTotal', 'finalAmountTotal', 'paymentAmountTotal', 'debtAccountTotal'],
        discountAmountTotal: ['discountRateTotal', 'finalAmountTotal', 'paymentAmountTotal', 'debtAccountTotal'],
        paymentAmountTotal: ['debtAccountTotal'],
        purchaseFeeTotal: ['debtAccountTotal']
    },
    // 订单各项信息元素（用于手工saveRow时计算）
    valueElements: ['totalQtyTotal', 'entryAmountTotal', 'discountRateTotal','discountAmountTotal', 'finalAmountTotal', 'paymentAmountTotal','purchaseFeeTotal','debtAccountTotal'],
    // 计算后的订单金额
    valueObj:{totalQtyTotal: 0, entryAmountTotal: 0, discountRateTotal: 0, discountAmountTotal: 0, finalAmountTotal: 0, paymentAmountTotal: 0, purchaseFeeTotal: 0, debtAccountTotal: 0},
    // 订单金额计算公式
    formula: {
        totalQtyTotal: 'totalObj.totalQtyTotal',
        entryAmountTotal: '(totalObj.entryAmountTotal / 1.0).toFixed(2)',
        discountRateTotal: 'totalObj.totalAmountTotal > 0 ? (valueObj.discountAmountTotal / totalObj.totalAmountTotal * 100.0).toFixed(2) : 0.0',
        discountAmountTotal: 'totalObj.totalAmountTotal > 0 ? (valueObj.discountRateTotal * totalObj.totalAmountTotal / 100.0).toFixed(2) : 0.0',
        finalAmountTotal: '(totalObj.totalAmountTotal - valueObj.discountAmountTotal).toFixed(2)',
        paymentAmountTotal: '(totalObj.totalAmountTotal - valueObj.discountAmountTotal).toFixed(2)',
        debtAccountTotal: '(math.add(totalObj.totalAmountTotal,valueObj.purchaseFeeTotal,valueObj.paymentAmountTotal * -1,valueObj.discountAmountTotal * -1)).toFixed(2)',
        purchaseFeeTotal: '(valueObj.purchaseFeeTotal / 1.0).toFixed(2)'
    }
};

function collectValueTotal() {
    var totalQtyTotal=$("#totalQtyTotal").val() || 0;
    var entryAmountTotal=$("#entryAmountTotal").val() || 0;
    var discountAmountTotal=$("#discountAmountTotal").val() || 0;
    var purchaseFeeTotal=$("#purchaseFeeTotal").val() || 0;
    var totalAmountTotal=$("#totalAmountTotal").val() || 0;
    amountOrder.valueObj = {totalQtyTotal: totalQtyTotal, entryAmountTotal: entryAmountTotal, discountAmountTotal: discountAmountTotal, purchaseFeeTotal:  purchaseFeeTotal,totalAmountTotal: totalAmountTotal };
    return amountOrder;
}

//计算表格合计行数据
function collectTotal(){
    var totalQtyTotal=$("#table_list").getCol('totalQty',false,'sum');
    var entryAmountTotal=$("#table_list").getCol('entryAmount',false,'sum').toFixed(2);
    var discountAmountTotal=$("#table_list").getCol('discountAmount',false,'sum').toFixed(2);
    var purchaseFeeTotal=$("#table_list").getCol('purchaseFee',false,'sum').toFixed(2);
    var totalAmountTotal=$("#table_list").getCol('totalAmount',false,'sum').toFixed(2);
    amountOrder.totalObj = {totalQtyTotal: totalQtyTotal, entryAmountTotal: entryAmountTotal, discountAmountTotal: discountAmountTotal, purchaseFeeTotal:  purchaseFeeTotal,totalAmountTotal: totalAmountTotal };
    // 全量计算订单各项金额
    utils.collectAmountManual(amountOrder);
    // 设置表格合计项金额
    $("#table_list").footerData('set', { entryName: '合计:', totalQty: totalQtyTotal, entryAmount: entryAmountTotal, discountAmount: discountAmountTotal, purchaseFee:  purchaseFeeTotal, totalAmount:  totalAmountTotal });
};

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
function collectRow($rowId){
    var rowId = $rowId || $("#table_list").jqGrid("getGridParam", "selrow");
    var originRow = $("#table_list tr[id="+(rowId)+"]");
    var entryPrice=originRow.find("[name='entryPrice']").val();
    var totalQty=originRow.find("[name='totalQty']").val();
    var entryAmount=originRow.find("[name='entryAmount']").val();
    var discountRate=originRow.find("[name='discountRate']").val();
    var discountAmount=originRow.find("[name='discountAmount']").val();
    var purchaseFee=originRow.find("[name='purchaseFee']").val();
    amountEntry.totalObj = {entryPrice: entryPrice, totalQty: totalQty, entryAmount: entryAmount, discountRate:  discountRate, discountAmount: discountAmount, purchaseFee: purchaseFee };
    return amountEntry;
}

//增加行
function addRow(rowid){
    var rowData = { };
    var ids = tableGrid.jqGrid('getDataIDs');
    var maxId = ids.length == 0 ? 1 : Math.max.apply(Math,ids);
    tableGrid.jqGrid('addRowData', maxId+1, rowData, 'after', rowid);//插入行
}

//删除行
function delRow(rowid) {
    var ids = tableGrid.jqGrid('getDataIDs')
    if (ids.length > 1) {
        tableGrid.jqGrid('delRowData', rowid);
    } else {
        layer.msg('至少保留一个分录',{time:1000});
    }
}

//保存
function save(add) {
    var order = dataForm.serializeObject();
    var entryArr = tableGrid.jqGrid("getRowData");
    order.entryVOList = [];

    if (order.vendorId == "" || order.billDate == "" || order.settleAccountTotal == "" || amountOrder.totalObj.totalQtyTotal <= 0) {
        layer.msg((order.vendorId == "" ? "[供应商信息]" : "") + (order.billDate == "" ? "[单据日期]" : "") + (order.settleAccountTotal == "" ? "[结算帐户]" : "")  + (amountOrder.totalObj.totalQtyTotal <= 0 ? "[合计数量]" : "") + "不能为空！");
        return;
    }

    var entryTotalAmount = 0;
    $.each(entryArr, function (key, val) {
        delete val['act'];
        if (val['entryName'] != "" && val['totalAmount'] != "") {
            entryTotalAmount = (entryTotalAmount * 1.0 + val.totalAmount * 1.0).toFixed(2);
            order.entryVOList.push(val);
        }
    });

    var validateFlag = true;
    $.each(order.entryVOList, function (key, val) {
        if (val['totalQty'] == "" || val['entryName'] == "" || val['stockNo'] == "") {
            layer.msg((val['totalQty'] == "" ? "[数量]" : "") + (val['entryName'] == "" ? "[商品]" : "") + (val['stockNo'] == "" ? "[仓库]" : "") + "列不能为空！");
            validateFlag = false;
            return false;
        }
    });

    if ((entryTotalAmount - order.finalAmountTotal - order.discountAmountTotal).toFixed(2) != 0) {
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
                if (r.code == 0 && add && add == 1) { //保存并新增
                    initBillNo();
                    tableGrid.clearGridData();
                    tableGrid.jqGrid('setGridParam', {data: [{}, {}, {}]}).trigger('reloadGrid');
                    layer.msg(r.msg);
                } else if (r.code == 0) { //新增
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
    var billNo = $("#billNo").val();
    if (billNo &&　billNo != "") {
        $.ajax({
            url : prefixOrder+"/audit",
            type : "post",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data : JSON.stringify({ 'billNos' : [billNo],  'auditStatus' : type == 0 ? 'YES' : 'NO' }),
            success : function(r) {
                if (r.code == 0) {
                    mask.removeClass('util-has-audit');
                    mask.addClass('util-has-audit');
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
    var dataIndex;
    //触发菜单单击
    window.parent.$(".J_menuItem").each(function (index) {
        if ($(this).attr('href') == dataUrl) {
            window.parent.$(this).trigger('click');
            dataIndex = window.parent.$(this).data('index');
            return false;
        }
    });
    //重新刷订单列表
    window.parent.$('.J_mainContent .J_iframe').each(function () {
        if ($(this).data('id') == dataUrl) {
            var win = window.parent.$('iframe[name="iframe' + dataIndex +'"]')[0].contentWindow;
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
    if (!datas || datas.length <= 0) return;
    var ids = tableGrid.jqGrid('getDataIDs');
    var maxId = ids.length == 0 ? 1 : Math.max.apply(Math, ids);
    var rowId = tableGrid.jqGrid("getGridParam", "selrow")
    for (var i = 0; i < datas.length; i++) {
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
    var ids = tableGrid.jqGrid('getDataIDs');
    $.each(ids, function (key, val) {
        var data = tableGrid.jqGrid("getRowData", val);
        if (data['entryName'] == "") {
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
        content : prefix + '/addVendor' // iframe的url
    });
}

//设置供应商下拉框值
function insertHead(data) {
    if (!data) return;
    $("#vendorId").val(data.no).trigger("chosen:updated");
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
                if (r.code == 0) {
                    tableGrid.clearGridData();
                    tableGrid.jqGrid('setGridParam', {data: r.order.entryVOList}).trigger('reloadGrid');
                    dataForm.setForm(r.order);
                    mask.removeClass('util-has-audit');
                    mask.addClass(r.order && r.order.auditStatus == 'YES' ? 'util-has-audit' :'');

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
        dataForm.resetForm();
        mask.removeClass('util-has-audit');
    }
}

//设置单据号隐藏域值
function initBillNo(ival) {
    ival = ival || "";
    $("[name=billNo]").each(function (index, element) {
        if ($(element).prop("tagName") == "SPAN") {
            $(element).html("单据编号: " + ival);
        } else {
            $(element).val(ival);
        }
    });
}