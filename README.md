# CourseHub

CourseHub is a shared learning workspace for accessing learning materials, downloading resources, participating in polls, and discussing content.

## Current features

- A public home page with empty states for learning materials and polls.
- Learner registration with server-side validation and duplicate username and email checks. Public registration always assigns the Learner role.
- CSRF protection for registration submissions and BCrypt password hashing.

Session login and logout are the next account features. See [Requirements](docs/requirements.md) for the planned workflows and access rules.

## Stack

CourseHub uses Java 17, Spring Boot 4.1.2, Spring MVC, JSP/JSTL, Spring Security, Spring Data JPA, and H2. Gradle Wrapper 9.8.2 builds a single executable WAR. WAR packaging lets embedded Tomcat render JSP pages when the application runs with `java -jar`.

## Run locally

Use JDK 17. The first build needs network access to download Gradle and its dependencies.

```sh
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Open the [home page](http://localhost:8080/) or [registration page](http://localhost:8080/register).

The `dev` profile stores accounts in `data/coursehub.mv.db` relative to the application's working directory. The database is ignored by Git and persists across restarts. Use fictional details and an `example.com` address when trying registration.

## Test and build

```sh
./gradlew test
./gradlew build
```

The tests use isolated in-memory H2 databases. They cover account persistence, username and email uniqueness, registration validation, CSRF protection, and the assigned role and stored password hash. GitHub Actions runs the build and tests on Java 17 for pushes and pull requests.

## Run the executable WAR

After `./gradlew build`, run the following commands from the project root on macOS. The temporary directory provides a path without spaces for JSP tag-library scanning.

```sh
war_dir=$(mktemp -d /private/tmp/coursehub-war.XXXXXX)
cp build/libs/coursehub-0.0.1-SNAPSHOT.war "$war_dir/coursehub.war"
(
  cd "$war_dir" &&
  java -jar coursehub.war --spring.profiles.active=dev
)
```

Open the [registration page](http://localhost:8080/register). This instance stores its development database under the temporary directory. Stop `bootRun` first if it is already using port 8080.