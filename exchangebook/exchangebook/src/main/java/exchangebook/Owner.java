package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement

public class Owner{
	private String userID;
	private String name;
	private String location;
	private List<String> bookOffer;
	private List<String> bookDemand;
	public Owner(){
		
	}
	public Owner(String userID, String name, String location, List<String> bookOffer, List<String> bookDemand){
		this.userID=userID;
		this.name=name;
		this.location=location;
		this.bookOffer=bookOffer;
		this.bookDemand=bookDemand;		
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}
	public void setLocation(String location){
		this.location=location;
	}
	public String getLocation(){
		return this.location;
	}
	public List<String> getBookOffer(){
		return bookOffer;
	}
	public List<String> getBookDemand(){
		return bookDemand;
	}
	
}

