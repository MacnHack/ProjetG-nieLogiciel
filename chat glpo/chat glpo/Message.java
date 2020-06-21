import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Message {
    
    UUID messageId;
	String text;
	String nom;
	String date;
	
	public Message(String text,String nom){
            
		this.messageId = UUID.randomUUID();
		this.nom = nom ;
		this.text = text;
		this.date = getDateNow();
	}
	public Message (String text,String date,String nom,UUID messageId)
	{
		this.messageId = messageId;
		this.text = text;
		this.date =date;
        this.nom =nom ;
	
	}
	
	public String getDateNow()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
		
		
	}
        public String getTextt()
        {
           return this.text;
        }
        public UUID getId() {
		return this.messageId;
	}
         public String getDate() {
		return this.date;
	}
         public String getNom() {
		return this.nom;
	}
}