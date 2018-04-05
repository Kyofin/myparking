layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //车位列表
    var tableIns = table.render({
        elem: '#portList',
        url : '/parkingport/parkingports',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20,
        size: 'lg',
        id : "portListTable",
        cols : [[

            {field: 'id', title: 'id', minWidth:100, align:"center"},
            {field: 'carportName', title: '车位编号', minWidth:100, align:"center"},
            {field: 'parkingUser', title: '车位关联用户', minWidth:200, align:'center',templet:function(d){
                if (d.parkingUser!='无')
                    return '<span class="layui-blue">'+d.parkingUser+' </span>';
                else
                    return '<span class="layui-disabled">'+d.parkingUser+' </span>';
            }},
            {field: 'status', title: '车位状态', minWidth:100, align:"center",templet:function (d) {
                if (d.status==0)
                {
                    return '<button class="layui-btn layui-btn-radius layui-btn-sm layui-btn-green">可使用</button>'
                }else if (d.status==1)
                {
                    return '<button class="layui-btn layui-btn-radius layui-btn-sm layui-btn-primary layui-btn-disabled">使用中</button>'
                }else if (d.status==2)
                {
                    return '<button class="layui-btn layui-btn-radius layui-btn-sm layui-btn-warm">预定中</button>'

                }

            }}
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