<%@ include file="../commons/include.jsp"%>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>		

<div id="menu" class="unselectable">
	<c:if test="${sessionScope.user.emailId!=null}">
	
		<ul id="nav">
            <li id="firstTab">
                <a id="firstText" href="../login/validate.htm">ALKS 
                	<c:if test="${sessionScope.user.admin}">
                	<span id="arrow">&#9660;</span>
                	</c:if>
                </a>
                <c:if test="${sessionScope.user.admin}">
	                <div id="sub-menu">
	                	<ul id="subMenuContainer">
							<li id="accountsTab" class="topSubMenuElement"><a href="../account/viewAllAccounts.htm" class="active"><span>Accounts</span></a>
								<div id="sub-menu2">
									<ul id="subMenu2Container">
										<li id="add/Update" class="subMenu2Element"><a href="../account/new.htm" class="active"><span>Add/Update</span></a></li> 
		                			</ul>
	                			</div>
							</li> 
							<li id="arpTab" class="middleSubMenuElement"><a href="../arp/viewAllARP.htm" class="active"><span>Role Policies</span></a>
								<div id="sub-menu2">
									<ul id="subMenu2Container">
										<li id="add/Update" class="subMenu2Element"><a href="../arp/new.htm" class="active"><span>Add/Update</span></a></li> 
		                			</ul>
	                			</div>
	                		</li>
							<li id="adgTab" class="bottomSubMenuElement"><a href="../adg/viewAllADG.htm" class="active"><span>AD Groups</span></a>
								<div id="sub-menu2">
									<ul id="subMenu2Container">
										<li id="add/Update" class="subMenu2Element"><a href="../adg/new.htm" class="active"><span>Add/Update</span></a></li> 
		                			</ul>
	                			</div>
	                		</li>
	                	</ul>
	                </div>
	           </c:if>
            </li>

			<c:if test="${sessionScope.user.admin}">

			</c:if>
		</ul>
		
		<div id="status">
			<div id="loginInfo">
				<sec:authentication property="name"/>
				${(sessionScope.user.emailId!=null)? fn:substring(sessionScope.user.emailId, 0, fn:indexOf(sessionScope.user.emailId, "@")):""}
				<c:url var="logoutUrl" value="/j_spring_security_logout"/> 
				${(sessionScope.user.emailId!=null)? "( <a href='../login/logout.htm'>Logout</a> )" : ""}
			</div>
			<div id="adminStatus">
				<c:if test="${sessionScope.user.admin}">
 					ADMIN
				</c:if>
			</div>
		</div>
		
		
	</c:if>
</div>

<script>
/*
function hideTabs(){
	var path = "";
	var href = window.location.href;
	var s = href.split("/");
	i=s.length-1;
	path =s[i];
	var text = path;
	
	if(text == "crossAccounts.htm") {
		alert("crossAccountURl");
	    document.getElementById("accountsTab").style.display = 'none';
	    document.getElementById("arpTab").style.display = 'none';
	    document.getElementById("adgTab").style.display = 'none';
	}
}
hideTabs();
$(window).bind('unload', hideTabs);*/

</script>
