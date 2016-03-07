package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement

public class Message {
 private String senderId;
 private String receiverId;
 private String title;
 private String body;
 private Date date;
 private int senderDelete;
 private int receiverDelete;
 public Message() {
   
 } 
 public Message(String sid, String rid, String title, String body, Date date, int sd, int rd) {
   senderId = sid;
   receiverId = rid;
   this.title = title;
   this.body = body;
   this.date = date;
   senderDelete = sd;
   receiverDelete = rd;
 }
   
 public String getSenderId() {
   return this.senderId;
 }
     
 public String getReceiverId() {
   return this.receiverId;
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
 public int getSenderDelete() {
   return this.senderDelete;
 }
     
 public int getReceiverDelete() {
   return this.receiverDelete;
 }
     
 public void setSenderId(String sid) {
   senderId = sid;
 }
     
 public void setReceiverId(String rid) {
   receiverId = rid;
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
 public void setSenderDelete(int sd) {
   senderDelete = sd;
 }
 public void setReceiverDelete(int rd) {
   receiverDelete = rd;
 }
   
}