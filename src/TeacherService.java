

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeacherService {
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/education";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Abcdef123";


    private static List<Teacher> teachers = null;


    private void insertTeacher(List<Teacher> teachers) {
        String sql = "INSERT INTO teacher(name,surname,age,salary) VALUES (?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql) ) {


            for (Teacher teacher : teachers) {
                statement.setString(1, teacher.getFirstName());
                statement.setString(2, teacher.getLastName());
                statement.setInt(3, teacher.getAge());
                statement.setDouble(4, teacher.getSalary());

                statement.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Error occurred while inserting teacher: " + e.getMessage());
        }
    }

    public void showMenu() {

        boolean exit = true;
        while (exit) {

            System.out.println("Hansi emeliyyati etmek isteyirsiz? ");
            System.out.println("0. ilk defe yaratmaq \n" +
                    "1. yenisini yaratmaq \n" +
                    "2. yenilemek (update) \n" +
                    "3. silmek \n" +
                    "4. axtarmaq \n" +
                    "5. hamisini gormek \n" +
                    "6. exit");

            int action = new Scanner(System.in).nextInt();


            switch (action) {
                case 0:
                     initializeTeacher();
                    break;
                case 1:
                    teachers = initializeNewTeacher(teachers);
                    break;
                case 2:
                    updateTeacher();
                    break;
                case 3:
                    deleteTeacher();
                    break;
                case 4:
                    findTeacher();
                    break;
                case 5:
                    printAll();
                    break;
                case 6:
                    exit = false;
                    break;
                default:
                    System.out.println("Sehv secim !");
                    exit = false;
                    break;
            }
        }
    }


    public  void initializeTeacher() {
        System.out.println("Nece nefer muellim qeydiyyatdan kecirdeceksiz: ");
        int count = new Scanner(System.in).nextInt();

        List<Teacher> teachers = new ArrayList<>();

        //Teleb ele ve yarat

        for (int i = 0; i < count; i++) {

            System.out.println("Qeydiyyat nomresi: " + (i + 1));
            teachers.add(requireAndCreate());
        }

        //Yaradilanlari capa ver

        insertTeacher(teachers);
        printAll();

    }


    public List<Teacher> initializeNewTeacher(List<Teacher> teachers) {
        System.out.println("neche nefer yaratmaq isteyirsiz?");
        int additionalCount = new Scanner(System.in).nextInt();
        List<Teacher> newTeacher = new ArrayList<>();


        for (int i = 0; i < additionalCount; i++) {
            newTeacher.add(requireAndCreate());
        }

        insertTeacher(newTeacher);
        printAll();
        return newTeacher;
    }

    public void updateTeacher() {
        String sql = null;
        System.out.println("Necenci muellimi update etmek isteyirsen");
        int updateNumber = new Scanner(System.in).nextInt();
        System.out.println("Hansi xanani update etmek isteyirsen? (name,surname,age,salary)");
        String field = new Scanner(System.in).nextLine();
        if (field.equals("name")) {
            System.out.println("Adini daxil edin: ");
            String newName = new Scanner(System.in).nextLine();
            sql = "UPDATE teacher SET name = ? WHERE id = ? ";
            try (Connection connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newName);
                statement.setInt(2, updateNumber);

                statement.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Error occurred while update name of  teacher: " + e.getMessage());
            }
        } else if (field.equals("surname")) {
            System.out.println("Soyadini daxil edin: ");
            String newSurname = new Scanner(System.in).nextLine();
            sql = "UPDATE teacher SET surname = ? WHERE id = ? ";

            try (Connection connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newSurname);
                statement.setInt(2, updateNumber);

                statement.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Error occurred while update surname of  teacher: " + e.getMessage());
            }
        } else if (field.equals("age")) {
            System.out.println("Yashini daxil edin: ");
            int newAge = new Scanner(System.in).nextInt();
            sql = "UPDATE teacher SET age = ? WHERE id = ? ";
            try (Connection connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, newAge);
                statement.setInt(2, updateNumber);

                statement.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Error occurred while update age of  teacher: " + e.getMessage());
            }
        } else if (field.equals("salary")) {
            System.out.println("Emekhaqqini daxil edin: ");
            double newSalary = new Scanner(System.in).nextDouble();
            sql = "UPDATE teacher SET salary = ? WHERE id = ? ";
            try (Connection connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, newSalary);
                statement.setInt(2, updateNumber);

                statement.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Error occurred while update salary of  teacher: " + e.getMessage());
            }
        }
        printAll();
    }

    public void deleteTeacher() {
        System.out.println("Necenci yerdeki muellimi silmek isteyirsen? ");
        int choose = new Scanner(System.in).nextInt();
        String sql = "DELETE FROM teacher WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, choose);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error occurred while delete  teacher: " + e.getMessage());
        }
        printAll();
    }

    public void findTeacher() {
        System.out.println("Axtardiginiz ad/soyadi daxil edin");
        String text = new Scanner(System.in).nextLine();

        String sql = "SELECT * FROM teacher WHERE name LIKE ? OR surname LIKE ?";

        try (Connection connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + text + "%");
            statement.setString(2, "%" + text + "%");

            try (ResultSet resultSet = statement.executeQuery()){
                boolean found = false;
                while (resultSet.next()) {
                    Teacher teacher = new Teacher();
                    teacher.setId(resultSet.getInt("id"));
                    teacher.setFirstName(resultSet.getString("name"));
                    teacher.setLastName(resultSet.getString("surname"));
                    teacher.setAge(resultSet.getInt("age"));
                    teacher.setSalary(resultSet.getDouble("salary"));
                    System.out.println("Sizin axtardiginiz ad/soyad muellim var:  " + teacher);
                    found = true;
                }
                if (!found) {
                    System.out.println("No Teacher found");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error occurred while delete  teacher: " + e.getMessage());
        }
    }


    public void printAll() {
        System.out.println("Qeydiyyatdan kecen muellimler: ");

        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT id,name,surname,age,salary FROM teacher";
        try (Connection connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(resultSet.getInt("id"));
                teacher.setFirstName(resultSet.getString("name"));
                teacher.setLastName(resultSet.getString("surname"));
                teacher.setAge(resultSet.getInt("age"));
                teacher.setSalary(resultSet.getDouble("salary"));
                teachers.add(teacher);

            }

        } catch (SQLException e) {
            System.err.println("Error occurred while findAll teacher: " + e.getMessage());
        }

        for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }
    }

    private static Teacher requireAndCreate() {
        Teacher teacher = new Teacher();

        System.out.println("Muellimin adini daxil edin: ");
        teacher.setFirstName(new Scanner(System.in).nextLine());
        System.out.println("Muellimin soyadini daxil edin: ");
        teacher.setLastName(new Scanner(System.in).nextLine());
        System.out.println("Muellimin yashini daxil edin: ");
        teacher.setAge(new Scanner(System.in).nextInt());
        System.out.println("Muelimin emekhaqqisini daxil edin: ");
        teacher.setSalary(new Scanner(System.in).nextDouble());
        return teacher;

    }
}
