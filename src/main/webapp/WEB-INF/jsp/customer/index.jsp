<%@page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="http://kwonnam.pe.kr/jsp/template-inheritance" prefix="layout"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<layout:extends name="/WEB-INF/jsp/layouts/application_layout.jsp">
    <layout:put block="header">
        <li><a href="/">Home</a></li>
        <li class="active"><a href="/customers/index">Customers</a></li>
    </layout:put>
    <layout:put block="body">
        <div class="container">
            <div class="starter-template">
                <h1>Spring Boot Web JSP Example</h1>
                    <h2>Customers:</h2>
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
        </div>
    </layout:put>
</layout:extends>
