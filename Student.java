import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

final class Student implements Archivable {
    private String name;
    private int id;
    private final Path path;

    public Student(Path path) {
        this.path = path;
         
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            System.out.println("Invalid name!");
        }
    }

    public void setID(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            System.out.println("Invalid ID!");
        }
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public void addStudent(String name, int id) {
        String student = id + "-" + name + "-\n";
        try (BufferedOutputStream output = new BufferedOutputStream(Files.newOutputStream(path, CREATE, StandardOpenOption.APPEND))) {
            byte[] data = student.getBytes();
            output.write(data);
            output.flush();
            System.out.println("Student " + name + " added!\n");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

   public void updateStudent(Scanner scan, int oldId) {
    String oldName = null;
    String newName;
    int newId;
    String transcript = ""; 
    try {
        List<String> datas = Files.readAllLines(path);
        boolean updated = false;
        for (int i = 0; i < datas.size(); i++) {
            String studentInfo = datas.get(i).trim();

            if (studentInfo.isEmpty()) {
                continue;
            }

            String[] studentDetails = studentInfo.split("-");


            if (studentDetails.length < 2) {
                System.out.println("Skipping invalid line: " + studentInfo);
                continue;
            }

            
            if (studentDetails.length > 2) {
                transcript = studentDetails[2];  
            }

            if (Integer.parseInt(studentDetails[0]) == oldId) {
                oldName = studentDetails[1];

                System.out.println("1. Update Name");
                System.out.println("2. Update ID");
                int choice = scan.nextInt();
                scan.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter new Name: ");
                        newName = scan.nextLine();
                        datas.set(i, oldId + "-" + newName + "-" + transcript);
                        updated = true;
                        break;
                    case 2:
                        System.out.print("Enter new ID: ");
                        newId = scan.nextInt();
                        scan.nextLine();
                        datas.set(i, newId + "-" + oldName + "-" + transcript);
                        updated = true;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
                break;
            }
        }

        if (updated) {
            Files.write(path, datas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Student information updated successfully.");
        } else {
            System.out.println("Student ID not found.");
        }

    } catch (IOException e) {
        System.out.println(e);
    }
}

// ADD SUbject
public void addSubjects(Scanner scan, int studentId) {
    try {
        List<String> datas = Files.readAllLines(path);
        boolean found = false;

        for (int i = 0; i < datas.size(); i++) {
            String line = datas.get(i);
            String[] parts = line.split("-");

            if (parts.length < 2) continue;

            if (Integer.parseInt(parts[0]) == studentId) {
                found = true;
                System.out.print("How many subjects to add? ");
                int count = scan.nextInt();
                scan.nextLine();

                StringBuilder updatedLine = new StringBuilder(line);
                List<Subject> subjects = new ArrayList<>();

                for (int j = 0; j < count; j++) {
                    Subject subject = Subject.createSubjectFromInput(scan);
                    subjects.add(subject);
                    updatedLine.append("/").append(subject.toFileFormat());
                }

                double total = 0;
                for (Subject subject : subjects) {
                    total += subject.getGrade();
                }
                double gpa = subjects.isEmpty() ? 0 : total / subjects.size();

                updatedLine.append("-").append(String.format("%.2f", gpa));

                datas.set(i, updatedLine.toString());

                Files.write(path, datas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

                archive(studentId, gpa);

                break;
            }
        }

        if (!found) {
            throw new IOException("Student not found.");
        }

    } catch (IOException e) {
        System.out.println("Error adding subjects: " + e);
    }
}


public List<String[]> getAllStudentDetails(int limit) {
    List<String[]> students = new ArrayList<>();
    try {
        List<String> datas = Files.readAllLines(path);
        for (String studentInfo : datas) {
            studentInfo = studentInfo.trim();
            if (studentInfo.isEmpty()) continue;

            String[] studentDetails = studentInfo.split("-", limit);
            students.add(studentDetails);
        }
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }

    return students;
}

    // Display trancript
public void displayTranscript(int studentId) {
    List<String[]> students = getAllStudentDetails(5); 

    for (String[] studentDetails : students) {
        if (studentDetails.length < 2) continue; 

        if (Integer.parseInt(studentDetails[0]) == studentId) {
            Transcript transcript = new Transcript();
            transcript.displayTranscript(studentDetails); 
            return;
        }
    }

    System.out.println("Student not found.");
}

public void displayRecords() {
    List<String[]> students = getAllStudentDetails(5);

    for (String[] studentDetails : students) {
        System.out.println();
        System.out.println("ID: " + studentDetails[0] + " Name: " + studentDetails[1]);

        if (studentDetails.length >= 3 && studentDetails[2] != null && !studentDetails[2].isEmpty()) {
            String[] subjects = studentDetails[2].split("/");
            for (String item : subjects) {
                if (item.isEmpty()) continue;
                String[] parts = item.split(":");
                if (parts.length == 3) {
                    System.out.println("Code: " + parts[0] + ", Subject: " + parts[1] + ", Grade: " + parts[2]);
                }
            }
        } else {
            System.out.println("No subjects yet.");
        }

        if (studentDetails.length >= 4) {
            System.out.println("GPA: " + studentDetails[3]);
        } else {
            System.out.println("GPA: N/A");
        }

        if (studentDetails.length >= 5) {
            System.out.println("Status: " + studentDetails[4]);
        } else {
            System.out.println("Status: N/A");
        }

        System.out.println("========================================");
    }
}
public void displayByName() {
    List<String[]> students = getAllStudentDetails(5); 
    students.sort((a, b) -> {
        if (a.length < 2 || b.length < 2) return 0; 
        return a[1].compareToIgnoreCase(b[1]);
    });
    for (String[] studentDetails : students) {
        if (studentDetails.length >= 2) {
            System.out.println("ID: " + studentDetails[0] + " Name: " + studentDetails[1]);

            if (studentDetails.length >= 3 && studentDetails[2] != null && !studentDetails[2].isEmpty()) {
                System.out.println("Subjects:");
                String[] subjects = studentDetails[2].split("/");
                for (String subject : subjects) {
                    if (subject.isEmpty()) continue;
                    String[] parts = subject.split(":");
                    System.out.println("- Code: " + parts[0] + ", Subject: " + parts[1] + ", Grade: " + parts[2]);
                }
            }

            if (studentDetails.length >= 4 && studentDetails[3] != null && !studentDetails[3].isEmpty()) {
                System.out.println("GPA: " + studentDetails[3]);
            }

            if (studentDetails.length >= 5 && studentDetails[4] != null && !studentDetails[4].isEmpty()) {
                System.out.println("Status: " + studentDetails[4]);
            }

            System.out.println("----------------------------------------");
        }
    }
}

public void displayByGpa() {
    List<String[]> students = getAllStudentDetails(5); 
students.sort((a, b) -> {
    if (a.length < 4 || b.length < 4) return 0; 

    try {
        double gpaA = Double.parseDouble(a[3]);
        double gpaB = Double.parseDouble(b[3]);
        return Double.compare(gpaB, gpaA); 
    } catch (NumberFormatException e) {
        return 0;
    }
});
    for (String[] studentDetails : students) {
        if (studentDetails.length >= 2) {
            System.out.println("ID: " + studentDetails[0] + " Name: " + studentDetails[1]);

            if (studentDetails.length >= 3 && studentDetails[2] != null && !studentDetails[2].isEmpty()) {
                System.out.println("Subjects:");
                String[] subjects = studentDetails[2].split("/");
                for (String subject : subjects) {
                    if (subject.isEmpty()) continue;
                    String[] parts = subject.split(":");
                    System.out.println("- Code: " + parts[0] + ", Subject: " + parts[1] + ", Grade: " + parts[2]);
                }
            }

            if (studentDetails.length >= 4 && studentDetails[3] != null && !studentDetails[3].isEmpty()) {
                System.out.println("GPA: " + studentDetails[3]);
            }

            if (studentDetails.length >= 5 && studentDetails[4] != null && !studentDetails[4].isEmpty()) {
                System.out.println("Status: " + studentDetails[4]);
            }

            System.out.println("----------------------------------------");
        }
    }
}


public void archive(int studentId, double gpa) {
    if (gpa >= 3.5) { 
        try {
            List<String> datas = Files.readAllLines(path);

            for (int i = 0; i < datas.size(); i++) {
                String line = datas.get(i);
                String[] parts = line.split("-");

                if (parts.length < 2) continue;

                if (Integer.parseInt(parts[0]) == studentId) {
                    if (!line.contains("granduand")) {
                        line += "-granduand"; 
                        datas.set(i, line);
                    }
                    break;
                }
            }

            Files.write(path, datas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
@Override
public void archive() {
    throw new UnsupportedOperationException("Not supported yet.");
}

}