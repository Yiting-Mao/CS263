package exchangebook;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement

public class MessageReceived extends Message {
  private String senderID;
  private boolean read;
  public MessageReceived() {
 
  } 
  public MessageReceived(String sid, String title, String body, Date date, long messageID, boolean read) {
    super(title, body, date, messageID);
    senderID = sid;
    this.read = read;
  }
 
  public String getSenderID() {
    return this.senderID;
  }

  public boolean getRead() {
    return this.read;
  }

  public void setSenderID(String sid) {
    senderID = sid;
  }

  public void setRead(boolean read) {
    this.read = read;
  }
}
