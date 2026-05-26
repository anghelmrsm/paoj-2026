DROP TABLE IF EXISTS scores;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS quizzes;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS instructors;

CREATE TABLE instructors (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    expertise TEXT NOT NULL
);

CREATE TABLE students (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    major TEXT NOT NULL
);

CREATE TABLE courses (
    code TEXT PRIMARY KEY,
    code_prefix TEXT NOT NULL,
    code_number INTEGER NOT NULL,
    title TEXT NOT NULL,
    category TEXT NOT NULL,
    instructor_id TEXT NOT NULL,
    enrolled_count INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (instructor_id) REFERENCES instructors(id)
);

CREATE TABLE quizzes (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    course_code TEXT NOT NULL,
    max_score INTEGER NOT NULL,
    FOREIGN KEY (course_code) REFERENCES courses(code)
);

CREATE TABLE enrollments (
    student_id TEXT NOT NULL,
    course_code TEXT NOT NULL,
    enrolled_at TEXT NOT NULL,
    PRIMARY KEY (student_id, course_code),
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_code) REFERENCES courses(code)
);

CREATE TABLE scores (
    quiz_id TEXT NOT NULL,
    student_id TEXT NOT NULL,
    score INTEGER NOT NULL,
    PRIMARY KEY (quiz_id, student_id),
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id),
    FOREIGN KEY (student_id) REFERENCES students(id)
);
