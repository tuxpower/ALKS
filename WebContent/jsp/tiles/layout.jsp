<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="robots" content="noindex,follow" />
	
	<link rel="stylesheet" href="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="../lib/sweet-alert.css">
	<link rel="stylesheet" href="../css/ui.css"  media="screen">
		
	<script src="../lib/sweet-alert.min.js"></script>
	<script src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
	<script src="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
	
	<title>
		<tiles:insertAttribute name="title" ignore="true" />
	</title>
</head>

<body class="background">
	<table class="noBorder background" cellspacing="10">
		<tr>
			<td>
				<tiles:insertAttribute name="header" />
			</td>
		</tr>
		<tr>
			<td>
				<tiles:insertAttribute name="menu" />
			</td>
		</tr>
		<tr>
			<td>
				<tiles:insertAttribute name="messages" />
			</td>
		</tr>
		<tr>
			<td>
				<div id="content">
					<tiles:insertAttribute name="body" />
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<tiles:insertAttribute name="footer" />
			</td>
		</tr>
	</table>
</body>
</html>