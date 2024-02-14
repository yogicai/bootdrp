let prefix = "/data/product";
let $searchForm;
let $exampleTable;

$(function () {
    $searchForm = $('#search');
    $exampleTable = $('#exampleTable');

    utils.createDateRangePicker('datepicker');
    utils.loadCategory(["PRODUCT"], ["type"], [{width: "120px"}]);
    utils.loadEnumTypes(["STATUS_TYPE"], ["status"], [{width: "120px", setValue: [1]}]);
    utils.loadTypes(["data_shop"], ["shopNo"], [{width: "120px"}]);

    load();
});

function load() {
    $exampleTable.bootstrapTable(
        {
            method: 'get', // 服务器数据的请求方式 get or post
            url: prefix + "/list", // 服务器数据的加载地址
            //	showRefresh : true,
            //	showToggle : true,
            //	showColumns : true,
            iconSize: 'outline',
            toolbar: '#exampleToolbar',
            striped: true, // 设置为true会有隔行变色效果
            dataType: "json", // 服务器返回的数据类型
            pagination: true, // 设置为true会在底部显示分页条
            // queryParamsType : "limit",
            // //设置为limit则会发送符合RESTFull格式的参数
            singleSelect: false, // 设置为true将禁止多选
            // contentType : "application/x-www-form-urlencoded",
            // //发送到服务器的数据编码类型
            pageSize: 10, // 如果设置了分页，每页数据条数
            pageNumber: 1, // 如果设置了分布，首页页码
            //search : true, // 是否显示搜索框
            showColumns: false, // 是否显示内容下拉框（选择显示的列）
            sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
            queryParams: function (params) {
                let param = {
                    //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                    limit: params.limit,
                    offset: params.offset
                    // name:$('#searchName').val(),
                    // username:$('#searchName').val()
                };
                return $.extend({}, param, $searchForm.serializeObject());
            },
            // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
            // queryParamsType = 'limit' ,返回参数必须包含
            // limit, offset, search, sort, order 否则, 需要包含:
            // pageSize, pageNumber, searchText, sortName,
            // sortOrder.
            // 返回false将会终止请求
            columns: [
                {checkbox: true},
                {field: 'no', title: '编号'},
                {field: 'name', title: '商品名称'},
                {field: 'barCode', title: '条形码'},
                {field: 'type', title: '类别', formatter: (value, row, index) => utils.formatCategory(value, "PRODUCT")},
                {field: 'brand', title: '品牌', formatter: (value, row, index) => utils.formatType(value, "data_brand")},
                {field: 'unit', title: '单位', formatter: (value, row, index) => utils.formatType(value, "data_unit")},
                {field: 'purchasePrice', title: '采购价'},
                {field: 'salePrice', title: '零售价'},
                {field: 'stockNo', title: '首选仓库', formatter: (value, row, index) => utils.formatType(value, "data_stock")},
                {field: 'shopNo', title: '店铺', formatter: (value, row, index) => utils.formatType(value, "data_shop")},
                {field: 'status', title: '状态', formatter: utils.formatYN},
                {field: 'createTime', title: '创建时间'},
                {field: 'updateTime', title: '修改时间'},
                {
                    title: '操作', field: 'id', align: 'center', formatter: (value, row, index) => {
                        let e = `<a class="btn btn-primary btn-sm ${s_edit_h}" href="#" title="编辑" onclick="edit('${row.id}')"><i class="fa fa-edit"></i></a> `;
                        let d = `<a class="btn btn-warning btn-sm ${s_remove_h}" href="#" title="删除" onclick="remove('${row.id}')"><i class="fa fa-remove"></i></a> `;
                        let f = `<a class="btn btn-success btn-sm" href="#" title="备用" onclick="resetPwd('${row.id}')"><i class="fa fa-key"></i></a> `;
                        return e + d;
                    }
                }]
        });
}

function search() {
    $exampleTable.bootstrapTable('refresh', {query: $.extend({offset: 0}, $searchForm.serializeObject())});
}

function add() {
    layer.open({
        type: 2,
        title: '增加',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/add' // iframe的url
    });
}

function edit(id) {
    layer.open({
        type: 2,
        title: '编辑',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/edit/' + id // iframe的url
    });
}

function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {'id': id},
            success: function (r) {
                if (r.code === 0) {
                    layer.msg(r.msg);
                    search();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function resetPwd(id) {
}

function batchRemove() {
    let rows = $exampleTable.bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length === 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        let ids = [];
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {"ids": ids},
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code === 0) {
                    layer.msg(r.msg);
                    search();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {

    });
}