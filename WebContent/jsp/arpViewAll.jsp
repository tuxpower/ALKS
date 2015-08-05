<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="../css/ui.css" media="screen">
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

<%@ include file="commons/include.jsp"%>

<table>
	<thead>
		<tr class='heading'>
			<th>AWS Account</th>
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
				<td><div class="center"><a href="#divIDNo${theCount.count}" data-rel="popup">Show</a></div>
					<div data-role="popup" id="divIDNo${theCount.count}">
						<h3>Policy Details</h3>
						<pre>${arp.policy}</pre>
						<BUTTON onClick="ClipBoard(divIDNo${theCount.count});">Click to Copy</BUTTON>
					</div></td>	
				<!-- td><input class="menu" type="button" value="Inactivate"
					onclick='deleteConfirm(${arp.id});' /></td-->
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

<div id="addButtonContainer" align="center"><a class="buttonLink" href="new.htm"><b id="addSign">+</b> ARP</a></div>