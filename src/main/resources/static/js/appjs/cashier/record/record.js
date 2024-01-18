let prefix = "/cashier/record";
let dataForm;
let tableGrid;
let recordDate;
let loading;
$(function() {
    dataForm = $("#search");
    initMultiSelect();
    load();
});

function initMultiSelect() {
    utils.loadMultiSelect(
        ["type","account","payDirect","payStatus","tradeClass","source","costType"],
        ["type","account","payDirect","payStatus","tradeClass","source","costType"],
        [{nonSelectedText: '交易渠道'},{nonSelectedText: '交易账号'},{nonSelectedText: '交易方向'},{nonSelectedText: '交易状态'},{nonSelectedText: '交易类型'},{nonSelectedText: '数据来源'},{nonSelectedText: '资金用途'}],
        prefix + '/multiSelect'
    )
}

function load() {
    recordDate = utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());

    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/list",
        datatype: "json",
        postData: dataForm.serializeObject(),
        height: window.innerHeight - 210,
        autowidth: true,
        shrinkToFit: true,
        multiselect: true, //自带多选
        multiboxonly: true, //变成单选
        rownumbers: true, //行号
        rowNum: 20,
        rowList: [20, 50, 100],
        colNames: ['编号', '昵称', '账号', '渠道', '交易时间', '交易分类', '交易对方', '交易对方账户', '商品说明', '交易方式', '资金用途', '金额(元)', '收/支', '交易状态', '数据来源'],
        colModel: [
            { name:'id', index:'id', editable:false, width:90, hidedlg:true, hidden:true },
            { name:'nick', index:'nick', editable:true, sorttype:"text", width:70 },
            { name:'account', index:'account', editable:true, sorttype:"text", width:100 },
            { name:'type', index:'type', editable:true, sorttype:"text", width:60 },
            { name:'tradeTime', index:'tradeTime', editable:true, edittype:'custom', width:120 },
            { name:'tradeClass', index:'tradeClass', editable:true, sorttype:"text", width:80 },
            { name:'targetName', index:'targetName', editable:true, sorttype:"text", width:120 },
            { name:'targetAccount', index:'targetAccount', editable:true, sorttype:"text", width:120 },
            { name:'tradeGoods', index:'tradeGoods', editable:true, sorttype:"text", width:120 },
            { name:'tradeType', index:'tradeType', editable:true, sorttype:"text", width:60 },
            { name:'costType', index:'costType', editable:true, sorttype:"text", width:80 },
            { name:'payAmount', index:'payAmount', editable:true, sorttype:"float", width:80, align:"right", formatter: utils.priceFormat},
            { name:'payDirect', index:'payDirect', editable:true, sorttype:"text", width:50 },
            { name:'payStatus', index:'payStatus', editable:true, sorttype:"text", width:80 },
            { name:'source', index:'source', editable:true, sorttype:"text", width:80 }
        ],
        pager: "#pager_list",
        viewrecords: true, //是否显示总记录数
        footerrow: true,
        beforeSelectRow: function (rowid) {
            // tableGrid.jqGrid('resetSelection');
            // return true;
        },
        ondblClickRow: function (rowid, iRow, iCol, e) {
            edit([rowid]);
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

    $("button#dropz").dropzone({
        id: "#dropz",
        url: prefix + "/importCsv",
        method: "post",  // 也可用put
        paramName: "file", // 提交的参数,默认为file
        disablePreviews: true,
        addedfile: function (file) {
            parent.layer.load(1, {
                shadeClose: false,
                title: '加载中..',
                shade: [0.5,'#000']
            });
        },
        success: function (file, message) {
            parent.layer.msg(message.msg);
            parent.layer.closeAll('loading');
        },
        queuecomplete: function () {
            //全部上传后重置文件队列
            Dropzone.forElement("#dropz").removeAllFiles(true);
            reLoad();
            initMultiSelect();
        },
        error: function (file, message) {
            parent.layer.closeAll('loading');
        }
    });
}

//计算表格合计行数据
function collectTotal(data){
    //全量行数
    let totalCount = tableGrid.jqGrid('getGridParam', 'records')
    //全量合计
    let totalAmount = data['totalAmount'];
    //本页行数
    let recordNum = tableGrid.jqGrid('getGridParam', 'reccount');
    //本页合计
    let payAmountTotal=tableGrid.getCol('payAmount',false,'sum');
    let totalAmountObj = {
        tradeTime:'本页合计：',
        tradeClass: `${recordNum}笔`,
        targetName: `${utils.priceFormat(payAmountTotal)}`,
        tradeType: `总计：` ,
        costType: `${totalCount}笔`,
        payAmount: `${utils.priceFormat(totalAmount)}`
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
    let postData = $.extend({}, dataForm.serializeObject(), { 'page': inputPage, 'rows': rowNum });

    Object.keys(postData).forEach((element, index, array) => {
        if (Array.isArray(postData[element])) {
            postData[element] = postData[element].join();
        }
    });

    tableGrid.jqGrid('setGridParam', {postData:  $.param(postData)}).trigger("reloadGrid");
}

function reLoad() {
    tableGrid.trigger("reloadGrid", { fromServer: true });
}

function add() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/add/' // iframe的url
    });
}
function edit(ids) {
    if (!ids || ids.length === 0) {
        ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    }
    if (ids.length !== 1) {
        layer.msg(ids.length === 0 ? "请选择要修改的记录" : "一次只能修改一条记录");
        return;
    }
    layer.open({
        type : 2,
        title : '编辑',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/edit/' + ids[0] // iframe的url
    });
}

function remove() {
    let ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length === 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm('确定要删除选中的记录？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix+"/remove",
            type : "post",
            data : {
                'ids' : ids
            },
            success : function(r) {
                if (r.code===0) {
                    layer.msg(r.msg);
                    search();
                }else{
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function exportExcel() {
    let queryParam = dataForm.serialize();
    let url = prefix + "/export?" + queryParam //下载地址
    utils.downloadAjax(url ,'TradeRecord.xls')
}
