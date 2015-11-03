<%@ include file="commons/include.jsp"%>
<script src="../lib/sweet-alert.min.js"></script>
<link rel="stylesheet" type="text/css" href="../lib/sweet-alert.css">

<script type="text/javascript">

<c:url var="findAccountNoURL" value="getAccountNo.htm" />
var showError = true;
<!--
$(document)
.ready(
		function() {
			$('#secretKey')
					.keyup(function() {
						var accessKey = $('#accessKey').val();
						var secretKey = $('#secretKey').val();
						if(accessKey != null && accessKey != '' && secretKey != null && secretKey != ''){
							callAjax();
						}
			});
			$('#accessKey')
					.keyup(function() {
						var accessKey = $('#accessKey').val();
						var secretKey = $('#secretKey').val();
						if(accessKey != null && accessKey != '' && secretKey != null && secretKey != ''){
							callAjax();;
						}
			});

		});


function callAjax(){
	$.ajax({
		type : 'GET',
		url : "${findAccountNoURL}",
		data : "accessKey="
				+ $('#accessKey').val() +
				"&secretKey="
				+ $('#secretKey').val(),
		success : function(data) {
			var obj = JSON
			.parse(data);
			
			$('#accountNo').val(obj[0]+"/"+obj[2]);
			$('#accountDesc').val(obj[1]);

			if(obj[0]==NaN){
				swal({   title: "Error!",   text: "Inactivated keys, please check!",   type: "error",   confirmButtonText: "Got it" });
			}
		},
		error : function(e) {
 			if(showError){
				showError = false;
				swal({   title: "Error!",   text: "Invalid keys, please check!",   type: "error",   confirmButtonText: "Got it" });
			}
 		}
	});
}



//-->
</script>

<script type="text/javascript">

function validateForm(){
	var accessKey = document.getElementById('accessKey').value;
	var secretKey = document.getElementById('secretKey').value;
	var accountDesc = document.getElementById('accountDesc').value;
	var accountForm = document.forms['AccountForm'];
	if(accessKey==NaN || accessKey==''){
		swal("Please enter accessKey");
	}
	else if(secretKey==NaN || secretKey==''){
		swal("Please enter secretKey");
	}
	else if(accountDesc==NaN || accountDesc==''){
		swal("Please enter accountDesc");
	}else{
		accountForm.submit();
	}
}

</script>

<form:form class="form-horizontal" id="AccountForm" role="form" action="add.htm" method="POST" modelAttribute="account">


  <div class="form-group">
    <label class="col-sm-2 control-label" for="Access Key">Access Key*</label>
    <div class="col-sm-5">
    <form:input class="form-control input-sm" path="accessKey"/>
    </div>
  </div>
 
  <div class="form-group">
    <label class="col-sm-2 control-label" for="Secret Key">Secret Key*</label>
    <div class="col-sm-8">
    <form:input class="form-control input-sm" path="secretKey"/>
    </div>
  </div>
  
 
 <div class="form-group">
    <label class="col-sm-2 control-label" for="Account Number">Account Number/AWS UserID (Read Only)</label>
    <div class="col-sm-6">
    <form:input class="form-control input-sm" readonly="true" path="accountNo"/>
    </div>
  </div>
 
  
 <div class="form-group">
    <label class="col-sm-2 control-label" for="Account Name">Account Name</label>
    <div class="col-sm-6">
    <form:input class="form-control input-sm" path="accountDesc"/>
    </div>
  </div>
   
   <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <a class="btn btn-primary btn-sm"  onclick="validateForm();"> Add/Update </a>
    </div>
  </div>
</form:form>
