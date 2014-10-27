package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import static org.imgscalr.Scalr.*;

import org.imgscalr.Scalr.Method;

import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    Cluster cluster;

    public PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void deletePic(java.util.UUID picid){
    
    	
    	try{	
    		Session session =cluster.connect("instagrimKWLI");
        	
        	
        	PreparedStatement selectUser =session.prepare("select user from pics where picid=?");
        	PreparedStatement selectDate =session.prepare("select interaction_time from pics where picid=?");
            BoundStatement bselectUser = new BoundStatement(selectUser);        	
        	BoundStatement bselectDate = new BoundStatement(selectDate);
        	ResultSet sU=null;
        	ResultSet sD=null;
        	String user=null;
        	Date pic_added=null;
        	sU = session.execute(bselectUser.bind(picid));        	
            if (sU.isExhausted()) {
                System.out.println("No user found in the pics");
            } else {
                for (Row row : sU) {
                	user=row.getString("user");
                	System.out.println(user);
                }
            }
            sD = session.execute(bselectDate.bind(picid));
            if (sD.isExhausted()) {
                System.out.println("No interactiontime found in the pics");
            } else {
                for (Row row : sD) {
                	pic_added=row.getDate("interaction_time");
                	System.out.println(pic_added.toString());
                }
            }    
        	PreparedStatement psDeletePicList = session.prepare("delete from userpiclist where user=? and pic_added=?");
        	
        	PreparedStatement psDeletePic = session.prepare("delete from pics where picid=?");
        	
        	BoundStatement bsDeletePic = new BoundStatement(psDeletePic);
        	
        	BoundStatement bsDeletePicList = new BoundStatement(psDeletePicList);
        	
        	session.execute(bsDeletePic.bind(picid));
        	
        	session.execute(bsDeletePicList.bind(user,pic_added));
        	
        	session.close();
    	} catch (Exception ex) {
            System.out.println("Error --> " + ex);
    	}
          
    }
          
    
    public void insertCom(java.util.UUID picid, String comment, String name){
    	try{
    		Session session =cluster.connect("instagrimKWLI");
    		Date dateadded=new Date();
        	PreparedStatement psInsertCom = session.prepare("insert into comments (picid, comment ,Addedtime ,commenter) values(?,?,?,?)");
        	BoundStatement bsInsertCom = new BoundStatement(psInsertCom);
        	session.execute(bsInsertCom.bind(picid,comment,dateadded,name));
        	session.close();
    	} catch (Exception ex) {
    		System.out.println("Error --> " + ex);
    	}
    }
    public void insertPic(byte[] b, String type, String name, String user) {
        try {
           
			Convertors convertor = new Convertors();

            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = Convertors.getTimeUUID();
            
            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            @SuppressWarnings("unused")
			Boolean success = (new File("/var/tmp/instagrimKWLI/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrimKWLI/" + picid));

            output.write(b);
            output.close();                // close it
            byte []  thumbb = picresize(picid.toString(),types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(),types[1]);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instagrimKWLI");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( user, picid, interaction_time, image, thumb, processed, imagelength, thumblength, processedlength, type, name) values(?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            
            Date DateAdded=new Date();
            
           
            session.execute(bsInsertPic.bind(user, picid, DateAdded, buffer, thumbbuf, processedbuf, length,thumblength,processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }

    public byte[] picresize(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrimKWLI/" + picid));
            BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }
    
    public byte[] picdecolour(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrimKWLI/" + picid));
            BufferedImage processed = createProcessed(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        return pad(img, 4);
    }
   
    public java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrimKWLI");
        try{
        	
        PreparedStatement ps = session.prepare("select picid,pic_added from userpiclist where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                Date pic_added=row.getDate("pic_added");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                pic.setPicadded(pic_added);
                Pics.add(pic);

            }
        }
        } catch (Exception et) {
            System.out.println("Can't get PicList" + et);
            return null;
        }
        session.close();
        return Pics;
    }
    public java.util.LinkedList<Comment> getCom(java.util.UUID picid){
    	java.util.LinkedList<Comment> Comment = new java.util.LinkedList<>();
    	Session session = cluster.connect("instagrimKWLI");
    	try {
    		
    	PreparedStatement ps =session.prepare("select comment,Addedtime,commenter from comments where picid=?");
    	BoundStatement boundStatement = new BoundStatement(ps);
    	ResultSet rs =session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        picid));
    	session.close();
    	if (rs.isExhausted()) {
            System.out.println("No comment returned");
            return null;
        } else {
            for (Row row : rs) {
            	Comment c=new Comment();
            	String StoredComment = row.getString("comment");
            	Date AddedTime=row.getDate("Addedtime");
            	String commenter=row.getString("commenter");
            	c.setComment(StoredComment);
            	c.setAddedTime(AddedTime);
            	c.setCommenter(commenter);
            	Comment.add(c);
            }
        }
    	} catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
     
    	session.close();
    	return Comment;
    }
    public Pic getPic(int image_type, java.util.UUID picid) {
        Session session = cluster.connect("instagrimKWLI");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            @SuppressWarnings("unused")
			Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;
         
            if (image_type == Convertors.DISPLAY_IMAGE) {
                
                ps = session.prepare("select image,imagelength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picid));

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");
                
                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }
                    
                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        p.setPic(bImage, length, type);

        return p;

    }
}
