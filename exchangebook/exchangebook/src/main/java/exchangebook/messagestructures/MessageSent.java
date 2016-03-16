package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

import exchangebook.Message;

@XmlRootElement

public class MessageSent extends Message{
 private String receiverID;
 public MessageSent() {
   
 } 
 public MessageSent(String rid, String title, String body, Date date, long messageID) {
   super(title, body, date, messageID);
   receiverID = rid;
 }  
 public String getReceiverID() {
   return this.receiverID;
 }

 public void setReceiverID(String rid) {
   receiverID = rid;
 }
}