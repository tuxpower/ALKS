<%@ include file="commons/include.jsp"%>
<script src="../lib/sweet-alert.min.js"></script>
<link rel="stylesheet" type="text/css" href="../lib/sweet-alert.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js"></script>


<c:url var="findRolesURL" value="getRoles.htm" />
<c:url var="findAccountIdURL" value="getAccountId.htm" />
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('#accountNo')
								.change(
										function() {
											$
													.ajax({
														type : 'GET',
														url : "${findRolesURL}",
														data : "accountNumber="
																+ $(this).val(),
														success : function(data) {
															var obj = JSON
																	.parse(data);
															var html = '<option value="">Select Role</option>';
															var len = obj.length;
															for (var i = 0; i < len; i++) {
																html += '<option value="' + obj[i] + '">'
																		+ obj[i]
																		+ '</option>';
															}
															html += '</option>';

															$('#role').html(
																	html);
														},
														error : function(e) {
															alert('Error in Processing, please try again later or contact system admin if error persists');
														}
													});
										});
					});

	$(document)
			.ready(
					function() {
						$('#role')
								.change(
										function() {
											$
													.ajax({
														type : 'GET',
														url : "${findAccountIdURL}",
														data : "role="
																+ $(this).val() +
																"&accountNumber="
																+ $('#accountNo').val(),
														success : function(data) {
															var obj = JSON
															.parse(data);
															$('#accountId').val(obj[0]);
														},
														error : function(e) {
															alert('Error in Processing, please try again later or contact system admin if error persists');
														}
													});
										});
					});

	//Enable AD group only after role is selected.
	/*function checkRole(){
		var role = document.getElementById("role");
		if(role.selectedIndex >0){
			adGroup.enable = true;
		}
	}*/
	
	function validateForm(){
		var roleInd = document.getElementById('role').selectedIndex;
		if(roleInd==0){
			swal("Please select account and role");
		}else{
			var adGroupInd = document.getElementById('adGroup').selectedIndex;
			if(adGroupInd==0){
				swal("Please select an AD Group");
			}else{
				ADGForm.submit();
			}
		}
	}


</script>


<form:form id="ADGForm" action="add.htm" method="POST" modelAttribute="adg">
	<table>
		<tr>
			<td align="right">
				<span style="display: block;"> 
					<label for="accountNo">Account Number:</label>
				</span>
			</td>
			<td align="center">
				<span style="display: inline-block; min-width: 300px;"> 
					<form:select id="accountNo" path="accountNo">
						<form:option value="NONE" label="Select Account " />
						<form:options items="${accounts}" />
					</form:select>
				</span>
			</td>
		</tr>
		<tr>
			<td align="right">
				<span style="display: block;">
					<label for="Role">AWS Role:</label>
				</span>
			</td>
			<td align="center">
				<span style="display: inline-block; min-width: 300px;">
					<form:select id="role" path="role">
						<form:option value="">Select Role</form:option>
					</form:select>
				</span>
			</td>
		</tr>
		<tr>
			<td align="right">
				<span style="display: block;">
					<label for="accountNo">AD Group:</label>
				</span>
			</td>
			<td align="center">
				<span style="display: inline-block; min-width: 300px;">
					<form:select id="adGroup" path="adGroup">
						<form:option value="">Select AD Group</form:option>
						<form:options items="${adGroups}" />
					</form:select>
				</span>
			</td>
		</tr>
		<tr>
			<td align="center" colspan="2">
				<span style="display: block;">
					<a class="buttonLink addButton" onclick="validateForm()"> Add/Update </a>
				</span>
			</td>
		</tr>
	</table>
<form:hidden path="accountId" />
</form:form>
