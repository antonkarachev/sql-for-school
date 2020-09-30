package ru.karachev.sqlforschool.creator;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseCreator {

    private static final Logger LOGGER = Logger.getLogger(CourseCreator.class);

    private static final int MAX_GROUPS = 10;
    private static final int PLACE_OF_NAME = 0;
    private static final int PLACE_OF_DESCRIPTION = 1;
    private static final String SPLITERATOR = "_";

    public List<Course> createCourses(List<String> courseNamesAndDescription) {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < MAX_GROUPS; i++) {
            String line = courseNamesAndDescription.get(i);
            courses.add(Course.builder()
                    .withCourseId(i + 1)
                    .withDescription(getFromLine(PLACE_OF_DESCRIPTION, line))
                    .withCourseName(getFromLine(PLACE_OF_NAME, line))
                    .build());
        }

        LOGGER.info("Creation of courses complete");
        return courses;
    }

    private String getFromLine(int placeOfWhatGet, String line) {
        return line.split(SPLITERATOR)[placeOfWhatGet];
    }

}
