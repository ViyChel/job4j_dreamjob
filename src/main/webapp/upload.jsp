<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Upload</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
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
                <a class="nav-link" href='<c:url value="/auth.do" /> '> <c:out value="${user.name}"/> | Выйти</a>
            </li>
        </ul>
    </div>
</div>
<div class="container">
    <h2>Upload image</h2>
    <form action="<c:url value='/upload'/>" method="post" enctype="multipart/form-data">
        <div class="checkbox">
            <input type="file" name="file">
        </div>
        <button type="submit" class="btn btn-default">Submit</button>
    </form>
</div>
</body>
</html>