<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>www.flybrownfield.com</title>
<link href="/ui-service/css/main.css"
	rel="stylesheet" media="screen" />
</head>
<body>
	<spring:url value="/ui-service" var="home" />
	<spring:url value="/ui-service/search-booking" var="searchBooking" />
	<spring:url value="/ui-service/search" var="searchQuery" />
	<nav>
		<ul>
			<li>BrownField Airline</li>
			<li><a href="${home}">Search</a></li>
			<li><a href="${searchBooking}">CheckIn</a></li>
		</ul>
	</nav>
	<div align="center" class="form-style-8">
		<h2>Flight Search</h2>
		<form:form action="${searchQuery}" modelAttribute="query" id="searchQueryForm"
			method="post">
			<table>
				<tr>
					<td>traveling from</td>
					<td><form:input path="origin" /></td>
				</tr>
				<tr>
					<td>going to</td>
					<td><form:input path="destination" /></td>
				</tr>
				<tr>
					<td>planning on</td>
					<td><form:input path="flightDate" /></td>
				</tr>
				<tr>
					<td></td>
					<td><button type="submit">Submit</button></td>
				</tr>
			</table>
		</form:form>
	</div>
</body>
</html>