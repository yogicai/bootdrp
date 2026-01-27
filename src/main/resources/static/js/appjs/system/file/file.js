// 编辑器图片上传函数
function sendFile(files, editor, $editable) {
    const maxSize = 2 * 1024 * 1024; // 2MB
    const file = files[0];

    // 验证文件大小
    if (file.size > maxSize) {
        alert('图片大小不能超过2MB');
        return false;
    }

    // 创建FormData对象
    const formData = new FormData();
    formData.append('file', file);

    // 发送AJAX请求
    $.ajax({
        url: '/common/sysFile/upload',
        type: 'POST',
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        dataType: 'json',
        success: function (data) {
            // 插入图片到编辑器
            $('.summernote').summernote('insertImage', data.fileName);
        },
        error: function () {
            alert('上传失败');
        }
    });
}