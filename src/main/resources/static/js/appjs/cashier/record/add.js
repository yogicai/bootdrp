$().ready(function() {
	validateRule();
	utils.createDateTimePicker('tradeTime');
	utils.loadTypes(["data_shop"], ["shopNo"], [{width: "100%"}]);
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/cashier/record/save",
		data : $('#signupForm').serialize(),// 你的formid
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

function validateRule() {
	let icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules: {
			tradeTime: { required: true },
			account: { required: true },
			targetName: { required: true },
			tradeGoods: { required: true },
			payDirect: { required: true },
			tradeType: { required: true },
		},
		messages: {
			tradeTime: { required: icon + "请输入" },
			account: { required: icon + "请输入" },
			targetName: { required: icon + "请输入" },
			tradeGoods: { required: icon + "请输入" },
			payDirect: { required: icon + "请输入" },
			tradeType: { required: icon + "请输入" },
		}
	})
}