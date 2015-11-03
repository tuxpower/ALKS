<%@ include file="commons/include.jsp"%>

<script type="text/javascript">
function submitForm(event) {
     if(event.keyCode == 13) {
          loginForm.submit();
     }
}
</script>
 
<div class="wrapper">
<!-- 	<h2 class="form-signin-heading">Login Form</h2> -->
<form:form class="form-signin" id="loginForm" action="../login/validate.htm" method="POST" modelAttribute="user">
<h2 class="form-signin-heading" align="center">Please Login</h2>
<form:input class="form-control" path="emailId"  placeholder="Email" />
<br>
<form:password class="form-control" path="password" onKeyUp="submitForm(event)"  placeholder="Password"/><br>
		
<button class="btn btn-lg btn-primary btn-block" type="submit" > Login </button>

<script>
 $("#emailId").attr('required', ''); 
 $("#password").attr('required', ''); 
 </script>

	</form:form>

</div>