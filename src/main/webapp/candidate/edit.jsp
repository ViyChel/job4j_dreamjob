<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.db.CandidatePsqlStore" %>
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
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>

    <script>
        function validate() {
            let fio = $('#fio').val();
            let city = $('#city').val();
            let photo = $('#photo').val();
            if (fio === '' || city === '' || photo === '') {
                alert('Please fill in all fields!');
                return false;
            }
        }
    </script>

    <script>
        $(document).ready(function showCities() {
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080/dreamjob/city',
                dataType: 'json'
            }).done(function (data) {
                let cityList = $('#city');
                $.each(data, function (key, value) {
                    cityList.append("<option value=" + key + ">" + value + "</option>");
                });
            }).fail(function (err) {
                alert(err);
            });
        })
    </script>

    <title>Работа мечты</title>
</head>
<body>
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "");
    if (id != null) {
        candidate = CandidatePsqlStore.instOf().findById(Integer.parseInt(id));
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
                Новый кандидат.
                <% } else { %>
                Редактирование кандидата.
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%> "
                      method="post" enctype="multipart/form-data" accept-charset="UTF-8">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" id="fio" name="name" value="<%=candidate.getName()%>">
                    </div>
                    <div class="form-group">
                        <label>Город</label>
                        <select name="city" id="city" class="browser-default custom-select" required> </select>
                    </div>
                    <div class="form-group">
                        <span>Фотография</span><br>
                        <input type="file" id="photo" name="file-name" accept="image/jpeg,image/png">
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="return validate();">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>