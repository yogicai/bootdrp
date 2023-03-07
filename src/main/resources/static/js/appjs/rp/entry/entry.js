var lastSel; //jqGrid最后编辑未保存的行号
var lastSel1; //jqGrid最后编辑未保存的行号
var tableGrid;
var tableGrid1;
var dataForm;
var debtorObj;
var prefix = "/rp/entry";
var prefixOrder = "/rp/order";
var initData = [{},{},{}];
var mask;

var colNames_SK = ['','ID','结算帐户', '收款金额',  '备注'];
var colNames_FK = ['','ID','结算帐户', '付款金额',  '备注'];
var billType = $('#billType').val();
var colNames = billType == 'CW_SK_ORDER' ? colNames_SK : colNames_FK;
var dataUrl = billType == 'CW_SK_ORDER' ? '/rp/order?billType=CW_SK_ORDER' : '/rp/order?billType=CW_FK_ORDER';

$(function() {
    dataForm  = $('#data_form');
    debtorObj = $('#debtorId');
    mask = $('#mask');

    if (billType == 'CW_SK_ORDER') {
        utils.loadCategory(["CUSTOMER_DATA","USER_DATA"], ["debtorId","checkId"], [{width:"200px"},{width:"200px"}]);
    } else {
        utils.loadCategory(["VENDOR_DATA","USER_DATA"], ["debtorId","checkId"], [{width:"200px"},{width:"200px"}]);
    }

    load();
});

function load() {

    utils.createDatePicker('date_1');

    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        datatype : "local",
        data: initData,
        height: 'auto',
        rowNum:1000,
        shrinkToFit: false,
        autoScroll: true,
        rownumbers:true,
        rownumWidth:30,
        footerrow : true,
        editurl: "clientArray",
        colNames: colNames,
        colModel: [
            { name: 'act', width:60, fixed:true, sortable:false, resize:false, formatter : function(cellValue, options, rowObject) {
                    var e = '<a class="btn btn-primary btn-xs" href="#" mce_href="#" onclick="addRow(' + options.rowId + ',tableGrid)"><i class="fa fa-plus"></i></a> ';
                    var d = '<a class="btn btn-warning btn-xs" href="#" mce_href="#" onclick="delRow('+ options.rowId + ',tableGrid)"><i class="fa fa-minus"></i></a> ';
                    return e + d ;
                }},
            { name:'id', index:'id', editable:false, hidedlg:true, hidden:true},
            { name:'settleAccount', index:'settleAccount', editable:true, width:300, edittype:'select', editoptions: utils.formatSelect("ACCOUNT_DATA"), formatter: "select" },
            { name:'paymentAmount', index:'paymentAmount', editable:true, width:200, editoptions: utils.commonEditOptions()  },
            { name:'remark', index:'remark', editable:true, width:300, editoptions: utils.commonEditOptions()  }
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

    tableGrid1 = $("#table_list1").jqGrid({
        datatype : "local",
        data: initData,
        height: 'auto',
        shrinkToFit: false,
        autoScroll: true,
        rownumbers:true,
        rownumWidth:30,
        footerrow : true,
        editurl: "clientArray",
        colNames: ['','ID','源单编号', '单据类别', '单据类别', '单据日期', '单据金额', '已核销金额', '未核销金额', '本次核销金额'],
        colModel: [
            { name: 'act', width:60, fixed:true, sortable:false, resize:false, formatter : function(cellValue, options, rowObject) {
                    var e = '<a class="btn btn-primary btn-xs" href="#" mce_href="#" onclick="addRow(' + options.rowId + ', tableGrid1)"><i class="fa  fa-plus"></i></a> ';
                    var d = '<a class="btn btn-warning btn-xs" href="#" mce_href="#" onclick="delRow('+ options.rowId + ', tableGrid1)"><i class="fa fa-minus"></i></a> ';
                    return e + d ;
                }},
            { name:'id', index:'id', editable:false, hidedlg:true, hidden:true},
            { name:'srcBillNo', index:'srcBillNo', editable:true, edittype:'custom', width:300, editoptions: utils.myElementAndValue() },
            { name:'srcBillType', index:'srcBillType', editable:false, width:100, formatter:function (cellValue){return utils.formatEnum(cellValue, 'BILL_TYPE')} },
            { name:'srcBillType', index:'srcBillType', editable:false, width:100, hidden:true },
            { name:'srcBillDate', index:'srcBillDate', editable:false, width:150 },
            { name:'srcTotalAmount', index:'srcTotalAmount', editable:false, width:100 },
            { name:'srcPaymentAmount', index:'srcPaymentAmount', editable:false, width:100 },
            { name:'srcUnPaymentAmount', index:'srcUnPaymentAmount', editable:false, width:100 },
            { name:'checkAmount', index:'checkAmount', editable:true, width:100, editoptions: utils.commonEditOptions() }
        ],
        beforeSelectRow: function(rowid) {
            if (rowid !== lastSel1) {
                tableGrid1.jqGrid('saveRow',lastSel1); // save row
                lastSel1 = rowid;
            }
            return true;
        },
        onCellSelect: function(rowId,iCol,contents,event) {
            if (iCol > 1) {
                tableGrid1.jqGrid('editRow', rowId, { focusField: iCol });
            }
        },
        gridComplete: function () {
            collectTotal1();
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
        if(tableGrid1.has(e.target).size() < 1 &&　lastSel1) {
            tableGrid1.jqGrid('saveRow', lastSel1);  // save row
            lastSel1 = undefined;
            collectTotal1();
        }
    });

    debtorObj.on('change', function(e, params) {
        clearGrid();
    });
}

//计算表格合计行数据
function collectTotal(){
    var paymentAmountTotal=$("#table_list").getCol('paymentAmount',false,'sum').toFixed(2);
    // 设置表格合计项金额
    $("#table_list").footerData('set', { settleAccount: '合计:', paymentAmount: paymentAmountTotal});
};


//计算表格合计行数据
function collectTotal1(){
    var srcTotalAmountTotal=$("#table_list1").getCol('srcTotalAmount',false,'sum').toFixed(2);
    var srcPaymentAmountTotal=$("#table_list1").getCol('srcPaymentAmount',false,'sum').toFixed(2);
    var srcUnPaymentAmountTotal=$("#table_list1").getCol('srcUnPaymentAmount',false,'sum').toFixed(2);
    var checkAmountTotal=$("#table_list1").getCol('checkAmount',false,'sum').toFixed(2);
    // 设置表格合计项金额
    $("#table_list1").footerData('set', { srcBillNo: '合计:', srcTotalAmount: srcTotalAmountTotal, srcPaymentAmount: srcPaymentAmountTotal, srcUnPaymentAmount: srcUnPaymentAmountTotal, checkAmount:  checkAmountTotal });
};

//增加行
function addRow(rowid, tableGrid){
    var rowData = { };
    var ids = tableGrid.jqGrid('getDataIDs');
    var maxId = ids.length == 0 ? 1 : Math.max.apply(Math,ids);
    tableGrid.jqGrid('addRowData', maxId+1, rowData, 'after', rowid);//插入行
}

//删除行
function delRow(rowid, tableGrid) {
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
    var settleArr = tableGrid.jqGrid("getRowData");
    var entryArr = tableGrid1.jqGrid("getRowData");
    order.settleVOList = [];
    order.entryVOList = [];

    if (order.debtorId == "" || order.checkId == "" || order.billDate == "") {
        if (billType == 'CW_SK_ORDER') {
            layer.msg((order.debtorId == "" ? "【零售客户】" : "") + (order.checkId == "" ? "【收款人】" : "") + (order.billDate == "" ? "【单据日期】" : "") + "不能为空！")
        } else {
            layer.msg((order.debtorId == "" ? "【供应商】" : "") + (order.checkId == "" ? "【付款人】" : "") + (order.billDate == "" ? "【单据日期】" : "") + "不能为空！")
        }
        return;
    }
    var settleAmount = 0, checkAmount = 0;
    $.each(settleArr, function (key, val) {
        delete val['act'];
        if (val['settleAccount'] != "") {
            order.settleVOList.push(val);
            settleAmount += parseFloat((val['paymentAmount'] * 1.0).toFixed(2))
        }
    });
    $.each(entryArr, function (key, val) {
        delete val['act'];
        if (val['srcBillNo'] != "") {
            order.entryVOList.push(val);
            checkAmount += parseFloat((val['checkAmount'] * 1.0).toFixed(2))
        }
    });

    var discountAmount = parseFloat($('input[name="discountAmount"]').val());
    if (settleAmount == 0 && checkAmount == 0) {
        layer.msg("收款金额、核销金额不能都为0！");
    } else if (math.equal(settleAmount + discountAmount, checkAmount)) {
        saveAjax(order);
    } else {
        layer.confirm('收款金额' + (math.larger(settleAmount + discountAmount, checkAmount) ? '大于' : '小于') + '本次折扣后的核销金额<br/>是否仍要修改？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            saveAjax(order);
        });
    }
}

function saveAjax(order) {
    $.ajax({
        url : prefix+"/save",
        type : "post",
        // dataType: "json",
        contentType: "application/json; charset=utf-8",
        data : JSON.stringify(order),
        success : function(r) {
            if (r.code == 0 && add == 1) { //保存并新增
                initBillNo();
                clearGrid();
            } else if (r.code == 0) {
                initBillNo(r.billNo);
            }
            layer.msg(r.msg);
        }
    });
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
                mask.removeClass('util-has-audit');
                mask.addClass('util-has-audit');
                layer.msg(r.msg);
            }
        });
    } else {
        layer.msg('请先保存订单！');
    }
}

//历史单据
function listOrder() {
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
    if (debtorObj.val() == "") {
        layer.msg('请先选择销货单位！');
    } else {
        layer.open({
            type : 2,
            title : '增加',
            maxmin : true,
            shadeClose : false, // 点击遮罩关闭层
            area : [ '1000px', '600px' ],
            content : prefix + '/add' // iframe的url
        });
    }
}

//订单表格插入数据
function insertData(datas) {
    if (!datas || datas.length <= 0) return;
    var ids = tableGrid1.jqGrid('getDataIDs');
    var maxId = ids.length == 0 ? 1 : Math.max.apply(Math, ids);
    var rowId = tableGrid1.jqGrid("getGridParam", "selrow");

    tableGrid1.jqGrid('saveRow', rowId);
    for (var i = 0; i < datas.length; i++) {
        var update = false;
        $.each(ids, function (key, val) {
            var rowData = tableGrid1.jqGrid("getRowData", val);
            if (rowData['srcBillNo'] == datas[i]['srcBillNo']) {
                rowId = val; update = true; return false;
            }
        });
        if (update || i == 0) {
            tableGrid1.jqGrid('setRowData', rowId, datas[i]);//更新当前行
        } else {
            maxId = maxId + 1;
            tableGrid1.jqGrid('addRowData', maxId, datas[i], 'after', rowId);//插入行
            rowId = maxId;
        }
    }
    //选中下一个空行
    var ids = tableGrid1.jqGrid('getDataIDs');
    $.each(ids, function (key, val) {
        var data = tableGrid1.jqGrid("getRowData", val);
        if (data['srcBillNo'] == "") {
            tableGrid1.setSelection(val, false);
            return false;
        }
    });
    collectTotal1();
}

//选择订单供应商弹窗
function addHead() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '1000px', '600px' ],
        content : prefix + '/add' // iframe的url
    });
}

//设置供应商下拉框值
function insertHead(data) {
    if (!data) return;
    $("#consumerId").val(data.id).trigger("chosen:updated");
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
                    tableGrid.jqGrid('setGridParam', {data: r.order.settleVOList}).trigger('reloadGrid');

                    tableGrid1.clearGridData();
                    tableGrid1.jqGrid('setGridParam', {data: r.order.entryVOList}).trigger('reloadGrid');

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
        clearGrid();
        dataForm.resetForm();
        mask.removeClass('util-has-audit');
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