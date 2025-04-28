import java.util.Scanner;

class Subject {
    private String code;
    private String name;
    private double grade;

    public Subject(String code,String name, double grade) {
        this.code = code;
        this.name = name;
        this.grade = grade;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

    public String toFileFormat() {
        return code + ":" +name + ":" + grade;
    }


    public static Subject createSubjectFromInput(Scanner scan) {
        System.out.print("Enter subject code: ");
        String code = scan.nextLine();
        System.out.print("Enter subject name: ");
        String name = scan.nextLine();
        System.out.print("Enter grade (0.0 - 4.0): ");
        double grade = scan.nextDouble();
        scan.nextLine(); 
        return new Subject(code,name, grade);
    }
}
