package uk.ac.dundee.computing.aec.instagrim.lib;

import com.datastax.driver.core.*;

public final class Keyspaces {

    public Keyspaces() {

    }

    public static void SetUpKeySpaces(Cluster c) {
        try {
            //Add some keyspaces here
            String createkeyspace = "create keyspace if not exists instagrimKWLI  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
            String CreatePicTable = "CREATE TABLE if not exists instagrimKWLI.pics ("
                    + " user varchar,"
                    + " picid uuid, "
                    + " interaction_time timestamp,"          
                    + " image blob,"
                    + " thumb blob,"
                    + " processed blob,"
                    + " imagelength int,"
                    + " thumblength int,"
                    + "  processedlength int,"
                    + " type  varchar,"
                    + " name  varchar,"
                    + " PRIMARY KEY (picid)"
                    + ")";
            String Createuserpiclist = "CREATE TABLE if not exists instagrimKWLI.userpiclist (\n"
                    + "picid uuid,\n"
                    + "user varchar,\n"
                    + "pic_added timestamp,\n"
                    + "PRIMARY KEY (user,pic_added)\n"
                    + ") WITH CLUSTERING ORDER BY (pic_added desc);";
            String CreateAddressType = "CREATE TYPE if not exists instagrimKWLI.address (\n"
                    + "      street text,\n"
                    + "      city text,\n"
                    + "      zip int\n"
                    + "  );";
            String CreateUserProfile = "CREATE TABLE if not exists instagrimKWLI.userprofiles (\n"
                    + "      login text PRIMARY KEY,\n"
                     + "     password text,\n"
                    + "      first_name text,\n"
                    + "      last_name text,\n"
                    + "      email set<text>,\n"   //varchar(320)???
                    + "      addresses  map<text, frozen <address>>\n"
                  
                    + "  );";
            
            String CreateCommentList = "CREATE TABLE if not exists instagrimKWLI.comments (\n"
            		+ " picid uuid ,\n "
                    + " comment text,\n"
                    + " Addedtime timestamp,\n"
                    + " Commenter varchar,\n"
                    + " PRIMARY KEY (picid,Addedtime)\n"
                    + ") WITH CLUSTERING ORDER BY (Addedtime desc);";
            String CreatePorTable = "CREATE TABLE if not exists instagrimKWLI.pors ("
                    + " user varchar,"
                    + " picid uuid, "
                    + " interaction_time timestamp,"          
                    + " image blob,"
                    + " thumb blob,"
                    + " processed blob,"
                    + " imagelength int,"
                    + " thumblength int,"
                    + "  processedlength int,"
                    + " type  varchar,"
                    + " name  varchar,"
                    + " PRIMARY KEY (picid)"
                    + ")";
            String Createuserporlist = "CREATE TABLE if not exists instagrimKWLI.userporlist (\n"
                    + "picid uuid,\n"
                    + "user varchar,\n"
                    + "pic_added timestamp,\n"
                    + "PRIMARY KEY (user,pic_added)\n"
                    + ") WITH CLUSTERING ORDER BY (pic_added desc);";
            Session session = c.connect();
            try {
                PreparedStatement statement = session
                        .prepare(createkeyspace);
                BoundStatement boundStatement = new BoundStatement(statement);
      
				session.execute(boundStatement);
                System.out.println("created instagrimKWLI ");
            } catch (Exception et) {
                System.out.println("Can't create instagrimKWLI " + et);
            }

            //now add some column families 
            System.out.println("" + CreatePicTable);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreatePicTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create tweet table " + et);
            }
            System.out.println("" + Createuserpiclist);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createuserpiclist);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user pic list table " + et);
            }
            System.out.println("" + CreateAddressType);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateAddressType);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address type " + et);
            }
            System.out.println("" + CreateUserProfile);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateUserProfile);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address Profile " + et);
            }
            System.out.println("" + CreateAddressType);
            System.out.println("" + CreateCommentList);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateCommentList);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Commentlist " + et);
            }
            System.out.println("" +CreatePorTable);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreatePorTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create PortraitTable " + et);
            }
            System.out.println("" +Createuserporlist);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createuserporlist);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create userporlist " + et);
            }
            session.close();

        } catch (Exception et) {
            System.out.println("Other keyspace or coulm definition error" + et);
        }

    }
}
