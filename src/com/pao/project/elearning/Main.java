package com.pao.project.elearning;

import com.pao.project.elearning.exception.CourseNotFoundException;
import com.pao.project.elearning.exception.UserAlreadyEnrolledException;
import com.pao.project.elearning.model.Course;
import com.pao.project.elearning.model.CourseCode;
import com.pao.project.elearning.model.Instructor;
import com.pao.project.elearning.model.Quiz;
import com.pao.project.elearning.model.ScoreRecord;
import com.pao.project.elearning.model.Student;
import com.pao.project.elearning.service.CourseService;
import com.pao.project.elearning.service.EnrollmentService;
import com.pao.project.elearning.service.QuizService;
import com.pao.project.elearning.service.UserService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = UserService.getInstance();
        CourseService courseService = CourseService.getInstance();
        EnrollmentService enrollmentService = EnrollmentService.getInstance();
        QuizService quizService = QuizService.getInstance();

        Instructor instructor = new Instructor("I001", "Ana Popescu", "ana.popescu@example.com", "Java");
        Student student1 = new Student("S001", "Ion Ionescu", "ion.ionescu@example.com", "Programare");
        Student student2 = new Student("S002", "Maria Georgescu", "maria.georgescu@example.com", "Design");

        userService.addInstructor(instructor);
        userService.addStudent(student1);
        userService.addStudent(student2);

        Course javaCourse = new Course(new CourseCode("EL", 101), "Programare Java", "Programare", instructor);
        Course webCourse = new Course(new CourseCode("EL", 102), "Dezvoltare Web", "Programare", instructor);
        Course designCourse = new Course(new CourseCode("EL", 201), "Design UI/UX", "Design", instructor);

        courseService.addCourse(javaCourse);
        courseService.addCourse(webCourse);
        courseService.addCourse(designCourse);

        try {
            enrollmentService.enrollStudent(student1, javaCourse);
            enrollmentService.enrollStudent(student2, javaCourse);
            enrollmentService.enrollStudent(student1, webCourse);
            enrollmentService.enrollStudent(student2, designCourse);
        } catch (UserAlreadyEnrolledException | CourseNotFoundException e) {
            System.out.println("Eroare la inscriere: " + e.getMessage());
        }

        Quiz javaQuiz = new Quiz("Q1", "Quiz Java 1", javaCourse, 100);
        Quiz webQuiz = new Quiz("Q2", "Quiz Web 1", webCourse, 80);

        quizService.addQuiz(javaQuiz);
        quizService.addQuiz(webQuiz);

        quizService.gradeQuiz(javaQuiz, student1, 85);
        quizService.gradeQuiz(javaQuiz, student2, 92);
        quizService.gradeQuiz(webQuiz, student1, 79);

        System.out.println("=== Lista cursurilor disponibile ===");
        courseService.listCoursesSorted().forEach(System.out::println);

        System.out.println("\n=== Cursuri din categoria Programare ===");
        courseService.findCoursesByCategory("Programare").forEach(System.out::println);

        System.out.println("\n=== Studenti inscrisi la Programare Java ===");
        enrollmentService.listStudentsInCourse(javaCourse).forEach(System.out::println);

        System.out.println("\n=== Cursurile studentului Ion Ionescu ===");
        enrollmentService.listCoursesForStudent(student1).forEach(System.out::println);

        System.out.println("\n=== Scoruri quiz pentru Ion Ionescu ===");
        List<ScoreRecord> ionScores = quizService.listScoresForStudent(student1);
        ionScores.forEach(System.out::println);

        System.out.println("\n=== Cautare curs dupa cod ===");
        System.out.println(courseService.findCourseByCode(new CourseCode("EL", 101)).orElse(null));

        System.out.println("\n=== Studenti in total ===");
        userService.listAllStudents().forEach(System.out::println);
    }
}
