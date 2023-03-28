
(function(win){

    var Utils = function(){
        this.v = '2.1.5'; //版本号
        this.dataCache = {};
        this.dataCache.sysDict = {};
        this.dataCache.sysEnum = {};
        this.dataCache.categoryData = {};
    }

/* ========================================================================
 * 数据字典缓存
 * ======================================================================== */
    //加载数据字典
    Utils.prototype.initSysDict = function initSysDict(types){
        $.ajax({
            url : '/common/sysDict/lists/' + types,
            success : function(datas) { //加载数据
                // utils.dataCache.sysDict=datas;
                $.extend(utils.dataCache.sysDict, datas);
            }
        });
    };

    //加载数据字典
    Utils.prototype.initExtra = function initExtra(){
        $.ajax({
            url : '/common/sysDict/listExtra',
            success : function(datas) { //加载数据
                // utils.dataCache.sysDict=datas;
                $.extend(utils.dataCache.sysDict, datas);
            }
        });
    };

    //加载数据字典下拉框
    Utils.prototype.loadTypes = function loadTypes(types, elementIds){
        if (types.length === elementIds.length) {
            var sysDict = utils.dataCache.sysDict;
            for (var t = 0; t < types.length; t++) {
                let html = "", data = sysDict[types[t]];
                let element = $("#" + elementIds[t]);
                let value = element.attr("value")
                for (let i = 0; data != null && i < data.length; i++) {
                    if (data[i].value == value) {
                        html += '<option value="' + data[i].value + '" selected>' + data[i].name + '</option>'
                    } else {
                        html += '<option value="' + data[i].value + '">' + data[i].name + '</option>'
                    }
                }
                element.append(html);
                element.chosen({maxHeight : 100 });
            }
        }
    };

    //加载数据字典
    Utils.prototype.formatType = function formatType(value, type) {
        if (type === undefined) return value;
        let data = utils.dataCache.sysDict[type];
        if (data === undefined) return value;
        for (let t = 0; t < data.length; t++) {
            if (data[t].value == value) {
                return data[t].name;
            }
        }
    };

    //加载数据字典
    Utils.prototype.unformatType = function unFormatType(value, type) {
        if (type === undefined) return value;
        let data = utils.dataCache.sysDict[type];
        if (data === undefined) return value;
        for (let t = 0; t < data.length; t++) {
            if (data[t].name === value) {
                return data[t].value;
            }
        }
    };

/* ========================================================================
 * 类目缓存
 * ======================================================================== */
    //类目数据
    Utils.prototype.initCategory = function initCategory(func) {
        Utils.prototype.initCategoryMeta("PRODUCT,CUSTOMER,VENDOR,PAYMENT,INCOME,ACCOUNT,USER");
        Utils.prototype.initCategoryData("PRODUCT,CUSTOMER,VENDOR,PAYMENT,INCOME,ACCOUNT,USER");
    }
    Utils.prototype.initCategoryMeta = function initCategoryMeta(types){
        $.ajax({
            url : '/data/category/listTree/' + types,
            success : function(datas) { //加载数据
                // utils.dataCache.categoryData=datas;
                $.extend(utils.dataCache.categoryData, datas);
            }
        });
    };
    Utils.prototype.initCategoryData = function initCategoryData(types){
        $.ajax({
            url : '/data/category/listTreeData/' + types,
            success : function(datas) { //加载数据
                $.extend(utils.dataCache.categoryData, datas);
            }
        });
    };

    //类目下拉框
    Utils.prototype.loadCategory = function loadCategory(types, elementIds, options){
        if (types.length === elementIds.length) {
            let categoryData = utils.dataCache.categoryData;
            for (let t = 0; t < types.length; t++) {
                let element = $("#" + elementIds[t]);
                let opts = $.extend({}, {maxHeight : 100, width:"100%"}, options && options[t]);
                let html = "", data = categoryData[types[t]];
                let value = element.attr("value")
                for (var i = 0; data != null && i < data.length; i++) {
                    html += Utils.prototype.dealCategory(data[i], value);
                }
                element.append(html);
                element.chosen(opts);
            }
        }
    };

    //类目下拉框
    Utils.prototype.dealCategory = function dealCategory(data, value) {
        var html = "";
        if (!data) return html;
        if (data.children.length === 0) {
            html = '<option value="' + data.id + '"' + (data.id == value ? ' selected>' : '>') + data.text + '</option>';
        } else if (data.children && data.children.length > 0) {
            html += '<optgroup label="' + data.text + '">'
            for (var i = 0; i < data.children.length; i++) {
                html += dealCategory(data.children[i], value);
            }
            html += '</optgroup>'
        }
        return html;
    }

    //加载数据字典
    Utils.prototype.formatCategory = function formatCategory(value, type) {
        if (type === undefined) return value;
        var data = utils.dataCache.categoryData[type];
        if (data === undefined) return value;
        for (var t = 0; t < data.length; t++) {
            var text = Utils.prototype.dealFormatCategory(data[t], value);
            if (text && text.length > 0) {
                return text;
            }
        }
        return value;
    };

    //类目下拉框
    Utils.prototype.dealFormatCategory = function dealFormatCategory(data, value) {
        if (data.children.length === 0 && data.id == value) {
            return data.text;
        } else if (data.children && data.children.length > 0) {
            for (var i = 0; i < data.children.length; i++) {
                var text = dealFormatCategory(data.children[i], value);
                if (text && text.length > 0) {
                    return text;
                }
            }
        }
    }

    //表格单元格下拉框
    Utils.prototype.formatSelect = function formatSelect(type,row,index){
        if (type === undefined) return value;
        var data = utils.dataCache.sysDict[type];
        var selectVal = {value: {}};
        if (data) {
            for (var t = 0; t < data.length; t++) {
                selectVal.value[data[t].value] = data[t].name;
            }
            return selectVal;
        }
        data = utils.dataCache.categoryData[type];
        if (data) {
            for (var t = 0; t < data.length; t++) {
                if (data[t].hasChildren && data[t].children && data[t].children.length > 0) {
                    for (var k=0; k < data[t].children.length; k++){
                        selectVal.value[data[t].children[k].id] = data[t].children[k].text;
                    }
                }
            }
        }
        return selectVal;
    };

    //加载数据字典
    Utils.prototype.formatYN = function formatYN(value,row,index){
        if(value==0){
            return '<span class="label label-danger">禁用</span>';
        }else if(value==1){
            return '<span class="label label-primary">启用</span>';
        }
    };

/* ========================================================================
 * 枚举值缓存
 * ======================================================================== */
    //枚举值
    Utils.prototype.initSysEnum = function initSysEnum(types){
        $.ajax({
            url : '/common/sysDict/listEnum/' + types,
            success : function(datas) { //加载数据
                utils.dataCache.sysEnum=JSON.parse(datas);
            }
        });
        $.ajax({
            url : '/common/sysDict/listEnumMap/' + types,
            success : function(datas) { //加载数据
                utils.dataCache.sysEnumMap=datas;
            }
        });
    };

    //加载数据字典
    Utils.prototype.loadEnumTypes = function loadEnumTypes(types, elementIds, options){
        if (types.length === elementIds.length) {
            let sysEnumMap = utils.dataCache.sysEnumMap;
            for (let t = 0; t < types.length; t++) {
                let html = "", data = sysEnumMap[types[t]];
                let element = $("#" + elementIds[t]);
                let value = element.attr("value")
                for (var i = 0; data != null && i < data.length; i++) {
                    if (data[i].value == value) {
                        html += '<option value="' + data[i].value + '" selected>' + data[i].name + '</option>'
                    } else {
                        html += '<option value="' + data[i].value + '">' + data[i].name + '</option>'
                    }
                }
                let opts = $.extend({}, {maxHeight : 100, width:"100%"}, options && options[t]);
                element.append(html);
                element.chosen(opts);
            }
        }
    };

    //加载数据字典
    Utils.prototype.loadChosenStatic = function loadChosenStatic(elementIds, options){
        for (var t = 0; t < elementIds.length; t++) {
            var opts = $.extend({}, {maxHeight : 100, width:"100%"}, options && options[t]);
            $("#" + elementIds[t]).chosen(opts);
        }
    };

    //加载枚举值（各种状态信息）
    Utils.prototype.formatEnum = function formatEnum(value, type, defaultValue) {
        if (type === undefined || value === undefined || value === null) {
            return defaultValue;
        }
        let data = utils.dataCache.sysEnum[type];
        if (data === undefined) return value;
        for (let t = 0; t < data.length; t++) {
            if (data[t].hasOwnProperty(value)) {
                return data[t][value];
            }
        }
        return value;
    };

    //加载枚举值（各种状态信息）
    Utils.prototype.unformatEnum = function unFormatEnum(value, type) {
        if (type === undefined || value === undefined) {
            return value;
        }
        let data = utils.dataCache.sysEnum[type];
        if (data === undefined) return value;
        for (let t = 0; t < data.length; t++) {
            for (const [k, v] of Object.entries(data[t])) {
                if (v === value) {
                    return k;
                }
            }
        }
        return value;
    };

    Utils.prototype.formatEnumS = function formatEnumS(value, type) {
        if (value === 'YES') {
            return '<span style="color:blue;font-weight:bold">' + Utils.prototype.formatEnum(value, type) + '</span>';
        } else {
            return Utils.prototype.formatEnum(value, type);
        }
    };

    Utils.prototype.formatListS = function formatListS(list,name,type){
        var html = "";
        if (list && list.length > 0) {
            for (var i=0; i<list.length; i++) {
                html += (list[i][name] == "" ? (type=="number" ? "0" : " ") : list[i][name]) +  (i != list.length -1 ? '<div class="util-line" />' : '');
            }
        }
        return html;
    };

    Utils.prototype.formatListM = function formatListM(cellValue, options, rowobj){
        var val = "";
        if (cellValue && cellValue.length > 0) {
            var index = options.pos - options.colModel.colLen - 1;
            val = cellValue[index].totalQty;
        }
        return val;
    };

/* ========================================================================
*  multiSelect ajax初始化
* ======================================================================== */
    //多选下拉框
    Utils.prototype.loadMultiSelect = function loadMultiSelect(types, elementIds, options, url){
        let o = {
            buttonWidth: '100px',
            includeSelectAllOption: true,
            enableFiltering: true,
            includeResetOption: true,
        };
        if (types.length === elementIds.length) {
            $.ajax({
                url : url,
                success : function(result) {
                    if (result) {
                        for (let t = 0; t < types.length; t++) {
                            let option = $.extend(o, options[t]);
                            let html = "", data = result[types[t]];
                            if (data) {
                                Object.values(data).forEach(function(value, index) { html += '<option value="' + value + '">' + value + '</option>'; })
                                let element = $("#" + elementIds[t]);
                                element.html(html);
                                element.multiselect(option);
                                element.multiselect('rebuild');
                            }
                        }
                    }
                }
            });
        }
    };

/* ========================================================================
*  订单金额计算用
* ======================================================================== */

    Utils.prototype.numberEditOptions = function numberEditOptions(func,type, delay) {
        var timeoutID;
        return {
            autocomplete: "off",
            dataEvents: [{
                type: type || 'keyup',
                data: {func: func},
                fn: function(e) {
                    clearTimeout(timeoutID);
                    var _e = e, _self = this;
                    timeoutID= window.setTimeout(function(){
                        var srcElement = _e.target.getAttribute('name');
                        var amountEntry = _e.data['func'].call(_self);
                        var valueObj = amountEntry['valueObj'];
                        var totalObj = amountEntry['totalObj'];
                        var elements = amountEntry['elements'];
                        var formulas = amountEntry['formula'];

                        var calElements = elements[srcElement];
                        for (var i=0; i<calElements.length; i++) {
                            var element = calElements[i];
                            valueObj[element] = totalObj[element] = eval(formulas[element]);
                            $('[name="' + element + '"]').val(valueObj[element])
                        }
                    }, delay || 100);
                }
            }]
        }
    };
    // 订单金额计算事件
    Utils.prototype.collectAmount = function collectAmount(e){
        var srcElement = e.target.getAttribute('name');
        var totalObj = e.data['amountOrder']['totalObj'];
        var valueObj = e.data['amountOrder']['valueObj'];
        var elements = e.data['amountOrder']['elements'];
        var formulas = e.data['amountOrder']['formula'];

        valueObj[srcElement] = this.value;
        var calElements = elements[srcElement];
        for (var i=0; i<calElements.length; i++) {
            var element = calElements[i];
            valueObj[element] = eval(formulas[element]);
            $('[name="' + element + '"]').val(valueObj[element])
        }
    };

    // 手工触发订单金额计算事件
    Utils.prototype.collectAmountManual = function collectAmountManual(amountOrder){
        var totalObj = amountOrder['totalObj'];
        var valueObj = amountOrder['valueObj'];
        var formulas = amountOrder['formula'];
        var valueElements = amountOrder.valueElements;

        for (var i=0; i<valueElements.length; i++) {
            var element = valueElements[i];
            valueObj[element] = eval(formulas[element]);
            $('[name="' + element + '"]').val(valueObj[element])
        }
    };

    if (win.parent.utils != null) {
        win.utils = new Utils();
        win.utils.dataCache = win.parent.utils.dataCache;
    } else {
        win.utils = new Utils();
        win.utils.initSysEnum("CATEGORY_TYPE");
        win.utils.initCategory();
        win.utils.initSysDict("data_category,data_area,data_unit,yes_no,data_brand,data_type,data_grade,data_status,data_wh_rk,data_wh_ck,oa_notify_type");
        win.utils.initExtra();
    }

    //jqGrid edit 选择商品列
    Utils.prototype.myElementAndValue = function myElementAndValue() {
        // 自定义表格编辑框
        function myelem (value, options) {
            var el =
                '<div class="input-group">' +
                '<input type="text" name="entryName" class="form-control" value="'+ value +'" autocomplete="off">' +
                '<span class="input-group-btn"> <button type="button" class="btn btn-white" onclick="add()">...</button> </span>' +
                '</div>';
            return el;
        }

        // 自定义表格编辑框get set方法
        function myvalue(elem, operation, value) {
            if(operation === 'get') {
                return $('input', elem).val();
            } else if(operation === 'set') {
                $('input', elem).val(value);
            }
        }
        return {
            custom_element: myelem, custom_value:myvalue
        }
    };

    //jqGrid edit input控件属性
    Utils.prototype.commonEditOptions = function commonEditOptions() {
        return {
            autocomplete: "off"
        }
    };

    /* ========================================================================
    *  bootStrap封装
    * ======================================================================== */
    Utils.prototype.createDatePicker = function createDatePicker(elem, opt, defaultValue) {
        var _date = $('#' + elem).datepicker(
            $.extend({}, {
                language: "zh-CN",
                todayBtn: "linked",
                autoclose: true,
                clearBtn: true,
                todayHighlight: true
            }, opt)
        );
        if (defaultValue) {
            _date.datepicker('setDate', defaultValue);
        }
        return _date;
    };

    Utils.prototype.createDateRangePicker = function createDateRangePicker(elem, opt, defaultV1, defaultV2) {
        let _date = $('#'+ elem).datepicker(
            $.extend({}, {
                language: "zh-CN",
                todayBtn: "linked",
                autoclose: true,
                clearBtn: true,
                todayHighlight: true,
                zIndexOffset: 10000
            }, opt)
        );
        if (defaultV1 && defaultV2) {
            $('#'+ elem).data("datepicker").pickers[0].setDate(defaultV1);
            $('#'+ elem).data("datepicker").pickers[1].setDate(defaultV2);
        }
        return _date;
    };



    /* ========================================================================
    *  报表模块展示订单明细列表(只适用于采购单、销售单)
    * ======================================================================== */
    Utils.prototype.listDataGrid = function listDataGrid(dataUrl, opttion) {
        let dataIndex;
        //触发菜单单击
        window.parent.$(".J_menuItem").each(function (index) {
            if ($(this).attr('href') === dataUrl) {
                window.parent.$(this).trigger('click');
                dataIndex = window.parent.$(this).data('index');
                return false;
            }
        });
        //防止frame未加载完成就触发dataGrid reloadGrid失败（菜单click事件首次加载页面会向后台发起请求导致页面加载慢）
        if (!loadGrid()) {
            setTimeout(loadGrid, 1500);
        }
        function loadGrid() {
            var loadFlag = false;
            //加载订单数据
            window.parent.$('.J_mainContent .J_iframe').each(function () {
                if ($(this).data('id') === dataUrl) {
                    var win = window.parent.$('iframe[name="iframe' + dataIndex +'"]')[0].contentWindow;
                    if (win.tableGrid) {
                        var postData = $.extend({}, opttion, { 'page': 1, 'rows': 100 });
                        win.tableGrid.jqGrid('setGridParam', {rowNum:100, postData: $.param(postData)}).trigger("reloadGrid");
                        win.$('.ui-pg-selbox').val(100); //设置jqGrid rowNum 展示值
                        win.dataForm.setForm(postData); //搜索条件的值
                        loadFlag = true;
                    } else if (win.frameElement) {
                        win.frameElement.onload = win.frameElement.onreadystatechange = function () {
                            var postData = $.extend({}, opttion, { 'page': 1, 'rows': 100 });
                            win.tableGrid.jqGrid('setGridParam', {rowNum:100, postData: $.param(postData)}).trigger("reloadGrid");
                            win.$('.ui-pg-selbox').val(100); //设置jqGrid rowNum 展示值
                            win.dataForm.setForm(postData); //搜索条件的值
                        };
                        loadFlag = true;
                    }
                    return false;
                }
            });
            return loadFlag;
        }
    };


    /* ========================================================================
    *  工具类
    * ======================================================================== */
    //文件下载
    Utils.prototype.download = function download(content, fileName) {
        let aEle = document.createElement("a"); // 创建a标签
        aEle.download = fileName; // 设置下载文件的文件名
        aEle.href = content; // content为后台返回的下载地址
        aEle.click(); // 设置点击事件;
    };


    /* ========================================================================
    *  javascript 原生对象功能扩展
    * ======================================================================== */
    // 对Date的扩展，将 Date 转化为指定格式的String
    // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
    // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
    // 例子：
    // (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
    // (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
    Date.prototype.format = function (fmt) {
        let o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (let k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };

    /**
     * 获取当前年份的第一天和最后一天
     * @returns {Date} 例如 2019-01-01
     */
    Utils.prototype.getYearFirstDay = function getYearFirstDay() {
        let firstDay = new Date();
        firstDay.setDate(1);
        firstDay.setMonth(0);
        return firstDay;
    }

    /**
     * 金额格式化——显示千位分隔符
     * @returns {String} 例如 '124.35'
     */
    Utils.prototype.priceFormat = function priceFormat(num) {
        if (!isFinite(num)) {
            return num;
        }
        let str =  (num/1).toFixed(2) + ''
        // 没有小数点时，在末尾补上一个小数点
        if (str.indexOf('.') === -1) {
            str += '.'
        }
        return str.replace(/(\d)(?=(\d{3})+\.)/g, '$1,').replace(/\.$/, '');
    }

    /**
     * 数值转化
     * @returns {number|number} 例如 124.35
     */
    Utils.prototype.parseNumeric = function parseNumeric(num, fraction = 2) {
        return $.isNumeric(num) ? parseFloat(num).toFixed(fraction) * 1.0 : 0.00;
    }

    /**
     * 设置jqGrid行背景色
     */
    Utils.prototype.changeRowCss = function changeRowCss(tableGrid, matchKey, matchValue) {
        let matchValueList = matchValue.split(',');
        //在表格加载完成后执行
        let ids = tableGrid.jqGrid("getDataIDs");
        let rowDataArr = tableGrid.jqGrid("getRowData");
        //采购单、入库单，行背景颜色设为红色
        for (let i = 0; i < rowDataArr.length; i++) {
            let rowData = rowDataArr[i];
            if (matchValueList.find(mv => rowData[matchKey].includes(mv))) {
                $("#" + ids[i] + " td").css("background-color", "#FDF5E6");
            }
        }
    }

    /**
     * 设置jqGrid单元格背景色
     */
    Utils.prototype.changeRowCellCss = function changeRowCellCss(cellValue, matchValue) {
        let matchValueList = matchValue.split(',');

        cellValue = isFinite(cellValue) ? this.priceFormat(cellValue) : cellValue;

        if (matchValueList.find(mv => cellValue.includes(mv))) {
            return '<span style="color:#FF9900;">' + cellValue + '</span>';
        } else {
            return cellValue;
        }
    }

}(window));
