package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON

public class Book{
	private String title;
	private String author;
	private String isbn;
	private long offerNum;
	private long demandNum;
	public Book(){

	}
	public Book(String title, String author, String isbn, long offerNum, long demandNum){
		this.title=title;
		this.author=author;
		this.isbn=isbn;
		this.offerNum=offerNum;
		this.demandNum=demandNum;

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
	public void setOwnersNum(long num){
		this.offerNum=num;
	}
	public long getOwnersNum(){
		return this.offerNum;
	}
	public void setDemandNum(long num){
		this.demandNum=num;
	}
	public long getDemandNum(){
		return this.demandNum;
	}
	// public void addOneRecord(){
	// 	this.offerNum++;
	// }
	// public void deleteOneRecord(){
	// 	this.offerNum--;
	// }
}
