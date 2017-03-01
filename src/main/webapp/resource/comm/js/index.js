var flag = 0;
function showDevContent(){
	//flag = "dev";
	if(flag == 1){
		return;
	}
	flag = 1;
	$('#title1').attr("src","resource/orange/images/dev_org" + $('#localName').val() + ".png");
	$('#title2').attr("src","resource/orange/images/api_white" + $('#localName').val() + ".png");
	$('#title3').attr("src","resource/orange/images/oper_white" + $('#localName').val() + ".png");
	showDev();
}

function showProvContent(){
	//flag = "prov";
	if(flag == 1){
		return;
	}
	flag = 1;
	$('#title1').attr("src","resource/orange/images/dev_white" + $('#localName').val() + ".png");
	$('#title2').attr("src","resource/orange/images/api_org" + $('#localName').val() + ".png");
	$('#title3').attr("src","resource/orange/images/oper_white" + $('#localName').val() + ".png");
	showProv();
}

function showParContent(){
	//flag = "par";
	if(flag == 1){
		return;
	}
	flag = 1;
	$('#title1').attr("src","resource/orange/images/dev_white" + $('#localName').val() + ".png");
	$('#title2').attr("src","resource/orange/images/api_white" + $('#localName').val() + ".png");
	$('#title3').attr("src","resource/orange/images/oper_org" + $('#localName').val() + ".png");
	showPar();
}

function showDev() {
	var devEle = document.getElementById("devImg");
	var devWidth = devEle.width;
	var provWidth = document.getElementById("provImg").width;
	var parWidth = document.getElementById("parImg").width;

	if (devWidth < 844) {
		devWidth = devWidth + 50;
		if (devWidth > 844) {
			devWidth = 844;
		}
		$('#devImg').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 1px solid;BORDER-RIGHT:#B8BECC 1px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		$('#devImg').css("width", devWidth);

		provWidth = provWidth - 50;
		parWidth = parWidth - 50;
		if (provWidth < 0) {
			provWidth = 0;
		}
		if (parWidth < 0) {
			parWidth = 0;
		}
		
		$('#provImg').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 0px solid;BORDER-RIGHT:#B8BECC 0px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		$('#provImg').css("width", provWidth);
		$('#parImg').css("width", parWidth);
		setTimeout(function() {
			showDev()
		}, 10);
	}
	else{
		$('#box3').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 1px solid;BORDER-RIGHT:#B8BECC 2px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		$('#parImg').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 0px solid;BORDER-RIGHT:#B8BECC 0px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		flag = 0;
	}
}

function showProv() {
	var provEle = document.getElementById("provImg");
	var provWidth = provEle.width;
	var devWidth = document.getElementById("devImg").width;
	var parWidth = document.getElementById("parImg").width;
	
	if (provWidth < 844) {
		provWidth = provWidth + 50;
		if (provWidth > 844) {
			provWidth = 844;
		}
		$('#provImg').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 1px solid;BORDER-RIGHT:#B8BECC 1px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		$('#provImg').css("width", provWidth);

		devWidth = devWidth - 50;
		parWidth = parWidth - 50;
		if (devWidth < 0) {
			devWidth = 0;
		}
		if (parWidth < 0) {
			parWidth = 0;
		}
		
		$('#devImg').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 0px solid;BORDER-RIGHT:#B8BECC 0px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		$('#devImg').css("width", devWidth);
		$('#parImg').css("width", parWidth);
		setTimeout(function() {
			showProv()
		}, 10);
	}
	else{
		$('#box3').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 1px solid;BORDER-RIGHT:#B8BECC 2px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		$('#parImg').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 0px solid;BORDER-RIGHT:#B8BECC 0px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		flag = 0;
	}
}

function showPar() {
	var parEle = document.getElementById("parImg");
	var parvWidth = parEle.width;
	var devWidth = document.getElementById("devImg").width;
	var provWidth = document.getElementById("provImg").width;
	$('#box3').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 1px solid;BORDER-RIGHT:#B8BECC 1px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
	
	if (parvWidth < 844) {
		parvWidth = parvWidth + 50;
		if (parvWidth > 844) {
			parvWidth = 844;
		}
		$('#parImg').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 1px solid;BORDER-RIGHT:#B8BECC 2px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		$('#parImg').css("width", parvWidth);
		
		devWidth = devWidth - 50;
		provWidth = provWidth - 50;
		if (devWidth < 0) {
			devWidth = 0;
		}
		if (provWidth < 0) {
			provWidth = 0;
		}
		$('#devImg').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 0px solid;BORDER-RIGHT:#B8BECC 0px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		$('#provImg').attr("style", "BORDER-TOP:#B8BECC 2px solid;BORDER-LEFT:#B8BECC 0px solid;BORDER-RIGHT:#B8BECC 0px solid;BORDER-BOTTOM:#B8BECC 2px solid;");
		$('#devImg').css("width", devWidth);
		$('#provImg').css("width", provWidth);
		setTimeout(function() {
			showPar()
		}, 10);
	}else{
		flag = 0;
	}
}