package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import exchangebook.OwnerInfo;
import exchangebook.BookInfoForOwner;

@XmlRootElement

public class Owner extends OwnerInfo {
  private List<BookInfoForOwner> bookOffer;
  private List<BookInfoForOwner> bookDemand;
	public Owner() {		
	
  } 
	public Owner(String userID, String name, String location, List<BookInfoForOwner> bookOffer, List<BookInfoForOwner> bookDemand) {
    super(userID, name, location);
    this.bookOffer = bookOffer;
    this.bookDemand = bookDemand;
	}
  public List<BookInfoForOwner> getBookOffer() {
    return bookOffer;
  }
  public List<BookInfoForOwner> getBookDemand() {
    return bookDemand;
  }
	
  public void setBookOffer(List<BookInfoForOwner> bookOffer) {
    this.bookOffer = bookOffer;
  }
  
  public void setBookDemand(List<BookInfoForOwner> bookDemand) {
    this.bookDemand = bookDemand;
  }
}

