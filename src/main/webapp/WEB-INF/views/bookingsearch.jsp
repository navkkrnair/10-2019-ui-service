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
	<spring:url value="/ui-service/search-booking-get" var="searchBookingGet" />

	<nav>
		<ul>
			<li>BrownField Airline</li>
			<li><a href="${home}">Search</a></li>
			<li><a href="${searchBooking}">CheckIn</a></li>
		</ul>
	</nav>
	<div align="center" class="form-style-8">
		<h2>Booking Search</h2>
		<form action="${searchBookingGet}" method="post">
			<table>
				<tr>
					<td>Booking Reference</td>
					<td><input type="text" name="bookingId" required="required"/></td>
				</tr>
				<tr>
					<td></td>
					<td><button type="submit">Search</button></td>
				</tr>
			</table>
		</form>
		<c:if test="${not empty sessionScope.searchBookingFlight}">
			<c:set var="selectedFlight"
				value="${sessionScope.searchBookingFlight}" />
			<c:set var="passenger" value="${sessionScope.passenger}" />
			<h3>
				<table>
					<tr>
						<td>${selectedFlight.flightNumber}</td>
						<td>${selectedFlight.origin}</td>
						<td>${selectedFlight.destination}</td>
						<td>${selectedFlight.flightDate}</td>
						<td>${passenger.firstName}</td>
						<td>${passenger.lastName}</td>
						<td><a
							href="/ui-service/checkin/${selectedFlight.flightNumber}/${selectedFlight.origin}/${selectedFlight.destination}/${selectedFlight.flightDate}/${selectedFlight.fare.amount}/${passenger.firstName}/${passenger.lastName}/${passenger.gender}/${bookingid}">CheckIn</a>
						</td>
					</tr>
				</table>
			</h3>
		</c:if>

	</div>
</body>
</html>