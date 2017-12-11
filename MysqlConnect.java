package socketauction; 
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */

import java.sql.*;
import javax.swing.JOptionPane;
public class MysqlConnect {
    Connection con=null;
    public static Connection ConnectDB()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/auctiondb","root","98CH#@aitNiK");
            //JOptionPane.showMessageDialog(null,"connected to DB");
            return con;
        }
        catch(Exception e)
                {
                    
                    JOptionPane.showMessageDialog(null,e);
                    return null;
                }
    }
}
