import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {



    public static void main(String[] args) throws Exception {


        TeacherService teacherService = new TeacherService();

        teacherService.showMenu();

    }
}