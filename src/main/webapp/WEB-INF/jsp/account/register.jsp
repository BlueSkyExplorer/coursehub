<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="currentPage" value="register" />
<c:url var="registerUrl" value="/register" />
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
    <%@ include file="../fragments/header.jspf" %>

    <main id="main-content" class="container site-main" tabindex="-1">
        <section class="panel account-panel" aria-labelledby="page-heading">
            <h1 id="page-heading">Create an account</h1>
            <p>Register as a learner to use CourseHub.</p>

            <form:form method="post" action="${registerUrl}"
                       modelAttribute="registrationForm" cssClass="account-form">
                <div>
                    <form:label path="username">Username</form:label>
                    <form:input path="username" htmlEscape="true"
                                autocomplete="username" maxlength="50"
                                aria-describedby="username-hint" />
                    <small id="username-hint">Use 3 to 50 letters, digits, or underscores.</small>
                    <form:errors path="username" htmlEscape="true" cssClass="field-error" />
                </div>

                <div>
                    <form:label path="fullName">Full name</form:label>
                    <form:input path="fullName" htmlEscape="true"
                                autocomplete="name" maxlength="100" />
                    <form:errors path="fullName" htmlEscape="true" cssClass="field-error" />
                </div>

                <div>
                    <form:label path="email">Email address</form:label>
                    <form:input path="email" type="email" htmlEscape="true"
                                autocomplete="email" maxlength="254" />
                    <form:errors path="email" htmlEscape="true" cssClass="field-error" />
                </div>

                <div>
                    <form:label path="password">Password</form:label>
                    <form:password path="password" showPassword="false"
                                   autocomplete="new-password"
                                   aria-describedby="password-hint" />
                    <small id="password-hint">Use at least 12 characters and at most 72 bytes.</small>
                    <form:errors path="password" htmlEscape="true" cssClass="field-error" />
                </div>

                <button type="submit">Create account</button>
            </form:form>
        </section>
    </main>

    <%@ include file="../fragments/footer.jspf" %>
</body>
</html>