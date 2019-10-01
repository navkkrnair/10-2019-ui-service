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
		<h2>Available Flights</h2>


		<table>
			<thead>
				<tr>
					<th>#</th>
					<th>Flight</th>
					<th>From</th>
					<th>To</th>
					<th>Date</th>
					<th>Fare</th>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${flights}" var="flight">
					<tr>
						<td>${flight.id}</td>
						<td>${flight.flightNumber}</td>
						<td>${flight.origin}</td>
						<td>${flight.destination}</td>
						<td>${flight.flightDate}</td>
						<td>${flight.fare.amount}</td>
						<td><a
							href="/ui-service/booking/${flight.flightNumber}/${flight.origin}/${flight.destination}/${flight.flightDate}/${flight.fare.amount}">Book</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>