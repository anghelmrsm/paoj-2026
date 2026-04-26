package com.pao.project.elearning.service;

import com.pao.project.elearning.model.Course;
import com.pao.project.elearning.model.CourseCode;

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

    private CourseService() {
    }

    public static CourseService getInstance() {
        return INSTANCE;
    }

    public void addCourse(Course course) {
        courses.putIfAbsent(course.getCode(), course);
        coursesByCategory.computeIfAbsent(course.getCategory(), key -> new ArrayList<>()).add(course);
        AuditService.getInstance().logAction("add_course");
    }

    public Optional<Course> findCourseByCode(CourseCode code) {
        AuditService.getInstance().logAction("find_course_by_code");
        return Optional.ofNullable(courses.get(code));
    }

    public List<Course> listCourses() {
        AuditService.getInstance().logAction("list_courses");
        return Collections.unmodifiableList(new ArrayList<>(courses.values()));
    }

    public List<Course> listCoursesSorted() {
        AuditService.getInstance().logAction("list_courses_sorted");
        TreeSet<Course> sorted = new TreeSet<>(courses.values());
        return Collections.unmodifiableList(new ArrayList<>(sorted));
    }

    public List<Course> findCoursesByCategory(String category) {
        AuditService.getInstance().logAction("find_courses_by_category");
        return Collections.unmodifiableList(coursesByCategory.getOrDefault(category, new ArrayList<>()));
    }
}
