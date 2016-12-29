import java.sql.*;
import java.util.*;
import oracle.sdoapi.OraSpatialManager;
import oracle.sdoapi.geom.*;
import oracle.sdoapi.adapter.*;
import oracle.sdoapi.sref.*;
import oracle.sql.STRUCT;
import java.awt.*; 
import java.applet.*; 
// import an extra class for the ActionListener 
import java.awt.event.*;
import java.applet.Applet;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;

public class Query5 {
	
	// list of 3 objects from query 	
    Connection mainConnection = null;
    Statement mainStatement = null;
    ResultSet mainResultSet = null;
    
    /*****************************/
    public static void main(String args[])
    {
        System.out.println();
        Query1 e = new Query1();
        System.out.println();
    }

    /*****************************/
    /*
    public Query5()
    {
    	// connect to DB
        ConnectToDB();	
        
        // search for all tuples
        Query5_buildings();	
		ShowMetaData();
		ShowAllTuples_buildings();
		
		Query5_students();
		ShowMetaData();
		ShowAllTuples_students();
		
		Query5_as();
		ShowMetaData();
		ShowAllTuples_as();
		
    }
    */
    
    public Query5(Graphics g, boolean c1, boolean c2, boolean c3, int xpos_fix, int ypos_fix) {
    	
    	g.setColor(Color.red);
    	g.fillRect(xpos_fix-2, ypos_fix-2, 5, 5);
    	
    	// connect to DB
        ConnectToDB();
    	
    	/******************/
    	/* default colors:: psa: pink, ohe: magenta, sgm: blue, hnb: cyan, vhe: green, ssc: yellow, helen: orange */
    	//Query5_paint(g);
    	
    	/******************/
        // get closest as
        Query5_as(xpos_fix, ypos_fix);
        ShowMetaData();
    	ShowAllTuples_as(g);
    	// get students in closest as and classify them
    	Query5_students(xpos_fix, ypos_fix);
        ShowMetaData();
        ShowAllTuples_students(g);
        /*
        // search for all tuples
        if (c1==true) {
        	Query5_buildings();
        	ShowMetaData();
        	ShowAllTuples_buildings(g);
        }
        if (c2==true) {
        	Query5_students();
        	ShowMetaData();
        	ShowAllTuples_students(g);
        }
        if (c3==true) {
        	Query5_as();
        	ShowMetaData();
        	ShowAllTuples_as(g);
        }
        */
    }
    
    /* 0. Default Paint */
    /*****************************/
    public void Query5_paint(Graphics g) {
		try
    	{
	        System.out.println("\n ** Selecting all tuples in the table annsystems" );
	        mainResultSet = mainStatement.executeQuery( "select * from annsystems" );
		}
    	catch( Exception e )
	    { System.out.println( " Error0: " + e.toString() ); }
		STRUCT locus;		
		Geometry geom;     
		try
		{
	        ResultSetMetaData meta = mainResultSet.getMetaData();
    	    System.out.println("\n ** Showing all Tuples ** " );
	  		GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
 	        while( mainResultSet.next() )
    	    {
 	        	String name = mainResultSet.getString(1);
	    	    locus = (STRUCT)mainResultSet.getObject(2);
				geom = sdoAdapter.importGeometry(locus);
				int radius = (int)mainResultSet.getDouble(3);
      			if ( (geom instanceof oracle.sdoapi.geom.Point) )
      			{
					oracle.sdoapi.geom.Point locus0 = (oracle.sdoapi.geom.Point) geom;
					int X = (int)locus0.getX();
					int Y = (int)locus0.getY();
					if (name.contains("1")) {g.setColor(Color.pink);}
					if (name.contains("2")) {g.setColor(Color.magenta);}
					if (name.contains("3")) {g.setColor(Color.blue);}
					if (name.contains("4")) {g.setColor(Color.cyan);}
					if (name.contains("5")) {g.setColor(Color.green);}
					if (name.contains("6")) {g.setColor(Color.yellow);}
					if (name.contains("7")) {g.setColor(Color.orange);}
					g.fillRect(X-7, Y-7, 15, 15);
					g.drawOval(X-radius,Y-radius,2*radius,2*radius);					
				}
       	    }
        }
		catch( Exception e )
	    { System.out.println(" Error : " + e.toString() ); }
		System.out.println();
    }
    
    /* 1. Buildings */
    /*****************************/
    /*
    public void Query5_buildings(int xpos_fix, int ypos_fix) {	
    }
    public void ShowAllTuples_buildings() {
    }
    public void ShowAllTuples_buildings(Graphics g) {
    }
    */
       
    /* 2. Students */
    /*****************************/
    public void Query5_students(int xpos_fix, int ypos_fix) {
    	try
    	{
            // searches for all tuples
	        System.out.println("\n ** Selecting tuples in the table annsystems" );
	        mainResultSet = mainStatement.executeQuery("select a3.asid, a3.locus, a3.radius, s.*  from students s, annsystems a1, annsystems a3 where "
	        		+ "SDO_GEOM.SDO_DISTANCE(s.location,a3.circle,0.005) = (select min(SDO_GEOM.SDO_DISTANCE(s.location,a4.circle,0.005)) from annsystems a4 where a4.asid<>a1.asid) "
	        		+ "and SDO_GEOM.RELATE(s.location, 'anyinteract', a1.circle, 0.005)='TRUE' and SDO_GEOM.SDO_DISTANCE(a1.circle, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix+",NULL),NULL,NULL),0.005) "
	        		+ "= (select min(SDO_GEOM.SDO_DISTANCE(a2.circle, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix+",NULL),NULL,NULL),0.005)) from annsystems a2) and a3.asid<>a1.asid");
    	}
    	catch( Exception e )
	    { System.out.println( " Error3: " + e.toString() ); }	
    }
    /*
    public void ShowAllTuples_students() {
    }
    */
    public void ShowAllTuples_students(Graphics g)
    {
		STRUCT location;
		STRUCT locus;
		Geometry geom1;
		Geometry geom2;
		try
		{
            // shows result of the query
	        ResultSetMetaData meta = mainResultSet.getMetaData();
    	    System.out.println("\n ** Showing all Tuples ** " );
	        int tupleCount=1;
	  		GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
 	        while( mainResultSet.next() )
    	    {
	    	    System.out.print( "Tuple " + tupleCount++ + " : " );	    	    
	    	    // ids
	    	    String asid = mainResultSet.getString(1);
	    	    String personid = mainResultSet.getString(4);
	    	    System.out.print( "\"" + personid + "\"," );	    	    
	    	    // location
	    	    location = (STRUCT)mainResultSet.getObject(5);
				geom1 = sdoAdapter.importGeometry(location);
				// locus and radius
				locus = (STRUCT)mainResultSet.getObject(2);
				geom2 = sdoAdapter.importGeometry(locus);
				int radius = (int)mainResultSet.getInt(3);
      			if ( (geom1 instanceof oracle.sdoapi.geom.Point))
      			{
					oracle.sdoapi.geom.Point location0 = (oracle.sdoapi.geom.Point) geom1;
					oracle.sdoapi.geom.Point locus0 = (oracle.sdoapi.geom.Point) geom2;
					int X = (int)location0.getX();
					int Y = (int)location0.getY();
					int xc = (int)locus0.getX(); 
					int yc = (int)locus0.getY();
					if (asid.contains("1")) {g.setColor(Color.pink);}
					if (asid.contains("2")) {g.setColor(Color.magenta);}
					if (asid.contains("3")) {g.setColor(Color.blue);}
					if (asid.contains("4")) {g.setColor(Color.cyan);}
					if (asid.contains("5")) {g.setColor(Color.green);}
					if (asid.contains("6")) {g.setColor(Color.yellow);}
					if (asid.contains("7")) {g.setColor(Color.orange);}
					g.fillRect(X-5, Y-5, 10, 10);
					g.fillRect(xc-7, yc-7, 15, 15);
					g.drawOval(xc-radius,yc-radius,2*radius,2*radius);
					System.out.print( "\"(X = " + X + ", Y = " + Y + ")\"," );
					System.out.print( "\"" + asid + "\"" );
				}
		        System.out.println();
       	    }
        }
		catch( Exception e )
	    { System.out.println(" Error : " + e.toString() ); }
		System.out.println();
    }

    
    /* 3. Announcement Systems */
    /*****************************/
    public void Query5_as(int xpos_fix, int ypos_fix) {
    	try
    	{
            // searches for all tuples
	        System.out.println("\n ** Selecting tuples in the table annsystems" );
	        mainResultSet = mainStatement.executeQuery( "select * from annsystems a1 where SDO_GEOM.SDO_DISTANCE(a1.circle, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix
	        +",NULL),NULL,NULL),0.005) = (select min(SDO_GEOM.SDO_DISTANCE(a2.circle, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix+",NULL),NULL,NULL),0.005)) from annsystems a2)");
		}
    	catch( Exception e )
	    { System.out.println( " Error3: " + e.toString() ); }	
    }
    /*
    public void ShowAllTuples_as() {
    }
    */
    public void ShowAllTuples_as(Graphics g)
    {
		STRUCT locus;		
		Geometry geom;     
		try
		{
            // shows result of the query
	        ResultSetMetaData meta = mainResultSet.getMetaData();
    	    System.out.println("\n ** Showing all Tuples ** " );
	        int tupleCount=1;
	  		GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
 	        while( mainResultSet.next() )
    	    {
	    	    System.out.print( "Tuple " + tupleCount++ + " : " );	    	    
	    	    // asid
	    	    String asid = mainResultSet.getString(1);
	    	    System.out.print( "\"" + asid + "\"," );
	    	    // radius
      			int radius = (int)mainResultSet.getDouble(3);
      			System.out.print( "\"" + radius + "\",");
	    	    // locus
	    	    locus = (STRUCT)mainResultSet.getObject(2);
				geom = sdoAdapter.importGeometry(locus);
      			if ( (geom instanceof oracle.sdoapi.geom.Point) )
      			{
					oracle.sdoapi.geom.Point locus0 = (oracle.sdoapi.geom.Point) geom;
					int X = (int)locus0.getX();
					int Y = (int)locus0.getY();
					g.setColor(Color.red);
					g.fillRect(X-7, Y-7, 15, 15);
					g.drawOval(X-radius,Y-radius,2*radius,2*radius);
					System.out.print( "\"(X = " + X + ", Y = " + Y + ")\"" );
				}    			     			
		        System.out.println();
       	    }
        }
		catch( Exception e )
	    { System.out.println(" Error : " + e.toString() ); }
		System.out.println();
    }
 

    
    /*****************************/
    public void ShowMetaData()
    {
    	try
		{
            // shows meta data
    	    System.out.println("\n ** Obtaining Meta Data ** " );
	        ResultSetMetaData meta = mainResultSet.getMetaData();
	        for( int col=1; col<=meta.getColumnCount(); col++ )
		        System.out.println( "Column: " + meta.getColumnName(col) + "\t, Type: " + meta.getColumnTypeName(col) );
    	}
		catch( Exception e )
    	{ System.out.println( " Error_meta: " + e.toString() ); }
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