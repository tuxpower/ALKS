<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">
<!--
function deleteConfirm(text){
	alert(text);
}
//-->
</script>


<script lang="text/javascript">

function ClipBoard(element) 
{
alert( $(element).find('pre:first').text());

holdtext.innerText = $(element).find('pre:first').text();
Copied = holdtext.createTextRange();
Copied.execCommand("Copy");

}

</script>

<script>
$(document).ready(function(){
    $('[data-toggle="popover"]').popover(); 
});
</script>
 
<%@ include file="commons/include.jsp"%>

<table class="table table-striped">
	<thead>
		<tr class='heading'>
			<th>AWS Account/User Name - Description</th>
			<th>Role</th>
			<th>Policy</th>
			<th>Last Modified</th> 
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${arps}" var="arp" varStatus="theCount">
			<tr  class="${theCount.index % 2 == 0 ? 'even' : 'odd'}">
				<td>${arp.accountNo}</td>
				<td>${arp.role}</td>
		
				<td>
				<a href="#" onclick="return false" data-toggle="popover" data-content='${arp.policy}'  >
					Show Policy
    			</a>
				</td>	
		
				<td>
					<div id="lastModified">
				    	<c:if test="${ fn:contains(arp.lastUpdatedBy, '@')}">
							${fn:substring(arp.lastUpdatedBy, 0, fn:indexOf(arp.lastUpdatedBy, "@"))} 
						</c:if>
						<c:if test="${!fn:contains(arp.lastUpdatedBy, '@')}">
							${arp.lastUpdatedBy}
						</c:if>
						<br>
						${arp.lastUpdateTime}
					</div>
				</td>	
			</tr>
		</c:forEach>
	</tbody>
</table>

<div id="addButtonContainer" align="center"><a class="btn btn-primary btn-sm" href="new.htm"><b id="addSign">+</b> ARP</a></div>