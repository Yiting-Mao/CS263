package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import exchangebook.OwnerInfo;

@XmlRootElement

public class OwnerInfoForBook extends OwnerInfo {
  private long num;
	public OwnerInfoForBook() {		
	
  }
  
	public OwnerInfoForBook(String userID, String name, String location, long num) {
    super(userID, name, location);
    this.num = num;
	}
  public void setNum(long num) {
     this.num = num;
  }
  public long getNum() {
   return this.num;
  }
  
}

