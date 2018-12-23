<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="topjava" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <%--@elvariable id="userTo" type="ru.javawebinar.topjava.to.UserTo"--%>
        <div class="row">
            <div class="col-5 offset-3">
                <h3>${userTo.name} <spring:message code="${register ? 'app.register' : 'app.profile'}"/></h3>
                <form:form class="form-group" modelAttribute="userTo" method="post"
                           action="${register ? 'register' : 'profile'}"
                           charset="utf-8" accept-charset="UTF-8">

                    <topjava:inputField labelCode="user.name" name="name"/>
                    <topjava:inputField labelCode="user.email" name="email"/>
                    <topjava:inputField labelCode="user.password" name="password" inputType="password"/>
                    <topjava:inputField labelCode="user.caloriesPerDay" name="caloriesPerDay" inputType="number"/>

                    <spring:hasBindErrors name="userTo">
                        <c:if test="${errors.hasGlobalErrors()}">
                            <%-- field errors are handled by fields, so we need only global errors here --%>
                            <c:forEach var="error" items="${errors.allErrors}">
                                <%-- ${error.getClass()} = org.springframework.validation.ObjectError--%>
                                <%--<b>${error}</b>--%>
                                <c:forEach var="code" items="${error.codes}">
                                    ${code}: <b><spring:message code="${code}" text=""/></b>
                                    <br/>
                                </c:forEach>
                            </c:forEach>
                        </c:if>
                    </spring:hasBindErrors>

                    <div class="text-right">
                        <a class="btn btn-secondary" href="#" onclick="window.history.back()">
                            <span class="fa fa-close"></span>
                            <spring:message code="common.cancel"/>
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <span class="fa fa-check"></span>
                            <spring:message code="common.save"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>