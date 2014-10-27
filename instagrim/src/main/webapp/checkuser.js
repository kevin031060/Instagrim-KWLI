/**
 * 
 */
function check()
     {
       
        var a=document.getElementById("account");
        
       if(a.value.length==0)
       {
    	 document.getElementById("result").innerHTML="Uername cannot be blank!";
         a.focus();
         return false;
       }
      
       var c=document.getElementById("pwd");
       if(c.value.length==0)
       {
    	 document.getElementById("checkP").innerHTML="Password cannot be blank!";
         c.focus();
         return false;
       }
       
       var d=document.getElementById("pwd1");
       if (c.value!=d.value){
    	   document.getElementById("checkP1").innerHTML="Please ensure your password input";
    	   c.focus();
    	   d.focus();
    	   return false;
    	   
       }
       
       return true;
     }


     function pwLength()
     {
    	 var l=document.getElementById("pwd");
    	 if(l.value.length<=2){
    		 document.getElementById("checkP").innerHTML="Too Short! Recommend Longer than two!"
    	 }else{
    		 document.getElementById("checkP").innerHTML="ok";
    	 }
     }
     
     
     //验证用户名是否存在
     var xmlHttp;
     function isExist()
     {
    
       
       var account=document.getElementById("account");
       
       if(window.ActiveXObject)
       {
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
       }
       else
       {
        xmlHttp=new XMLHttpRequest();
       }
      // String path = request.getContextPath(); 
      // String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
      // System.out.println("path:" + path);
      // var s=basePath+"CheckUser?id="+document.getElementById("account").value.toString();
      // Why not work??
      // var s="http://localhost:8080/instagrimKWLI/CheckUser?id="+document.getElementById("account").value.toString(); 
       var s="/instagrimKWLI/CheckUser?id="+document.getElementById("account").value.toString();
       xmlHttp.onreadystatechange=handlStateChage;
       xmlHttp.open("POST",s,true);        
       xmlHttp.send(null);
           
     }
     function handlStateChage()
     {
        
      if(xmlHttp.readyState==4) 
        {
          if(xmlHttp.status==200) //请求状态，200表示正常返回
          {
            document.getElementById("result").innerHTML=xmlHttp.responseText;
          }
        }
     }
     