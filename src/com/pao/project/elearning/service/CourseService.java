package com.pao.project.elearning.service;

import com.pao.project.elearning.model.Course;
import com.pao.project.elearning.model.CourseCode;
import com.pao.project.elearning.repository.CourseRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

public class CourseService {
    private static final CourseService INSTANCE = new CourseService();
    private final Map<CourseCode, Course> courses = new HashMap<>();
    private final Map<String, List<Course>> coursesByCategory = new HashMap<>();
    private final CourseRepository courseRepository = new CourseRepository();

    private CourseService() {
    }

    public static CourseService getInstance() {
        return INSTANCE;
    }

    public void addCourse(Course course) {
        if (courses.putIfAbsent(course.getCode(), course) == null) {
            coursesByCategory.computeIfAbsent(course.getCategory(), key -> new ArrayList<>()).add(course);
            courseRepository.save(course);
        }
        AuditService.getInstance().logAction("add_course");
    }

    public Optional<Course> findCourseByCode(CourseCode code) {
        AuditService.getInstance().logAction("find_course_by_code");
        return courseRepository.findById(code);
    }

    public List<Course> listCourses() {
        AuditService.getInstance().logAction("list_courses");
        return Collections.unmodifiableList(courseRepository.findAll());
    }

    public List<Course> listCoursesSorted() {
        AuditService.getInstance().logAction("list_courses_sorted");
        TreeSet<Course> sorted = new TreeSet<>(courseRepository.findAll());
        return Collections.unmodifiableList(new ArrayList<>(sorted));
    }

    public List<Course> findCoursesByCategory(String category) {
        AuditService.getInstance().logAction("find_courses_by_category");
        return Collections.unmodifiableList(courseRepository.findByCategory(category));
    }
}
