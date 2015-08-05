<%@ include file="/jsp/commons/include.jsp"%>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js"></script>
    

<c:url var="findRolesURL" value="/getRoles.htm" />


<script type="text/javascript">
$(document).ready(function() { 
	$('#usStates').change(
		function() {
			$.getJSON('${findRolesURL}', {
				stateName : $(this).val(),
				ajax : 'true'
			}, function(data) {
				var html = '<option value="">Role</option>';
				var len = data.length;
				for ( var i = 0; i < len; i++) {
					html += '<option value="' + data[i].name + '">'
							+ data[i].name + '</option>';
				}
				html += '</option>';
 
				$('#roles').html(html);
			});
		});
});
</script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#city").change(onSelectChange);
	});
 
	function onSelectChange() {
		var selected = $("#city option:selected");		
		var output = "";
		if(selected.val() != 0){
			output = "You selected City " + selected.text();
		}
		$("#output").html(output);
	}
</script>

<c:url value="/add.htm" var="signupUrl" />

<form:form action="${signupUrl}" method="post"	modelAttribute="arp">
 
	<fieldset>
		<form:select id="usStates" path="usState">
		<form:option value="123423">3434</form:option>
		<form:option value="232323">3434</form:option>
		<form:option value="323243">434</form:option>
		<form:option value="54545">43343</form:option>
		
		</form:select>
 
		<form:select id="city" path="city">
			<form:option value="">City</form:option>
		</form:select>
	</fieldset>
	
	<div id="output"></div>
	<p>
		<button type="submit">Sign Up</button>
	</p>
</form:form>