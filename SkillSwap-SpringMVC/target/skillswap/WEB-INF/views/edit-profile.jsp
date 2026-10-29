<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>

<head>

    <title>Edit Profile</title>

    <meta name="viewport"
          content="width=device-width, initial-scale=1">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

</head>


<body>

<nav class="navbar navbar-dark bg-dark">

    <div class="container">

        <b class="navbar-brand">
            SkillSwap
        </b>

        <a class="btn btn-outline-light btn-sm"
           href="${pageContext.request.contextPath}/dashboard">

            Back to Dashboard

        </a>

    </div>

</nav>


<div class="container py-5">

    <div class="row justify-content-center">

        <div class="col-md-6">

            <div class="card p-4 shadow-sm">

                <h3 class="mb-4">
                    Edit Profile
                </h3>


                <form method="post"
                      action="${pageContext.request.contextPath}/profile/update">


                    <!-- NAME -->

                    <div class="mb-3">

                        <label class="form-label">
                            Full Name
                        </label>

                        <input type="text"
                               class="form-control"
                               name="name"
                               value="${user.name}"
                               required>

                    </div>


                    <!-- EMAIL -->

                    <div class="mb-3">

                        <label class="form-label">
                            Email
                        </label>

                        <input type="email"
                               class="form-control"
                               value="${user.email}"
                               readonly>

                    </div>


                    <!-- BIO -->

                    <div class="mb-3">

                        <label class="form-label">
                            Bio
                        </label>

                        <textarea class="form-control"
                                  name="bio"
                                  rows="4"
                                  placeholder="Tell something about yourself">${user.bio}</textarea>

                    </div>


                    <button type="submit"
                            class="btn btn-primary w-100">

                        Save Changes

                    </button>


                </form>

            </div>

        </div>

    </div>

</div>

</body>

</html>