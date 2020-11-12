<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Add new tag</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="<c:url value='/css/table.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/body.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/ui.css'/>">
</head>
<body>
<div class="go-back">
    <a href="${pageContext.request.contextPath}/tags">Go back</a>
</div>
<form:form method="PUT" action="${pageContext.request.contextPath}/tags/add" cssClass="add-form">
    <label for="name">
        <input type="text" id ="name" name="name" placeholder="Add tag name" required/>
    </label>
    <input type="submit" value="update">
</form:form>
</body>
</html>
