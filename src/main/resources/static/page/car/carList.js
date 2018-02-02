layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //车辆列表
    var tableIns = table.render({
        elem: '#carList',
        url : '/car/cars',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20,
        id : "carListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: 'id', minWidth:100, align:"center"},
            {field: 'carNumber', title: '车牌号码', minWidth:100, align:"center"},
            {field: 'carUserName', title: '车主', minWidth:200, align:'center',templet:function(d){
                return '<span class="layui-blue">'+d.carUserName+' </span>';
            }},
            {title: '操作', width:105, templet:'#carListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("newsListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    key: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加（编辑）车辆
    function addCar(edit){
        var index = layui.layer.open({
            title : "添加车辆",
            type : 2,
            content : "/car/addCarPage",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find(".userName").val(edit.userName);  //登录名
                    body.find(".email").val(edit.email);  //邮箱
                    body.find(".password").text(edit.password);    //车辆简介
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回车辆列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        })
    }
    //为添加按钮增加点击事件
    $(".addNews_btn").click(function(){
        addCar();
    })

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('carListTable'),
            data = checkStatus.data,
            carsId = [];
        if(data.length > 0) {
            for (let i in data) {
                carsId.push(data[i].id);
            }
            layer.confirm('确定删除选中的车辆？', {icon: 3, title: '提示信息'}, function (index) {
                console.log(carsId);
                for (i=0;i<carsId.length; i++) {
                    $.ajax({
                        url: '/car/cars',   //删除的接口地址
                        data: { id : carsId[i] ,_method:'delete'},
                        type: 'post',
                        success: function(result) {
                            tableIns.reload();
                            layer.close(index);
                        }
                    });
                }

            })
        }else{
            layer.msg("请选择需要删除的车辆");
        }
    })

    //列表操作
    table.on('tool(carList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            addCar(data);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此车辆？',{icon:3, title:'提示信息'},function(index){
               /* $.get("/user/users",{
                    newsId : data.newsId  //将需要删除的newsId作为参数传入
                 },function(data){
                    tableIns.reload();
                    layer.close(index);
                })*/
               console.log(data.id);
                $.ajax({
                    url: '/car/cars',
                    data: { id : data.id ,_method:'delete'},
                    type: 'post',
                    success: function(result) {
                        tableIns.reload();
                        layer.close(index);
                    }
                });
            });
        }
    });

})