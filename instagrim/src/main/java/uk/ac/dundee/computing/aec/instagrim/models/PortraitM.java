package uk.ac.dundee.computing.aec.instagrim.models;

import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.OP_GRAYSCALE;
import static org.imgscalr.Scalr.pad;
import static org.imgscalr.Scalr.resize;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr.Method;

import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class PortraitM {
	Cluster cluster;

    public PortraitM() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    public void insertPor(byte[] b, String type, String name, String user) {
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

            PreparedStatement psInsertPic = session.prepare("insert into pors ( user, picid, interaction_time, image, thumb, processed, imagelength, thumblength, processedlength, type, name) values(?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userporlist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
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
        img = resize(img, Method.SPEED, 100, OP_ANTIALIAS, OP_GRAYSCALE);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        return pad(img, 4);
    }
   
   public java.util.LinkedList<Pic> getPorsForUser(String User) {
       java.util.LinkedList<Pic> Pors = new java.util.LinkedList<>();
       Session session = cluster.connect("instagrimKWLI");
       PreparedStatement ps = session.prepare("select picid from userporlist where user =?");
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
               System.out.println("UUID" + UUID.toString());
               pic.setUUID(UUID);
               Pors.add(pic);

           }
       }
       return Pors;
   }
   
   public Pic getPor(int image_type, java.util.UUID picid) {
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
               
               ps = session.prepare("select image,imagelength,type from pors where picid =?");
           } else if (image_type == Convertors.DISPLAY_THUMB) {
               ps = session.prepare("select thumb,imagelength,thumblength,type from pors where picid =?");
           } else if (image_type == Convertors.DISPLAY_PROCESSED) {
               ps = session.prepare("select processed,processedlength,type from pors where picid =?");
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
           System.out.println("Can't get Por" + et);
           return null;
       }
       session.close();
       Pic p = new Pic();
       p.setPic(bImage, length, type);

       return p;

   }
}
