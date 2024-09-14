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
import java.sql.SQLException;


public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public static final String LOAD_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/userdb";
    public static final String PASSWORD = "Yash@3221";
    public static final String USER = "root";
    
    Connection connection;
    
    public RegisterServlet() {
        super();
        
    }

	
    public void init(ServletConfig config) throws ServletException {
        super.init(config); // Call parent's init
        try {
            Class.forName(LOAD_DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established!");
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found!", e); // Let it throw exception
        } catch (SQLException e) {
            throw new ServletException("Unable to establish a database connection!", e);
        }
    }


	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    if (connection == null) {
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection is not initialized!");
	        return;
	    }
	    
	    // Proceed with the query
	    String fname = req.getParameter("fname");
	    String lname = req.getParameter("lname");
	    String uname = req.getParameter("uname");
	    String pword = req.getParameter("pass");

	    String q= "insert into uinfo values (?,?,?,?)";
	    try (PreparedStatement ps = connection.prepareStatement(q)) {
	        ps.setString(1, fname);
	        ps.setString(2, lname);
	        ps.setString(3, uname);
	        ps.setString(4, pword);
	        int c= ps.executeUpdate();
	        
	        // Sending the response
	        PrintWriter pw = res.getWriter();
	        pw.println("<html><body bgcolor=black text=white><center>");
	        pw.println("<h2>Registration Successful...</h2>");
	        pw.println("<a href=login.html>Login</a>");
	        pw.println("</center></body><html>");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void destroy() {
	    try {
	        if (connection != null && !connection.isClosed()) {
	            connection.close();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


}
