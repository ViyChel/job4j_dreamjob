<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <!-- Required meta tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <script src='https://kit.fontawesome.com/a076d05399.js'></script>
    <title>Работа мечты</title>
</head>
<body>
<div class="container">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/posts.do"/>'>Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/candidates.do"/>'>Кандидаты</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/post/edit.jsp"/>'>Добавить вакансию</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/candidate/edit.jsp"/>'>Создать кандидата</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/login.jsp" /> '> <c:out value="${user.name}"/> | Выйти</a>
            </li>
        </ul>
    </div>
</div>
<div class="container pt-3">

    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                Кандидаты
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Названия</th>
                        <th scope="col">Фото</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="candidate" items="${candidates}">
                        <tr>
                            <td>
                                <a href='<c:url value="/candidate/edit.jsp?id=${candidate.id}"/>'>
                                    <i class="fas fa-edit mr-3"></i>
                                </a>
                                <a href='<c:url value="/candidates.do">
                                    <c:param name="action" value="delete" />
                                    <c:param name="id" value="${candidate.id}" />
                                    </c:url>'>
                                    <i class='far fa-trash-alt mr-3'></i>
                                </a>
                                <c:out value="${candidate.name}"/>
                            </td>
                            <td>
                                <c:if test="${candidate.photo == null}">
                                    <img src='<c:url value="/static/defaultPhoto.jpg"/>' width="150px"
                                         height="100px"/>
                                    <a href="<c:url value='/static/defaultPhoto.jpg'/>">Download</a>
                                </c:if>
                                <c:if test="${candidate.photo != null}">
                                    <img src="<c:url value='/download?photoId=${candidate.photo.id}'/>" width="150px"
                                         height="100px"/>
                                    <a href="<c:url value='/download?photoId=${candidate.photo.id}'/>">Download</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>