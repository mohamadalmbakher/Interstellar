/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author MOHAMAD ALMBAKHER
 */
public class DbConnection {

    private static String dbURL = "jdbc:derby://localhost:1527/interstellarDB";

    private static String username = "interstellarUS";
    private static String password = "123";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        Connection con = null;
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        con = DriverManager.getConnection(dbURL, username, password);

        return con;
    }
 
      public static String getMd5(String input) 
    { 
       return input;
    } 
       public static Subscription[] addX(int n, Subscription arr[], Subscription x)
    {
        int i;
  
        // create a new array of size n+1
        Subscription newarr[] = new Subscription[n + 1];
  
        // insert the elements from
        // the old array into the new array
        // insert all elements till n
        // then insert x at n+1
        for (i = 0; i < n; i++)
            newarr[i] = arr[i];
  
        newarr[n] = x;
  
        return newarr;
    }
    
}



