package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON

public class Book{
	private String title;
	private String author;
	private String isbn;
	private List<String> peopleOffer;
	private List<String> peopleDemand;
	public Book(){

	}
	public Book(String title, String author, String isbn, List<String> peopleOffer, List<String> peopleDemand){
		this.title=title;
		this.author=author;
		this.isbn=isbn;
		this.peopleOffer=peopleOffer;
		this.peopleDemand=peopleDemand;

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

	public void setPeopleOffer(List<String> peopleOffer){
		this.peopleOffer=peopleOffer;
	}
	public List<String> getPeopleOffer(){
		return this.peopleOffer;
	}
	public void setPeopleDemand(List<String> peopleOffer){
		this.peopleDemand=peopleOffer;
	}
	public List<String> getPeopleDemand(){
		return this.peopleDemand;
	}
}
