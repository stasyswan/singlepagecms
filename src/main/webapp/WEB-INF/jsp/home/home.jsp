<%@page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="http://kwonnam.pe.kr/jsp/template-inheritance" prefix="layout"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<layout:extends name="/WEB-INF/jsp/layouts/application_layout.jsp">
    <layout:put block="header">
        <li class="active"><a href="/">Home</a></li>
        <li><a href="/customers/index">Customers</a></li>
    </layout:put>

    <layout:put block="body">
        <div class="container">
            <div class="starter-template">
                <h1>Spring Boot Web JSP Example</h1>
                <h2>Message: ${message}</h2>
                <h2>Time: ${timestamp}</h2>
            </div>
        </div>
    </layout:put>
</layout:extends>


