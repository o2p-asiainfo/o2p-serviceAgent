
$(function(){
	$.formValidator.initConfig({wideword:true,autotip:true,formid:"myform",onerror:function(msg){},onsuccess:function(){
		return confirm('请确认您填写的身份认证信息，该信息再通过审核后将无法修改!');
	}});
	
	$("#cert_number").formValidator({onshow:"请输入证件号码",onfocus:"请正确输入证件号码",onerror:"证件号码错误，请检查"})
	.inputValidator({min:0,max:18,onerror:"证件号码错误，请检查"})
	.regexValidator({regexp:'^(\\d{18,18}|\\d{15,15}|\\d{14,14}x|\\d{17,17}x)$',onerror:"证件号码错误，请检查"});
	$("#realname").formValidator({onshow:"请填写真实姓名",onfocus:"请填写真实姓名",onempty:"&nbsp", empty:true}).inputValidator({min:1,max:30,onerror:"请填写真实姓名"});
	
	$("#phonenum").val('189591aa88204').formValidator({onshow:"请正确填写手机号码",onfocus:"请正确填写手机号码",onempty:"&nbsp",empty:true})
	.inputValidator({min:0,max:11,onerror:"请正确填写手机号码"})
	.regexValidator({regexp:"^(1)[0-9]{10}$",onerror:"请正确填写手机号码"});
	

	$("#username").formValidator({onshow:"用户名是您在本平台上显示的名称",onfocus:"用户名不超过6个汉字或者20个英文字母"}).inputValidator({min:3,max:20,onerror:"用户名不超过6个汉字或者20个英文字母"}).regexValidator({regexp:"ps_username",datatype:"enum",onerror:"用户名格式错误"}).ajaxValidator({
	    type : "get",
		url : "",
		data :"m=member&c=index&a=public_checkname_ajax",
		datatype : "html",
		async:'false',
		success : function(data){
			if( data == "1" ) {
                return true;
			} else {
                return false;
			}
		},
		buttons: $("#dosubmit"),
		onerror : "禁止注册或者用户已存在",
		onwait : "检测用户名是否已存在..."
	});
	$("#email").formValidator({onshow:"请填写常用邮箱,该邮箱用于接收重要信息",onfocus:"请填写常用邮箱,该邮箱用于接收重要信息",oncorrect:"邮箱格式正确"}).regexValidator({regexp:"email",datatype:"enum",onerror:"邮箱格式错误"}).ajaxValidator({
	    type : "get",
		url : "",
		data :"m=member&c=index&a=public_checkemail_ajax",
		datatype : "html",
		async:'false',
		success : function(data){	
            if( data == "1" ) {
                return true;
			} else {
                return false;
			}
		},
		buttons: $("#dosubmit"),
		onerror : "禁止注册或邮箱已存在",
		onwait : "请稍候..."
	});
	$("#company_licence").attr('readonly','true');
	$("#logo").attr('readonly','true');
	$("#cert_photo").attr('readonly','true');
	
	
$("#realname").formValidator({onshow:"请填写真实姓名",onfocus:"请填写真实姓名"}).inputValidator({min:1,max:30,onerror:"请填写真实姓名"});
$("#cert_number").formValidator({onshow:"请正确输入证件号码",onfocus:"请正确输入证件号码"}).inputValidator({min:1,max:50,onerror:"请正确输入证件号码"}).regexValidator({regexp:"^(\\d{18,18}|\\d{15,15}|\\d{14,14}X|\\d{17,17}X|\\d{14,14}x|\\d{17,17}x)$",onerror:"请正确输入证件号码"});
$("#cert_photo").inputValidator({min:0,max:0,onerror:"请上传证件照片"});
$("#phonenum").formValidator({onshow:"请正确填写手机号码",onfocus:"请正确填写手机号码"}).inputValidator({min:0,max:11,onerror:"请正确填写手机号码"}).regexValidator({regexp:"^(1)[0-9]{10}$",onerror:"请正确填写手机号码"});	$(":checkbox[name='protocol']").formValidator({tipid:"protocoltip",onshow:"请阅读协议",oncorrect:" ",onfocus:"请阅读协议"}).inputValidator({min:1,onerror:"请阅读协议"});
	
	$("#code").formValidator({
		onshow:"=HIDE=",
		onfocus:"=HIDE=",
		oncorrect:"正确"
	}).ajaxValidator({
		type : "get",
		url : "",
		data :"m=member&c=index&a=public_check_code",
		datatype : "html",
		async:'false',
		success : function(data){	
            if( data == "1" ) {
                return true;
			} else {
                return false;
			}
		},
		buttons: $("#dosubmit"),
		onerror : "验证码输入错误",
		onwait : "请稍候..."
	});
	
	$("#knewfrom").formValidator({
		onshow : "请选择来源",
		onfocus : "请选择来源"
	}).functionValidator({fun:isSelected,onerror : "请选择来源"});
	
	//刷新验证码
	$("#refresh_code").click(function(){
		$("#code_img").attr('src',$("#code_img").attr('src')+'&'+Math.random());
	});
	
	$("#cert_photo").change(function(){
		if($("#cert_photo").val()!="" || $("#cert_photo").val()!=null){
			$("#cert_photoTip").attr("class","onCorrect");
		}
	});
	
});
function isSelected(){
	var v = $("#knewfrom").val();
	if(v=='0'){
		return false;
	}
	return true;
}
 
function check_protocol(id) {
	var flag = $("#protocol").attr("checked");
	if($("#protocol").attr("checked") == 'checked') {
		show_protocol(id);
		$("#protocoltip").hide();
	} else {
		$("#protocol").removeAttr("checked");
		$("#protocoltip").show();
	}
	
}
 
function show_protocol(id) {
	art.dialog({lock:true,title:'联通开放平台服务协议',id:'protocoliframe', iframe:'?m=agreement&a=show&id='+id,width:'700',height:'400',yesText:'同意'}, function(){
		$("#protocol").attr("checked",true);
	});
};
function person_chck(){
	if($("#cert_photo").val() == '') {
		alert(aaa);
		return false;
	}
 
	return true;
}
function company_chck(){
	if($('#live_area').val()==''){
		alert('请填写完整企业所在地.');
		return false;
	}
	if($('#area').val()==''){
		alert('请填写完整企业注册地.');
		return false;
	}
	if($("#company_licence").val()==''){
		alert('请上传企业执照');
		return false;
	}
	
	return true;
}
$(function(){
	
	$('#area_a').change(function(){
	   $('#area_c').hide();
	   var selectedId = $('#area_a option:selected') .val();
	   if(selectedId!=''){
		     $.getJSON('/index.php?m=member&c=index&a=public_getlinkage', {bid:selectedId,date:new Date().getTime()},function(json){
		    	 if(json!=''){
			    	 $('#area_b').html("<option value=''>请选择市县</option>");
			    	 for(var i=0;i<json.length;i++){
						$('#area_b').append("<option value="+json[i].linkageid+">"+json[i].name+"</option>");
					}
		    	}
	       }); 
	   }else{
		   $('#area_b').html("<option value=''>请选择市县</option>");
		   $('#area').val('');
	   }
	});
	$('#area_b').change(function(){
		   var selectedId = $('#area_b option:selected') .val();
		   
		   if(selectedId!=''){
			     $.getJSON('/index.php?m=member&c=index&a=public_getlinkage', {cid:selectedId,date:new Date().getTime()},function(json){
			    	 if(json!=''){
				    	 $('#area_c').show();
				    	 $('#area_c').html("<option value=''>请选择区县</option>");
				    	 for(var i=0;i<json.length;i++){	
							$('#area_c').append("<option value="+json[i].linkageid+">"+json[i].name+"</option>");
						}
			    	}else{
			    		$('#area_c').hide();
			    	}
		       }); 
			     $('#area').val(selectedId);
			}else{
				$('#area_c').hide();
				$('#area').val('');
			}
		  });
	
	$('#area_c').change(function(){
		var selectedId = $('#area_c option:selected') .val();
		 if(selectedId!=''){
			 $('#area').val(selectedId);
		 }else{
			 $('#area').val('');
		 }
	});
	
	//----------------------------------------------------------
	$('#live_area_a').change(function(){
		   $('#live_area_c').hide();
		   var selectedId = $('#live_area_a option:selected') .val();
		   if(selectedId!=''){
			     $.getJSON('/index.php?m=member&c=index&a=public_getlinkage', {bid:selectedId,date:new Date().getTime()},function(json){
			    	 if(json!=''){
				    	 $('#live_area_b').html("<option value=''>请选择市县</option>");
				    	 for(var i=0;i<json.length;i++){
							$('#live_area_b').append("<option value="+json[i].linkageid+">"+json[i].name+"</option>");
						}
			    	}
		       }); 
		   }else{
			   $('#live_area_b').html("<option value=''>请选择市县</option>");
			   $('#live_area').val('');
		   }
		});
		$('#live_area_b').change(function(){
			   var selectedId = $('#live_area_b option:selected') .val();
			   
			   if(selectedId!=''){
				     $.getJSON('/index.php?m=member&c=index&a=public_getlinkage', {cid:selectedId,date:new Date().getTime()},function(json){
				    	 if(json!=''){
					    	 $('#live_area_c').show();
					    	 $('#live_area_c').html("<option value=''>请选择区县</option>");
					    	 for(var i=0;i<json.length;i++){	
								$('#live_area_c').append("<option value="+json[i].linkageid+">"+json[i].name+"</option>");
							}
				    	}else{
				    		$('#live_area_c').hide();
				    	}
			       }); 
				     $('#live_area').val(selectedId);
				}else{
					$('#live_area_c').hide();
					$('#live_area').val('');
				}
			  });
		
		$('#live_area_c').change(function(){
			var selectedId = $('#live_area_c option:selected') .val();
			 if(selectedId!=''){
				 $('#live_area').val(selectedId);
			 }else{
				 $('#live_area').val('');
			 }
		});
		
		
});