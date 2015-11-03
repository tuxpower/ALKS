<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">

function confirmDelete(actId,actNo,actRole,adGrp){		
		swal(
				{
					title : "Are you sure to delete ADG Relation for AWS account : " + actNo,
					text : "Association between " + actRole + " and " + adGrp + " will be deleted!",
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "#DD6B55",
					confirmButtonText : "Yes, delete association!",
					cancelButtonText : "No, cancel please!",
					closeOnConfirm : false,
					closeOnCancel : false
				},
				function(isConfirm) {
					if (isConfirm) {
				   		var hiddenField1 = document.getElementById("accountId");
				   		var hiddenField2 = document.getElementById("adGroup");
				   		hiddenField1.value = actId;
				   		hiddenField2.value = adGrp;
				   		ADGForm.action = "deleteADG.htm?accountId="+actId+"&adGroup="+adGrp;
				   		ADGForm.submit();
						swal("Deleted", "Association is deleted as requested",
								"success");
					} else {
						swal("Cancelled", "Association remains active :)", "error");
					}
				});
}

</script>
  	
<%@ include file="commons/include.jsp"%>

<form:form id="ADGForm" method="get"
	modelAttribute="adg">
<div class="container-fluid">
 <table class="table table-hover table-striped">
  <thead>
  <tr class='heading'><th></th><th>AWS Account/User Name - Description</th> <th>Role</th> <th>AD Group</th><th>Last Modified</th> </tr></thead>
  <tbody>
  <c:forEach items="${adgs}" var="adg" varStatus="loopStatus">
  <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
  	<td nowrap><a><span class="glyphicon glyphicon-trash" onClick="confirmDelete('${adg.accountId}','${adg.accountNo}','${adg.role}','${adg.adGroup}');"></span></a></td>
  	<td nowrap>${adg.accountNo}</td><td nowrap>${adg.role}</td>
  	<td nowrap>${adg.adGroup}</td>
  	<!-- td><input class="menu" type="button" value="Inactivate" onclick='deleteConfirm(${arp.id});'/></td-->
  	<td nowrap>
					<div id="lastModified">
				    	<c:if test="${ fn:contains(adg.lastUpdatedBy, '@')}">
							${fn:substring(adg.lastUpdatedBy, 0, fn:indexOf(adg.lastUpdatedBy, "@"))} 
						</c:if>
						<c:if test="${!fn:contains(adg.lastUpdatedBy, '@')}">
							${adg.lastUpdatedBy}
						</c:if>
						<br>
						${adg.lastUpdateTime}
					</div>
	</td>	
  </tr>
  </c:forEach> 
  </tbody>
 </table>
 </div>
 
 <div id="addButtonContainer" align="center"><a class="btn btn-primary btn-sm" href="new.htm"><b id="addSign">+</b> ADG</a></div>
 
 <form:hidden path="accountId" id="accountId"/>
<form:hidden path="adGroup" id="adGroup"/>
</form:form>