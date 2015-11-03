<%@ include file="../commons/include.jsp"%>

<div id=messages>
<%-- 	<c:if test="${sessionScope.user.emailId==null}"> --%>
<!-- 		<div id=greatingMessage class="alert-info"> -->
<!-- 			<span>Guest, please use your network credentials to login -->
<!-- 		</div> -->
<%-- 	</c:if> --%>
	
	<c:if test="${!empty error || !empty warning || !empty success || !empty notice}">
		<div id=errorMessage class="alert alert-warning" align="center">
			<c:choose>
		    	<c:when test="${!empty error}">
		        	<span id="error" >${error} </span>
				</c:when>
		        <c:when test="${!empty warning}">
		            <span id="warning"> ${warning} </span>
		        </c:when> 
		        <c:when test="${!empty success}">
		            <span id="success"> ${success} </span>
		        </c:when>
		        <c:when test="${!empty notice}">
		            <span id="notice"> ${notice} </span>
		        </c:when>
			</c:choose>
		</div>
	</c:if>
</div>
 
<sec:authorize access="authenticated" >
</sec:authorize>
