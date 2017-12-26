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