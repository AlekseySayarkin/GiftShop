<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Tags</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="<c:url value='/css/table.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/body.css'/>">
</head>
<body>
<jsp:useBean id="tags" scope="request" type="java.util.List"/>
<c:if test="${!empty tags}">
    <div class="container">
        <table aria-describedby="tags">
            <tr>
                <th id>Id</th>
                <th scope="col">Name</th>
            </tr>
            <c:forEach items="${tags}" var="tag">
                <tr>
                    <td><a href="${pageContext.request.contextPath}/tags/tag/${tag.id}">${tag.id}</a> </td>
                    <td>${tag.name}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</c:if>
</body>
</html>
