<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>www.flybrownfield.com</title>
<link href="/ui-service/css/main.css" rel="stylesheet" media="screen" />
</head>
<body>
	<spring:url value="/ui-service/" var="home" />
	<spring:url value="/ui-service/search-booking" var="searchBooking" />

	<nav>
		<ul>
			<li>BrownField Airline</li>
			<li><a href="${home}">Search</a></li>
			<li><a href="${searchBooking}">CheckIn</a></li>
		</ul>
	</nav>
	<div align="center" class="form-style-8">
		<h2>Booking Confirmation</h2>
		<div align="center" style="color: green">
			<h3>${message}</h3>
		</div>
	</div>
</body>
</html>