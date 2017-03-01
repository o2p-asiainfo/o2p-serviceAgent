
function doshowApi(action,id){
	self.location = action + "&id="+id;
}

function doQuery(action,id){
    if(id == ''){
    	self.location = action;
    }   
    else{
     	self.location = action + "?id="+id;
    }
}

function showIntfo(action,id){

	self.location = action + "?dirId=" + id;
	
}