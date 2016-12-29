import java.io.File;
import java.util.Scanner;
import java.sql.*;
import java.util.*;
import oracle.sdoapi.OraSpatialManager;
import oracle.sdoapi.geom.*;
import oracle.sdoapi.adapter.*;
import oracle.sdoapi.sref.*;
import oracle.sql.STRUCT;

public class ReadFile {
	
    Connection mainConnection = null;
    Statement mainStatement = null;
    ResultSet mainResultSet = null;
    
    /*****************************/
    public static void main(String[] args) {
    	
    	System.out.println();
    	ReadFile e = new ReadFile();
        System.out.println();
    	
    }
    
    /*****************************/
    public ReadFile()
    {
        ConnectToDB();			// connect to DB

        PublishBuildings();			
        PublishStudents();
        PublishAS();

    }
    
    /*****************************/
    public void PublishBuildings() {
    	
    	try
        {
    		// delete previous data from the DB
            System.out.print( "\n ** Deleting previous tuples from buildings ..." );
            mainStatement.executeUpdate( "delete from buildings" );
            System.out.println( ", Deleted. **" );
            
	    	/* Read buildings.xy */
	    	File file1 = new File("data/buildings.xy");
	    	System.out.println(file1.getCanonicalPath());
	    	Scanner scanner1 = new Scanner(file1);
	    	System.out.print( " ** Inserting Data ..." );
	        while(scanner1.hasNext() ) {
	        	String phrase = scanner1.nextLine();
	        	String delims = "[,]";
	        	String[] tokens = phrase.split(delims);
	        	String query = "insert into buildings values ('"+tokens[0].trim()+"','"+tokens[1].trim()+"',"+tokens[2]+", sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,1),sdo_ordinate_array(";
	        	for(int i=3; i<tokens.length-1; i++){
	        		query = query+tokens[i]+",";
	        	}
	        	query = query+tokens[tokens.length-1]+")))";
	        	mainStatement.executeUpdate(query);
	        	//System.out.println(query);
	        } 
	        scanner1.close();
	        System.out.println( ", Done. ** \n" );
        }
    	catch( Exception e )
        { System.out.println( " Error 1: " + e.toString() ); }
    }
    
    public void PublishStudents() {
    	
    	try
        {
    		// delete previous data from the DB
            System.out.print( "\n ** Deleting previous tuples from students ..." );
            mainStatement.executeUpdate( "delete from students" );
            System.out.println( ", Deleted. **" );
            
	        /* Read students.xy */
	    	File file2 = new File("data/students.xy");
	    	System.out.println(file2.getCanonicalPath());
	    	Scanner scanner2 = new Scanner(file2);
	    	System.out.print( " ** Inserting Data ..." );
	        while(scanner2.hasNext() ) {
	        	String phrase = scanner2.nextLine();
	        	String delims = "[,]";
	        	String[] tokens = phrase.split(delims);
	        	String query = "insert into students values ('"+tokens[0].trim()+"', sdo_geometry(2001,NULL,sdo_point_type("+tokens[1]+","+tokens[2]+",NULL),NULL,NULL))";
	        	mainStatement.executeUpdate(query);
	        	//System.out.println(query);
	        }       
	        scanner2.close();  
	        System.out.println( ", Done. ** \n" );
        }
    	catch( Exception e )
        { System.out.println( " Error 2: " + e.toString() ); }
    }
    
    public void PublishAS() {
    	
    	try
        {
    		// delete previous data from the DB
            System.out.print( "\n ** Deleting previous tuples from annsystems ..." );
            mainStatement.executeUpdate( "delete from annsystems" );
            System.out.println( ", Deleted. **" );
            
	        /* Read AS.xy */
	    	File file3 = new File("data/announcementSystems.xy");
	    	System.out.println(file3.getCanonicalPath());
	    	Scanner scanner3 = new Scanner(file3);
	    	System.out.print( " ** Inserting Data ..." );
	        while(scanner3.hasNext() ) {
	        	String phrase = scanner3.nextLine();
	        	String delims = "[,]";
	        	String[] tokens = phrase.split(delims);
	        	int x = Integer.valueOf(tokens[1].trim());
	        	int y = Integer.valueOf(tokens[2].trim());
	        	int r = Integer.valueOf(tokens[3].trim());
	        	int x1 = x+r; int y1 = y; 
	        	int x2 = x-r; int y2 = y;
	        	int x3 = x; int y3 = y+r;
	        	String query = "insert into annsystems values ('"+tokens[0].trim()+"',sdo_geometry(2001,NULL,sdo_point_type("+x+","+y+",NULL),NULL,NULL)"+","+r+",";
	        	query = query+"sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,4),sdo_ordinate_array("+x1+","+y1+","+x2+","+y2+","+x3+","+y3+")))";
	        	mainStatement.executeUpdate(query);
	        	//System.out.println(query);
	        }       
	        System.out.println( ", Done. ** \n" );
	        scanner3.close(); 
        }
    	catch( Exception e )
        { System.out.println( " Error 3: " + e.toString() ); }
    }
    
    /*****************************/
    public void ConnectToDB()
    {
		try
		{
			// loading Oracle Driver
    		System.out.print("Looking for Oracle's jdbc-odbc driver ... ");
	    	DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
	    	System.out.println(", Loaded.");

			String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	    	String userName = "SYSTEM";
	    	String password = "yling";

	    	System.out.print("Connecting to DB...");
	    	mainConnection = DriverManager.getConnection(URL, userName, password);
	    	System.out.println(", Connected!");

    		mainStatement = mainConnection.createStatement();

   		}
   		catch (Exception e)
   		{
     		System.out.println( "Error while connecting to DB: "+ e.toString() );
     		e.printStackTrace();
     		System.exit(-1);
   		}
    }
}

