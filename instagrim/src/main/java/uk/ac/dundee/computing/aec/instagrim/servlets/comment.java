package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.datastax.driver.core.Cluster;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Logout
 */
@WebServlet(name = "comment", urlPatterns = {"/comment"})
public class comment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public comment() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PicModel tm = new PicModel();
		tm.setCluster(cluster);
		java.util.LinkedList<Comment> des = tm.getCom(java.util.UUID.fromString(request.getParameter("id")));
        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:MM");
        if (des == null) {
        	out.println("No Comments found");
            } else {
                Iterator<Comment> iterator;
                iterator = des.iterator();
                while (iterator.hasNext()) {
                	Comment c = (Comment) iterator.next();
                    String comment = c.getComment();
                    Date AddedTime= c.getAddedTime();
                    String commenter=c.getCommenter();
                    out.println(comment+"</br>");
                    out.println("<font color='#00dddd' size='1'  face='Comic Sans MS'>Commented by "+commenter+" on "+sdf.format(AddedTime)+"</font></br>");
                    
                }
            }
        out.println("<footer><ul><li class='footer'><a href='/instagrimKWLI/TotheUserPics.jsp'>Back</a></li></ul></footer>    ");
       // The Ajax I tried to show comments just below the picture didnt work
        out.close();
        
        return;
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
        String comment=request.getParameter("commen");
		java.util.UUID picid=java.util.UUID.fromString(request.getParameter("id"));
		PicModel tm = new PicModel();
        tm.setCluster(cluster);
        LoggedIn lg=(LoggedIn) request.getSession().getAttribute("LoggedIn"); 
        String name=lg.getUsername();
        tm.insertCom(picid,comment,name);
        RequestDispatcher rd = request.getRequestDispatcher("/TotheUserPics.jsp");
        rd.forward(request, response);
        

      }
}
