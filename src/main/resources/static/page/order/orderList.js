layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //beginTime":1517907381000,"carPortId":18,"id":28,"userId":18

    //订单列表
    var tableIns = table.render({
        elem: '#orderList',
        url : '/order/orders',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20,
        size: 'lg',
        id : "orderListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: 'id', minWidth:100, align:"center"},
            {field: 'carPortName', title: '车位编号', minWidth:100, align:"center"},
            {field: 'orderUser', title: '订单关联用户', minWidth:200, align:'center',templet:function(d){
                    return '<span class="layui-blue">'+d.orderUser+' </span>';
            }},
            {field: 'price', title: '价格', minWidth:50, align:"center"},
            {field: 'status', title: '状态', minWidth:50, align:"center",templet:function(d){
                    if (d.status==0)
                    {
                        return '<button class="layui-btn layui-btn-radius layui-btn-sm layui-btn-warm">未支付</button>'
                    }else if (d.status==2)
                    {
                        return '<button class="layui-btn layui-btn-radius layui-btn-sm layui-btn-primary layui-btn-disabled">订单已取消</button>'
                    }else if (d.status==1)
                    {
                        return '<button class="layui-btn layui-btn-radius layui-btn-sm layui-btn-green">已支付</button>'

                    }
                }},
            {field: 'duration', title: '持续时间（Hour）', minWidth:200, align:"center"},
            {field: 'beginTime', title: '创建时间', minWidth:200, align:"center",templet:function (d) {
                    var data = new Date(d.beginTime);
                    return data.getFullYear()+"年"+(data.getMonth()+1)+"月"+data.getHours()+"时"+data.getMinutes()+"分";
                }},
            {field: 'endTime', title: '结束时间', minWidth:200, align:"center",templet:function (d) {
                if(d.endTime==null)
                    return 'not end';
                else {
                    var data = new Date(d.endTime);
                    return data.getFullYear() + "年" + (data.getMonth() + 1) + "月" + data.getHours() + "时" + data.getMinutes() + "分";
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
    table.on('tool(orderList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            addCar(data);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此订单？',{icon:3, title:'提示信息'},function(index){
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