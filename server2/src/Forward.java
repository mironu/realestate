
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Servlet implementation class Forward
 */
@WebServlet("/Forward")
public class Forward extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Forward() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
        String  userAgent  =   request.getHeader("User-Agent");
		String resp ;
		if(email!=null) {
			String params = "name="+username+"&password="+password+"&email="+email;
			resp = executePost("http://localhost:8080/boss/Register",params);
			
			if(userAgent == null) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("REGISTER", "SUCCESS");
					PrintWriter pw = response.getWriter();
					pw.write(jsonObject.toString());
					pw.print(jsonObject.toString());
					
					System.out.println("Register successful" + jsonObject.toString());
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			else {
				request.setAttribute("message", resp);
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
		else {
			String params = "name="+username+"&password="+password;
			resp = executePost("http://localhost:8080/boss/Login",params);
			
			if(userAgent == null) {
				try {
					PrintWriter pw = response.getWriter();
					if(resp.equals("ok\r"))
						pw.write("success");
					else
						pw.write("fail");
					
					System.out.println("Login successful");
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			else {
				if(resp.equals("ok\r")) {
					HttpSession session = request.getSession(true);
					session.setAttribute("name", username);   
					response.sendRedirect("home.jsp");
				}
				else {
					request.setAttribute("message", resp);
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			}
				
		}
		
	}
	
	public static String executePost(String targetURL, String urlParameters) {
		  HttpURLConnection connection = null;

		  try {
		    //Create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");

		    connection.setRequestProperty("Content-Length", 
		        Integer.toString(urlParameters.getBytes().length));
		    connection.setRequestProperty("Content-Language", "en-US");  

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.close();

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    return null;
		  } finally {
		    if (connection != null) {
		      connection.disconnect();
		    }
		  }
		}

}
