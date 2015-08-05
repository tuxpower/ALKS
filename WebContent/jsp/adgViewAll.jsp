<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="../css/ui.css"  media="screen">
<script type="text/javascript">
<!--
function deleteConfirm(text){
	alert(text);
}
//-->
</script>


  	
<%@ include file="commons/include.jsp"%>

 <table>
  <thead>
  <tr class='heading'><th>AWS Account</th> <th>Role</th> <th>AD Group</th><th>Last Modified</th> </tr></thead>
  <tbody>
  <c:forEach items="${adgs}" var="adg" varStatus="loopStatus">
  <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
  	<td>${adg.accountNo}</td><td>${adg.role}</td>
  	<td>${adg.adGroup}</td>
  	<!-- td><input class="menu" type="button" value="Inactivate" onclick='deleteConfirm(${arp.id});'/></td-->
  	<td>
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
 
 <div id="addButtonContainer" align="center"><a class="buttonLink" href="new.htm"><b id="addSign">+</b> ADG</a></div>