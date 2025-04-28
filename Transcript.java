import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Transcript {
    private List<Subject> subjects;

    public Transcript() {
        this.subjects = new ArrayList<>();
    }
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public double calculateGPA() {
        double totalGrades = 0;
        for (Subject subject : subjects) {
            totalGrades += subject.getGrade();
        }
        return subjects.isEmpty() ? 0 : totalGrades / subjects.size();
    }

public void displayTranscript(String[] studentDetails) {
    double total = 0.0;

    System.out.println("Student ID: " + studentDetails[0]);
    System.out.println("Name: " + studentDetails[1]);

    if (studentDetails.length < 3 || studentDetails[2].isEmpty()) {
        System.out.println("No subjects found.");
        System.out.println();
        return;
    }

    System.out.println("\nSubjects:");
    System.out.println();
    String[] subjects = studentDetails[2].split("/");
    System.out.println("studentDetails"+Arrays.toString(studentDetails));
    System.out.println("subjects"+Arrays.toString(subjects));

    for (String s : subjects) {
        if (s.isEmpty()) continue;
        String[] parts = s.split(":");
        System.out.println("Code: " + parts[0]);
        System.out.println("Subject: " + parts[1]);
        System.out.println("Grade: " + parts[2]);

        total += Double.parseDouble(parts[2]);
        System.out.println();
    }

    int subjectLength = subjects.length-1;
    double gpa = total / subjectLength;
    System.out.printf("GPA: %.2f%n", gpa); 
    System.out.println();

    if (studentDetails.length >= 5) {
        System.out.println("Status: " + studentDetails[4]);
    }
}

}
