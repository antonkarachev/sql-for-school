package ru.karachev.sqlforschool.dao.impl;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.dao.CourseDao;
import ru.karachev.sqlforschool.entity.Course;
import ru.karachev.sqlforschool.exception.DataBaseException;
import ru.karachev.sqlforschool.service.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CourseDaoImpl extends AbstractCrudDaoImpl<Course> implements CourseDao {

    private static final Logger LOGGER = Logger.getLogger(CourseDaoImpl.class);

    private static final String SAVE_QUERY = "INSERT INTO courses(course_id, course_name, description) VALUES (?, ?,?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM courses ORDER BY course_id";
    private static final String FIND_ALL_PAGINATION_QUERY = "SELECT * FROM courses ORDER BY course_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE courses SET course_name = ? , description = ?" +
            " WHERE course_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM courses WHERE course_id=?";

    private static final String FIND_ALL_STUDENT_COURSES_QUERY = "SELECT * FROM courses WHERE course_id IN" +
            "(SELECT course_id FROM students_to_courses WHERE student_id = ?)";
    private static final String DELETE_STUDENT_FROM_ONE_COURSE_QUERY = "DELETE FROM students_to_courses " +
            "WHERE student_id=? AND course_id = ? ";

    public CourseDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Course> findAllByStudentId(int studentId) {
        return findAllByIntParameter(studentId, FIND_ALL_STUDENT_COURSES_QUERY);
    }

    @Override
    public void removeStudentFromCourse(Integer studentId, Integer courseId) {

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     DELETE_STUDENT_FROM_ONE_COURSE_QUERY)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
            LOGGER.info("Student with id: " + studentId + "removed from course with id:" + courseId);
        } catch (SQLException e) {
            LOGGER.error("Can`t remove student with id: " + studentId +
                    " from course with id:" + courseId, e);
            throw new DataBaseException("Can`t remove student with id: " + studentId +
                    " from course with id:" + courseId, e);
        }
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Course course) throws SQLException {
        preparedStatement.setInt(1, course.getId());
        preparedStatement.setString(2, course.getName());
        preparedStatement.setString(3, course.getDescription());
    }

    @Override
    protected Course mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Course.builder()
                .withCourseId(resultSet.getInt("course_id"))
                .withCourseName(resultSet.getString("course_name"))
                .withDescription(resultSet.getString("description"))
                .build();
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Course course) throws SQLException {
        preparedStatement.setString(1, course.getName());
        preparedStatement.setString(2, course.getDescription());
        preparedStatement.setInt(3, course.getId());
    }

}
