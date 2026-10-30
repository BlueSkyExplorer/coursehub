<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="fragments/head.jspf" %>
</head>
<body>
    <%@ include file="fragments/header.jspf" %>

    <main id="main-content" class="container site-main" tabindex="-1">
        <section class="intro" aria-labelledby="page-heading">
            <h1 id="page-heading"><c:out value="${pageTitle}" /></h1>
            <p><c:out value="${introduction}" /></p>
        </section>

        <div class="content-grid">
            <section class="panel" aria-labelledby="materials-heading">
                <h2 id="materials-heading">Learning materials</h2>
                <p class="empty-state">
                    No learning materials are available yet.
                </p>
            </section>

            <section class="panel" aria-labelledby="polls-heading">
                <h2 id="polls-heading">Polls</h2>
                <p class="empty-state">
                    No polls are available yet.
                </p>
            </section>
        </div>
    </main>

    <%@ include file="fragments/footer.jspf" %>
</body>
</html>