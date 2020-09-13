var len =0; var request;
var page  = 1; var obj = 'public'
var user = '';
$(document).ready(function(){
    request = $.ajax({
         type: "post",
         url: "/minioProject/pic/getObjects",
         dataType: "JSON",
         success: function (res) {
             $('#user').append(res.user).append("   <b class=\"caret\"></b>");
             user = res.user;
             render(res);
             addExpand();
         },
         error:function(err){
             console.log(err);
         }

     })
 })
//正则匹配
function getName(str, start, end) {
    let res = str.match(new RegExp(`${start}(.*?)${end}`))
    return res ? res[1] : null
}

 //共有文件夹的渲染
 function render(res) {
     var data = res.data;
     $("#pic").empty();
     var imgs = "";
     if (data.length == 0) imgs = "<h3>空空如也！</h3>"
     for (var i = 0; i < data.length; i++) {
         var imageUrl = getName(data[i], '0/', '\\?')
         var username = getName(data[i], 'images/', '-');
         imgs += "<li><img src=\"" + data[i] + "\" alt=\"图片\"/>" +
             "<br><i class=\"iconfont icon icon-touxiang\">  " + username + "</i>" +
             "<input type=button onclick=\"window.location.href='/minioProject/pic/images?imageUrl=" + imageUrl + "'\" value=\"下载\" class=\"btn btn-default btn2\"></li>";
     }
     $("#pic").append(imgs);

 }

 //翻页操作
function getMore() {
    request = $.ajax({
        type: "post",
        url: "/minioProject/pic/getMore",
        dataType: "JSON",
        data:{"bucketName":obj,"page":page+1},
        success: function (res) {
            if (res.data == null){
                alert("到底了！！！");
                return
            }
            renderMore(res);
            page = page + 1;
            addExpand();
        },
        error:function(err){
            console.log(err);
        }

    })
}
 //下拉获取更多
function renderMore(res){
    var data = res.data;
    var imgs = "";
    if(data.length == 0) imgs = "<h3>空空如也！</h3>"
    for(var i=0;i<data.length;i++){
        var imageUrl = getName(data[i],'0/','\\?')
        var username = getName(data[i],'images/','-');
        imgs += "<li><img src=\""+data[i]+"\" alt=\"图片\"/>"+
            "<br><i class=\"iconfont icon icon-touxiang\">  "+username+"</i>"+
            "<input type=button onclick=\"window.location.href='/pic/images?imageUrl="+imageUrl+"'\" value=\"下载\" class=\"btn btn-default btn2\"></li>";
    }
    $("#pic").append(imgs);

}

 function addExpand() {
             var imgs = document.getElementsByTagName("img");
             //imgs[0].focus();
             for(var i = 0;i<imgs.length;i++){
                 imgs[i].onclick = expandPhoto;
                 imgs[i].onkeydown = expandPhoto;
             }
  }
  function expandPhoto(){
             var overlay = document.createElement("div");
             overlay.setAttribute("id","overlay");
             overlay.setAttribute("class","overlay");
             document.body.appendChild(overlay);

             var img = document.createElement("img");
             img.setAttribute("id","expand")
             img.setAttribute("class","overlayimg");
             img.src = this.getAttribute("src");
             document.getElementById("overlay").appendChild(img);

             img.onclick = restore;
         }
 function restore(){
     document.body.removeChild(document.getElementById("overlay"));
 }
 $("li").click(function (e) {
  $(this).addClass('active').siblings().removeClass('active');
  var index = $(this).index() + 1;
  if(index == 1){
      obj = "public";
      page = 1;
     if(request != null){
         request.abort();
     }
     request = $.ajax({
         type: "post",
         url: "/minioProject/pic/getObjects",
         dataType: "JSON",
         success: function (res) {
             render(res);
             addExpand();
         },
         error:function(err){
             console.log(err);
         }
         
     })
  }
  if(index == 2){
     if(request != null){
         request.abort();
     }
      obj = user;
      page = 1;
     request = $.ajax({
         type: "post",
         url: "/minioProject/pic/getPersonalObjects",
         dataType: "JSON",
         success: function (res) {
             render(res);
             addExpand();
         },
         error:function(err){
             console.log(err);
         }
         
     })
    
  }
 })
//同步设备模态框
$("#sync").click(function(){
 $.ajax({
     type: "post",
     type:"POST",
     url:'/minioProject/account/reqSync',
     data:{devId:$("#id").val(),devName:$("#name").val()},
     dataType:'JSON',
     success:function(res){
         alert(res.msg);
         $('#myModal').modal('hide');
     },
     error:function(err){
         alert("申请失败！");
     }
 })
 

})