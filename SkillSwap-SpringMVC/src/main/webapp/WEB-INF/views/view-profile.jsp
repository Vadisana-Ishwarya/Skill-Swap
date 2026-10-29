<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

<head>

    <title>View Profile - SkillSwap</title>

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

            Hi, ${sessionScope.user.name}

            <a
                class="btn btn-outline-light btn-sm ms-2"
                href="${pageContext.request.contextPath}/dashboard">

                Dashboard

            </a>

            <a
                class="btn btn-outline-light btn-sm ms-2"
                href="${pageContext.request.contextPath}/logout">

                Logout

            </a>

        </span>

    </div>

</nav>


<div class="container py-5">

    <div class="row justify-content-center">

        <div class="col-lg-7">


            <!-- PROFILE CARD -->

            <div class="card shadow-sm p-4">

                <div class="text-center mb-4">

                    <div
                        class="rounded-circle bg-primary text-white d-inline-flex align-items-center justify-content-center"
                        style="width:90px;height:90px;font-size:36px;">

                        ${profileUser.name.substring(0,1).toUpperCase()}

                    </div>

                    <h2 class="mt-3 mb-1">
                        ${profileUser.name}
                    </h2>

                    <p class="text-muted mb-0">
                        SkillSwap Member
                    </p>

                </div>


                <!-- ABOUT -->

                <div class="mb-4">

                    <h5>
                        About
                    </h5>

                    <p class="text-muted">

                        <c:choose>

                            <c:when test="${not empty profileUser.bio}">

                                ${profileUser.bio}

                            </c:when>

                            <c:otherwise>

                                No bio available.

                            </c:otherwise>

                        </c:choose>

                    </p>

                </div>


                <!-- EMAIL -->

                <div class="mb-4">

                    <h5>
                        Contact
                    </h5>

                    <p class="mb-0">

                        <strong>Email:</strong>

                        ${profileUser.email}

                    </p>

                </div>


                <!-- SKILLS -->

                <div>

                    <h5>
                        Skills
                    </h5>


                    <c:if test="${empty profileSkills}">

                        <p class="text-muted">

                            No skills added yet.

                        </p>

                    </c:if>


                    <div class="row g-3">

                        <c:forEach
                            var="skill"
                            items="${profileSkills}">

                            <div class="col-md-6">

                                <div class="border rounded p-3">

                                    <h6 class="mb-2">

                                        ${skill.skillName}

                                    </h6>


                                    <c:choose>

                                        <c:when test="${skill.type == 'TEACH'}">

                                            <span class="badge text-bg-success">

                                                Can Teach

                                            </span>

                                        </c:when>


                                        <c:otherwise>

                                            <span class="badge text-bg-primary">

                                                Wants to Learn

                                            </span>

                                        </c:otherwise>

                                    </c:choose>

                                </div>

                            </div>

                        </c:forEach>

                    </div>

                </div>


                <!-- BACK BUTTON -->

                <div class="mt-4">

                    <a
                        href="${pageContext.request.contextPath}/dashboard"
                        class="btn btn-secondary">

                        ← Back to Dashboard

                    </a>

                </div>


            </div>

        </div>

    </div>

</div>


</body>

</html>