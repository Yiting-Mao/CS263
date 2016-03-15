package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement

public class OwnerInfo{
	private String userID;
	private String name;
	private String location;
	public OwnerInfo(){
		
	}
	public OwnerInfo(String userID, String name, String location){
		this.userID=userID;
		this.name=name;
		this.location=location;
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
	public String getUserID() {
	  return this.userID;
	}
  public void setUserID(String userID) {
    this.userID = userID;
  }
}

