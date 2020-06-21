import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class ClientGui extends Thread{

  final JTextPane jtextDiscu = new JTextPane();
  final JTextPane jtextUsers = new JTextPane();
  final JTextField jtextChat = new JTextField();
  private String oldMsg = "";
  private Thread read;
  private String serverName;
  private int PORT;
  private String name;
  BufferedReader input;
  PrintWriter output;
  Socket server;
  Message msgCourant;
  Chats cht = new Chats();


  public ClientGui() 
  {
	  
    this.serverName = "localhost";
    this.PORT = 12345;
    this.name = "login";

    String fontfamily = "Arial, sans-serif";
    Font font = new Font(fontfamily, Font.PLAIN, 15);

    final JFrame jfr = new JFrame("Chat");
    jfr.getContentPane().setLayout(null);
    jfr.setSize(700, 500);
    jfr.setResizable(false);
    jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   
    // Module du fil de discussion
    jtextDiscu.setBounds(520, 25, 490, 320);
    jtextDiscu.setFont(font);
    jtextDiscu.setMargin(new Insets(6, 6, 6, 6));
    jtextDiscu.setEditable(false);
    JScrollPane jtextFilDiscuSP = new JScrollPane(jtextDiscu);
    jtextFilDiscuSP.setBounds(190, 25, 490, 320);

    jtextDiscu.setContentType("text/html");
    jtextDiscu.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    // Module de la liste des utilisateurs
    jtextUsers.setBounds(25, 25, 490, 320);
    jtextUsers.setEditable(true);
    jtextUsers.setFont(font);
    jtextUsers.setMargin(new Insets(6, 6, 6, 6));
    jtextUsers.setEditable(false);
    JScrollPane jsplistuser = new JScrollPane(jtextUsers);
    jsplistuser.setBounds(25, 25, 156, 320);

    jtextUsers.setContentType("text/html");
    jtextUsers.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    // Champ Msg
    jtextChat.setBounds(0, 350, 400, 50);
    jtextChat.setFont(font);
    jtextChat.setMargin(new Insets(6, 6, 6, 6));
    final JScrollPane jtextInputChatSP = new JScrollPane(jtextChat);
    jtextInputChatSP.setBounds(25, 350, 650, 50);

    // button envoyer
    final JButton jsbtn = new JButton("Envoyer");
    jsbtn.setFont(font);
    jsbtn.setBounds(575, 410, 100, 35);

    // button Deco
    final JButton jsbtndeco = new JButton("Deconnecter");
    jsbtndeco.setFont(font);
    jsbtndeco.setBounds(25, 410, 130, 35);

    jtextChat.addKeyListener(new KeyAdapter() 
    {
      // Entrer
      public void keyPressed(KeyEvent e) 
      {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) 
        {
          sendMessage();
        }
      }
    });

    // Envoyer
    jsbtn.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent ae) 
      {
        sendMessage();
      }
    });

    // Vue Connection
    final JTextField jtfName = new JTextField(this.name);
    final JTextField jtfport = new JTextField(Integer.toString(this.PORT));
    final JTextField jtfAddr = new JTextField(this.serverName);
    final JTextField jtfMdp = new JTextField();
    final JButton btnConnect = new JButton("Connect");
    JLabel lblAddr = new JLabel("Adresse : ");
    JLabel lblNom = new JLabel("Nom : ");
    JLabel lblPort = new JLabel("Port : ");
    JLabel lblMdp = new JLabel("Mot de passe : ");

    // Check
    jtfName.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr,jtfMdp, btnConnect));
    jtfport.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr,jtfMdp, btnConnect));
    jtfAddr.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr,jtfMdp, btnConnect));

    // position des Modules
    btnConnect.setFont(font);
    jtfAddr.setBounds(250, 75, 135, 40);
    jtfName.setBounds(250, 145, 135, 40);
    jtfport.setBounds(250, 215, 135, 40);
    jtfMdp.setBounds(250, 285, 135, 40);
    btnConnect.setBounds(270, 400, 100, 40);
    lblAddr.setBounds(80,35,100,100);
    lblNom.setBounds(80,115,100,100);
    lblPort.setBounds(80,185,100,100);
    lblMdp.setBounds(80,250,100,100);

    
    jtextDiscu.setBackground(Color.LIGHT_GRAY);
    jtextUsers.setBackground(Color.LIGHT_GRAY);

    jfr.add(btnConnect);
    jfr.add(jtfName);
    jfr.add(jtfport);
    jfr.add(jtfAddr);
    jfr.add(jtfMdp);
    jfr.add(lblAddr);
    jfr.add(lblNom);
    jfr.add(lblPort);
    jfr.add(lblMdp);
    jfr.setVisible(true);

    appendToPane(jtextDiscu, "<h1>Bienvenue dans ESIEA LIVE Chats</h1>"
        +"<ul>"
        +"<li>Envoyez vos messages au différents utilisateurs <b>En Ligne </b></li>"
        +"<li>Pour envoyer un msg en privé : @'le pseudo que vous desirez'</li>"
        +"</ul><br/>");

    //Listener button connection
    btnConnect.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent ae) 
      {
    	cht.chargeListUtilisateur();
    	String mdpp = jtfMdp.getText();
      	name = jtfName.getText();
      	boolean t  = cht.check(name,mdpp);
      	if(t==true)
      	{           
			try 
			{               
				String port = jtfport.getText();
	             serverName = jtfAddr.getText();
	             PORT = Integer.parseInt(port);
	
	             appendToPane(jtextDiscu, "<span>Connecting to " + serverName + " on port " + PORT + "...</span>");
	             server = new Socket(serverName, PORT);
	
	             appendToPane(jtextDiscu, "<span>Connected to " +server.getRemoteSocketAddress()+"</span>");
	             appendToPane(jtextDiscu, "<span><b>Ancien Messages : </b> </span>");
	             cht.chargeListMessage();
	             LinkedList<Message> currentlistMsg = (LinkedList<Message>) cht.getMessageList();
	             String msgt;
	             String msgd;
	             String msgn;
	          
	             for(Message m : currentlistMsg)
	             {
	   		
	                    msgt= m.getTextt();
	                    msgd = m.getDate();
	                    msgn = m.getNom();
	                    appendToPane(jtextDiscu,"<span>"+msgn+" :  "+msgt+"       "+msgd+"</span>");   
	             }  
	
	             input = new BufferedReader(new InputStreamReader(server.getInputStream()));
	             output = new PrintWriter(server.getOutputStream(), true);
	
	             output.println(name);
	
	             // creation threads
	             read = new Read();
	             read.start();
	             jfr.add(jtextFilDiscuSP);
	             jfr.add(jsplistuser);
	             jfr.remove(jtfName);
	             jfr.remove(jtfport);
	             jfr.remove(jtfAddr);
	             jfr.remove(jtfMdp);
	             jfr.remove(lblAddr);
	             jfr.remove(lblNom);
	             jfr.remove(lblPort);
	             jfr.remove(lblMdp);
	             jfr.remove(btnConnect);
	             jfr.add(jsbtn);
	             jfr.add(jtextInputChatSP);
	             jfr.add(jsbtndeco);
	             jfr.revalidate();
	             jfr.repaint();
	             jtextDiscu.setBackground(Color.WHITE);
	             jtextUsers.setBackground(Color.WHITE);
	           } 
	           catch (Exception ex) 
	           {
	             appendToPane(jtextDiscu, "<span>Could not connect to Server</span>");
	             JOptionPane.showMessageDialog(jfr, ex.getMessage());
	           }
	    	   
        	   
           }
           else{
               System.out.print("Erreur vérifié vos identifiants");
           }
  		 
    	
      }

    });
    //Listener button deco
    jsbtndeco.addActionListener(new ActionListener()  
    {
      public void actionPerformed(ActionEvent ae) 
      {
        jfr.add(jtfName);
        jfr.add(jtfport);
        jfr.add(jtfAddr);
        jfr.add(btnConnect);
        jfr.remove(jsbtn);
        jfr.remove(jtextInputChatSP);
        jfr.remove(jsbtndeco);
        jfr.revalidate();
        jfr.repaint();
        read.interrupt();
        jtextUsers.setText(null);
        jtextDiscu.setBackground(Color.LIGHT_GRAY);
        jtextUsers.setBackground(Color.LIGHT_GRAY);
        appendToPane(jtextDiscu, "<span>Connection closed.</span>");
        output.close();
      }
    });

  }

  public class TextListener implements DocumentListener
  {
    JTextField jtf1;
    JTextField jtf2;
    JTextField jtf3;
    JTextField jtf4;
    JButton jcbtn;

    public TextListener(JTextField jtf1, JTextField jtf2, JTextField jtf3, JTextField jtf4,JButton jcbtn)
    {
      this.jtf1 = jtf1;
      this.jtf2 = jtf2;
      this.jtf3 = jtf3;
      this.jtf4 = jtf4;
      this.jcbtn = jcbtn;
    }

    public void changedUpdate(DocumentEvent e) {}

    public void removeUpdate(DocumentEvent e) 
    {
      if(jtf1.getText().trim().equals("") || jtf2.getText().trim().equals("") || jtf3.getText().trim().equals("") || jtf4.getText().trim().equals(""))
      {
        jcbtn.setEnabled(false);
      }
      else
      {
        jcbtn.setEnabled(true);
      }
    }
    public void insertUpdate(DocumentEvent e) {
      if(jtf1.getText().trim().equals("") || jtf2.getText().trim().equals("") || jtf3.getText().trim().equals("") || jtf4.getText().trim().equals(""))
      {
        jcbtn.setEnabled(false);
      }
      else
      {
        jcbtn.setEnabled(true);
      }
    }

  }

  // envoi des messages
  public void sendMessage()
  {
    try 
    {
      String message = jtextChat.getText().trim();
      if (message.equals("")) 
      {
        return;
      }
      this.oldMsg = message;
      output.println(message);
      msgCourant = new Message(message,name);
      cht.addMessageXml(msgCourant);
      jtextChat.requestFocus();
      jtextChat.setText(null);
    } 
    catch (Exception ex) 
    {
      JOptionPane.showMessageDialog(null, ex.getMessage());
      System.exit(0);
    }
  }

  public static void main(String[] args) throws Exception
  {
    ClientGui client = new ClientGui();
  }

  // lecture nouveau messages
  class Read extends Thread
  {
    public void run() 
    {
      String message;
      while(!Thread.currentThread().isInterrupted())
      {
        try 
        {
          message = input.readLine();
          if(message != null)
          {
            if (message.charAt(0) == '[') 
            {
              message = message.substring(1, message.length()-1);
              ArrayList<String> ListUser = new ArrayList<String>(Arrays.asList(message.split(", ")));
              jtextUsers.setText(null);
              for (String user : ListUser) 
              {
                appendToPane(jtextUsers, "@" + user);
              }
            }
            else
            {
              appendToPane(jtextDiscu, message);
            }
          }
        }
        catch (IOException ex) 
        {
          System.err.println("Failed to parse incoming message");
        }
      }
    }
  }

  // envoyer html au pane
  private void appendToPane(JTextPane tp, String msg)
  {
    HTMLDocument doc = (HTMLDocument)tp.getDocument();
    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
    try
    {
      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
      tp.setCaretPosition(doc.getLength());
    } 
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}
