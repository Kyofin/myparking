layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(addUser)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post("/user/addUser",{
            userName : $(".userName").val(),  //登录名
            nickName : $(".nickName").val(),  //昵称
            email : $(".email").val(),  //邮箱
            password : $(".password").val(),    //密码
            headUrl : $("#headUrl").val(),    //头像url
            },function(res){

        });
        setTimeout(function(){
            top.layer.close(index);
            top.layer.msg("用户添加成功！");
            layer.closeAll("iframe");
            //刷新父页面
            parent.location.reload();
        },2000);
        return false;
    })

    //格式化时间
    function filterTime(val){
        if(val < 10){
            return "0" + val;
        }else{
            return val;
        }
    }
    //定时发布
    var time = new Date();
    var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());




})
//todo 文件上传前端工作
//上传文件的模块
layui.use('upload', function() {
    var $ = layui.jquery
        , upload = layui.upload;
    var layer = layui.layer;

    //普通图片上传
    var uploadInst = upload.render({
        elem: '#imageFile'
        , url: '/portal/user/uploadImage'
        , before: function (obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                $('#previewImg').attr('src', result); //图片链接（base64）
            });
        }
        , done: function (res) {

            //如果上传失败
            if (res.code > 0) {
                return layer.msg('上传失败');
            }
            //上传成功
            if (res.code == 0) {
                 layer.msg('上传成功');
            }

            //添加hearURL到表单中
            var headUrl =res.msg;
            var demoText = $('#headUrl');
            demoText.val(headUrl)

        }

    })
});