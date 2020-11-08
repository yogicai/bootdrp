var prefix = "/cashier/journal";
var tableGrid;
var recordDate;
var recordDateS;
var recordDateE;
$(function() {
    utils.loadCategory(["ACCOUNT_DATA"], ["account"], [{width:"120px"}]);

    load();
});

function load() {
    recordDate = utils.createDateRangePicker('datepicker', {
        format: 'yyyy年mm期',
        startView: 'months',
        maxViewMode:'years',
        minViewMode:'months'
    }, new Date(),new Date());

    recordDateS= recordDate.data("datepicker").pickers[0];
    recordDateE= recordDate.data("datepicker").pickers[1];

    $.jgrid.defaults.styleUI = 'Bootstrap';

    tableGrid = $("#table_list").jqGrid({
        url: prefix + "/list",
        datatype: "json",
        postData: $.extend({}, $('#search').serializeObject(), {'start' : recordDateS.getDate().format('yyyy-MM-dd'), 'end' : recordDateE.getDate().format('yyyy-MM-dd')}),
        height: window.innerHeight - 170,
        autowidth: true,
        shrinkToFit: true,
        multiselect: true,
        footerrow : true,
        viewrecords: true,
        rowNum: -1,
        colNames: ['编号', '日期', '摘要', '收入', '支出', '余额', '备注'],
        colModel: [
            { name:'id', index:'id', editable:false, width:90, hidedlg:true, hidden:true },
            { name:'date', index:'date', editable:true, edittype:'custom', width:60, formatter:"date",formatoptions: {newformat:'Y-m-d'} },
            { name:'digest', index:'digest', editable:true, sorttype:"text", width:150 },
            { name:'debit', index:'debit', editable:true, sorttype:"float", width:90, align:"right", formatter:"number" },
            { name:'credit', index:'credit', editable:true, sorttype:"float", width:90, align:"right", formatter:"number" },
            { name:'balance', index:'balance', editable:true, sorttype:"float", width:90, align:"right", formatter:"number" },
            { name:'remark', index:'remark', editable:true, sorttype:"text", width:120 }
        ],
        gridComplete: function () {
            collectTotal();
        }
    });

    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#table_list').setGridWidth(width);
    });
}

//计算表格合计行数据
function collectTotal(){
    var debitAmountTotal=$("#table_list").getCol('debit',false,'sum');
    var creditAmountTotal=$("#table_list").getCol('credit',false,'sum');
    var balanceAmountTotal=$("#table_list").getCol('balance',false,'sum');
    var totalAmountObj = { date: '合计:', debit: debitAmountTotal, credit: creditAmountTotal, balance: balanceAmountTotal };
    // 设置表格合计项金额
    $("#table_list").footerData('set', totalAmountObj);
};
function reLoad() {
    search();
}
function search() {
    var postData = $.extend({}, $('#search').serializeObject(), { 'start' : recordDateS.getDate().format('yyyy-MM-dd'), 'end' : recordDateE.getDate().format('yyyy-MM-dd') });
    tableGrid.jqGrid('setGridParam', {postData:  $.param(postData)}).trigger("reloadGrid");
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
function edit() {
    var ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length != 1) {
        layer.msg(ids.length == 0 ? "请选择要修改的记录" : "一次只能修改一条记录");
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
    var ids = tableGrid.jqGrid("getGridParam", "selarrrow");
    if (ids.length == 0) {
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