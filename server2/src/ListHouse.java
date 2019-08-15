

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.tomcat.util.codec.binary.Base64;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;






/**
 * Servlet implementation class ListHouse
 */
@WebServlet("/ListHouse")
public class ListHouse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListHouse() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream resp = new URL("http://localhost:8080/boss/RetrieveHouse").openStream();
		Object object = null;
		  try {
			object = new ObjectInputStream(resp).readObject();
			System.out.println(object.getClass() + ": " + object);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  ArrayList<HouseBeam> hs = (ArrayList<HouseBeam>)object;
		  ArrayList<String> prices = new ArrayList<>();
		  ArrayList<String> adrs = new ArrayList<>();
		  ArrayList<File> files = new ArrayList<>();
		  for(HouseBeam h:hs) {
			  System.out.println(h.getAddress());
			  System.out.println(h.getPrice());
			  System.out.println(h.getFile());
			  prices.add(h.getPrice());
			  adrs.add(h.getAddress());
			  files.add(h.getFile());
		  }
		  String  userAgent  =   request.getHeader("User-Agent");
		  if(userAgent == null) {
			  JSONObject json = new JSONObject();
			  for(int i = 0 ; i < prices.size();i++) {
				 // JSONArray array = new JSONArray();
				  JSONObject item = new JSONObject();
				 
				  try {
					item.put("prices", prices.get(i));
					item.put("address", adrs.get(i));
					
					BufferedImage img = ImageIO.read(files.get(i));             
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(img, "bmp", baos);
					baos.flush();
					
					Encoder base = Base64.getEncoder();
					String encodedImage = base.encodeToString(baos.toByteArray());
					baos.close();
					
					//encodedImage = java.net.URLEncoder.encode(encodedImage, "ISO-8859-1");
					
					item.put("img", encodedImage);
					//array.put(item);
					json.put("house"+i, item);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 

			  }
			  PrintWriter pw = response.getWriter();
				pw.write(json.toString());
				pw.print(json.toString());
		  }
		  else {
			  request.setAttribute("prices", prices);
			  request.setAttribute("adrs", adrs);
			  request.setAttribute("imgs", files);
			  request.getRequestDispatcher("list.jsp").forward(request, response);
		  }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
	}

}
