<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html>

<head>


<title>SkillSwap Dashboard</title>

<meta
    name="viewport"
    content="width=device-width,initial-scale=1">

<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
    rel="stylesheet">

<link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/css/style.css">


</head>

<body>

<nav class="navbar navbar-dark bg-dark">


<div class="container">

    <b class="navbar-brand">
        SkillSwap
    </b>

    <span class="text-white">

        Hi, ${user.name}

        <a
            class="btn btn-outline-light btn-sm ms-2"
            href="${pageContext.request.contextPath}/profile/edit">

            Edit Profile

        </a>

        <a
            class="btn btn-outline-light btn-sm ms-2"
            href="${pageContext.request.contextPath}/logout">

            Logout

        </a>

    </span>

</div>


</nav>

<div class="container py-4">


<!-- MESSAGE -->

<c:if test="${not empty message}">

    <div
        class="alert alert-info alert-dismissible fade show"
        role="alert">

        ${message}

        <button
            type="button"
            class="btn-close"
            data-bs-dismiss="alert">
        </button>

    </div>

</c:if>


<!-- HERO -->

<div class="hero p-4 mb-4">

    <h2>
        Welcome, ${user.name} 👋
    </h2>

    <p>
        ${user.bio}
    </p>

</div>


<div class="row g-4">


    <!-- LEFT SIDE -->

    <div class="col-lg-4">


        <!-- ADD SKILL -->

        <div class="card p-4 shadow-sm">

            <h5>
                Add Skill
            </h5>

            <p class="text-muted">

                Enter a skill you can teach
                or want to learn.

            </p>


            <form
                method="post"
                action="${pageContext.request.contextPath}/skills/add">


                <!-- SKILL NAME -->

                <label class="form-label">

                    Skill Name

                </label>

                <input
                    type="text"
                    class="form-control mb-3"
                    name="skillName"
                    list="skillSuggestions"
                    placeholder="Example: Spring Boot"
                    maxlength="100"
                    required>


                <!-- EXISTING SKILL SUGGESTIONS -->

                <datalist id="skillSuggestions">

                    <c:forEach
                        var="s"
                        items="${skills}">

                        <option value="${s.name}">
                        </option>

                    </c:forEach>

                </datalist>


                <!-- TYPE -->

                <label class="form-label">

                    Skill Type

                </label>

                <select
                    class="form-select mb-3"
                    name="type">

                    <option value="TEACH">

                        I can teach

                    </option>

                    <option value="LEARN">

                        I want to learn

                    </option>

                </select>


                <!-- BUTTON -->

                <button
                    type="submit"
                    class="btn btn-primary w-100">

                    Add Skill

                </button>

            </form>

        </div>


        <!-- MY SKILLS -->

        <div class="card p-4 shadow-sm mt-4">

            <h5>
                My Skills
            </h5>


            <c:if test="${empty mySkills}">

                <p class="text-muted">

                    You have not added any skills yet.

                </p>

            </c:if>


            <c:forEach
                var="s"
                items="${mySkills}">

                <div
                    class="d-flex align-items-center justify-content-between mb-2">

                    <span
                        class="badge ${s.type=='TEACH'?'text-bg-primary':'text-bg-success'} skill">

                        ${s.skillName} · ${s.type}

                    </span>


                    <form
                        method="post"
                        action="${pageContext.request.contextPath}/skills/delete">

                        <input
                            type="hidden"
                            name="skillId"
                            value="${s.skillId}">

                        <input
                            type="hidden"
                            name="type"
                            value="${s.type}">

                        <button
                            type="submit"
                            class="btn btn-sm btn-outline-danger">

                            Delete

                        </button>

                    </form>

                </div>

            </c:forEach>

        </div>

    </div>


    <!-- RIGHT SIDE -->

    <div class="col-lg-8">


        <!-- SEARCH USERS BY SKILL -->

        <div
            class="card p-4 shadow-sm mb-4">

            <h5>
                Find Users by Skill
            </h5>

            <p class="text-muted">

                Search for people who can teach
                a particular skill.

            </p>


            <form
                method="get"
                action="${pageContext.request.contextPath}/dashboard"
                class="row g-2">


                <div class="col-md-8">

                    <select
                        class="form-select"
                        name="searchSkillId"
                        required>

                        <option value="">

                            Select a skill

                        </option>


                        <c:forEach
                            var="s"
                            items="${skills}">

                            <option
                                value="${s.id}"
                                <c:if test="${searchSkillId == s.id}">
                                    selected
                                </c:if>>

                                ${s.name}

                            </option>

                        </c:forEach>

                    </select>

                </div>


                <div class="col-md-4">

                    <button
                        type="submit"
                        class="btn btn-primary w-100">

                        Search Users

                    </button>

                </div>

            </form>

        </div>


        <!-- SEARCH RESULTS -->

        <c:if test="${searchSkillId != null}">

            <div
                class="card p-4 shadow-sm mb-4">


                <div
                    class="d-flex justify-content-between align-items-center mb-3">

                    <div>

                        <h5>
                            Search Results
                        </h5>

                        <p class="text-muted mb-0">

                            Users who can teach
                            the selected skill.

                        </p>

                    </div>


                    <a
                        href="${pageContext.request.contextPath}/dashboard"
                        class="btn btn-sm btn-outline-secondary">

                        Clear Search

                    </a>

                </div>


                <c:if test="${empty searchResults}">

                    <p class="text-muted mb-0">

                        No users found who can
                        teach this skill.

                    </p>

                </c:if>


                <div class="row g-3">


                    <c:forEach
                        var="result"
                        items="${searchResults}">

                        <div class="col-md-6">

                            <div class="match">


                                <h5>
                                    ${result.userName}
                                </h5>


                                <p>
                                    ${result.bio}
                                </p>


                                <p class="mb-2">

                                    <span
                                        class="badge text-bg-success">

                                        Can teach:
                                        ${result.skillName}

                                    </span>

                                </p>


                                <!-- VIEW PROFILE -->

                                <a
                                    href="${pageContext.request.contextPath}/profile/view?userId=${result.userId}"
                                    class="btn btn-outline-primary btn-sm mb-2">

                                    View Profile

                                </a>


                                <c:set
                                    var="searchRequestKey"
                                    value="${result.userId}-${result.skillId}" />


                                <c:choose>


                                    <c:when
                                        test="${activeRequests.contains(searchRequestKey)}">

                                        <span
                                            class="badge text-bg-secondary">

                                            Request already sent

                                        </span>

                                    </c:when>


                                    <c:otherwise>

                                        <form
                                            method="post"
                                            action="${pageContext.request.contextPath}/exchange/send">

                                            <input
                                                type="hidden"
                                                name="receiverId"
                                                value="${result.userId}">

                                            <input
                                                type="hidden"
                                                name="skillId"
                                                value="${result.skillId}">

                                            <button
                                                type="submit"
                                                class="btn btn-primary btn-sm">

                                                Send Exchange Request

                                            </button>

                                        </form>

                                    </c:otherwise>

                                </c:choose>


                            </div>

                        </div>

                    </c:forEach>

                </div>

            </div>

        </c:if>


        <!-- SUGGESTED MATCHES -->

        <div
            class="card p-4 shadow-sm">

            <h5>
                Suggested Matches
            </h5>

            <p class="text-muted">

                People who teach what you
                want to learn.

            </p>


            <c:if test="${empty matches}">

                <p class="text-muted">

                    Add a LEARN skill and another
                    user who can TEACH it.

                </p>

            </c:if>


            <div class="row g-3">


                <c:forEach
                    var="match"
                    items="${matches}">

                    <div class="col-md-6">

                        <div class="match">


                            <h5>
                                ${match.userName}
                            </h5>


                            <p>
                                ${match.bio}
                            </p>


                            <p class="mb-2">

                                <span
                                    class="badge text-bg-success">

                                    Can teach:
                                    ${match.skillName}

                                </span>

                            </p>


                            <!-- VIEW PROFILE -->

                            <a
                                href="${pageContext.request.contextPath}/profile/view?userId=${match.userId}"
                                class="btn btn-outline-primary btn-sm mb-2">

                                View Profile

                            </a>


                            <c:set
                                var="requestKey"
                                value="${match.userId}-${match.skillId}" />


                            <c:choose>


                                <c:when
                                    test="${activeRequests.contains(requestKey)}">

                                    <span
                                        class="badge text-bg-secondary">

                                        Request already sent

                                    </span>

                                </c:when>


                                <c:otherwise>

                                    <form
                                        method="post"
                                        action="${pageContext.request.contextPath}/exchange/send">

                                        <input
                                            type="hidden"
                                            name="receiverId"
                                            value="${match.userId}">

                                        <input
                                            type="hidden"
                                            name="skillId"
                                            value="${match.skillId}">

                                        <button
                                            type="submit"
                                            class="btn btn-primary btn-sm">

                                            Send Exchange Request

                                        </button>

                                    </form>

                                </c:otherwise>

                            </c:choose>


                        </div>

                    </div>

                </c:forEach>

            </div>

        </div>


        <!-- INCOMING REQUESTS -->

        <div
            class="card p-4 shadow-sm mt-4">

            <h5>
                Incoming Requests
            </h5>


            <c:if test="${empty incoming}">

                <p class="text-muted">

                    No incoming requests.

                </p>

            </c:if>


            <c:forEach
                var="r"
                items="${incoming}">

                <div
                    class="border rounded p-3 mb-2">

                    <b>
                        ${r.senderName}
                    </b>

                    wants ${r.skillName}


                    <span
                        class="badge text-bg-secondary">

                        ${r.status}

                    </span>


                    <c:if test="${r.status=='PENDING'}">

                        <form
                            class="d-inline"
                            method="post"
                            action="${pageContext.request.contextPath}/exchange/status">

                            <input
                                type="hidden"
                                name="requestId"
                                value="${r.id}">

                            <input
                                type="hidden"
                                name="status"
                                value="ACCEPTED">

                            <button
                                type="submit"
                                class="btn btn-sm btn-success ms-2">

                                Accept

                            </button>

                        </form>


                        <form
                            class="d-inline"
                            method="post"
                            action="${pageContext.request.contextPath}/exchange/status">

                            <input
                                type="hidden"
                                name="requestId"
                                value="${r.id}">

                            <input
                                type="hidden"
                                name="status"
                                value="REJECTED">

                            <button
                                type="submit"
                                class="btn btn-sm btn-outline-danger">

                                Reject

                            </button>

                        </form>

                    </c:if>

                </div>

            </c:forEach>

        </div>


        <!-- SENT REQUESTS -->

        <div
            class="card p-4 shadow-sm mt-4">

            <h5>
                Sent Requests
            </h5>


            <c:if test="${empty outgoing}">

                <p class="text-muted">

                    No sent requests.

                </p>

            </c:if>


            <c:forEach
                var="r"
                items="${outgoing}">

                <div
                    class="border rounded p-3 mb-2">

                    To

                    <b>
                        ${r.receiverName}
                    </b>

                    — ${r.skillName}


                    <span
                        class="badge text-bg-secondary">

                        ${r.status}

                    </span>

                </div>

            </c:forEach>

        </div>


        <!-- ACTIVE EXCHANGES -->

        <div
            class="card p-4 shadow-sm mt-4">

            <h5>
                🤝 Active Exchanges
            </h5>

            <p class="text-muted">

                Your accepted skill exchanges.
                Contact your partner to arrange
                a learning session.

            </p>


            <c:if test="${empty activeExchanges}">

                <p class="text-muted mb-0">

                    No active exchanges yet.
                    Accept an exchange request
                    to start learning.

                </p>

            </c:if>


            <c:forEach
                var="r"
                items="${activeExchanges}">

                <div
                    class="border rounded p-3 mb-3">


                    <h6 class="mb-1">

                        ${r.otherName}

                    </h6>


                    <p class="mb-2">

                        <span
                            class="badge text-bg-success">

                            ${r.skillName}

                        </span>

                    </p>


                    <p class="mb-1">

                        <strong>
                            Email:
                        </strong>

                        ${r.otherEmail}

                    </p>


                    <p class="mb-1">

                        <strong>
                            Bio:
                        </strong>

                        ${r.otherBio}

                    </p>


                    <small class="text-muted">

                        Exchange accepted on
                        ${r.createdAt}

                    </small>


                </div>

            </c:forEach>

        </div>


    </div>

</div>


</div>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>

</body>

</html>
