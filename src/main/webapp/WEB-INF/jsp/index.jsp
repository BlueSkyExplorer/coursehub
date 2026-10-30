<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${pageTitle}" /></title>
</head>
<body>
    <main>
        <h1><c:out value="${pageTitle}" /></h1>
        <p><c:out value="${introduction}" /></p>

        <section aria-labelledby="materials-heading">
            <h2 id="materials-heading">Learning materials</h2>
            <p>No learning materials are available yet.</p>
        </section>

        <section aria-labelledby="polls-heading">
            <h2 id="polls-heading">Polls</h2>
            <p>No polls are available yet.</p>
        </section>
    </main>
</body>
</html>