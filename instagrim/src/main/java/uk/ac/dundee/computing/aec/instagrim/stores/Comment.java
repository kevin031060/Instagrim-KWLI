package uk.ac.dundee.computing.aec.instagrim.stores;

import java.util.Date;

public class Comment {
	
	private String Comment=null;
	private String Commenter=null;
	private java.util.UUID UUID=null;
    private Date AddedTime=null; 
    
    public void setComment(String com){
        this.Comment =com;
    }
    public String getComment(){
        return Comment;
    }
    public void setCommenter(String com){
        this.Commenter =com;
    }
    public String getCommenter(){
        return Commenter;
    }
    public Date getAddedTime(){
    	return AddedTime;
    }
    public void setAddedTime(Date AddedTime){
    	this.AddedTime=AddedTime;
    }
	public void setUUID(java.util.UUID UUID){
        this.UUID =UUID;
    }
    public String getSUUID(){
        return UUID.toString();
    }

}
