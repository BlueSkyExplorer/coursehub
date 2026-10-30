# CourseHub

CourseHub is a standalone learning workspace for accessing learning materials, downloading resources, participating in polls, and discussing content.

## Current status

The application renders a public CourseHub home page with a workspace introduction and empty states for learning materials and polls.
Account, content management, voting, and discussion workflows are not implemented yet.

## Planned capabilities

- Learner registration and session login
- Personal account management
- Instructor-managed learning materials and downloadable resources
- Five-option polls with vote changes and results
- Discussion on learning materials and polls
- Personal activity history
- Instructor account management with session and last-instructor safeguards

## Technical direction

The current foundation uses Java 17, Spring Boot 4.1.2, Spring MVC, and Gradle Wrapper 9.8.2, packaged as an executable WAR.

CourseHub uses a single application because its account, content, and discussion workflows share one deployment and data model. Spring MVC with server-rendered JSP/JSTL pages keeps form handling and presentation in the same application without a separate frontend build. WAR packaging supports JSP rendering with embedded Tomcat and allows the packaged application to run with `java -jar`.

Spring Security and JPA are planned for authentication and persistence. Dependencies are introduced when their features are implemented. The Gradle Wrapper fixes the build-tool version; contributors do not need a separate Gradle installation.

## Scope

CourseHub provides one shared learning workspace. Payments, streaming, live chat, course enrolment, and multi-course management are outside the initial scope.

See [Requirements](docs/requirements.md) for access rules, deletion behavior, and acceptance examples.

## Prerequisites

- JDK 17
- Network access for the first Gradle and dependency download

## Run

`./gradlew bootRun`

The application uses port 8080 by default. The root URL currently returns
404 because the home page is not implemented yet.

## Test and build

```sh
./gradlew test
./gradlew build
```

GitHub Actions runs `./gradlew build --no-daemon`, including tests, on Java 17 for pushes and pull requests. The workflow can also be started manually.

## Run the packaged application

Build the executable WAR:

```sh
./gradlew build
```

For the current build, place the WAR under a path without spaces before
launching it. JSP tag-library scanning fails when the WAR is launched
from the project's current path containing spaces.

On macOS, run the following commands from the project root:

```sh
mkdir -p /private/tmp/coursehub-war-check
cp build/libs/coursehub-0.0.1-SNAPSHOT.war /private/tmp/coursehub-war-check/coursehub.war
cd /private/tmp/coursehub-war-check
java -jar coursehub.war
```

Open http://localhost:8080/.