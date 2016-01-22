package cs263w16;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON
public class TaskData {
  private String keyname;
  private String value;
  private Date date;

  public TaskData(){
  	
  }
  public TaskData(String key, String v, Date d){
	  this.keyname=key;
	  this.value=v;
	  this.date=d;
  }
  public void setKeyname(String key){
	  this.keyname=key;
  }
  public String getKeyname(){
	  return keyname;
  }
  public void setValue(String v){
	  this.value=v;
  }
  public String getValue(){
	  return value;
  }
  public void setDate(Date date){
	  this.date=date;
  }
  public Date getDate(){
	  return date;
  }
  //add constructors (default () and (String,String,Date))
  //add getters and setters for all fields
} 