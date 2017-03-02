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
                    <h2>Customer:</h2>
                    <div>
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Customer Id:</label>
                                <div class="col-sm-10">
                                    <p class="form-control-static">${customer.id}</p></div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">First Name:</label>
                                <div class="col-sm-10">
                                    <p class="form-control-static">${customer.firstName}</p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Last Name:</label>
                                <div class="col-sm-10">
                                    <p class="form-control-static">${customer.lastName}</p>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </layout:put>
</layout:extends>
