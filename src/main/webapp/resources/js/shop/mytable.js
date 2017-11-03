var TableInit = function(){
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#table').bootstrapTable({
            url:'/getData',
            method:'get',
            toolbar:'#toolbar',
            striped:true,
            cache:false,
            pagination:true,
            sortable:false,
            sortOrder:"asc",
            queryParams:oTableInit.queryParams,
            sidePagination:"server",
            pageNumber:1,
            pageSize:10,
            pageList:[10,25,50,100],
            contentType:"application/x-www-form-urlencoded",
            strictSearch:true,
            showColumns:true,
            showRefresh:true,
            minimumCountColumns:2,
            clickToSelect:true,
            height:700,
            uniqueId:"no",
            showToggle:true,
            cardView:false,
            detailView:false,
            columns:[
                {
                    field:'shopId',
                    title:'ID'
                },{
                    field:'shopName',
                    title:'店铺名'
                },{
                    field:'phone',
                    title:'联系电话'
                },{
                    field:'priority',
                    title:'优先级'
                },{
                    field:'createTime',
                    title:'创建时间'
                },{
                    field:'lastEditTime',
                    title:'修改时间'
                },{
                    field:'enableStatus',
                    title:'店铺状态'
                },{
                    field:'area',
                    title:'所属区域'
                },{
                    field:'shopCategory',
                    title:'所属分类'
                },{
                    field:'operate',
                    title:'操作',
                    formatter:operateFormatter
                },
            ],
            rowStyle:function (row,index) {
                var classesArr = ['success','info'];
                var strclass = "";
                if (index%2===0){
                    strclass = classesArr[0];
                }else {
                    strclass = classesArr[1];
                }
                return {classes:strclass};
            },
        });
    };
    //得到查询参数
    oTableInit.queryParams = function (params) {
        var temp = {
            //这里的键的名字和控制器的变量名必须一致
            limit:params.limit,//页面大小
            offset:params.offset
        };
        return temp;
    };
    return oTableInit;
};
function operateFormatter(value,row,index) {
    return[
        '<a class="btn active disabled" href="#">编辑</a>'
    ].join('');
}