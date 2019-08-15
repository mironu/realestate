import java.io.File;
import java.io.Serializable;

public class HouseBeam implements Serializable{
	String address,price;
	File img;
	
	
	public void setAddress(String s) {
		address = s;
	}
	public void setPrice(String s) {
		price = s;
	}
	public void setFile(File f) {
		img = f;
	}
	
	public String getAddress() {
		return this.address;
	}
	public String getPrice() {
		return this.price;
	}
	public File getFile() {
		return img;
	}

}
