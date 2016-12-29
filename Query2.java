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

public class Query2 {
	
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
    public Query2()
    {
    	// connect to DB
        ConnectToDB();	
        
        // search for all tuples
        Query2_buildings();	
		ShowMetaData();
		ShowAllTuples_buildings();
		
		Query2_students();
		ShowMetaData();
		ShowAllTuples_students();
		
		Query2_as();
		ShowMetaData();
		ShowAllTuples_as();
		
    }
    */
    
    public Query2(Graphics g, boolean c1, boolean c2, boolean c3, int xpos_fix, int ypos_fix) {
    	
    	int r = 50;
    	g.setColor(Color.red);
    	g.fillRect(xpos_fix-2, ypos_fix-2, 5, 5);
    	g.drawOval(xpos_fix-r, ypos_fix-r, 2*r, 2*r);
    	
    	// connect to DB
        ConnectToDB();
        
        // search for all tuples
        if (c1==true) {
        	Query2_buildings(xpos_fix,ypos_fix,r);
        	ShowMetaData();
        	ShowAllTuples_buildings(g);
        }
        if (c2==true) {
        	Query2_students(xpos_fix,ypos_fix,r);
        	ShowMetaData();
        	ShowAllTuples_students(g);
        }
        if (c3==true) {
        	Query2_as(xpos_fix,ypos_fix,r);
        	ShowMetaData();
        	ShowAllTuples_as(g);
        }
        
    }
    
    /* 1. Buildings */
    /*****************************/
    public void Query2_buildings(int xpos_fix, int ypos_fix, int r) {
    	try
    	{
            // searches for all tuples
	        System.out.println("\n ** Selecting tuples in the table buildings" );
	        mainResultSet = mainStatement.executeQuery( "select * from buildings where SDO_GEOM.RELATE(shape, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,4),sdo_ordinate_array("
	        		+(int)(xpos_fix-r)+","+ypos_fix+","+(int)(xpos_fix+r)+","+ypos_fix+","+xpos_fix+","+(int)(ypos_fix+r)+")), 0.005)='TRUE'" );
		}
    	catch( Exception e )
	    { System.out.println( " Error1: " + e.toString() ); }	
    }
    /*
    public void ShowAllTuples_buildings() {
    }
    */
    public void ShowAllTuples_buildings(Graphics g)
    {
		STRUCT polygon;		
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
	    	    // bid
	    	    String bid = mainResultSet.getString(1);
	    	    System.out.print( "\"" + bid + "\"," );	    	    
	    	    // bname
	    	    String bname = mainResultSet.getString(2);
	    	    System.out.print( "\"" + bname + "\",");	    	    
	    	    // nover
	    	    int nover = mainResultSet.getInt(3);
	    	    System.out.print( "\"" + nover + "\"");	    	    
	    	    // polygon
	    	    polygon = (STRUCT)mainResultSet.getObject(4);
				geom = sdoAdapter.importGeometry(polygon);
      			if ( (geom instanceof oracle.sdoapi.geom.Polygon) )
      			{
					oracle.sdoapi.geom.Polygon polygon0 = (oracle.sdoapi.geom.Polygon) geom;
					for (Enumeration e = polygon0.getRings();e.hasMoreElements();)
					{
						LineString linestring = (LineString)e.nextElement();
						CoordPoint[] coordArray = linestring.getPointArray();
						int[] x = new int[coordArray.length*2];
						int[] y = new int[coordArray.length*2];
						int i=0;
						for (i=0;i<coordArray.length;i++)
						{
							x[i] = (int)coordArray[i].getX();
							y[i] = (int)coordArray[i].getY();
							g.setColor(Color.yellow);
							if (i>0) {
								g.drawLine(x[i], y[i], x[i-1], y[i-1]);
							}
							if (i==coordArray.length-1) {
								g.drawLine(x[i], y[i], x[0], y[0]);
							}
							System.out.print( ",(" + x[i] + "," + y[i] + ")" );
						}
					}
				}
		        System.out.println();
       	    }
        }
		catch( Exception e )
	    { System.out.println(" Error : " + e.toString() ); }
		System.out.println();
    }
       
    /* 2. Students */
    /*****************************/
    public void Query2_students(int xpos_fix, int ypos_fix, int r) {
    	try
    	{
            // searches for all tuples
	        System.out.println("\n ** Selecting tuples in the table students" );
	        mainResultSet = mainStatement.executeQuery( "select * from students where SDO_GEOM.RELATE(location, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,4),sdo_ordinate_array("
	        		+(int)(xpos_fix-r)+","+ypos_fix+","+(int)(xpos_fix+r)+","+ypos_fix+","+xpos_fix+","+(int)(ypos_fix+r)+")), 0.005)='TRUE'" );
		}
    	catch( Exception e )
	    { System.out.println( " Error2: " + e.toString() ); }	
    }
    /*
    public void ShowAllTuples_students() {
    }
    */
    public void ShowAllTuples_students(Graphics g)
    {
		STRUCT location;		
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
	    	    // personid
	    	    String personid = mainResultSet.getString(1);
	    	    System.out.print( "\"" + personid + "\"," );	    	    
	    	    // location
	    	    location = (STRUCT)mainResultSet.getObject(2);
				geom = sdoAdapter.importGeometry(location);
      			if ( (geom instanceof oracle.sdoapi.geom.Point) )
      			{
					oracle.sdoapi.geom.Point location0 = (oracle.sdoapi.geom.Point) geom;
					int X = (int)location0.getX();
					int Y = (int)location0.getY();
					g.setColor(Color.green);
					g.fillRect(X-5, Y-5, 10, 10);
					System.out.print( "\"(X = " + X + ", Y = " + Y + ")\"" );
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
    public void Query2_as(int xpos_fix, int ypos_fix, int r) {
    	try
    	{
            // searches for all tuples
	        System.out.println("\n ** Selecting tuples in the table annsystems" );
	        mainResultSet = mainStatement.executeQuery( "select * from annsystems where SDO_GEOM.RELATE(circle, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,4),sdo_ordinate_array("
	        		+(int)(xpos_fix-r)+","+ypos_fix+","+(int)(xpos_fix+r)+","+ypos_fix+","+xpos_fix+","+(int)(ypos_fix+r)+")), 0.005)='TRUE'" );
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