import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static StudentManager manager = new StudentManager();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== SCHOOL MANAGEMENT SYSTEM ===");
            System.out.println("1. Add Student");
            System.out.println("2. Add Course");
            System.out.println("3. Enroll Student");
            System.out.println("4. Update Grade");
            System.out.println("5. List Students & Grades");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    handleAddStudent();
                    break;
                case 2:
                    handleAddCourse();
                    break;
                case 3:
                    handleEnroll();
                    break;
                case 4:
                    handleUpdateGrade();
                    break;
                case 5:
                    manager.listStudentsAndGrades();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleAddStudent() {
        try {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            if(name.isEmpty()) throw new Exception("Name empty");

            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            manager.addStudent(new Student(name, email));
        } catch (Exception e) {
            System.out.println("Input Error: " + e.getMessage());
        }
    }

    private static void handleAddCourse() {
        try {
            System.out.print("Enter Course Title: ");
            String title = scanner.nextLine();
            if(title.isEmpty()) throw new Exception("Title empty");

            System.out.print("Enter Credits: ");
            int credits = Integer.parseInt(scanner.nextLine());

            manager.addCourse(new Course(title, credits));
        } catch (Exception e) {
            System.out.println("Input Error: " + e.getMessage());
        }
    }

    private static void handleEnroll() {
        try {
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Course ID: ");
            int courseId = Integer.parseInt(scanner.nextLine());

            manager.enrollStudent(studentId, courseId);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number.");
        }
    }

    private static void handleUpdateGrade() {
        try {
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Course ID: ");
            int courseId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Grade: ");
            double grade = Double.parseDouble(scanner.nextLine());

            manager.updateStudentGrade(studentId, courseId, grade);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format.");
        }
    }
}
