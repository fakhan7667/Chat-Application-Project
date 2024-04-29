import java.io.*;
import java.net.*;

class Server{

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;
    //Constructor

    public Server(){
        try
        {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept the connection");
            System.out.println("Waiting.....");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

       
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

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
                            System.out.println("Client has Ended the Chat");
                            socket.close();
                            break;
                        }
                        System.out.println("Client : "+msg);
                }
            }catch(Exception e){
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
        System.out.println("This is Server");
        new Server();
        
     }
}