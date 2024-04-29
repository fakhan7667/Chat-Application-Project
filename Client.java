import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import java.awt.event.KeyListener;



public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //Declare Component
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

     

    public Client(){
        try {
            System.out.println("Sending Request to Server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Connnection Done");

             br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvent();
            startReading();
            // startWriting();
           

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    private void handleEvent() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                //throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
               //System.out.println("Key Released "+e.getKeyCode());
               if(e.getKeyCode() == 10){
                    String tosend = messageInput.getText();
                    messageArea.append("Me : "+tosend+"\n");
                    out.println(tosend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
               }
            }
            
        });
    }
    public void createGUI(){
        //gui code.....
        this.setTitle("Client Messanger");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Code for Components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageArea.setEditable(false);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20 ));
        //Setting Frame Layout
        this.setLayout(new BorderLayout());
        // Adding the Component to the frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jscroll = new JScrollPane(messageArea);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);
    }
    public void startReading(){
        //Thread - Read karke print karega
        Runnable r1=()->{
                System.out.println("Reader is Active");
                try{
                    while(true){
                        
                        String msg = br.readLine();
                        if(msg.equals("Exit"))
                        {
                            System.out.println("Server has Ended the Chat");
                            JOptionPane.showMessageDialog(this, "server terminated the chat");
                            messageInput.setEnabled(false);
                            socket.close();
                            break;
                        }
                        //System.out.println("Server : "+msg);
                        messageArea.append("Server : "+msg+"\n");
                    
                } 
            }catch (Exception e) {
                    
                e.printStackTrace();
            }      
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        //thread - User Data padhega aur Data send karega Client tak
        Runnable r2=()->{
            System.out.println("Client is Active");
            try{
            while(!socket.isClosed())
            {
                
                    BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
                    String str = read.readLine();
                    out.println(str);
                    out.flush();

                    if(str.equals("Exit")){
                        socket.close();
                        break;
                    }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        };

        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("This is Client");
        new Client();
    }
}
