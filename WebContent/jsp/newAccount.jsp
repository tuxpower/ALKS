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
						if(accessKey != null && secretKey != null){
							callAjax();
						}
			});
			$('#accessKey')
			.keyup(function() {
				var accessKey = $('#accessKey').val();
				var secretKey = $('#secretKey').val();
				if(accessKey != null && secretKey != null){
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
			$('#accountNo').val(obj[0]);
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
	if(accessKey==NaN || accessKey==''){
		swal("Please enter accessKey");
	}
	else if(secretKey==NaN || secretKey==''){
		swal("Please enter secretKey");
	}
	else if(accountDesc==NaN || accountDesc==''){
		swal("Please enter accountDesc");
	}else{
		AccountForm.submit();
	}
}

</script>
<form:form id="AccountForm" action="add.htm" method="POST" modelAttribute="account">

<table>
	<tr>
		<td align="right">
				<label for="accessKey">Access Key: *</label>
		</td>
		<td align="center">
				<span style="display: inline-block; min-width: 400px;">
					<form:input path="accessKey" />	
				</span>
		</td>	
	</tr>
	<tr>
		<td align="right">
				<label for="secretKey">Secret Key: *</label>
		</td>
		<td align="center">
				<span style="display: inline-block; min-width: 400px;">
					<form:input path="secretKey" />	
				</span>
		</td>	
	</tr>
		<tr>
		<td align="right">
				<label for="accountNo">Account Number <br>(Read Only) : *</label>
		</td>
		<td align="center">
				<span style="display: inline-block; min-width: 400px;">
					<form:input readonly="true" path="accountNo"/>
				</span>
		</td>
	</tr>
	<tr>
		<td align="right">
				<label for="accountDesc">Account Name:</label>
		</td>
		<td align="center">
				<span style="display: inline-block; min-width: 400px;">
					<form:input path="accountDesc"/>
				</span>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<span style="display: inline-block; min-width: 100px;">
				<a class="buttonLink addButton" onclick="validateForm();"> Add/Update </a>
			</span>
		</td>
	</tr>
	
</table>
</form:form>
