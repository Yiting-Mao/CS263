package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

import exchangebook.Message;

@XmlRootElement

public class MessageSent extends Message{
 private String receiverId;
 public MessageSent() {
   
 } 
 public MessageSent(String rid, String title, String body, Date date, long messageID) {
   super(title, body, date, messageID);
   receiverId = rid;
 }  
 public String getReceiverId() {
   return this.receiverId;
 }

 public void setReceiverId(String rid) {
   receiverId = rid;
 }
}