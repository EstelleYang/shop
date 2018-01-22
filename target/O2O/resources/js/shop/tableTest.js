$(document).ready(function () {
    alert("开始执行");
    initTable();
   // getCategoryList();
})
function queryProduct_click(index) {
    //刷缓存
    $("#table_product").bootstrapTable("destroy");
    //加载基础信息数据
    var row = getRow("table", index);
    $('#queryShopInfoId').val(row.shopId);
    $('#modifyDetailModel').modal('show');
    initProductTable();
}
function getProduct() {
    $.ajax({
        url:"/O2O/product/aProduct",
        data:$('#productId').val(),
        type:'GET',
        success:function (data) {
            window.location.href='product.html?data='+data;
        }
    })
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
                /*return '<a href="#">'+value+'</a>';*/

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
            var entity = $("#queryShopInfoId").val();
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
function initTable() {
    var $table = $('#table');
    $table.bootstrapTable({
        method:"get",
        striped:true,
        url:"/O2O/shoplists/allShop",
        sidePagination:"server",
        dataType:"json",
        columns:[{
            field:'',
            checkbox:true,
            align:'center'
        },{
            title:'操作',
            field:'operation',
            align:'center',
            formatter:function (value,row,index) {
                var str = '<a class="edit ml10" style="color:black;" href="javascript:void(0);" onclick="javascript:queryProduct_click(\''
                    + index
                    + '\');" title="查看"><span class="glyphicon glyphicon-list-alt"></span></a>&nbsp;&nbsp;';
                return str;
            }
        },{
            title:'ID',
            field:'shopId',
            valign:'middle',
            align:'center',
            visiable:false
        },{
            title:'商店名',
            field:'shopName',
            valign:'middle',
            align:'center'
        }, {
            title: '联系方式',
            field: 'phone',
            valign: 'middle',
            align: 'center'
        }],
        queryParams:function (params) {
            var limit = params.limit;
            var offset = params.offset;
            var currentPage = (limit+offset)/limit;

            var temp = {
                pageSize:limit,
                offset:offset,
                currentPage:currentPage
            };
            return temp;
        }
    })
}

//responseHandler 处理结果集
/*function responseHandler(res) {
    $.each(res.rows, function (i, row) {
        row.state = $.inArray(row.id, selections) !== -1;
    });
    return res;
}*/
/**
 * 获取表格行数据
 * @param tableid
 * @param index
 * @returns
 */
function getRow(tableid, index) {
    return $("#" + tableid).bootstrapTable('getData')[index];
}

//var $table = $('#table_product');

function exportExcel() {
    var data = $("#table_product").bootstrapTable("getData");
    if (data==null||data==''){
        $.alert("提示","操作数据不能为空");
        return;
    }
    //$.confirm("提示", "是否确认导出?", function() {
       var columns = $("#table_product").bootstrapTable('getVisibleColumns');
       var map = {};
       $.each(columns,function (index,value) {
           if (value.field==''||value.field=='operation'){
               return;
           }
           map[value.field]=value.title;
       });
       if (map==undefined){
           $.alert("请选择导入的列");
           return false;
       }
       var $exportTable = $("form");
       var hiddenHTML = '<input type="hidden" name="custColumns"/>';
       $exportTable.append(hiddenHTML);
       $("input[name=custColumns]").val(JSON.stringify(map));
        $exportTable.attr("method", "post");
        $exportTable.attr("action", "/O2O/shoplists/export");
        $exportTable.submit();
        $exportTable.find("input[name=custColumns]").remove();
  //  });
}
// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
/*Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}*/


/*serializeObject = function (form) {*/
/*    var o = {};*/
/*    $.each(form.serializeArray(), function (index) {*/
/*        if (this['value'] != undefined && this['value'].length > 0) {// 如果表单项的值非空，才进行序列化操作*/
/*            if (o[this['name']]) {*/
/*                o[this['name']] = o[this['name']] + "," + this['value'];*/
/*            } else {*/
/*                o[this['name']] = this['value'];*/
/*            }*/
/*        }*/
/*    });*/
/*    return o;*/
/*
};*/
function getCategoryList(){
    $.ajax({
        url:"/O2O/product/queryProductC",
        type:"get",
        dataType:"json",
        success:function (data) {
            getProductCategory(data.categoryList);
        }
    });
}
function getProductCategory(data) {
    data.map(function (item,index) {
        $("#productType").append("<li><a href='#'>"+item.productCategoryName+"</a></li>");
    });
}
function getProductDesc(data){

}
/*$(function () {
//业务类型
    $.get("/O2O/product/queryProductC", function (data) {
        var result = eval("(" + data + ")");
        $.each(result, function (i, v) {
            $("#productType").append("<li value='" + v.name + "'>" + v.cname + "</li>");
            $("#productType").append("<li><a href='#'>"+v.);
        })
    });
    data.map(function (item,index) {
        html += '<div class="row row-shop"><div class="col-40">'+item.shopName+
            '</div><div class="col-40">'+shopStatus(item.enableStatus)+
            '</div><div class="col-20">'+goShop(item.enableStatus,item.shopId)+'</div></div>'
    });
})*/






