<%@ include file="commons/include.jsp"%>

<div class="login-form">
	<br>
	<h1>Login Form</h1>
	<form:form id="loginForm" action="validate.htm" method="POST" modelAttribute="user">

<form:input path="emailId"  placeholder="email" />
<form:password path="password"  placeholder="password"/><br>
		
<a class="buttonLink center" onkeypress="" onclick="loginForm.submit();"> Login </a>

<script>
 $("#emailId").attr('required', ''); 
 $("#password").attr('required', ''); 
 </script>

	</form:form>

</div>
