<%@ include file="commons/include.jsp"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="../lib/sweet-alert.min.js"></script>
<link rel="stylesheet" type="text/css" href="../lib/sweet-alert.css">

<script type="text/javascript">
<c:url var="getKeysURL" value="getKeys.htm" />
function showKeyPopup(accountIndex, roleIndex) {
	var accountNo = document.getElementById('selectedAccount'+(accountIndex)).value;
	var role = document.getElementById('selectedRole'+(accountIndex)+(roleIndex)).value;
	var e = document.getElementById('duration'+(accountIndex)+(roleIndex));
	var time = e.options[e.selectedIndex].value;
	
    $.ajax({
    	type : 'GET',
    	url : "${getKeysURL}",
    	data : "selectedAccount="
    			+ accountNo +
    			"&selectedRole="
    			+ role +
    			"&time="
    			+time,
    	success : function(data) {
    		var obj = JSON.parse(data);
    		var accessKey = obj[0];
    		var secretKey = obj[1];
    		var sessionToken = obj[2];
    		var consoleUrl = obj[3];
    		
    		 var generator = window.open('',accountIndex+roleIndex,'directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,height=350,width=700');
     		  generator.document.write('<head><title>AWS Temporary Session Tokens</title><link rel="stylesheet" href="../css/ui.css"  media="screen"></head>');
    		  generator.document.write('<table>');
    		  generator.document.write('<tr><td colspan=1>Account No :</td><td colspan=2>' + accountNo + '</td></tr><tr>');
    		  generator.document.write('<tr><td colspan=1>Role :</td><td colspan=2>' + role + '</td></tr><tr>');
    		  generator.document.write('<tr><td colspan=1>Expires in (Hours) :</td><td colspan=2>' + time + '</td></tr><tr>');
     		  generator.document.write('<td>Access Key    :</td>');
    		  generator.document.write("<td colspan=2><input type='text' size=25  name='accessKey' readonly value=" + obj[0] +  "></td>");
//    		  generator.document.write('<td><input type="button" class="active" value="Easy Copy" onclick="copyToClipboard1(AWSKeyForm.accessKey.value);"/></td>');
			  generator.document.write('</tr><tr>');
    		  generator.document.write('<td>Secret Key    :</td>');		
    		  generator.document.write('<td colspan=2><input type="text" size=80 name="secretKey" readonly value=' + obj[1] + '></td>');		
//    		  generator.document.write('<td><input type="button" class="active" value="Easy Copy" onclick="copyToClipboard1(AWSKeyForm.secretKey.value);"/></td>');		
			  generator.document.write('</tr><tr>');
    		  generator.document.write('<td>Session Token :</td>');
    		  generator.document.write('<td colspan=2><textarea rows="8" cols="50" name="sessionToken" readonly>' + obj[2] +'</textarea></td></tr>');
    		 // generator.document.write('<td><input type="button" class="active" value="Easy Copy" onclick="copyToClipboard1(AWSKeyForm.sessionToken.value);"/></td>');
			 // generator.document.write('</tr>');
     		  generator.document.write('<tr><td>Temporary AWS Console URL :</td>');
    		  generator.document.write('<td><a href=' + obj[3] + '>Access AWS console</a></td></tr>');
			  generator.document.write('</table>');
    		  generator.document.close();
    	},
    	error : function(e) {
    		swal(
    		{   title: "Opps!",   text: 'Session expired/Invalid policy/Permissions issue. Please try again or contact helpdesk if repeats!!',   imageUrl: "../images/dontpanic.jpg" });
    		
    	}
    });   
}
</script>

<form:form action="../key/KeyController.htm" id="KeyForm" method="post"
	modelAttribute="selectedAccount">
	<fieldset>
		<div id="login_div">
			<c:choose>
				<c:when test="${(fn:length(acctRoles) > 0)}">
					<table  width="100%" align="center">
						<tbody>
							<c:forEach var="selectedAccount" items="${acctRoles}"
								varStatus="selectedAccountIndex">
								<tr class='heading'>
									<th colspan="1">Account Number : ${selectedAccount.key}</th><th>Session Time</th><th>Generate</th>
									<form:hidden id="selectedAccount${selectedAccountIndex.count}" path="accountNo" value="${selectedAccount.key}"/>
								</tr>
								<c:forEach var="role" items="${selectedAccount.value}"
									varStatus="selectedRoleIndex">
									<tr
										class="${selectedRoleIndex.index % 2 == 0 ? 'even' : 'odd'}">
										<td>Role: ${role}</td>
										<form:hidden id="selectedRole${selectedAccountIndex.count}${selectedRoleIndex.count}" path="role" value="${role}"/>
										
										<!-- Don't need all of these fields -->
										<form:hidden id="accessKey${selectedAccountIndex.count}${selectedRoleIndex.count}" path="accessKey" value=""/>
										<form:hidden id="secretKey${selectedAccountIndex.count}${selectedRoleIndex.count}" path="secretKey" value=""/>
										<form:hidden id="sessionToken${selectedAccountIndex.count}${selectedRoleIndex.count}" path="sessionToken" value=""/>
										<!-- Don't need all of these fields -->
										
										<td><select id="duration${selectedAccountIndex.count}${selectedRoleIndex.count}">
										
										<option value="2">2 Hour</option>
										<option value="6">6 Hour</option>
										<option value="12">12 Hour</option>
										<option value="18">18 Hour</option>
										<option value="24">24 Hour</option>
										<option value="36">36 Hour</option>
										
										</select></td>
										<td><a class="menu buttonLink center" onclick="showKeyPopup(${selectedAccountIndex.count},${selectedRoleIndex.count})">
											Generate Keys </a></td>
									</tr>
								</c:forEach>
								<tr>
									<td colspan="3"></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:when test="${!(fn:length(acctRoles) > 0)}">
								Sorry there are no <u><b>active</b></u> accounts to create temporary tokens, please contact help desk.
				</c:when>
			</c:choose>
		</div>
	</fieldset>
</form:form>