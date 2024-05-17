import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Connection connection = null;
        // PreparedStatement statement = null;
        // ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/music", "root", "root");

            String sql = "SELECT id, password FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (password.equals(storedPassword)) {  // For simplicity, we use plain text comparison. Consider using hashed passwords in production.
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id", resultSet.getInt("id"));
                    session.setAttribute("email", email);
                    response.sendRedirect("home.html");
                } else {
                    response.getWriter().println("Invalid email or password.");
                }
            } else {
                response.getWriter().println("No user found with that email address.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException(e);
        }
    }
}
