import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/login_system"; // 替换为你的数据库名称
    private static final String USER = "root"; // 替换为你的数据库用户名
    private static final String PASSWORD = "123456"; // 替换为你的数据库密码

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
