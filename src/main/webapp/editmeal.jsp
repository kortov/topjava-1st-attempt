<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>EditMeal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<c:choose>
    <c:when test="${not empty meal.id }">
        <h2>Update Meal</h2>
    </c:when>
    <c:otherwise>
        <h2>Add Meal</h2>
    </c:otherwise>
</c:choose>
<form method="POST" action='meals' name="formAddMeal">
    <table>
        <tr>
            <th>New Meal attribute</th>
            <th>Attribute value</th>
        </tr>
        <c:if test="${not empty meal.id}">
            <tr>
                <td>Meal ID</td>
                <td><input readonly="readonly" name="mealId" value="${meal.id}"/></td>
            </tr>
        </c:if>
        <tr>
            <td>DateTime</td>
            <td><input
                    type="datetime-local" name="mealDateTime" required
                    value="<c:out value="${meal.dateTime}" />"/></td>
        </tr>
        <tr>
            <td>Description</td>
            <td><input
                    type="text" name="mealDescription"
                    value="<c:out value="${meal.description}" />"/></td>
        </tr>
        <tr>
            <td>Calories</td>
            <td><input
                    type="number" name="mealCalories" required
                    value="<c:out value="${meal.calories}" />"/></td>
        </tr>
    </table>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>