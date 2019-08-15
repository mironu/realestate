

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;

/**
 * Servlet implementation class House
 */
@WebServlet("/House")
@MultipartConfig
public class House extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public House() {
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
		String price = request.getParameter("price");
		String address = request.getParameter("address");
		//String user = request.getParameter("username");
		Part photo = request.getPart("photo");
		File file = new File("temp");
		try(OutputStream outputStream = new FileOutputStream(file)){
		    IOUtils.copy(photo.getInputStream(), outputStream);
		} catch (FileNotFoundException e) {
		    // handle exception here
		} catch (IOException e) {
		    // handle exception here
		}
		
		DataBase db = new DataBase();
		int res = db.insertHouse(photo.getInputStream(),price,address,(int)file.length());
		
		String message;
		if(res > 0) {
			message = "ok";
		}
		else {
			message = "error";
		}
		response.setContentType("application/x-www-form-urlencoded");
		  PrintWriter out = response.getWriter();
		  out.append(message);
		  out.close();
	}

}
