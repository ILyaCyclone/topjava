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

<section classs="meals">
    <table class="meals__table">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th>Day Calories</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${mealsWithExceed}" var="mealWithExceed">
            <tr class="${mealWithExceed.exceed ? "exceed" : "nonExceed"}" data-id="${mealWithExceed.id}" >
                <td class="meals__table-datetime" datetime="${mealWithExceed.dateTime}"><javatime:format value="${mealWithExceed.dateTime}" style="MS" /></td>
                <td class="meals__table-description">${mealWithExceed.description}</td>
                <td class="meals__table-calories">${mealWithExceed.calories}</td>
                <td>${mealWithExceed.exceed ? "exceeded" : "normal"}</td>
                <td>
                    <button class="meals__table-edit">Edit</button>
                    <button class="meals__table-delete">Delete</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div><button class="meals__create">Add meal</button> </div>

    <div class="meals__form">
        <h3 class="meals__form-label meals__form-label_create">Add new meal</h3>
        <h3 class="meals__form-label meals__form-label_edit">Edit meal</h3>

        <form method="POST" name="save" action="meals">
            <input type="hidden" name="action" value="save"/>
            <input type="hidden" name="id" value="id"/>

            <label>Date <input type="datetime-local" name="datetime"/></label>
            <label>Description <input type="text" name="description"/></label>
            <label>Calories <input type="number" min="0" name="calories"/></label>

            <div class="meals__form-actions">
                <input type="submit" value="Submit">
                <a class="meals__form-cancel" href="javascript:void(0)">Cancel</a>
            </div>
        </form>
    </div>

    <form method="POST" name="delete" action="meals">
        <input type="hidden" name="action" value="delete"/>
        <input type="hidden" name="id"/>
    </form>

</section>

<script src="assets/js/meals.js"></script>
</body>
</html>