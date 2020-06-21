import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Chats {

    LinkedList<Message> listMessage = new LinkedList<Message>();
    LinkedList<User> listUtilisateur = new LinkedList<User>();
    
    public void addMessage(Message msg)
	{
		listMessage.add(msg);
	}
    public void addUtilisateur(User utl)
	{
		listUtilisateur.add(utl);
	}
    
    public List<Message> getMessageList()
	{	
		LinkedList<Message> copyListMessage = new LinkedList<Message>(listMessage);
		return copyListMessage;
	}
    
    	public Message recherche(UUID id ,List<Message>listMsg)
	{
		for(Message m : listMsg)
		{
			UUID mid = m.getId();
			if(mid.equals(id))
			{
				return m;
			}
		}
		return null;
	}
        
        
public void chargeListMessage()
	{
		XMLMessage demo = new XMLMessage();
		demo.readXML(listMessage);
		//demo.writeXML();
	}

public void addMessageXml(Message m)
	{
		XMLMessage demo = new XMLMessage();
		addMessage(m);
		demo.writeXML(listMessage);
	}
	
        
     public List<User> getUtilisateurList()
	{	
		LinkedList<User> copyListUtilisateur = new LinkedList<User>(listUtilisateur);
		return copyListUtilisateur;
	}
    
    	public boolean check(String login ,String mdp)
	{
		for(User u : this.listUtilisateur)
		{
			String unom = u.getNom();
                        String umdp = u.getMdp();
			if(unom.equals(login))
			{
                            if(umdp.equals(mdp))
                            {
                                return true;
                            }
				
			}
		}
		return false;
	}
        
        
        public void chargeListUtilisateur()
	{
		XMLUtilisateur demo = new XMLUtilisateur();
		demo.readXML(listUtilisateur);
		//demo.writeXML();
	}

        public void addUtilisateurXml(User m)
	{
		XMLUtilisateur demo = new XMLUtilisateur();
		addUtilisateur(m);
		demo.writeXML(listUtilisateur);
	}
}