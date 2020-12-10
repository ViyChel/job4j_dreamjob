<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.job4j.dream.model.Post" %>
<%@ page import="ru.job4j.dream.store.db.PostPsqlStore" %>
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
    <script>
        function validate() {
            let name = $('#name').val();
            let description = $('#description').val();
            if (name === '' || description === '') {
                alert('Please fill in all fields!');
                return false;
            }
        }
    </script>

    <title>Работа мечты</title>
</head>
<body>
<%
    String id = request.getParameter("id");
    Post post = new Post(0, "", "");
    if (id != null) {
        post = PostPsqlStore.instOf().findById(Integer.parseInt(id));
    }
%>
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
                <a class="nav-link"
                   href='<c:url value="/auth.do"> <c:param name="exit" value="true"/> </c:url> '>
                    <c:out value="${user.name}"/> | Выйти
                </a>
            </li>
        </ul>
    </div>
</div>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новая вакансия.
                <% } else { %>
                Редактирование вакансии.
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/posts.do?id=<%=post.getId()%>" method="post">
                    <div class="form-group">
                        <label>Название</label>
                        <input type="text" class="form-control" id="name" name="name" value="<%=post.getName()%>">
                        <label>Описание</label>
                        <input type="text" class="form-control" id="description" name="description" value="<%=post.getDescription()%>">
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="return validate();">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>