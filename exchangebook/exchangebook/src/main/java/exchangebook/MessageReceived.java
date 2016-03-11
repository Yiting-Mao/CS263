package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement

public class MessageReceived extends Message {
  private String senderId;
  private boolean read;
  public MessageReceived() {
 
  } 
  public MessageReceived(String sid, String title, String body, Date date, long messageID, boolean read) {
    super(title, body, date, messageID);
    senderId = sid;
    this.read = read;
  }
 
  public String getSenderId() {
    return this.senderId;
  }

  public boolean getRead() {
    return this.read;
  }

  public void setSenderId(String sid) {
    senderId = sid;
  }

  public void setRead(boolean read) {
    this.read = read;
  }
}
