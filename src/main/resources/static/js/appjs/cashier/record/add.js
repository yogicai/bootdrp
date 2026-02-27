$().ready(function() {
	utils.createDateTimePicker('tradeTime');
	utils.loadTypes(["data_shop"], ["shopNo"], [{width: "100%"}]);
	utils.loadSelectStatic(["type", "payDirect", "costType", "tradeType"], [{width: "100%"}, {width: "100%"}, {width: "100%"}, {width: "100%"}]);
	validateRule();
});

function validateRule() {
	$("#signupForm").validate({
		submitHandler: function () {
			save();
		}
	})
}

function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/cashier/record/save",
		data : $('#signupForm').serialize(),
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				let index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});
}