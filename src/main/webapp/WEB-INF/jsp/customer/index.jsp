<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
    <head>
        <!-- Access the bootstrap Css like this,
            Spring boot will handle the resource mapping automcatically -->
        <link rel="stylesheet" type="text/css" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

        <!--
        <spring:url value="/css/main.css" var="springCss" />
        <link href="${springCss}" rel="stylesheet" />
         -->
        <c:url value="/css/main.css" var="jstlCss" />
        <link href="${jstlCss}" rel="stylesheet" />
    </head>

    <body>
        <nav class="navbar navbar-inverse">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">Spring Boot</a>
                </div>
                <div id="navbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li><a href="/">Home</a></li>
                        <li class="active"><a href="/customers/index">About</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container">
            <div class="starter-template">
                <h1>Spring Boot Web JSP Example</h1>
                <h2>Customers: </h2>
                <table class="table table-striped">
                    <tr>
                        <th>Id</th>
                       <th>First Name</th>
                        <th>Last Name</th>
                    </tr>
                    <c:forEach var="customer" items="${customers}">
                        <tr>
                            <td><a href="/customer/${customer.id}">${customer.id}</a></td>
                            <td>${customer.firstName}</td>
                            <td>${customer.lastName}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
        <script type="text/javascript" src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </body>
</html>