import java.sql.*;

public class StudentManager {

    private boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM student WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkCourseTitleExists(String title) {
        String sql = "SELECT COUNT(*) FROM course WHERE title = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkIdExists(String table, int id) {
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addStudent(Student student) {
        if (checkEmailExists(student.getEmail())) {
            System.out.println("Error: Email already exists.");
            return;
        }
        String sql = "INSERT INTO student (name, email) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.executeUpdate();
            System.out.println("Success: Student added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCourse(Course course) {
        if (checkCourseTitleExists(course.getTitle())) {
            System.out.println("Error: Course title already exists.");
            return;
        }
        String sql = "INSERT INTO course (title, credits) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getTitle());
            stmt.setInt(2, course.getCredits());
            stmt.executeUpdate();
            System.out.println("Success: Course added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void enrollStudent(int studentId, int courseId) {
        if (!checkIdExists("student", studentId)) {
            System.out.println("Error: Student ID not found.");
            return;
        }
        if (!checkIdExists("course", courseId)) {
            System.out.println("Error: Course ID not found.");
            return;
        }

        String sql = "INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
            System.out.println("Success: Student enrolled in course.");
        } catch (SQLException e) {
            System.out.println("Error: Already enrolled or DB error.");
        }
    }

    public void updateStudentGrade(int studentId, int courseId, double grade) {
        String sql = "UPDATE enrollment SET grade = ? WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, grade);
            stmt.setInt(2, studentId);
            stmt.setInt(3, courseId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Success: Grade updated.");
            } else {
                System.out.println("Error: Enrollment record not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listStudentsAndGrades() {
        String sql = "SELECT s.name, c.title, e.grade " +
                     "FROM student s " +
                     "JOIN enrollment e ON s.id = e.student_id " +
                     "JOIN course c ON e.course_id = c.id " +
                     "ORDER BY s.name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-20s | %-20s | %-10s\n", "Student", "Course", "Grade");
            System.out.println("-------------------------------------------------------");
            while (rs.next()) {
                String gradeStr = rs.getString("grade");
                if (gradeStr == null) gradeStr = "N/A";
                
                System.out.printf("%-20s | %-20s | %-10s\n",
                    rs.getString("name"),
                    rs.getString("title"),
                    gradeStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
