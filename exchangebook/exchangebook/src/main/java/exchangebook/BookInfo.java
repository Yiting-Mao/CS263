package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON

public class BookInfo{
	private String title;
	private String author;
	private String isbn;
	public BookInfo(){

	}
	public BookInfo(String title, String author, String isbn){
		this.title=title;
		this.author=author;
		this.isbn=isbn;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public String getTitle(){
		return this.title;
	}
	public void setAuthor(String author){
		this.author=author;
	}
	public String getAuthor(){
		return this.author;
	}
	public void setIsbn(String isbn){
		this.isbn=isbn;
	}
	public String getIsbn(){
		return this.isbn;
	}
}
