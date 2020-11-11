<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Tag</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="<c:url value='/css/table.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/body.css'/>">
</head>
<body>
<jsp:useBean id="tag" scope="request" type="com.epam.esm.model.Tag"/>
<c:if test="${!empty tag}">
    <div class="container">
        <table aria-describedby="tags">
            <tr>
                <th id>Id</th>
                <th scope="col">Name</th>
            </tr>
            <tr>
                <td>${tag.id}</td>
                <td>${tag.name}</td>
            </tr>
        </table>
    </div>

    <form>
        <button type="submit" name="delete-button">Delete</button>
    </form>
</c:if>
</body>
</html>
