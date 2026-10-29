# SkillSwap
Java + Spring MVC (NO Spring Boot) + JDBC + Oracle SQL + JSP + HTML + CSS + JavaScript + Bootstrap.

Use JDK 17, Maven and Tomcat 9.
1. Run database/skillswap.sql in Oracle.
2. If your Oracle URL/user/password differs, edit DBConnection.java.
3. Import as Maven project in Eclipse.
4. Add Apache Tomcat 9 server.
5. Run on server.
6. Open http://localhost:8080/skillswap/
7. Register two users. User A adds a skill as LEARN; user B adds the same skill as TEACH; login as A and send a request.

This learning version uses plain-text passwords intentionally to keep the Spring/JDBC flow easy to understand. Production software should use password hashing and Spring Security.