package xchat;

import java.awt.image.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import javax.swing.border.LineBorder;

public class XChatScreen extends javax.swing.JFrame implements Runnable{
    
    //Attributes of class XChatScreen
    String mode;
    Socket sock;
    String my_name = "Server";
    String his_name = "Client";
    Font font_cur = new Font("Arial",Font.PLAIN,13);
    DataOutputStream out;
    DataInputStream in;
    int loc = 10;
    ArrayList<JLabel> list;
    String path_save_download = "D:\\";
    String path_save_chat = "D:\\";
    Color font_color_cur = Color.BLACK;
    Color bg_color = Color.WHITE;
    Boolean notification = true;
    private Options option;
    /**
     * Creates new form XChatScreen
     */
    private void add_send(String msg){
        JLabel l = new  JLabel();
        list.add(l);
        l.setFont(font_cur);
        l.setForeground(font_color_cur);
        l.setText(String.format("<html><p style=\"width:305px;word-warp:break-word;\">" + msg + "</p></html>"));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel1Layout);
        jScrollPane3.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane3.setAutoscrolls(true);
        try{
        
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(5, 5, 5)
                .addComponent(l))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(loc-10, loc, loc+10)
                .addComponent(l))
        );
        }catch(Exception e){
            
        }
         loc +=l.getMaximumSize().height + 10;
        jScrollPane3.revalidate();
        jPanel2.repaint();
        JScrollBar vertical = jScrollPane3.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() + 30); 
    }
    
    private void add_rec(String received){
        JLabel l = new  JLabel();
        list.add(l);
        l.setFont(font_cur);
        l.setForeground(font_color_cur);
        l.setText(String.format("<html><p style=\"width:285px;word-warp:break-word;text-align:right\">" + received + "</p></html>"));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel1Layout);
        jScrollPane3.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane3.setAutoscrolls(true);
        
        try{
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30,30)
                .addComponent(l))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(loc-10, loc, loc+10)
                .addComponent(l))
        );
        }catch(Exception e){
            
        }
        loc +=l.getMaximumSize().height + 10;
        jScrollPane3.revalidate();
        jPanel2.repaint();
        JScrollBar vertical = jScrollPane3.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() + 30);
    }
    public XChatScreen(){
    }
    public void setMyName(String my_name){  
       this.my_name = my_name;
       change_name();
    }
    public void setHisName(String name){
        if(this.his_name.equals("Client")){
            this.his_name = name;
            populate();
        }
        else
            this.his_name = name;
        NAME.setText(his_name);
    }  
    public void setFontColor(Color color){
        Iterator<JLabel> i = list.iterator();
            while(i.hasNext()){
               i.next().setForeground(color);
            }
            jPanel2.setForeground(color);
            font_color_cur = color;
    }
    public void setChatTheme(Color color){
        jPanel2.setBackground(color);
        bg_color = color;
    }
    public void setChatFont(Font font){
           Iterator<JLabel> i = list.iterator();
            while(i.hasNext()){
               i.next().setFont(font);
            }
            jEditorPane1.setFont(font);
            font_cur = font;
    }
    public void setDownloadPath(String download_path){
        this.path_save_download = download_path;
    }
    public void setBackupPath(String backup_path){
    
        this.path_save_chat = backup_path;
    }
    public void change_name(){
        try {
            out.writeUTF("~!!NAME!!~" + my_name);
        } catch (IOException ex) {
            Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public XChatScreen(String mode,Socket sock) {
        PrintWriter writer = null; 
        this.my_name = JOptionPane.showInputDialog("Enter your user name");
        this.font_cur = new Font("Arial",Font.PLAIN,13);
        this.path_save_download = "D:\\";
        this.path_save_chat = "D:\\";
        this.font_color_cur = Color.BLACK;
        this.bg_color = Color.WHITE;
        this.notification = true;
        option = new Options(this);
        this.mode = mode;
        this.sock = sock;
        try {
            out = new DataOutputStream(this.sock.getOutputStream());
            in = new DataInputStream(this.sock.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        list = new ArrayList<>();
        initComponents();
        try {
            writer = new PrintWriter("temp.txt");
            writer.print("");
        } catch (FileNotFoundException ex) {
        } finally {
        writer.close();
        }
        this.setMyName(my_name);
    }
    private void addToFile(String msg){
        File file;
        file=new File("temp.txt");
        FileWriter fw;
        BufferedWriter buffer = null;
        
        try {
            if(!file.exists())
                file.createNewFile();
            fw=new FileWriter(file,true);
          buffer=new BufferedWriter(fw);
           buffer.write(msg+ "\r\n");
        } catch (IOException ex) {
            Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try {
                buffer.close();
            } catch (IOException ex) {
                Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void backup(){
      FileInputStream fin = null;
      FileOutputStream fout = null;
      int c;
      
            try
            {
                fin = new FileInputStream("temp.txt");
                fout = new FileOutputStream(path_save_chat+ his_name+".txt");
                while ((c = fin.read()) != -1) {
                    fout.write(c);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
      finally {
         if (fin != null) {
             try {
                 fin.close();
             } catch (IOException ex) {
                 Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         if (fout != null) {
             try {
                 fout.close();
             } catch (IOException ex) {
                 Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
      }
    }
    private void populate(){
        System.out.println("file name = " + his_name);
        File file = new File(path_save_chat+ his_name+".txt");
        if(!file.exists())
            return;
        System.out.println("In the chat section");
        BufferedReader br =null;
        try {
            br = new BufferedReader(new FileReader(path_save_chat+ his_name+".txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains("his~")){
                    line = line.substring(line.indexOf("~")+1);
                    
                    add_rec(line);
                }
                else if(line.contains("my~")){
                    line = line.substring(line.indexOf("~")+1);
                    
                    add_send(line);
                }
                
            }
        }
        catch(EOFException e){
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code"> 
    private void initComponents() {
        
        NAME = new javax.swing.JLabel();
        NAME.setSize(100,100);
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        
        this.setChatTheme(this.bg_color);
        this.setFontColor(this.font_color_cur);
        this.setChatFont(font_cur);
        this.setDownloadPath("D:\\");
        this.setBackupPath("D:\\");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowEventHandler());
        NAME.setFont(new java.awt.Font("Oxygen-Sans Sans-Book", 1, 18)); // NOI18N
        NAME.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jEditorPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jEditorPane1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jEditorPane1);
        

        jButton1.setForeground(new java.awt.Color(240, 240, 240));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/xchat/ic_send_48px-128.png"))); // NOI18N
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setOpaque(false);
        jButton1.setBorder( new LineBorder(Color.BLACK) );
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Drag");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("And");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Drop");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Files");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Here");

        javax.swing.ImageIcon bg = new javax.swing.ImageIcon(getClass().getResource("/xchat/Gear-2-icon.png"));
        
        jButton2.setMaximumSize(new Dimension(bg.getIconWidth(),bg.getIconHeight()));
        jButton2.setSize(bg.getIconWidth(),bg.getIconHeight());
        jButton2.setIcon(bg); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5, jLabel6});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5, jLabel6});

        jPanel2.setMaximumSize(new java.awt.Dimension(327, 327));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 416, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NAME, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NAME, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    new FileDrop( System.out, jPanel1, /*dragBorder,*/ new FileDrop.Listener()
            {   public void filesDropped( java.io.File[] files )
                {   for( int i = 0; i < files.length; i++ )
                    {   
                        try {
                             byte[] b = new byte[(int) files[i].length()];
                             try {
                                   FileInputStream fileInputStream = new FileInputStream(files[i]);
                                   fileInputStream.read(b);
                                   fileInputStream.close();
                              } catch (FileNotFoundException e) {
                                          System.out.println("File Not Found.");
                                          e.printStackTrace();
                              }
                              catch (IOException e1) {
                                       System.out.println("Error Reading The File.");
                                        e1.printStackTrace();
                              } int pos =files[i].getAbsolutePath().lastIndexOf("\\");
                            out.writeUTF("~!!FILE!!~"+ b.length + "~" +  files[i].getAbsolutePath().substring(pos+1));
                            out.write(b);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }   
                }   
            });
        pack();
    }// </editor-fold>  
    private void Dialog(){

        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "Do you want a backup?", "Backup", dialogButton);
    
        if(dialogResult == 0) {
            backup(); 
        } 
        else 
            return;
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String msg = this.jEditorPane1.getText();
        msg = msg.replaceAll("(\r\n)+", "");
        if (msg.equals("") || msg.equals("\r\n"))
            return;
        try {
        out.writeUTF(msg);
        //set text
        add_send(msg);
        addToFile("my~"+msg);
     } catch (IOException ex) {
         JOptionPane.showMessageDialog(rootPane, "Connection aborted by User");
         Dialog();
         System.exit(5);
     }
    jEditorPane1.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed
  
    private void jEditorPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jEditorPane1KeyPressed
        // TODO add your handling code here:
        String msg = this.jEditorPane1.getText();
        msg = msg.replaceAll("(\r\n)+", "");
        evt.consume();
        if (evt.getKeyCode()==KeyEvent.VK_ENTER && !msg.equals("") && !msg.equals("\r\n"))
        {
           try {
                // TODO add your handling code here:
                
                out.writeUTF(msg);
                    //set text
                add_send(msg);
                addToFile("my~"+msg);
            } catch (IOException ex) {
                //Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(rootPane, "Connection aborted by User");
                Dialog();
                System.exit(5);
            }
           jEditorPane1.setText("");
           jEditorPane1.setCaretPosition(0);
        }
    }//GEN-LAST:event_jEditorPane1KeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        option.setLocation(100, 100);
        option.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(XChatScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(XChatScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(XChatScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(XChatScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
    }
    private class WindowEventHandler extends WindowAdapter {
        public void windowClosing(WindowEvent evt) {
          Dialog();
        }
      }
    private void setProfileImage(File imgsrc){
        try {
            BufferedImage image = ImageIO.read(imgsrc);
            Image resized = image.getScaledInstance(jButton1.getWidth(), jButton1.getHeight(), Image.SCALE_SMOOTH);
            jButton1.setIcon(new ImageIcon(resized));
        } catch (IOException ex) {
            Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
 @Override
    public void run(){
        while(true){
            String received;
            try {
                received = in.readUTF();
                if(received != null && !received.equals("") && !received.equals("\r\n")){
                    System.out.println(received);
                    if(received.contains("~!!FILE!!~")){
                        if(notification != false)
                            Toolkit.getDefaultToolkit().beep();
                        String filename = received;
                        received = received.replaceAll("[^0-9]+", " "); 
                        received = received.trim();
                        int length;
                        if(received.indexOf(" ") <= 0)
                            length = (int)Integer.parseInt(received.trim());                            
                        else
                            length = (int)Integer.parseInt(received.substring(0,received.indexOf(" ")).trim());
                        byte[] message = new byte[length];
                        try {
                            if(length > 0)
                                in.readFully(message, 0, message.length);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(rootPane, "File transfer aborted");
                            e.printStackTrace();
                        }
                        int pos =filename.lastIndexOf("~");
                        filename = filename.substring(pos+1);
                        int dialogResult = JOptionPane.showConfirmDialog (null, "You are currently recieving a file ( " + filename +" ) .Would you like to save it?" ,"Warning",JOptionPane.YES_NO_OPTION);
                        if(dialogResult == JOptionPane.YES_OPTION){
                            try{ 
                                FileOutputStream fileOutputStream = new FileOutputStream(path_save_download + filename);
                                fileOutputStream.write(message);
                                fileOutputStream.close();
                                
                                
                                JLabel l = new  JLabel();
                                list.add(l);
                                l.setFont(font_cur);
                                l.setForeground(font_color_cur);
                                l.setText(String.format("<html><p style=\"width:285px;word-warp:break-word;text-align:right\">" + filename + "</p></html>"));
                                l.addMouseListener(new MouseListener()
                                {
                                public void mouseClicked(MouseEvent arg0) {
                                    try {
                                        Runtime.getRuntime().exec("explorer.exe /root," + path_save_download );
                                    } catch (IOException ex) {
                                        Logger.getLogger(XChatScreen.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                public void mouseEntered(MouseEvent arg0) {
                                }
                                public void mouseExited(MouseEvent arg0) {
                                }
                                public void mousePressed(MouseEvent arg0) {
                                }
                                public void mouseReleased(MouseEvent arg0) {
                                }
                                });

                                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel2);
                                jPanel2.setLayout(jPanel1Layout);
                                jScrollPane3.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
                                jScrollPane3.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
                                jScrollPane3.setAutoscrolls(true);

                                jPanel1Layout.setHorizontalGroup(
                                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30,30)
                                        .addComponent(l))
                                );
                                jPanel1Layout.setVerticalGroup(
                                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(loc-10, loc, loc+10)
                                        .addComponent(l))
                                );
                                loc +=l.getMaximumSize().height + 10;
                                jScrollPane3.revalidate();
                                jPanel2.repaint();
                                JScrollBar vertical = jScrollPane3.getVerticalScrollBar();
                                vertical.setValue( vertical.getMaximum() + 30);
                            }
                            catch(IOException ex){
                                 JOptionPane.showMessageDialog(rootPane, "File transfer aborted");
                                 ex.printStackTrace();
                            }
                        }
                    }
                    else if(received.contains("~!!NAME!!~")){
                        setHisName(received.substring(10));
                    }
                    else if(received.contains("~!!IMG!!~")){
                        String filename = received;
                        received = received.replaceAll("[^0-9]+", " "); 
                        received = received.trim();
                        int length;
                        if(received.indexOf(" ") <= 0)
                            length = (int)Integer.parseInt(received.trim());                            
                        else
                            length = (int)Integer.parseInt(received.substring(0,received.indexOf(" ")).trim());
                        byte[] message = new byte[length];
                        try {
                            if(length > 0)
                                in.readFully(message, 0, message.length);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(rootPane, "Profile image transfer aborted");
                            e.printStackTrace();
                        }
                        int pos =filename.lastIndexOf("~");
                        filename = filename.substring(pos+1);
                        FileOutputStream fileOutputStream = new FileOutputStream("profile"+filename.substring(filename.indexOf(".")));
                        fileOutputStream.write(message);
                        fileOutputStream.close();
                        File imgsrc = new File("profile"+filename.substring(filename.indexOf(".")));
                        setProfileImage(imgsrc);
                    }
                    else{
                        if(notification != false)
                            Toolkit.getDefaultToolkit().beep();
                        add_rec(received);
                        addToFile("his~"+received);
                    }
                }
            }catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Connection aborted by User");
                Dialog();
                System.exit(5);
            }
            
                
        }
    }
    
    
    
    public void sendImage(File imgsrc) throws IOException{
    try {
            byte[] b = new byte[(int) imgsrc.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(imgsrc);
                fileInputStream.read(b);
                fileInputStream.close();
            }catch(NullPointerException e){
                e.printStackTrace(); //change
            } 
            catch (FileNotFoundException e) {
                       System.out.println("File Not Found.");
                       e.printStackTrace();
            }
            catch (IOException e1) {
                    System.out.println("Error Reading The File.");
                     e1.printStackTrace();
            } int pos =imgsrc.getAbsolutePath().lastIndexOf("\\");
            out.writeUTF("~!!IMG!!~"+ b.length + "~" +  imgsrc.getAbsolutePath().substring(pos+1));
            out.write(b);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel NAME;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
