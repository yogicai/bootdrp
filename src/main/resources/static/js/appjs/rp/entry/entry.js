let lastSel; //jqGrid最后编辑未保存的行号
let lastSel1; //jqGrid最后编辑未保存的行号
let tableGrid;
let tableGrid1;
let $tableList;
let $tableList1;
let $dataForm;
let $mask;
let $debtorId;
let loginUserId = utils.dataCache.loginUserInfo.userId
let loginShopNo = utils.dataCache.loginShopInfo.no
let initFormData = {checkId: loginUserId, shopNo: loginShopNo, billDate: new Date().format('yyyy-MM-dd')}

let prefix = "/rp/entry";
let prefixOrder = "/rp/order";
let initData = [{}, {}, {}];

let colNames_SK = ['', 'ID', '结算帐户', '收款金额', '备注'];
let colNames_FK = ['', 'ID', '结算帐户', '付款金额', '备注'];
//var参数加附加到window对象属性中
let billType = $('#billType').val();
let colNames = _.eq(billType, 'CW_SK_ORDER') ? colNames_SK : colNames_FK;
let dataUrl = _.eq(billType, 'CW_SK_ORDER') ? '/rp/order?billType=CW_SK_ORDER' : '/rp/order?billType=CW_FK_ORDER';


$(function () {
    $mask = $('#mask');
    $dataForm = $('#data_form');
    $debtorId = $('#debtorId');
    $tableList = $('#table_list');
    $tableList1 = $('#table_list1');

    utils.createDatePicker('date_1', {}, new Date());

    if (_.eq(billType, 'CW_SK_ORDER')) {
        utils.loadCategory(["CUSTOMER_DATA", "USER_DATA"], ["debtorId", "checkId"],
            [{width: "200px", liveSearch: true, setData: []}, {width: "200px", setValue: [loginUserId]}]);
        
        utils.loadTypes(["data_shop"], ["shopNo"],
            [{width: "120px", setValue: [loginShopNo], changeOption: {types: ["CUSTOMER_DATA"], elementIds: ["debtorId"]}}]);
    } else {
        utils.loadCategory(["VENDOR_DATA", "USER_DATA"], ["debtorId", "checkId"],
            [{width: "200px", liveSearch: true, setData: []}, {width: "200px", setValue: [loginUserId]}]);

        utils.loadTypes(["data_shop"], ["shopNo"],
            [{width: "120px", setValue: [loginShopNo], changeOption: {types: ["VENDOR_DATA"], elementIds: ["debtorId"]}}]);
    }

    load();
})

function load() {
    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $tableList.jqGrid({
        datatype: "local",
        data: initData,
        height: 'auto',
        rowNum: 1000,
        shrinkToFit: false,
        autoScroll: true,
        rownumbers: true,
        rownumWidth: 30,
        footerrow: true,
        editurl: "clientArray",
        colNames: colNames,
        colModel: [
            {
                name: 'act', width: 60, fixed: true, sortable: false, resize: false, formatter: function (cellValue, options, rowObject) {
                    let e = '<a class="btn btn-primary btn-xs" href="#" onclick="addRow(' + options.rowId + ',tableGrid)"><i class="fa fa-plus"></i></a> ';
                    let d = '<a class="btn btn-warning btn-xs" href="#" onclick="delRow(' + options.rowId + ',tableGrid)"><i class="fa fa-minus"></i></a> ';
                    return e + d;
                }
            },
            {name: 'id', index: 'id', editable: false, hidedlg: true, hidden: true},
            {name: 'settleAccount', index: 'settleAccount', editable: true, width: 300, edittype: 'select', editoptions: utils.formatSelect("ACCOUNT_DATA"), formatter: "select"},
            {name: 'paymentAmount', index: 'paymentAmount', editable: true, width: 200, align: "right", editoptions: utils.commonEditOptions()},
            {name: 'remark', index: 'remark', editable: true, width: 300, editoptions: utils.commonEditOptions()}
        ],
        beforeSelectRow: function (rowid) {
            if (rowid !== lastSel) {
                tableGrid.jqGrid('saveRow', lastSel); // save row
                lastSel = rowid;
            }
            return true;
        },
        onCellSelect: function (rowId, iCol, contents, event) {
            if (iCol > 1) {
                tableGrid.jqGrid('editRow', rowId, {focusField: iCol});
            }
        },
        gridComplete: function () {
            collectTotal();
        }
    });

    tableGrid1 = $tableList1.jqGrid({
        datatype: "local",
        data: initData,
        height: 'auto',
        shrinkToFit: false,
        autoScroll: true,
        rownumbers: true,
        rownumWidth: 30,
        footerrow: true,
        editurl: "clientArray",
        colNames: ['', 'ID', '源单编号', '单据类别', '单据类别', '单据日期', '单据金额', '历史核销金额', '未核销金额', '本次核销金额'],
        colModel: [
            {
                name: 'act', width: 60, fixed: true, sortable: false, resize: false, formatter: function (cellValue, options, rowObject) {
                    let e = `<a class="btn btn-primary btn-xs" href="#" onclick="addRow('${options.rowId}')"><i class="fa fa-plus"></i></a> `;
                    let d = `<a class="btn btn-warning btn-xs" href="#" onclick="delRow('${options.rowId}')"><i class="fa fa-minus"></i></a> `
                    return e + d;
                }
            },
            {name: 'id', index: 'id', editable: false, hidedlg: true, hidden: true},
            {name: 'srcBillNo', index: 'srcBillNo', editable: true, edittype: 'custom', width: 300, editoptions: utils.myElementAndValue()},
            {name: 'srcBillType', index: 'srcBillType', editable: false, width: 100, formatter: cellValue => utils.formatEnum(cellValue, 'BILL_TYPE', '')},
            {name: 'srcBillType', index: 'srcBillType', editable: false, width: 100, hidden: true},
            {name: 'srcBillDate', index: 'srcBillDate', editable: false, width: 150},
            {name: 'srcTotalAmount', index: 'srcTotalAmount', editable: false, width: 100, align: "right"},
            {name: 'srcPaymentAmount', index: 'srcPaymentAmount', editable: false, width: 100, align: "right"},
            {name: 'srcUnPaymentAmount', index: 'srcUnPaymentAmount', editable: false, width: 100, align: "right"},
            {name: 'checkAmount', index: 'checkAmount', editable: true, width: 100, editoptions: utils.commonEditOptions()}
        ],
        beforeSelectRow: function (rowid) {
            if (rowid !== lastSel1) {
                tableGrid1.jqGrid('saveRow', lastSel1); // save row
                lastSel1 = rowid;
            }
            return true;
        },
        onCellSelect: function (rowId, iCol, contents, event) {
            if (iCol > 1) {
                tableGrid1.jqGrid('editRow', rowId, {focusField: iCol});
            }
        },
        gridComplete: function () {
            collectTotal1();
        }
    });

    // 设置横向滚动条
    $('.jqGrid_wrapper .ui-jqgrid').attr('style', 'overflow:auto');

    // 表格失去焦点保存Row数据
    $(document).click(function (e) {
        if (tableGrid.has(e.target).size() < 1 && lastSel) {
            tableGrid.jqGrid('saveRow', lastSel);  // save row
            lastSel = undefined;
            collectTotal();
        }
        if (tableGrid1.has(e.target).size() < 1 && lastSel1) {
            tableGrid1.jqGrid('saveRow', lastSel1);  // save row
            lastSel1 = undefined;
            collectTotal1();
        }
    });

    $debtorId.on('change', function (e, params) {
        clearGrid();
    });
}

//计算表格合计行数据
function collectTotal() {
    let paymentAmountTotal = $tableList.getCol('paymentAmount', false, 'sum').toFixed(2);
    // 设置表格合计项金额
    $tableList.footerData('set', {settleAccount: '合计:', paymentAmount: paymentAmountTotal});
}


//计算表格合计行数据
function collectTotal1() {
    let srcTotalAmountTotal = $tableList1.getCol('srcTotalAmount', false, 'sum').toFixed(2);
    let srcPaymentAmountTotal = $tableList1.getCol('srcPaymentAmount', false, 'sum').toFixed(2);
    let srcUnPaymentAmountTotal = $tableList1.getCol('srcUnPaymentAmount', false, 'sum').toFixed(2);
    let checkAmountTotal = $tableList1.getCol('checkAmount', false, 'sum').toFixed(2);
    // 设置表格合计项金额
    $tableList1.footerData('set', {srcBillNo: '合计:', srcTotalAmount: srcTotalAmountTotal, srcPaymentAmount: srcPaymentAmountTotal, srcUnPaymentAmount: srcUnPaymentAmountTotal, checkAmount: checkAmountTotal});
}

//增加行
function addRow(rowid) {
    let rowData = {};
    let ids = tableGrid.jqGrid('getDataIDs');
    let maxId = ids.length === 0 ? 1 : Math.max.apply(Math, ids);
    tableGrid.jqGrid('addRowData', maxId + 1, rowData, 'after', rowid);//插入行
}

//删除行
function delRow(rowid) {
    let ids = tableGrid.jqGrid('getDataIDs')
    if (ids.length > 1) {
        tableGrid.jqGrid('delRowData', rowid);
    } else {
        layer.msg('至少保留一个分录', {time: 1000});
    }
}

//新增
function addNewOrder() {
    initBillNo();
    clearGrid();
}

//保存
function save(add) {
    let order = $dataForm.serializeObject();
    let settleArr = tableGrid.jqGrid("getRowData");
    let entryArr = tableGrid1.jqGrid("getRowData");
    order.settleVOList = [];
    order.entryVOList = [];

    if (_.some([order.shopNo, order.debtorId, order.checkId, order.billDate], _.isEmpty)) {
        if (billType === 'CW_SK_ORDER') {
            layer.msg((order.shopNo === "" ? "【属性店铺】" : "") + (order.debtorId === "" ? "【零售客户】" : "") + (order.checkId === "" ? "【收款人】" : "") + (order.billDate === "" ? "【单据日期】" : "") + "不能为空！")
        } else {
            layer.msg((order.shopNo === "" ? "【属性店铺】" : "") + (order.debtorId === "" ? "【供应商】" : "") + (order.checkId === "" ? "【付款人】" : "") + (order.billDate === "" ? "【单据日期】" : "") + "不能为空！")
        }
        return;
    }
    let settleAmount = 0, checkAmount = 0;
    $.each(settleArr, function (key, val) {
        delete val['act'];
        if (!_.isEmpty(val['settleAccount'])) {
            order.settleVOList.push(val);
            settleAmount += utils.parseNumeric(val['paymentAmount'])
        }
    });
    $.each(entryArr, function (key, val) {
        delete val['act'];
        if (!_.isEmpty(val['srcBillNo'])) {
            order.entryVOList.push(val);
            checkAmount += utils.parseNumeric(val['checkAmount'])
        }
    });

    let discountAmount = utils.parseNumeric($('input[name="discountAmount"]').val());
    if (settleAmount === 0 && checkAmount === 0) {
        layer.msg("收款金额、核销金额不能都为0！");
    } else if (math.equal(settleAmount + discountAmount, checkAmount)) {
        saveAjax(order);
    } else {
        layer.confirm('收款金额' + (math.larger(settleAmount + discountAmount, checkAmount) ? '大于' : '小于') + '本次折扣后的核销金额<br/>是否仍要修改？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            saveAjax(order);
        });
    }
}

function saveAjax(order) {
    $.ajax({
        url: prefix + "/save",
        type: "post",
        // dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(order),
        success: function (r) {
            if (r.code === 0 && add === 1) { //保存并新增
                initBillNo();
                clearGrid();
            } else if (r.code === 0) {
                initBillNo(r.billNo);
            }
            layer.msg(r.msg);
        }
    });
}

//审核
function audit(type) {
    let billNo = $("#billNo").val();
    if (!_.isEmpty(billNo)) {
        $.ajax({
            url: prefixOrder + "/audit",
            type: "post",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({'billNos': [billNo], 'auditStatus': type === 0 ? 'YES' : 'NO'}),
            success: function (r) {
                $mask.removeClass('util-has-audit');
                $mask.addClass('util-has-audit');
                layer.msg(r.msg);
            }
        });
    } else {
        layer.msg('请先保存订单！');
    }
}

//历史单据
function listOrder() {
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
    if (_.isEmpty($debtorId.val())) {
        layer.msg('请先选择销货单位！');
    } else {
        layer.open({
            type: 2,
            title: '增加',
            maxmin: true,
            shadeClose: false, // 点击遮罩关闭层
            area: ['1000px', '600px'],
            content: prefix + '/add' // iframe的url
        });
    }
}

//订单表格插入数据
function insertData(datas) {
    if (_.isEmpty(datas)) return;
    let ids = tableGrid1.jqGrid('getDataIDs');
    let maxId = ids.length === 0 ? 1 : Math.max.apply(Math, ids);
    let rowId = tableGrid1.jqGrid("getGridParam", "selrow");

    tableGrid1.jqGrid('saveRow', rowId);
    for (let i = 0; i < datas.length; i++) {
        let update = false;
        $.each(ids, function (key, val) {
            let rowData = tableGrid1.jqGrid("getRowData", val);
            if (rowData['srcBillNo'] === datas[i]['srcBillNo']) {
                rowId = val;
                update = true;
                return false;
            }
        });
        if (update || i === 0) {
            tableGrid1.jqGrid('setRowData', rowId, datas[i]);//更新当前行
        } else {
            maxId = maxId + 1;
            tableGrid1.jqGrid('addRowData', maxId, datas[i], 'after', rowId);//插入行
            rowId = maxId;
        }
    }
    //选中下一个空行
    ids = tableGrid1.jqGrid('getDataIDs');
    $.each(ids, function (key, val) {
        let data = tableGrid1.jqGrid("getRowData", val);
        if (data['srcBillNo'] === "") {
            tableGrid1.setSelection(val, false);
            return false;
        }
    });
    collectTotal1();
}

//选择订单供应商弹窗
function addHead() {
    layer.open({
        type: 2,
        title: '增加',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['1000px', '600px'],
        content: prefix + '/add' // iframe的url
    });
}

//设置供应商下拉框值
function insertHead(data) {
    if (_.isEmpty(data)) return;
    $debtorId.val(data.id).trigger("change");
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
                if (r.code === 0) {
                    $dataForm.setForm(r.order);
                    tableGrid.clearGridData();
                    tableGrid.jqGrid('setGridParam', {data: r.order.settleVOList}).trigger('reloadGrid');

                    tableGrid1.clearGridData();
                    tableGrid1.jqGrid('setGridParam', {data: r.order.entryVOList}).trigger('reloadGrid');

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
        clearGrid();
        $dataForm.resetForm();
        $dataForm.setForm(initFormData);
        $mask.removeClass('util-has-audit');
    }
}

//清空表格数据
function clearGrid() {
    tableGrid.clearGridData();
    tableGrid.jqGrid('setGridParam', {data: [{}, {}, {}]}).trigger('reloadGrid');
    tableGrid1.clearGridData();
    tableGrid1.jqGrid('setGridParam', {data: [{}, {}, {}]}).trigger('reloadGrid');
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