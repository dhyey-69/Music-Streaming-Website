import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class SignupServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/music", "root", "root");

            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (email, password) VALUES (?, ?)");
            statement.setString(1, email);
            statement.setString(2, password); // For simplicity, using plain text. Use hashed passwords in production.

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                response.sendRedirect("index.html");
            } else {
                response.getWriter().println("Failed to register user.");
            }

            connection.close();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException(e);
        }
    }
}
