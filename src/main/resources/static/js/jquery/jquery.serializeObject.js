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

(function ($) {
    $.fn.serializeObject = function () {
        "use strict";

        let result = {};
        let extend = function (i, element) {
            let node = result[element.name];

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
     * @method serializeJson
     * @param otherString
     */
    $.fn.serializeJson = function (otherString) {
        "use strict";

        let serializeObj = {}, array = this.serializeArray();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                serializeObj[this.name] += ';' + this.value;
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        if (otherString) {
            let otherArray = otherString.split(';');
            $(otherArray).each(function () {
                let otherSplitArray = this.split(':');
                serializeObj[otherSplitArray[0]] = otherSplitArray[1];
            });
        }
        return serializeObj;
    };

    /**
     * 将json对象赋值给form
     * @method serializeJson
     * @param jsonValue
     */
    $.fn.setForm = function (jsonValue) {
        "use strict";

        let obj = this;

        $.each(jsonValue, function (name, value) {
            let $input = obj.find("[name=" + name + "]");

            $.each($input, function (index, item) {
                let $item = $(item);
                if ($item.attr("type") === "checkbox") {
                    if (value !== null) {
                        let checkboxObj = $("[name=" + name + "]");
                        let checkArray = value.split(";");
                        for (let i = 0; i < checkboxObj.length; i++) {
                            for (let j = 0; j < checkArray.length; j++) {
                                if (checkboxObj[i].value === checkArray[j]) {
                                    checkboxObj[i].click();
                                }
                            }
                        }
                    }
                } else if ($item.attr("type") === "radio") {
                    $item.each(function () {
                        let radioObj = $("[name=" + name + "]");
                        for (let i = 0; i < radioObj.length; i++) {
                            if (radioObj[i].value === value) {
                                radioObj[i].click();
                            }
                        }
                    });
                } else if ($item.attr("type") === "textarea") {
                    obj.find("[name=" + name + "]").html(value);
                } else if ($item.parent().hasClass('bootstrap-select')) {
                    obj.find("[name=" + name + "]").val(value).trigger("change");
                } else if ($item.attr("type") === "chosen" || $item.prop("type") === "select-one") {
                    obj.find("[name=" + name + "]").val(value).trigger("chosen:updated");
                } else if ($item.attr("type") === "span") {
                    obj.find("[name=" + name + "]").html(value);
                } else {
                    obj.find("[name=" + name + "]").val(value);
                }
            });
        });
    };

    /**
     * 将form对象清空,chosen下拉控件特殊处理
     */
    $.fn.resetForm = function () {
        "use strict";

        let obj = this;
        $(obj)[0].reset(); //jQuery中是没有reset()方法, 通过调用 DOM 中的reset方法来重置表单
        let $input = obj.find("select, span");
        $.each($input, function (index, item) {
            let $item = $(item);
            if ($item.attr("type") === "span") {
                $item.html('');
            }
            if ($item.attr("type") === "chosen") {
                $item.val('').trigger("chosen:updated");
            }
            if ($item.parent().hasClass('bootstrap-select')) {
                $item.val('').trigger("change");
            }
        })
    };
})(jQuery);
