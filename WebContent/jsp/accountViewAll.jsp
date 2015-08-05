<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="commons/include.jsp"%>

<script type="text/javascript">
function confirmUpdate(accountNumber,flag){
	if(flag==1){
		
		swal(
				{
					title : "Are you sure to inactivate AWS account : " + accountNumber,
					text : "You will no longer be able to create keys using this account, until it is reactivated again!",
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "#DD6B55",
					confirmButtonText : "Yes, inactivate account!",
					cancelButtonText : "No, cancel please!",
					closeOnConfirm : false,
					closeOnCancel : false
				},
				function(isConfirm) {
					if (isConfirm) {
				   		var hiddenField1 = document.getElementById("account");
				   		var hiddenField2 = document.getElementById("flag");
				   		hiddenField1.value = accountNumber;
				   		hiddenField2.value = 1;
				   		AccountForm.action = "updateAccount.htm?account="+accountNumber+"&flag="+flag;
				   		AccountForm.submit();
						swal("Inactivated", "Inactivated account as requested",
								"success");
					} else {
						swal("Cancelled", "Your account remains active :)", "error");
					}
				});
	}
	if(flag==0){

		swal(
				{
					title : "Are you sure to activate AWS account : " + accountNumber,
					text : "Any earlier associations to generate keys will be shown to user once the account is activated. But you will be able to inactivate again!",
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "#DD6B55",
					confirmButtonText : "Yes, activate account!",
					cancelButtonText : "No, cancel please!",
					closeOnConfirm : false,
					closeOnCancel : false
				},
				function(isConfirm) {
					if (isConfirm) {
				   		var hiddenField1 = document.getElementById("account");
				   		var hiddenField2 = document.getElementById("flag");
				   		hiddenField1.value = accountNumber;
				   		hiddenField2.value = 0;
				   		AccountForm.action = "updateAccount.htm?account="+accountNumber+"&flag="+flag;
				   		AccountForm.submit();
						swal("Activate", "Activated account as requested",
								"success");
					} else {
						swal("Cancelled", "Your account remains inactive :)", "error");
					}
				});
	}

}

function confirmRegenerate(accountNumber){
	var flag=2;
		swal(
				{
					title : "Are you sure to rotate keys for AWS account : " + accountNumber,
					text : "Earlier keys will be overwritten and no longer be active!",
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "#DD6B55",
					confirmButtonText : "Yes, rotate keys for this account!",
					cancelButtonText : "No, cancel please!",
					closeOnConfirm : false,
					closeOnCancel : false
				},
				function(isConfirm) {
					if (isConfirm) {
				   		var hiddenField1 = document.getElementById("account");
				   		var hiddenField2 = document.getElementById("flag");
				   		hiddenField1.value = accountNumber;
				   		hiddenField2.value = flag;
				   		AccountForm.action = "updateAccount.htm?account="+accountNumber+"&flag="+flag;
				   		AccountForm.submit();
						swal("Regenerate Keys", "Rotated keys for account as requested",
								"success");
					} else {
						swal("Cancelled", "Your account keys remain active :)", "error");
					}
				});
}

</script>

<c:set var="accountsToSearch" value="${accounts}" />

<!-- <form method="get" action="">
<input type="text" id="searchBox" name="q" />
<input type="submit" id="searchButton" value="Search" />
</form>

<script type="text/javascript">
var timer = null;
$('#searchBox').keydown(function(){
       clearTimeout(timer); 
       timer = setTimeout(doStuff, 1000)
});

function doStuff() {
    alert('do stuff');
    //acountsBody inner html equals same but with new accounts  
}

//str1.toLowerCase().contains(str2.toLowerCase())
</script>   -->


<form:form id="AccountForm" method="get"
	modelAttribute="account">
<table>
	<thead>
		<tr class='heading'> <th> AWS Account </th><th> Details </th> <th> Status Toggle </th> <th> Last Modified </th> </tr>
	</thead>
	<tbody id="accountsBody">
		<c:forEach items="${accountsToSearch}" var="account" begin="0" end="${fn:length(accounts)}" varStatus="loopStatus">
			<tr  class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
				<td><p><b>Account Name</b><br> ${account.accountDesc}<br>
				<p><b>Account Number</b> <br> ${account.accountNo}</p></td>


					<td> 
					
					<b> Access Key</b> <br>
					${account.accessKey} 
				    (<a id="rotateLink" onclick="confirmRegenerate('${account.accountNo}');">Rotate</a>)
					<p>
					<b>Last Rotated<br></b>
				    <c:if test="${account.rotateDate != null && account.rotateBy != null}">
				    	<c:if test="${ fn:contains(account.rotateBy, '@')}">
							${fn:substring(account.rotateBy, 0, fn:indexOf(account.rotateBy, "@"))} 
						</c:if>
						<c:if test="${!fn:contains(account.rotateBy, '@')}">
							${account.rotateBy}
						</c:if>
						${account.rotateDate}
					</c:if>
					<c:if test="${account.rotateDate == null || account.rotateBy == null}">
						Never Rotated
					</c:if>
					</p>
					</td>
					

				<td id="toggle">
					<c:choose>
						<c:when test="${(account.active == 1)}">	    					
							<a class="buttonActive" onclick="confirmUpdate('${account.accountNo}',1);"> Active </a>
						</c:when>
						<c:when test="${(account.active == 0)}">
	   						<a class="buttonInactive" onclick="confirmUpdate('${account.accountNo}',0);"> Inactive </a>
						</c:when>
					</c:choose>
				</td>
				<td>
					<div id="lastModified">
				    	<c:if test="${ fn:contains(account.lastUpdatedBy, '@')}">
							${fn:substring(account.lastUpdatedBy, 0, fn:indexOf(account.lastUpdatedBy, "@"))} 
						</c:if>
						<c:if test="${!fn:contains(account.lastUpdatedBy, '@')}">
							${account.lastUpdatedBy}
						</c:if>
						<br>
						${account.lastUpdateTime}
					</div>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div id="addButtonContainer" align="center"><a class="buttonLink" href="new.htm"><b id="addSign">+</b> Account</a></div>

<form:hidden path="accountNo" id="account"/>
<form:hidden path="flag" id="flag"/>
</form:form>