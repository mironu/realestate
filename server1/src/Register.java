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
@WebServlet("/Register")
public class Register extends HttpServlet{

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message;
		int result = 0;
		String username = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		UserBeam user=new UserBeam();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		DataBase db = new DataBase();
		try {
			result = db.registerUser(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result > 0)
		{
			 message = "Accoun created.You can now log in.";
		}
		else
		{
			message = "Something went wrong. Try again";
		}
		response.setContentType("text/html");
		  PrintWriter out = response.getWriter();
		  out.append(message);
		  out.close();
	
	}
}
