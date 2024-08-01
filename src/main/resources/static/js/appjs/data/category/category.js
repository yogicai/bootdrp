let prefix = "/data/category"
let treeTable;
let $searchForm;
let $exampleTable;

let defaultType = 'CUSTOMER';

$(function () {
    $searchForm = $('#search');
    $exampleTable = $('#exampleTable');

    utils.createDateRangePicker('datepicker');
    utils.loadEnumTypes(["CATEGORY_TYPE", "STATUS_TYPE"], ["type", "status"], [{width: "120px", setValue: [defaultType]}, {width: "120px"}]);

    load();
});

function load() {
    treeTable = $exampleTable.bootstrapTreeTable({
        id: 'categoryId',
        code: 'categoryId',
        parentCode: 'parentId',
        type: "GET", // 请求数据的ajax类型
        url: prefix + '/list', // 请求数据的ajax的url
        ajaxParams: {'type': defaultType}, // 请求数据的ajax的data属性
        expandColumn: '1', // 在哪一列上面显示展开按钮
        striped: true, // 是否各行渐变色
        bordered: true, // 是否显示边框
        expandAll: false, // 是否全部展开
        // toolbar : '#exampleToolbar',
        columns: [
            {field: 'categoryId', title: '编号', visible: false, align: 'center', valign: 'middle', width: '50px'},
            {field: 'name', title: '名称'},
            {
                field: 'type', title: '分类', formatter: function (item, index) {
                    return utils.formatEnum(item.type, 'CATEGORY_TYPE')
                }
            },
            {field: 'orderNum', title: '排序'},
            {
                field: 'status', title: '状态', align: 'center', formatter: function (item, index) {
                    return utils.formatYN(item.status)
                }
            },
            {
                title: '操作', field: 'id', align: 'center',
                formatter: function (item, index) {
                    let e = `<a class="btn btn-primary btn-sm ${s_edit_h}" href="#" title="编辑" onclick="edit('${item.categoryId}')"><i class="fa fa-edit"></i></a> `;
                    let a = `<a class="btn btn-primary btn-sm ${s_add_h}" href="#" title="增加下級" onclick="edit('${item.categoryId}')"><i class="fa fa-plus"></i></a> `;
                    let d = `<a class="btn btn-warning btn-sm ${s_remove_h}" href="#" title="删除" onclick="remove('${item.categoryId}')"><i class="fa fa-remove"></i></a> `;
                    let f = `<a class="btn btn-success btn-sm" href="#" title="备用" onclick="resetPwd('${item.categoryId}')"><i class="fa fa-key"></i></a> `;
                    return e + a + d;
                }
            }
        ]
    });
}

function search() {
    treeTable.load($searchForm.serializeObject());
}

function add(id) {
    layer.open({
        type: 2,
        title: '增加',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '460px'],
        content: prefix + '/add/' + id // iframe的url
    });
}

function edit(id) {
    layer.open({
        type: 2,
        title: '编辑',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '460px'],
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
            data: {'categoryId': id},
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
            ids[i] = row['categoryId'];
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