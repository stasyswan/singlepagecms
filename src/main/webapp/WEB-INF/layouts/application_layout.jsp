<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://kwonnam.pe.kr/jsp/template-inheritance" prefix="layout"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <link rel="stylesheet" type="text/css" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/css/main.css" />

        <script type="text/javascript" src="/webjars/jquery/1.11.1/jquery.min.js"></script>
        <script type="text/javascript" src="/webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>

    <body>
        <nav class="navbar navbar-inverse">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="/">Spring Boot</a>
                </div>
                <div id="navbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="/">Home</a></li>
                        <li><a href="/customers/index">About</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <div id="body">
             <layout:block name="body"></layout:block>
        </div>
    </body>
</html>