package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement

public class BookOwner{
	private String userID;
	private String name;
	private String location;
	private List<String> bookOffer;
	private List<String> bookDemand;
	public BookOwner(){
		
	}
	public BookOwner(String userID){
		this.userID=userID;
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
	// public void addBookOffer(String isbn){
// 		bookOffer.add(isbn);
// 	}
// 	public void addBookDemand(String isbn){
// 		bookDemand.add(isbn);
// 	}
// 	public boolean removeOneBookOffer(String isbn){
// 		return bookOffer.remove(isbn);
// 	}
// 	public boolean removeOneBookDemand(String isbn){
// 		return bookDemand.remove(isbn);
// 	}
	
}

