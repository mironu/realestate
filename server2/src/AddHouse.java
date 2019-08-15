

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Base64.Decoder;

/**
 * Servlet implementation class AddHouse
 */
@WebServlet("/AddHouse")
@MultipartConfig
public class AddHouse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddHouse() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String  userAgent  =   request.getHeader("User-Agent");
		  if(userAgent == null) {
			  String price = request.getParameter("price");
			  String address = request.getParameter("address");
			  String img = request.getParameter("img");
			  BufferedImage image = null;
			  byte[] imageByte;

			  Decoder decoder = Base64.getDecoder();
			  imageByte = decoder.decode(img);
			  ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			  image = ImageIO.read(bis);
			  bis.close();

			  // write the image to a file
			  File outputfile = new File("temp");
			  ImageIO.write(image, "bmp", outputfile);
			  
			  String resp = executePost("http://localhost:8080/boss/House",price,address,outputfile);
				System.out.println(resp);
				PrintWriter pw = response.getWriter();
				if(resp.equals("OK"))
					pw.write("success");
				else {
					pw.write("fail");
				}
			  
		  }
		  else {
		
		String price = request.getParameter("price");
		String address = request.getParameter("address");
		//HttpSession session = request.getSession(true);
		//String username = (String)session.getAttribute("name");
		Part photo = request.getPart("photo");
		String resp;
		File file = new File("temp");
				try(OutputStream outputStream = new FileOutputStream(file)){
				    IOUtils.copy(photo.getInputStream(), outputStream);
				} catch (FileNotFoundException e) {
				    // handle exception here
				} catch (IOException e) {
				    // handle exception here
				}
				
		resp = executePost("http://localhost:8080/boss/House",price,address,file);
		System.out.println(resp);
		if(resp.equals("OK"))
			response.sendRedirect("ListHouse");
		else {
			//request.setAttribute("message", resp);
			//request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		  }
	}
	public static String executePost(String targetURL, String price,String address,File file) throws IOException {
		String charset = "UTF-8";
		String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
		String CRLF = "\r\n"; // Line separator required by multipart/form-data.

		URLConnection connection=null;
		try {
			connection = new URL(targetURL).openConnection();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

		try {
		    OutputStream output = connection.getOutputStream();
		    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
		
		    // Send normal param.
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"address\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		    writer.append(CRLF).append(address).append(CRLF).flush();
		    
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"price\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		    writer.append(CRLF).append(price).append(CRLF).flush();


		    // Send binary file.
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"photo\"; filename=\"" + file.getName() + "\"").append(CRLF);
		    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(CRLF);
		    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
		    writer.append(CRLF).flush();
		    try {
				Files.copy(file.toPath(), output);
				output.flush(); // Important before continuing with writer!
				writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    // End of multipart/form-data.
		    writer.append("--" + boundary + "--").append(CRLF).flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int responseCode = ((HttpURLConnection) connection).getResponseCode();
		System.out.println(responseCode); // Should be 200
		return ((HttpURLConnection) connection).getResponseMessage();
		
	}


}
