layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(addCar)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post("/car/cars",{
            carNumber : $(".carNumber").val(),  //车牌号码
            carUserId : $(".carUserId").val(),  //车主id
            },function(res){
            layer.msg(JSON.parse(res).msg)
            setTimeout(function(){
                top.layer.close(index);
                layer.closeAll("iframe");
                //刷新父页面
                parent.location.reload();
            },2000);
        });

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