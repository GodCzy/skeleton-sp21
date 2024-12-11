import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


//JFrame扩展类
public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private JButton registerButton;

    public LoginWindow() {
        setTitle("登录系统");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭按钮的行为
        setLocationRelativeTo(null); // 居中显示
        setLayout(new GridLayout(5, 1));//设置网格布局

        // 用户名输入
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("用户名:"));//不需要创建变量,后续无访问
        usernamePanel.add(usernameField = new JTextField(20));//创建变量,后续方便访问
        add(usernamePanel);//将 usernamePanel 添加到 LoginWindow

        // 密码输入
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(new JLabel("密码:"));
        passwordPanel.add(passwordField = new JPasswordField(20));
        add(passwordPanel);

        // 登录按钮
        loginButton = new JButton("登录");
        add(loginButton);

        // 注册按钮
        registerButton = new JButton("注册");
        add(registerButton);

        // 登录状态显示
        statusLabel = new JLabel("", SwingConstants.CENTER);
        add(statusLabel);

        // 登录按钮事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();//登录时调用
            }
        });

        // 注册按钮事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterWindow registerWindow = new RegisterWindow();
                registerWindow.setVisible(true);//打开注册窗口
            }
        });
    }

    private void handleLogin() {
        //获取账号和密码
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        //检查账号和密码的有效性,进行认证
        if (authenticate(username, password)) {
            statusLabel.setText("登录成功！");
            statusLabel.setForeground(Color.GREEN);
            JOptionPane.showMessageDialog(this, "欢迎, " + username + "!", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            statusLabel.setText("账号或密码错误！");
            statusLabel.setForeground(Color.RED);
        }
    }

    private boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        //使用 SQL 查询验证用户,是否有匹配的账号和密码

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            //将输入参数绑定到 SQL 查询
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            //检查查询结果
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        //异常处理
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        //更换了一个主题
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //显示窗口
        SwingUtilities.invokeLater(() -> {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
        });
    }
}
