//
// Use internal $.serializeArray to get list of form elements which is
// consistent with $.serialize
//
// From version 2.0.0, $.serializeObject will stop converting [name] values
// to camelCase format. This is *consistent* with other serialize methods:
//
//   - $.serialize
//   - $.serializeArray
//
// If you require camel casing, you can either download version 1.0.4 or map
// them yourself.
//

(function($){
	$.fn.serializeObject = function () {
		"use strict";

		var result = {};
		var extend = function (i, element) {
			var node = result[element.name];

	// If node with same name exists already, need to convert it to an array as it
	// is a multi-value field (i.e., checkboxes)

			if ('undefined' !== typeof node && node !== null) {
				if ($.isArray(node)) {
					node.push(element.value);
				} else {
					result[element.name] = [node, element.value];
				}
			} else {
				result[element.name] = element.value;
			}
		};

		$.each(this.serializeArray(), extend);
		return result;
	};

    /**
     * 将form里面的内容序列化成json
     * 相同的checkbox用分号拼接起来
     * @param {dom} 指定的选择器
     * @param {obj} 需要拼接在后面的json对象
     * @method serializeJson
     */
    $.fn.serializeJson = function (otherString) {
        "use strict";

        var serializeObj = {}, array = this.serializeArray();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                serializeObj[this.name] += ';' + this.value;
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        if (otherString) {
            var otherArray = otherString.split(';');
            $(otherArray).each(function () {
                var otherSplitArray = this.split(':');
                serializeObj[otherSplitArray[0]] = otherSplitArray[1];
            });
        }
        return serializeObj;
    };

    /**
     * 将json对象赋值给form
     * @param {dom} 指定的选择器
     * @param {obj} 需要给form赋值的json对象
     * @method serializeJson
     */
    $.fn.setForm = function (jsonValue) {
        "use strict";

        var obj = this;
        $.each(jsonValue, function (name, ival) {
            var $oinput = obj.find("[name=" + name + "]");
            if ($oinput.attr("type") == "checkbox") {
                if (ival !== null) {
                    var checkboxObj = $("[name=" + name + "]");
                    var checkArray = ival.split(";");
                    for (var i = 0; i < checkboxObj.length; i++) {
                        for (var j = 0; j < checkArray.length; j++) {
                            if (checkboxObj[i].value == checkArray[j]) {
                                checkboxObj[i].click();
                            }
                        }
                    }
                }
            } else if ($oinput.attr("type") == "radio") {
                $oinput.each(function () {
                    var radioObj = $("[name=" + name + "]");
                    for (var i = 0; i < radioObj.length; i++) {
                        if (radioObj[i].value == ival) {
                            radioObj[i].click();
                        }
                    }
                });
            } else if ($oinput.attr("type") == "textarea") {
                obj.find("[name=" + name + "]").html(ival);
            }  else if ($oinput.attr("type") == "chosen" || $oinput.prop("type") == "select-one") {
                obj.find("[name=" + name + "]").val(ival).trigger("chosen:updated");
            } else {
                obj.find("[name=" + name + "]").val(ival);
            }
        })
    };


    /**
     * 将form对象清空,chosen下拉控件特殊处理
     */
    $.fn.resetForm = function () {
        "use strict";

        var obj = this;
        $(obj)[0].reset(); //jQuery中是没有reset()方法, 通过调用 DOM 中的reset方法来重置表单
        var $oinput = obj.find("select");
        $.each($oinput, function (name, ival) {
            if ($(ival).attr("type") == "chosen") {
                $(ival).val("").trigger("chosen:updated");
            }
        })
    };
})(jQuery);
