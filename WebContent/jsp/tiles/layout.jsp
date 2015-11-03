<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" href="../css/bootstrap.min.css">	
<link rel="stylesheet" href="https://bootswatch.com/yeti/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>


<!-- Sweet Alerts -->
<link rel="stylesheet" type="text/css" href="../lib/sweet-alert.css">
<script src="../lib/sweet-alert.min.js"></script>


<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<tiles:insertAttribute name="header" />	
	<title>
		<tiles:insertAttribute name="title" ignore="true" />
	</title>
	<tiles:insertAttribute name="menu" />
</head>


<body class="background">

<div class="container">				
	<div id="content">
	<tiles:insertAttribute name="messages" />
	
	<tiles:insertAttribute name="body" />
	<tiles:insertAttribute name="footer" />
	</div>
</div>

</body> 
</html>

