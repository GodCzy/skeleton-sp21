import java.sql.*;

public class JdbcExample {
    public static void main(String[] args) {

        // 建立数据库连接
        String url = "jdbc:mysql://127.0.0.1:3306/school?characterEncoding=utf8&useSSL=false";
        String user = "root";
        String password = "123456";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            // 执行SQL查询
            String sql = "SELECT * FROM student";
            ResultSet resultSet = statement.executeQuery(sql);

            // 处理结果集
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }

            // 关闭资源
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}