<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="assets/css/meals.css">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<h3>Normal calories per day: ${caloriesPerDay}</h3>

<table class="meals">
    <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th>Day Calories</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${mealsWithExceed}" var="mealWithExceed">
            <tr class="${mealWithExceed.exceed ? "exceed" : "nonExceed"}" >
                <td><javatime:format value="${mealWithExceed.dateTime}" style="MS" /></td>
                <td>${mealWithExceed.description}</td>
                <td>${mealWithExceed.calories}</td>
                <td>${mealWithExceed.exceed ? "exceeded" : "normal"}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>


</body>
</html>