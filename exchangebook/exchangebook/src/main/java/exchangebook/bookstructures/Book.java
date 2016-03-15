package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import exchangebook.BookInfo;
import exchangebook.OwnerInfoForBook;

@XmlRootElement
// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON

public class Book extends BookInfo{
  private List<OwnerInfoForBook> peopleOffer;
  private List<OwnerInfoForBook> peopleDemand;
	public Book() {

	}
	public Book(String title, String author, String isbn, List<OwnerInfoForBook> peopleOffer, List<OwnerInfoForBook> peopleDemand) {
    super(title, author, isbn);
    this.peopleOffer = peopleOffer;
    this.peopleDemand = peopleDemand;
	}
  public void setPeopleOffer(List<OwnerInfoForBook> peopleOffer) {
     this.peopleOffer = peopleOffer;
   }
   public List<OwnerInfoForBook> getPeopleOffer(){
     return this.peopleOffer;
   }
   public void setPeopleDemand(List<OwnerInfoForBook> peopleOffer){
     this.peopleDemand = peopleOffer;
   }
   public List<OwnerInfoForBook> getPeopleDemand(){
     return this.peopleDemand;
   }
	
}

