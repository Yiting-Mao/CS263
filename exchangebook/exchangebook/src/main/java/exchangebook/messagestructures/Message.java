package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement

public class Message {
 private String title;
 private String body;
 private Date date;
 private long messageID;
 public Message() {
   
 } 
 public Message(String title, String body, Date date, long messageID) {
   this.title = title;
   this.body = body;
   this.date = date;
   this.messageID = messageID;
 }

 public String getTitle() {
   return this.title;
 } 
 public String getBody() {
   return this.body;
 }
 
 public Date getDate() {
   return this.date;
 }
 public long getMessageID() {
   return this.messageID;
 }
 public void setTitle(String title) {
   this.title = title;
 }
 
 public void setBody(String body) {
   this.body = body;
 }
 public void setDate(Date date) {
   this.date = date;
 }
 public void setMessageIF(long messageID) {
   this.messageID = messageID;
 }
}