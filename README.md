# WGU-C196
Summary of features implemented since this project was recently completed:

   - The database uses the Room Database structure.  This is a much more user friendly option.

   - Swipe to delete for all RecyclerView lists of Terms, Courses, Assessments, Mentors, and Notes.  

   - Swipe to delete for Terms does a check to ensure it cannot be deleted if it has associated courses.

   - Alerts/notifications are able to be set for the Courses and Assessments as required.  Terms were left out as this was         not a requirement at the time of submission.  Its easy enough to add if you like, just follow the code for the other two.

   - The AddMentor and AddAssessment buttons will not be selectable within the CourseEditorActivity if you are doing an ADD for Course.  Also, you will have to fill all fields before those buttons are activated. This functionality extends to other Add buttons in other activities.

   - Spinners are implemented where selections must be limited to a restricted set of inputs.  For example, Add/Edit Course status has the following choices {Plan To Take, In Progress, Completed, Dropped}.  If you are editing an existing Course, the spinners will initially point to the saved value for that Course. Also, you will note that when you change the Course 'Status' spinner selection, the textview changes accordingly to give visual confirmation of your selection change.

   - There is a second Course spinner labeled Term that associates the current course being added/edited to its associated Term.  There is one limitation in this area that has not been overcome as of yet.  Example, if you are editing a Course associated with "Term 1", the Term textView field will show Term 1 as expected. However, the spinner does not point to the stored value as it does for the Status spinner.  The user will need to pay special attention to the spinner selection so the Course is saved to the correct term.  Also, when selecting a Term from the spinner, the Term textview will not update with the change.  For example, you are adding a Course, the Term textView will show the hint text 'Course Term Title'.  It will save the Term you have selected when saving the Course.  

   - The previously mentioned spinner limitation applies to other activities such as Mentor and Assessment.  Both of those are associated with a Course, so instead of seeing the Term spinner, you will see a Course spinner. 
 
 

WGU-C196

Introduction

As a competent mobile application developer, your understanding of mobile application structure and design will help you to develop applications to meet customer requirements. The following project to develop a student scheduler/student progress tracking application, will help you to apply these skills in a familiar, real-world scenario. This task will allow you to demonstrate your ability to apply the skills learned in the course.

You will develop a multiple-screen mobile application for WGU students to track their terms, courses associated with each term, and assessment(s) associated with each course. The application will allow students to enter, edit, and delete term, course, and assessment data. It should provide summary and detailed views of courses for each term and provide alerts for upcoming performance and objective assessments. This application will use a SQLite database.

Input

Each of the following needs to be input into the application:

• terms, including the following:

     the term title (e.g., Term 1, Term 2)

     the start date

     the end date

• courses, including the following:

     the course title

     the start date

     the anticipated end date

     the status (e.g., in progress, completed, dropped, plan to take)

     the course mentor name(s), phone number(s), and email address(es)

     objective and performance assessments associated with each course

• the ability to add optional notes for each course

• the ability to set alerts or notifications for the start and end date of each course

• the ability to set alerts for goal dates for each objective and performance assessment

Output

Each of the following needs to be displayed by the application on a separate screen:

• the navigation panel

• a list of terms

• a detailed view of each term, including the following:

     the title (e.g., Term 1, Term 2)

     the start date

     the end date

• a list of courses for each term

• a detailed view of each course, including the following:

     the course title

     the start date

     the anticipated end date

     assessments

• a list of performance and objective assessments for a selected course

• a detailed view of each objective and performance assessment, including the following:

     the due date for a selected course

     notes for the selected course

     sharing features (e.g., email, SMS)

Requirements

Your submission must be your original work. No more than a combined total of 30% of the submission and no more than a 10% match to any one individual source can be directly quoted or closely paraphrased from sources, even if cited correctly. An originality report is provided when you submit your task that can be used as a guide.

You must use the rubric to direct the creation of your submission because it provides detailed criteria that will be used to evaluate your work. Each requirement below may be evaluated by more than one rubric aspect. The rubric aspect titles may contain hyperlinks to relevant portions of the course

Note: The assessment must be submitted in an Android Studio project using Java as the programming language. External plugins and libraries are not allowed in the project.

Note: Submit your performance assessment by including all Android project files, APK, and signed apk files in one zipped.zip file.

Note: The zip file submission must also keep the project file and folder structure intact for the Android Studio IDE.

A. Create an Android (version 4.0 or higher) mobile application with the following functional requirements:

    Include the following information for each term:

• the term title (e.g., Term 1, Term 2, Spring Term)

• the start date

• the end date

    Include features that allow the user to add as many terms as needed.

    Implement validation so that a term cannot be deleted if courses are assigned to it.

    Include features that allow the user to do the following for each term:

a. add as many courses as needed

b. display a list of courses associated with each term

c. display a detailed view of each term, including the following information:

• the term title (e.g., Term 1, Term 2, Spring Term)

• the start date

• the end date

    Include the following details for each course:

• the course title

• the start date

• the anticipated end date

• the status (in progress, completed, dropped, plan to take)

• the course mentor names, phone numbers, and e-mail addresses

    Include features that allow the user to do the following for each course:

a. add as many assessments as needed

b. add a minimum of one optional note per course

c. enter, edit, and delete course information

d. display optional notes

e. display a detailed view of the course, including the due date

f. set alerts for the start and end date, that will trigger when the application is not running

g. share notes via a sharing feature (either e-mail or SMS)

    Include features that allow the user to do the following for each assessment:

a. add performance and objective assessments for each course, including the titles and due dates of the assessments

b. enter, edit, and delete assessment information

c. set alerts for goal dates, that will trigger when the application is not running

B. Design the following screen layouts, including appropriate GUI (graphical user interface) elements (e.g., navigation, input, and information) for each layout:

• home screen

• list of terms

• list of courses

• list of assessments

• detailed course view

• detailed term view

• detailed assessment view

C. Create a scheduler and progress tracking application as part of your mobile application from part A.

Note: This can be the implementation of part A.

    Include the following implementation requirements in the application from part C. Be sure your application is a minimum version 4.0 mobile application:

• an ArrayList

• an intent

• navigation capability between multiple screens using activity

• three activities

• events (e.g., a click event)

• the ability to work in portrait and landscape layout

• interactive capability (e.g., the ability to accept and act upon user input)

• a database to store and retrieve application data

• an application title and an icon

• notifications or alerts

• the use of both declarative and programmatic methods to create a user interface

    Include the following interface requirements in the application from part C:

• the ability to scroll vertically

• an action bar

• two layouts

• input controls

• buttons

D. Create a storyboard to demonstrate application flow that includes each of the menus and screens from part B.

E. Provide screen shots of generating the signed APK to demonstrate that you have created a deployment package.

Note: Verify that all the required functions of your application are working by executing the apk file.

F. Reflect on the creation of your mobile application by doing the following:

    Explain mobile application development through the context of the architecture involved, including hardware and software capabilities and limitations.

a. Identify the version of the operating system your application was developed under and is compatible with.

    Describe (suggested length of 1–2 paragraphs) the challenges you faced during the development of the mobile application.

    Describe (suggested length of 1–2 paragraphs) how you overcame each challenge discussed in part F2.

    Describe (suggested length of 1–2 paragraphs) what you would do differently if you did the project again.

    Describe how emulators are used and the pros and cons of using an emulator vs. using a development device.

G. Acknowledge sources, using APA-formatted in-text citations and references, for content that is quoted, paraphrased, or summarized.

H. Demonstrate professional communication in the content and presentation of your submission.
