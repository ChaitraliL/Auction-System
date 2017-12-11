/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketauction;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author admin
 */
public class Server 
{
    static private BufferedReader in;
    static private PrintWriter out;
    static private ObjectOutputStream oos;

    public static void main(String[] args) throws Exception {
        System.out.println("The Auction server is running.");
        int userNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new Auctioner(listener.accept(), userNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    /**
     * A private thread to handle auction requests on a particular
     * socket.
     */
    private static class Auctioner extends Thread 
    {
        
        private Socket socket;
        private int userNumber;
        private int userid;
        private Connection conn=null; 
        public Auctioner(Socket socket, int userNumber) 
        {
            this.socket = socket;
            this.userNumber = userNumber;
            log("New connection with user# " + userNumber + " at " + socket);
        }

        void loginAction(String fname,String fpassword)    // server action for login page
        {
           // Connection conn=null;  
            PreparedStatement p= null;  
            ResultSet r=null; 
            try
            {
                        String flag="false";
                        conn=  MysqlConnect.ConnectDB();
                        String sql="select * from registeredmembers";
                        PreparedStatement ps=conn.prepareStatement(sql);
                        ResultSet re=ps.executeQuery(sql);
                        while(re.next())
                        {
                            if( re.getString(1).equals(fname) && re.getString(2).equals(fpassword) )
                            {
                              flag="true";
                              userid = re.getInt(7);
                              break;
                            }
                        }
                out.println(flag);
                if(flag.equals("true"))
                {
                  String uid = String.valueOf(userid); //int to string conversion to send stream using printwriter class object
                  out.println(uid);                    
                }

            }
            catch(Exception e){ }
        }
   
   public void getproductsList(String query) throws IOException
   {
       ArrayList<Product> productsList = new ArrayList<Product>();
       //Connection connection = MysqlConnect.ConnectDB();
       Statement st;
       ResultSet rs; 
       try {
           st = conn.createStatement();
           rs = st.executeQuery(query);
           //Product product;
           while(rs.next())
           {
              Product product = new Product(rs.getInt("PRODUCTID"),rs.getString("PRONAME"),rs.getString("CATEGORY"),rs.getFloat("BASEPRICE"),rs.getFloat("MAXPRICE"),rs.getString("DESCRIPTION"),rs.getInt("USRID"));
               productsList.add(product);
           }
       } catch (Exception e) {
           e.printStackTrace();
           //System.out.println("error1");
       }
      oos.writeObject(productsList); //sending product list
   }
   
   
      public void executeQueries(String query, String message)
   {
       //Connection con = MysqlConnect.ConnectDB();
       Statement st;
       try{
           st = conn.createStatement();
           if((st.executeUpdate(query)) == 1)
           {
              out.println("executed");
           }else
           {
              out.println("notexecuted");
           }
       }catch(Exception ex){ ex.printStackTrace(); }
   }
        public void run() {
            try {

                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                    String username = in.readLine();  
                    String password = in.readLine();
                    loginAction(username,password);
                    
                    String value = in.readLine();
                    if(value.startsWith("home enabled"))
                    {
                        oos  = new ObjectOutputStream(socket.getOutputStream());
                        String Query = "SELECT * FROM  productinfo where USRID="+userid+"";
                        getproductsList(Query);  //function for making product list and sending it to client
                        
                        
                        while(true)
                        {  String querymsg = in.readLine();
                            if(querymsg.equals("Deleted"))
                            {
                               String query = in.readLine();
                               executeQueries(query,"Deleted");
                            }
                            
                            else if(querymsg.equals("Inserted"))
                            {
                               String query = in.readLine();
                               executeQueries(query,"Inserted");
                            }
                            
                            else if(querymsg.equals("Updated"))
                            {
                               String query = in.readLine();
                               executeQueries(query,"Updated");
                            }
                            else if(querymsg.equals("auction"))
                            {
                                break;
                            }
                            getproductsList(Query);
                        }   
                        
                        Query = "SELECT * FROM  productinfo where USRID<>"+userid+"";
                        getproductsList(Query);  
                        while(true)
                        {
                            String querymsg = in.readLine();
                            if(querymsg.equals("remove"))
                            {
                               String query = in.readLine();
                               executeQueries(query,"Deleted");
                            }
                            else if(querymsg.equals("change"))
                            {
                               String query = in.readLine();
                               executeQueries(query,"Updated");
                            }
                            else if(querymsg.equals("exit"))
                            {
                                break;
                            }
                            
                        }
                    }

               
            } catch (IOException e) {
                log("Error handling user# " + userNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                    
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with user# " + userNumber + " closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }
    }    
}
