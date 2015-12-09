/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xchat;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author adithya
 */
public class XChatClient {
    Socket socket;
    String IPv4;
    int Port;
    private static final Pattern IPv4c = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
   
    XChatClient(){
        IPv4 = JOptionPane.showInputDialog("Enter IP address of the Server");
        try{
        while(!IPv4c.matcher(IPv4).matches()){
            IPv4 = JOptionPane.showInputDialog("ERROR:Try Again. Enter IP address of the Server");
        }
        if(IPv4==null)
        {
            JOptionPane.showMessageDialog(null,"Thank you for using XChat");
            System.exit(4);
        }
         
        String port = JOptionPane.showInputDialog("Enter the Port Number [Hint:Default port = 8080]");
        if("".equals(port)) //edited
                port = "8080";//edited
        String regex="\\d+";
        while(true){
        if(port.matches(regex))
        {
           if(Integer.parseInt(port)<0 || Integer.parseInt(port)>65535 )
            {
                port=JOptionPane.showInputDialog("<html> <b>INVALID PORT</b> : Enter Port Number [Hint:Default port = 8080]</html>");
            }
           else
               break;
        }
        else
        {
          port=JOptionPane.showInputDialog("<html> <b>INVALID PORT</b> : Enter Port Number [Hint:Default port = 8080]</html>");
        }
        }
        Port = Integer.parseInt(port);
        }
       catch(java.lang.NullPointerException ex){
            JOptionPane.showMessageDialog(null,"Thank you for using XChat");
            System.exit(4);
        }
        catch(java.lang.NumberFormatException ex){
            JOptionPane.showMessageDialog(null,"Thank you for using XChat");
            System.exit(4);
        }
        try {
            socket = new Socket(IPv4,Port);
        } 
         catch(java.lang.NullPointerException ex){
            JOptionPane.showMessageDialog(null,"Thank you for using XChat");
            System.exit(4);
        }
        catch (IOException ex) {
            //Logger.getLogger(XChatClient.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Unable to Open Socket. [Hint: Please ensure the Server is on and running.]");
            System.exit(3);
        }
        XChatScreen xchat = new XChatScreen("Client",socket);
        xchat.setTitle("XChat:Connected");
        xchat.setLocation(600,400);
        xchat.setResizable(false);
        xchat.setVisible(true);
        new Thread(xchat).start();
    }
}
