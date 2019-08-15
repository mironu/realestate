

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int result = 0;
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		UserBeam user = new UserBeam();
		user.setUsername(username);
		user.setPassword(password);
		DataBase db = new DataBase();
		String message;
		try {
			result = db.checkLogin(user);
		}
		catch (SQLException e) {
		
			e.printStackTrace();
		}
		if(result > 0)
		{
			message = "ok";

		}
		else {
			message = "Wrong username or password";
		}
		response.setContentType("text/html");
		  PrintWriter out = response.getWriter();
		  out.append(message);
		  out.close();
	}
}
