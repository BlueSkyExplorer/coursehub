# CourseHub

CourseHub is a standalone learning workspace for accessing learning materials, downloading resources, participating in polls, and discussing content.

## Current status

The product scope and access rules are defined. A minimal Spring Boot application is available. The application can be tested, built, and started. Product pages and business features are not implemented yet.

## Planned capabilities

- Learner registration and session login
- Personal account management
- Instructor-managed learning materials and downloadable resources
- Five-option polls with vote changes and results
- Discussion on learning materials and polls
- Personal activity history
- Instructor account management with session and last-instructor safeguards

## Technical direction

Java 17, Spring Boot, Spring MVC, JSP/JSTL, Spring Security, JPA, and Gradle in one deployable application.

Dependencies are introduced when their features are implemented.

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

`./gradlew test`
`./gradlew build`

## Run the packaged application

`java -jar build/libs/coursehub-0.0.1-SNAPSHOT.war`