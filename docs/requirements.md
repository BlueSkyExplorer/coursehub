# CourseHub requirements

## Product purpose

CourseHub is a standalone learning workspace where learners and instructors can access learning materials, download resources, participate in polls, discuss content, and manage their accounts.

The application provides one shared learning workspace. It does not include a course catalogue or enrolment model.

## Naming

- Product: CourseHub
- Project identifier: coursehub
- Java package and Gradle group: dev.coursehub
- Application entry point: CourseHubApplication
- Roles: LEARNER and INSTRUCTOR
- Product labels: Learning materials, Resources, Polls, Discussion, My activity, and Account management

The home page introduces CourseHub as a product and one shared learning workspace. It does not represent a named academic module or a course catalogue.

Use `LearningMaterial` for the learning-material entity, `LearningMaterialController` for its web controller, and `/materials` as the route root when that feature is implemented. Use `/materials/{id}` for a material detail page. Keep the visible label "Learning materials" consistent across navigation, forms, and documentation; do not introduce a parallel "Lecture" feature name.

## Home page acceptance

- `GET /` introduces CourseHub and explains the purpose of the workspace.
- The initial page shows truthful empty states for content that is not available yet.
- Do not display invented usage statistics, testimonials, or controls for features that are not implemented.
- The page renders correctly both through `./gradlew bootRun` and through the executable WAR launched with `java -jar`.
- Verify actual HTML rendering in a browser. A controller test that only checks the view name is not sufficient.
- Keep shared layout and styling work in the subsequent layout step, rather than implementing future account or content features with the first page.

## Access rules

| Action | Visitor | Learner | Instructor |
| --- | --- | --- | --- |
| View the home page and public introduction | Allowed | Allowed | Allowed |
| Register or open the login page | Allowed | Redirect to home | Redirect to home |
| Read learning materials and poll details or results | Login required | Allowed | Allowed |
| Download resources | Login required | Allowed | Allowed |
| Vote, change a vote, and add comments | Login required | Allowed | Allowed |
| View and update personal details, password, and activity | Login required | Own account only | Own account only |
| Create, edit, and delete learning materials and resources | Login required | Forbidden | Allowed |
| Create and delete polls | Login required | Forbidden | Allowed |
| Delete comments | Login required | Forbidden | Allowed |
| List and manage accounts | Login required | Forbidden | Allowed with account safeguards |

Public registration always creates a learner account. Registration input cannot assign roles, account IDs, or ownership.

Instructor accounts are introduced through development fixtures or authorized instructor account management. There is no separate administrator role.

## Learning materials and resources

Instructors can create, edit, and delete learning materials with a title and description.

Authenticated users can read learning materials and download their resources.

Uploaded files use generated storage names under one configured storage root. Original filenames are display metadata and cannot determine arbitrary filesystem paths.

Initial upload limits are 20 MB per file and 50 MB per request. The allowed file types and precise boundary handling must be specified before implementing uploads in step 14.

## Polls

Each poll has one question and exactly five required options.

Each authenticated user can select one option per poll and can change that selection.

There is at most one vote record per user and poll, enforced by a database constraint.

Results are calculated from vote records. Separate mutable option counters are not used.

Submitting the same selection again does not increase the total vote count. Changing a selection moves the vote between options without changing the total.

Invalid selections are rejected without changing the existing vote.

Instructors can create and delete polls. Poll editing is outside the initial scope.

## Discussion and activity

Authenticated users can comment on learning materials and polls.

Each comment belongs to exactly one learning material or one poll.

Comment content must contain non-whitespace text and must not exceed 1,000 characters.

The server assigns the author and creation time. User content is displayed as escaped text.

Instructors can delete comments. Learners cannot edit or delete comments in the initial scope.

Users can view only their own voting and comment history.

## Account safeguards

Users can update their own name, email address, and phone number.

Password changes require the current password and matching new-password confirmation.

Instructors can update permitted account details and roles. They cannot demote their own account or remove the final instructor role.

Instructor password resets produce a random temporary password displayed once. The affected user must change it before accessing other features.

Role changes, password resets, and account deletion invalidate the affected user's existing sessions.

Instructors cannot delete their own account through account management.

Self-deletion uses a separate identity-confirmation flow and cannot delete the final instructor.

## Deletion rules

Deleting a learning material removes its resource records, stored files, and comments.

Deleting a poll removes its votes and comments.

Deleting an account removes its votes and comments. Poll results then reflect the remaining vote records.

Deleting an instructor account does not delete the shared learning materials or polls.

Filesystem failures require an explicit cleanup or retry strategy. A database transaction alone does not roll back filesystem changes.

## Engineering constraints

Use Java 17, Spring Boot, Spring MVC, JSP/JSTL, Spring Security, JPA, and Gradle in one deployable application.

Controllers handle HTTP concerns. Services enforce business rules and transactions. Repositories handle persistence. JSP views render presentation.

Forms use purpose-specific DTOs.

Authorization is enforced on the server. State-changing forms use CSRF protection, and destructive actions use POST.

Passwords are hashed. Passwords, password hashes, temporary passwords, and session secrets are not logged.

Development fixtures and the H2 console are limited to development configuration. Tests use isolated data and temporary storage.

## Out of scope

- Payments and subscriptions
- Video streaming
- Live chat
- Course catalogue, enrolment, and multi-course management
- A separate JavaScript frontend
- Microservices
- A separate administrator role
- Poll editing
- Comment editing and learner comment deletion

## Acceptance examples

- A visitor opening a learning material is directed to login.
- A learner submitting an instructor-only request is denied.
- Registration cannot create an instructor, even when extra role fields are submitted.
- Changing a vote from option 1 to option 2 keeps the total unchanged.
- Concurrent duplicate vote submissions cannot create two records for the same user and poll.
- Deleting a poll leaves no related votes or comments.
- Deleting an account removes its activity and updates poll results.
- A comment containing HTML is displayed as text.
- The final instructor cannot be demoted or deleted.
- Tests do not access reference-project databases or uploads.
