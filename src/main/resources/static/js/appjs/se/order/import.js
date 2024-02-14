let prefix = "/order";
let billType = "XS_ORDER";
let loginShopNo = utils.dataCache.loginUserInfo.shopNo

$(function () {

    let $recordDate = $('#datepicker');
    let $shopNo = $('#shopNo');

    utils.createDateRangePicker('datepicker', {}, new Date(), new Date())
    utils.loadTypes(["data_shop"], ["shopNo"], [{width: "100%", setValue: [loginShopNo]}]);

    //配置dropzone
    Dropzone.autoDiscover = false; //取消自动提交
    $("#dropzSe").dropzone({
        url: prefix + "/import/excel", //文件上传的路径
        method: "post",
        maxFiles: 1, //一次上传的量
        maxFilesize: 1024, //M为单位
        acceptedFiles: ".xls,.xlsx", //可接受的上传类型
        autoProcessQueue: false, //是否自动上传，false时需要触发上传
        parallelUploads: 100, //手动触发时一次最大可以上传多少个文件
        paramName: "file", //后台接受文件参数名
        addRemoveLinks: true,
        dictRemoveFile: "删除文件",
        filesizeBase: 1024,
        uploadMultiple: false, // 关闭多文件上传
        dictDefaultMessage: "点击选择要上传的文件",
        dictMaxFilesExceeded: "您最多只能上传10个文件！",
        dictResponseError: '文件上传失败!',
        dictInvalidFileType: "你不能上传该类型文件,文件类型只能是*.jpg,*.gif,*.png。",
        dictFallbackMessage: "浏览器不受支持",
        dictFileTooBig: "文件过大上传文件最大支持.",
        init: function () {
            let myDropzone = this, submitButton = $("#submit"), cancelButton = $("#cancel");
            myDropzone.on('accept', function (file, done) {
                //添加上传文件的过程，可再次弹出弹框，添加上传文件的信息
            });
            myDropzone.on('addedfile', function (file) {
                //添加上传文件的过程，可再次弹出弹框，添加上传文件的信息
            });
            myDropzone.on('sending', function (data, xhr, formData) {
                //向后台发送该文件的参数
                formData.append('billType', billType);
                formData.append('shopNo', $shopNo.val());
                formData.append('billDateB', $recordDate.data("datepicker").pickers[0].getDate().format('yyyy-MM-dd'));
                formData.append('billDateE', $recordDate.data("datepicker").pickers[1].getDate().format('yyyy-MM-dd'));
            });
            myDropzone.on('success', function (files, response) {
                //文件上传成功之后的操作
                parent.layer.msg(response.msg);
                parent.layer.closeAll('loading');
                parent.reLoad && parent.reLoad();
            });
            myDropzone.on('error', function (files, response) {
                //文件上传失败后的操作
                parent.layer.msg(response.msg);
                parent.layer.closeAll('loading');
            });
            myDropzone.on('queuecomplete', function (files, response) {
                //全部上传后重置文件队列
                Dropzone.forElement("#dropzSe").removeAllFiles(true);
            });

            myDropzone.on('totaluploadprogress', function (progress, byte, bytes) {
                //progress为进度百分比
            });
            submitButton.bind('click', function () {
                //点击上传文件
                myDropzone.processQueue();
            });
            cancelButton.bind('click', function () {
                //取消上传
                myDropzone.removeAllFiles();
            });
        }
    });
});

