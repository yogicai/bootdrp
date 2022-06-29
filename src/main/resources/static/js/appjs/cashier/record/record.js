let prefix = "/cashier/record";
let tableGrid;
let recordDate;
let recordDateS;
let recordDateE;
$(function() {
    utils.loadMultiSelect(
        ["type","account","payDirect","payStatus","tradeClass","source"],
        ["type","account","payDirect","payStatus","tradeClass","source"],
        [{nonSelectedText: '交易渠道'},{nonSelectedText: '交易账号'},{nonSelectedText: '交易方向'},{nonSelectedText: '交易状态'},{nonSelectedText: '交易类型'},{nonSelectedText: '数据来源'}],
        prefix + '/multiSelect'
    );
    dataForm = $("#search");
    load();
});

function load() {
    recordDate = utils.createDateRangePicker('datepicker', {}, utils.getYearFirstDay(), new Date());
    recordDateS= recordDate.data("datepicker").pickers[0];
    recordDateE= recordDate.data("datepicker").pickers[1];

    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/list",
        datatype: "json",
        postData: $.extend({}, $('#search').serializeObject(), {'start' : recordDateS.getDate().format('yyyy-MM-dd'), 'end' : recordDateE.getDate().format('yyyy-MM-dd')}),
        height: window.innerHeight - 210,
        autowidth: true,
        shrinkToFit: true,
        multiselect: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        colNames: ['编号', '昵称', '账号', '渠道', '交易时间', '交易分类', '交易对方', '交易对方账户', '商品说明', '交易方式', '金额(元)', '收/支', '交易状态', '数据来源'],
        colModel: [
            { name:'id', index:'id', editable:false, width:90, hidedlg:true, hidden:true },
            { name:'nick', index:'nick', editable:true, sorttype:"text", width:70 },
            { name:'account', index:'account', editable:true, sorttype:"text", width:100 },
            { name:'type', index:'type', editable:true, sorttype:"text", width:70 },
            { name:'tradeTime', index:'tradeTime', editable:true, edittype:'custom', width:120 },
            { name:'tradeClass', index:'tradeClass', editable:true, sorttype:"text", width:80 },
            { name:'targetName', index:'targetName', editable:true, sorttype:"text", width:150 },
            { name:'targetAccount', index:'targetAccount', editable:true, sorttype:"text", width:150, hidden: true },
            { name:'tradeGoods', index:'tradeGoods', editable:true, sorttype:"text", width:150 },
            { name:'tradeType', index:'tradeType', editable:true, sorttype:"text", width:80 },
            { name:'payAmount', index:'payAmount', editable:true, sorttype:"float", width:90, align:"right" },
            { name:'payDirect', index:'payDirect', editable:true, sorttype:"text", width:80 },
            { name:'payStatus', index:'payStatus', editable:true, sorttype:"text", width:80 },
            { name:'source', index:'source', editable:true, sorttype:"text", width:80 }
        ],
        pager: "#pager_list",
        viewrecords: true, //是否显示总记录数
        footerrow: true,
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
        success: function (file, message) {
            parent.layer.msg(message.msg);
        },
        queuecomplete: function () {
            //全部上传后重置文件队列
            Dropzone.forElement("#dropz").removeAllFiles(true);
        },
        error: function (file, message) {
            parent.layer.msg(message);
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
        targetName: `${utils.priceFormat(payAmountTotal)}元`,
        tradeGoods: `总计：` ,
        tradeType: `${totalCount}笔`,
        payAmount: `${utils.priceFormat(totalAmount)}元`
    };
    // 设置表格合计项金额
    tableGrid.footerData('set', totalAmountObj);
}

function search(pageBtn) {
    var inputPage = 1;
    var rowNum = tableGrid.getGridParam('rowNum');//获取每页数
    var curPage = tableGrid.getGridParam('page');//获取返回的当前页
    var totalPage = tableGrid.getGridParam('lastpage');//获取总页数
    if (pageBtn == 'first') {
        inputPage = 0;
    } else if (pageBtn == 'last') {
        inputPage = totalPage;
    } else if (pageBtn == 'prev') {
        inputPage = curPage - 1;
    } else if (pageBtn == 'next') {
        inputPage = curPage + 1;
    } else if (pageBtn == 'user') {
        inputPage = $('.ui-pg-input').val();//输入框页数
    } else if (pageBtn == 'records') {
        rowNum = $('.ui-pg-selbox').val();//输入框页数
    }
    inputPage = inputPage > totalPage ? totalPage : inputPage;
    inputPage = inputPage < 1 ? 1 : inputPage;
    let postData = $.extend({}, $('#search').serializeObject(), { 'page': inputPage, 'rows': rowNum });
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
                if (r.code==0) {
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
    utils.download(url ,'TradeRecord.xls')
}
