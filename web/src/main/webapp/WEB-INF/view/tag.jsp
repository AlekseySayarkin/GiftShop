<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Tag</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="<c:url value='/css/table.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/body.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/ui.css'/>">
</head>
<body>
<div class="go-back">
    <a href="${pageContext.request.contextPath}/tags">Go back</a>
</div>
<jsp:useBean id="tag" scope="request" type="com.epam.esm.model.Tag"/>
<c:if test="${!empty tag}">
    <div class="container">
        <table aria-describedby="tags">
            <tr>
                <th id>Id</th>
                <th scope="col">Name</th>
                <th scope="col"></th>
            </tr>
            <tr>
                <td>${tag.id}</td>
                <td>${tag.name}</td>
                <form:form method="DELETE" action="${pageContext.request.contextPath}/tags/tag/${tag.id}">
                    <td><input type="submit" value="delete" class="delete"></td>
                </form:form>
            </tr>
        </table>
    </div>
</c:if>
</body>
</html>
