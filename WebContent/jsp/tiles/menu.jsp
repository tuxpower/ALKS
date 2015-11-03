<%@ include file="../commons/include.jsp"%>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>	
<style>
  /* Note: Try to remove the following lines to see the effect of CSS positioning */
  .affix {
      top: 0;
      width: 100%;
  }

  .affix + .container-fluid {
      padding-top: 70px;
  }
  </style>
<div id="menu">
<nav class="navbar navbar-xs navbar-default navbar-static-top" data-spy="affix" data-offset-top="150">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="../login/validate.htm">Air Lift Key Services</a>
    </div>
    <c:if test="${sessionScope.user.emailId!=null}">
    	<ul id =nav class="nav navbar-nav">
    	<li class="dropdown">
			          <a class="dropdown-toggle" data-toggle="dropdown" href="#">View<span class="caret"></span></a>
			         	 <ul class="dropdown-menu">
			           		 <li><a href="../login/validate.htm">List Roles</a></li>
    					    <c:if test="${sessionScope.user.admin}"> 
			           		 <li><a href="../account/viewAllAccounts.htm">Accounts</a></li>
			           		 <li><a href="../arp/viewAllARP.htm">Role Policies</a></li>
			           		 <li><a href="../adg/viewAllADG.htm">AD Groups</a></li>
							</c:if>			          	
			          	</ul>
			        </li>
   					    <c:if test="${sessionScope.user.admin}"> 
			        <li class = "dropdown">
			        	<a class ="dropdown-toggle" data-toggle="dropdown" href="#">Add/Update<span class="caret"></span></a>
			        		<ul class="dropdown-menu">
			        			<li><a href="../account/new.htm">Accounts</a></li>
			        			<li><a href="../arp/new.htm">Role Policies</a></li>
			        			<li><a href="../adg/new.htm">AD Groups</a><li>
			        		</ul>
			        </li>
							</c:if>			          	

               
<%--                	<c:if test="${sessionScope.user.admin}"> --%>
<!--                	<li><a href="../account/new.htm">Add/Update</a></li> -->
<%--                	</c:if> --%>
         </ul>
         <ul class="nav navbar-nav navbar-right">
			<li class="user">
			<a ><span class="glyphicon glyphicon-user"></span> ${(sessionScope.user.emailId!=null)? fn:substring(sessionScope.user.emailId, 0, fn:indexOf(sessionScope.user.emailId, "@")):""}
			</a>
			</li>
			<li><a href="../login/logout.htm"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
	         
         </ul>
    </c:if>
    <div class="navbar-inner">
   </div>
   <c:if test="${sessionScope.user.emailId==null}">
    <div>
      <ul class="nav navbar-nav">
        <li class="active"><a href="../account/viewAllAccounts.htm">Home</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
       
        <li><a href="../login/loginPage.htm"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
        
      </ul>
    </div>
    </c:if>
  </div>
</nav>
</div>