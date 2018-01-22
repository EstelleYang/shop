function createIEXmlDom(){
    var arr=["MSXML2.DOMDocument.6.0","MSXML2.DOMDocument.5.0","MSXML2.DOMDocument.4.0","MSXML2.DOMDocument.3.0","MSXML2.DOMDocument","Microsoft.XMLDOM"];
    try{
        for (var i=0;i<arr.length;i++){
            return new ActiveXObject(arr[i]);
        }
    }catch (e){
        throw e;
    }
}
function xmlDom() {
    var dom;
    if (window.ActiveXObject){//如果是IE
        createIEXmlDom();
    }else if(document.implementation&&document.implementation.createDocument){//火狐及其他浏览器的xml解析器
        dom = document.implementation.createDocument("","",null);
    }else{
        throw new Error("浏览器不支持");
    }
    return dom;
}
function addOption(node,node_obj) {//node:要添加的节点对象，node_obj:要添加的节点的值（select下面的option里面的值）
    if(node.nodeType==1){
        var option = document.createElement("option");
        option.appendChild(document.createTextNode(node.getAttribute("name")));
        option.setAttribute("value",node.getAttribute("zipcode"));
        node_obj.appendChild(option);
    }
}
function addOption2(node,obj) {
    if(node.nodeType==1){
        var opt = document.createElement("option");
        opt.appendChild(document.createTextNode(node.getAttribute("name")));
        opt.setAttribute("value",node.getAttribute("name"));
        obj.appendChild(opt);
    }
}
window.onload = function () {
    var xmldom;
    if (window.DOMParser) {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("GET", "/O2O/resources/city.xml", false);//获取服务器中的xml文件
        xmlhttp.send(null);
        xmldom = xmlhttp.responseXML;
    } else {
        xmldom = xmlDom();
        xmldom.async = false;
        xmldom.load("/O2O/resources/city.xml");
    }
    var prov_obj = document.getElementById("province");
    var city_obj = document.getElementById("city");
    var dist_obj = document.getElementById("district");

    var proves = xmldom.getElementsByTagName("province");
    for (var i = 0; i < proves.length; i++) {
        addOption2(proves[i], prov_obj);
    }
    //当省份发生变化
    prov_obj.onchange = function () {
        var flag = prov_obj.value;//获取当前省份的value值
        //清空市区中原有的数据
        city_obj.length = 1;
        dist_obj.length = 1;
        for (var i = 0; i < proves.length; i++) {
            if (proves[i].nodeType == 1 && proves[i].getAttribute("name") == flag) {
                var cities = proves[i].childNodes;
                for (var j = 0; j < cities.length; j++) {
                    addOption2(cities[j], city_obj);
                }
            }
        }
    }
    //当市发生改变时
    var citiess = xmldom.getElementsByTagName("city");
    city_obj.onchange = function () {
        //获取当前选中的市
        var flag_city = city_obj.value;
        //清空地区中原有的数据
        dist_obj.length = 1;
        for (var i = 0; i < citiess.length; i++) {
            if (citiess[i].nodeType == 1 && citiess[i].getAttribute("name") == flag_city) {//citiess[i].nodeType==1 1:表示元素节点，2：表示属性节点，3：表示文本节点
                var district = citiess[i].childNodes;
                for (var j = 0; j < district.length; j++) {
                    addOption(district[j],dist_obj);
                }
            }
        }
    }
}


