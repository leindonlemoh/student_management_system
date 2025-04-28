import java.nio.file.*;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Path fpath = Paths.get("C:\\Users\\leind\\OneDrive\\Desktop\\Leindon_Reimoh_Dilan\\student.txt");
        Student student = new Student(fpath);

        System.out.println("\n===========Welcome to Student Records Management System===========");
        student.displayRecords();

        while (true) {
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Add Subjects per Student");
            System.out.println("4. Display student transcript");
            System.out.println("5. Sort student list by Name or GPA");
            System.out.println("6. Exit");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    String name;
                    int id;
                    System.out.println("=========================== Add Student ==========================");
                    System.out.println("Enter Student Name: ");
                    name = scan.nextLine();
                    System.out.println("Enter Student ID: ");
                    id = scan.nextInt();
                    student.addStudent(name, id);
                    break;
                case 2:
                    System.out.println("=========================== Update Student ==========================");
                    System.out.print("Enter Student ID to update: ");
                    int oldId = scan.nextInt();
                    student.updateStudent(scan, oldId);
                    break;
                case 3:
                    System.out.println("=========================== Add Subjects ==========================");
                    System.out.print("Enter Student ID: ");
                    int studentId = scan.nextInt();
                    scan.nextLine();
                    student.addSubjects(scan, studentId);
                    break;
                case 4:
                    System.out.println("=========================== Display Transcript ==========================");
                    System.out.print("Enter Student ID: ");
                    int transId = scan.nextInt();
                    student.displayTranscript(transId);
                    break;
                case 5:
                 System.out.println("=========================== Sort By Name or Gpa ==========================");
                 
                     System.out.println();
                     System.out.println("1 Sort by Name");
                     System.out.println("2 Sort by Gpa");
                     int sortby = scan.nextInt();
                     scan.nextLine();
                     switch (sortby) {
                        case 1:
                            student.displayByName();
                            break;
                        case 2:
                            student.displayByGpa();
                            break;
                     
                        default:
                            break;
                     }
                 
                  

                case 6:
                    scan.close();
                    return;
                default:
                    break;
            }
        }
    }
}