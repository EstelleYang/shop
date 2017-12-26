/**
 * Created by 496934 on 2017/12/14.
 */
var myChart;
$(document).ready(function () {
    myChart = echarts.init(document.getElementById('main'));
    myChart.showLoading({
        text:"加载中....."
    });
    //定义图表option
    var options = {
        title:{
            text:"未来一周气温变化",
            subtext:"纯属虚构",
            subLink:"http://www.baidu.com"
        },
        tooltip:{
            trigger:'axis'
        },
        legend:{
            data:["最高气温"]
        },
        toolbox: {
            show : true,
            feature : {
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis:[{
            type:'category',
            boundaryGrap:false,
            data:[]
        }],
        yAxis:[{
            type:'value',
            axisLabel:{
                formatter:'{value}s'
            },
            boundaryGap: false,
            splitArea:{
                show:true
            }
        }],
        series:[{
            name:'商品数量',
            type:'bar',
            data:[],
            barWidth: 15,//固定柱子宽度
            markPoint:{
                data:[{
                    type:'max',
                    name:'最大值'
                },{
                    type:'min',
                    name:'最小值'
                }]
            },
            markLine:{
                data:[{
                    type:'average',
                    name:'平均值'
                }]
            }
        },{
            name:'商品数量2',
            type:'bar',
            data:[],
            barWidth: 15,//固定柱子宽度
            markPoint:{
                data:[{
                    type:'max',
                    name:'最大值'
                },{
                    type:'min',
                    name:'最小值'
                }]
            },
            markLine:{
                data:[{
                    type:'average',
                    name:'平均值'
                }]
            }
        }]}
    myChart.setOption(options);
    myChart.hideLoading();
    getChartData();//ajax后台交互
})
function getChartData(){
    //获得图表的options对象
    var options = myChart.getOption();
    $.ajax({
        type:"post",
        async:false,//同步执行
        url:"/O2O/product/getEcharts",
        data:{},
        dataType:"json",
        success:function (result) {
            if(result){
                options.legend.data = result.legend;
                options.xAxis[0].data = result.category;
                options.series[0].data = result.series[0].data;

                options.series[1].data = result.series[1].data;
                myChart.hideLoading();
                myChart.setOption(options);
                // myChart.setOption(option);
                /*myChart.on('click', function (params) {
                 window.open('https://www.baidu.com/s?wd=' + encodeURIComponent(params.name));
                 })*/
// name:类目名，横轴  seriesName:纵轴名  data:纵轴值

                myChart.on('click', function(params){
                    if(params.componentType === 'series'){
                        if (params.name){
                            queryProduct_click(params.name);
                        }
                    }
                });
            }},
        error:function (errorMsg) {
            alert("失败");
            myChart.hideLoading();
        }
    })
}
function queryProduct_click(index) {
    //刷缓存
    $("#table_product").bootstrapTable("destroy");
    //加载基础信息数据
    $('#queryShopInfoId').val(index);
    $('#modifyDetailModel').modal('show');
    initProductTable();
}
function initProductTable() {
    var $table = $('#table_product');
    $table.bootstrapTable({
        method:"get",
        striped:true,
        url:"/O2O/shoplists/allProduct",
        sidePagination:"server",
        dataType:"json",
        columns:[{
            title:'ID',
            field:'productId',
            algin:'center',
            valign:'middle',
            formatter:function (value,row,index) {
                var html = '<p id="productId'+index+'">'+value+'</p>';
                return html;
            }
        },{
            title:'商品名称',
            field:'productName',
            algin:'center',
            valign:'middle',
            formatter:function (value,row,index) {
                if (null==value||''==value){
                    return '-';
                }
                return '<a href="javascript:void(0);" onclick="javascript:getProduct();">'+value+'</a>';
            }
        },{
            title:'原价',
            field:'normalPrice',
            algin:'center',
            valign:'middle'
        },{
            title:'现价',
            field:'promotionPrice',
            algin:'center',
            valign:'middle'
        },{
            title:'上架时间',
            field:'createTime',
            algin:'center',
            valgin:'middle',
            formatter: function (value, row, index) {
                if (null == value || '' == value) {
                    return '-';
                }
                var date = new Date(value);
                return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
            }
        },{
            title:'更新时间',
            field:'lastEditTime',
            algin:'center',
            valign:'middle',
            formatter: function (value, row, index) {
                if (null == value || '' == value) {
                    return '-';
                }
                var date = new Date(value);
                return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
            }
        }],
        queryParams:function (params) {
            var limit = params.limit;
            var offset = params.offset;
            var currentPage = (limit+offset)/limit;
            var entity = 1;
            var temp = {
                pageSize:limit,
                offset:offset,
                data:entity,
                currentPage:currentPage
            }
            return temp;
        }
    })
}
