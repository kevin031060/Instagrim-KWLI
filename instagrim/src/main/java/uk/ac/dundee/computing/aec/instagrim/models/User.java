/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Administrator
 */
@SuppressWarnings("unused")
public class User {
    Cluster cluster;
    public User(){
        
    }
    
    public boolean RegisterUser(String username, String Password,
			String firstname, String lastname, Set<String> email){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        
        try {
            EncodedPassword= AeSimpleSHA1.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrimKWLI");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,first_name,last_name,email) Values(?,?,?,?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username,EncodedPassword,firstname,lastname,email));
        //We are assuming this always works.  Also a transaction would be good here !
        
        return true;
    }
    
    public boolean IsExistedUser(String username){
    	Session session = cluster.connect("instagrimKWLI");
    	PreparedStatement ps = session.prepare("select login from userprofiles where login =?");
    	ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("ok to register");
            return false;
        } else {
        	System.out.println("User name exists");
        	return true;
        }
    }
    public boolean IsValidUser(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= AeSimpleSHA1.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrimKWLI");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
               
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
   
    
    return false;  
    }
    public void ChangePassword(String username, String password){
    	AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= AeSimpleSHA1.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            
        }
        Session session = cluster.connect("instagrimKWLI");
        PreparedStatement ps = session.prepare("update userprofiles SET password=? WHERE login=?");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                		EncodedPassword,username)); 
        
       
    }
    
    public void insertProfile(byte[] b,  String name, String user){
    	ByteBuffer buffer = ByteBuffer.wrap(b);
        int length = b.length;
        Boolean success = (new File("/var/tmp/instagrimKWLI/")).mkdirs();
        FileOutputStream output = null;
		try {
			output = new FileOutputStream(new File("/var/tmp/instagrimKWLI/" + user));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
			output.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Session session = cluster.connect("instagrimKWLI");

        PreparedStatement psInsertPro = session.prepare("update userprofiles SET profile=? WHERE login=?");
        BoundStatement bsInsertPro = new BoundStatement(psInsertPro);
        session.execute(bsInsertPro.bind(buffer,user));
        session.close();
    }
    public String Getfirstname(String username){
    	Session session = cluster.connect("instagrimKWLI");
        PreparedStatement ps = session.prepare("select first_name from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        String Storedfirstname = null;
        if (rs.isExhausted()) {
            System.out.println("No first_name found");
            return null;
        } else {
            for (Row row : rs) {
               
                Storedfirstname = row.getString("first_name");      
               
            }
            
        }
        session.close();
    	return Storedfirstname;
    }
    public String Getlastname(String username){
    	Session session = cluster.connect("instagrimKWLI");
        PreparedStatement ps = session.prepare("select last_name from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        String Storedlastname = null;
        if (rs.isExhausted()) {
            System.out.println("No last_name found");
            return null;
        } else {
            for (Row row : rs) {
               
                Storedlastname = row.getString("last_name");      
               
            }
            
        }
        session.close();
    	return Storedlastname;
    }
    
    public String Getemail(String username){
    	Session session = cluster.connect("instagrimKWLI");
        PreparedStatement ps = session.prepare("select email from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        Set<String> Storedemail = null;
        if (rs.isExhausted()) {
            System.out.println("No email found");
            return null;
        } else {
            for (Row row : rs) {
               
                Storedemail = row.getSet("email", String.class); 
               
            }
            
        }
        session.close();
    	return Storedemail.toString();
    }
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

	

    
}
