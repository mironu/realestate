import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBase {
	int result;
	Connection con;
	
	public Connection getConn() {
		Connection con=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","test","test");
		}
		catch (SQLException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return con;
		
	}
	public int registerUser(UserBeam user) throws SQLException
	{
		String sql="INSERT INTO users (username,email,password) VALUE (?,?,?)";
		try {
			con = getConn();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			result = ps.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	return result;
	}
	
	public int checkLogin(UserBeam user) throws SQLException
	{
		String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
		try {
			con = getConn();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return 1;
			else
				return 0;
				
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	public int insertHouse(InputStream img,String price,String address,int len) {
		String sql="INSERT INTO houses (price,address,picture) VALUE (?,?,?)";
//		FileInputStream fin=null;
//		try {
//			fin = new FileInputStream(img);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		try {
			con = getConn();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, price);
			ps.setString(2, address);
			ps.setBinaryStream(3, img,len);
			//ps.setBinaryStream(3, (InputStream)fin,(int)img.length());
			result = ps.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	return result;
	}
	public ArrayList<HouseBeam> retrieveHouse() {
		ArrayList<HouseBeam> hs = new ArrayList<>(); 
		String sql = "SELECT price,picture,address FROM houses";
		try {
			con = getConn();
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        int i = 0;
	        while(rs.next()) {
	        	String price = rs.getString("price");
	        	String adr = rs.getString("address");
	        	InputStream in = rs.getBinaryStream("picture");
	        	try {
	        	OutputStream f = new FileOutputStream(new File("/home/catalin/tomcat/webapps/boss2/images/test"+i));
	            
	            int c = 0;
	            while ((c = in.read()) > -1) {
	                f.write(c);
	            }
	            f.close();
	            in.close();
	        	}
	        	catch(Exception e) {
	        		e.printStackTrace();
	        	}
	        	HouseBeam h = new HouseBeam();
	        	h.setAddress(adr); h.setPrice(price);
	        	h.setFile(new File("/home/catalin/tomcat/webapps/boss2/images/test"+i));
	        	hs.add(h);
	        	i++;
	        }
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return hs;
	}
	
}

