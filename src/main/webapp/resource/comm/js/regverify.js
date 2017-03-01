
$(function(){
	$.formValidator.initConfig({wideword:true,autotip:true,formid:"myform",onerror:function(msg){},onsuccess:function(){
		return confirm('��ȷ������д�������֤��Ϣ������Ϣ��ͨ����˺��޷��޸�!');
	}});
	
	$("#cert_number").formValidator({onshow:"������֤������",onfocus:"����ȷ����֤������",onerror:"֤�������������"})
	.inputValidator({min:0,max:18,onerror:"֤�������������"})
	.regexValidator({regexp:'^(\\d{18,18}|\\d{15,15}|\\d{14,14}x|\\d{17,17}x)$',onerror:"֤�������������"});
	$("#realname").formValidator({onshow:"����д��ʵ����",onfocus:"����д��ʵ����",onempty:"&nbsp", empty:true}).inputValidator({min:1,max:30,onerror:"����д��ʵ����"});
	
	$("#phonenum").val('189591aa88204').formValidator({onshow:"����ȷ��д�ֻ�����",onfocus:"����ȷ��д�ֻ�����",onempty:"&nbsp",empty:true})
	.inputValidator({min:0,max:11,onerror:"����ȷ��д�ֻ�����"})
	.regexValidator({regexp:"^(1)[0-9]{10}$",onerror:"����ȷ��д�ֻ�����"});
	

	$("#username").formValidator({onshow:"�û��������ڱ�ƽ̨����ʾ������",onfocus:"�û���������6�����ֻ���20��Ӣ����ĸ"}).inputValidator({min:3,max:20,onerror:"�û���������6�����ֻ���20��Ӣ����ĸ"}).regexValidator({regexp:"ps_username",datatype:"enum",onerror:"�û�����ʽ����"}).ajaxValidator({
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
		onerror : "��ֹע������û��Ѵ���",
		onwait : "����û����Ƿ��Ѵ���..."
	});
	$("#email").formValidator({onshow:"����д��������,���������ڽ�����Ҫ��Ϣ",onfocus:"����д��������,���������ڽ�����Ҫ��Ϣ",oncorrect:"�����ʽ��ȷ"}).regexValidator({regexp:"email",datatype:"enum",onerror:"�����ʽ����"}).ajaxValidator({
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
		onerror : "��ֹע��������Ѵ���",
		onwait : "���Ժ�..."
	});
	$("#company_licence").attr('readonly','true');
	$("#logo").attr('readonly','true');
	$("#cert_photo").attr('readonly','true');
	
	
$("#realname").formValidator({onshow:"����д��ʵ����",onfocus:"����д��ʵ����"}).inputValidator({min:1,max:30,onerror:"����д��ʵ����"});
$("#cert_number").formValidator({onshow:"����ȷ����֤������",onfocus:"����ȷ����֤������"}).inputValidator({min:1,max:50,onerror:"����ȷ����֤������"}).regexValidator({regexp:"^(\\d{18,18}|\\d{15,15}|\\d{14,14}X|\\d{17,17}X|\\d{14,14}x|\\d{17,17}x)$",onerror:"����ȷ����֤������"});
$("#cert_photo").inputValidator({min:0,max:0,onerror:"���ϴ�֤����Ƭ"});
$("#phonenum").formValidator({onshow:"����ȷ��д�ֻ�����",onfocus:"����ȷ��д�ֻ�����"}).inputValidator({min:0,max:11,onerror:"����ȷ��д�ֻ�����"}).regexValidator({regexp:"^(1)[0-9]{10}$",onerror:"����ȷ��д�ֻ�����"});	$(":checkbox[name='protocol']").formValidator({tipid:"protocoltip",onshow:"���Ķ�Э��",oncorrect:" ",onfocus:"���Ķ�Э��"}).inputValidator({min:1,onerror:"���Ķ�Э��"});
	
	$("#code").formValidator({
		onshow:"=HIDE=",
		onfocus:"=HIDE=",
		oncorrect:"��ȷ"
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
		onerror : "��֤���������",
		onwait : "���Ժ�..."
	});
	
	$("#knewfrom").formValidator({
		onshow : "��ѡ����Դ",
		onfocus : "��ѡ����Դ"
	}).functionValidator({fun:isSelected,onerror : "��ѡ����Դ"});
	
	//ˢ����֤��
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
	art.dialog({lock:true,title:'��ͨ����ƽ̨����Э��',id:'protocoliframe', iframe:'?m=agreement&a=show&id='+id,width:'700',height:'400',yesText:'ͬ��'}, function(){
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
		alert('����д������ҵ���ڵ�.');
		return false;
	}
	if($('#area').val()==''){
		alert('����д������ҵע���.');
		return false;
	}
	if($("#company_licence").val()==''){
		alert('���ϴ���ҵִ��');
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
			    	 $('#area_b').html("<option value=''>��ѡ������</option>");
			    	 for(var i=0;i<json.length;i++){
						$('#area_b').append("<option value="+json[i].linkageid+">"+json[i].name+"</option>");
					}
		    	}
	       }); 
	   }else{
		   $('#area_b').html("<option value=''>��ѡ������</option>");
		   $('#area').val('');
	   }
	});
	$('#area_b').change(function(){
		   var selectedId = $('#area_b option:selected') .val();
		   
		   if(selectedId!=''){
			     $.getJSON('/index.php?m=member&c=index&a=public_getlinkage', {cid:selectedId,date:new Date().getTime()},function(json){
			    	 if(json!=''){
				    	 $('#area_c').show();
				    	 $('#area_c').html("<option value=''>��ѡ������</option>");
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
				    	 $('#live_area_b').html("<option value=''>��ѡ������</option>");
				    	 for(var i=0;i<json.length;i++){
							$('#live_area_b').append("<option value="+json[i].linkageid+">"+json[i].name+"</option>");
						}
			    	}
		       }); 
		   }else{
			   $('#live_area_b').html("<option value=''>��ѡ������</option>");
			   $('#live_area').val('');
		   }
		});
		$('#live_area_b').change(function(){
			   var selectedId = $('#live_area_b option:selected') .val();
			   
			   if(selectedId!=''){
				     $.getJSON('/index.php?m=member&c=index&a=public_getlinkage', {cid:selectedId,date:new Date().getTime()},function(json){
				    	 if(json!=''){
					    	 $('#live_area_c').show();
					    	 $('#live_area_c').html("<option value=''>��ѡ������</option>");
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