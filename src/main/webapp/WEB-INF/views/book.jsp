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
	<spring:url value="/ui-service/confirm" var="confirm" />

	<nav>
		<ul>
			<li>BrownField Airline</li>
			<li><a href="${home}">Search</a></li>
			<li><a href="${searchBooking}">CheckIn</a></li>
		</ul>
	</nav>
	<div align="center" class="form-style-8">
		<h2>Selected Flight</h2>
		<h3>
			<table>
				<tbody>
					<tr>
						<td><span>${selectedFlight.flightNumber}</span></td>
						<td><span>${selectedFlight.origin}</span></td>
						<td><span>${selectedFlight.destination}</span></td>
						<td><span>${selectedFlight.flightDate}</span></td>
						<td><span>${selectedFlight.fare.amount}</span></td>
					</tr>
				</tbody>
			</table>
		</h3>
		<form:form action="${confirm}" method="post"
			modelAttribute="passenger">
			<table>
				<tr>
					<td>First Name</td>
					<td><form:input path="firstName" /></td>
				</tr>
				<tr>
					<td>Last Name</td>
					<td><form:input path="lastName" /></td>
				</tr>
				<tr>
					<td>Gender</td>
					<td><form:select path="gender">
							<form:option value="Male">Male</form:option>
							<form:option value="Female">Female</form:option>
						</form:select></td>
				</tr>
				<tr>
					<td><button type="submit">Confirm</button></td>
				</tr>
			</table>
		</form:form>
	</div>
</body>
</html>