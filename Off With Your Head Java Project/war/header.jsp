<!doctype html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title>${param.title}</title>
		<% if (request.getParameterValues("stylesheet") != null) { 
		for (String style :  request.getParameterValues("stylesheet")) { %>
		<link rel="stylesheet" type="text/css" href="/CSS/<%=style %>"><% } } %>
	</head>
	<body>