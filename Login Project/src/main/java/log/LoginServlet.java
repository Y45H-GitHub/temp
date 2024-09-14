package log;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String LOAD_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/userdb";
    public static final String PASSWORD = "Yash@3221";
    public static final String USER = "root";
    Connection connection;

    public LoginServlet() {
        
    }

	public void init(ServletConfig config) throws ServletException {
		try {
			Class.forName(LOAD_DRIVER);
			connection= DriverManager.getConnection(URL,USER,PASSWORD);
		} catch (ClassNotFoundException e) {
	        e.printStackTrace(); 
	    }catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    if (connection == null) {
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection is not initialized!");
	        return;
	    }

	    String uname = request.getParameter("uname");
	    String upass = request.getParameter("upass");
	    try (PreparedStatement ps = connection.prepareStatement("select * from uinfo where uname=?")) {
	        ps.setString(1, uname);
	        
	        ResultSet rs = ps.executeQuery();
	        
	        PrintWriter pw = response.getWriter();
	        pw.println("<html><body><center>");
	        
	        if(rs.next())    
	            pw.println("Welcome "+uname);
	        else
	            pw.println("User Not Found");
	        
	        pw.println("</center></body></html>");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}

