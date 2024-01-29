$().ready(function () {
    validateRule();
    utils.loadEnumTypes(["STATUS_TYPE"], ["status"], [{width: "100%"}]);
    utils.loadCategory(["USER_DATA"], ["managerId"], [{width: "100%", liveSearch: false, multiple: true}]);
});

$.validator.setDefaults({
    submitHandler: function () {
        update();
    }
});

function update() {
    $.ajax({
        type: "POST",
        url: "/data/shop/save",
        data: $('#signupForm').serialize(),
        async: false,
        error: function (request) {
            parent.layer.alert("Connection error");
        },
        success: function (data) {
            if (data.code === 0) {
                parent.layer.msg("操作成功");
                parent.search();
                let index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);

            } else {
                parent.layer.alert(data.msg)
            }

        }
    });

}

function validateRule() {
    let icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            name: {
                required: true
            }
        },
        messages: {
            name: {
                required: icon + "请输入名字"
            }
        }
    })
}