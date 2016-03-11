package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import exchangebook.BookInfo;

@XmlRootElement
// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON

public class BookInfoForOwner extends BookInfo{
  private long num; 
	public BookInfoForOwner() {

	}
	public BookInfoForOwner(String title, String author, String isbn, long num) {
    super(title, author, isbn);
    this.num = num;
	}
  public void setNum(long num) {
     this.num = num;
  }
  public long getNum() {
   return this.num;
  }
	
}

