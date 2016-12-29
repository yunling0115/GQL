// GUI for Graphical Query Language (GQL)
// Yun Ling - 2013 Fall

import java.awt.*; 
import java.applet.*; 
// import an extra class for the ActionListener 
import java.awt.event.*;
import java.applet.Applet;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.awt.Polygon;
import javax.imageio.ImageIO;

public class hw2 extends Applet implements ActionListener, MouseMotionListener, MouseListener {
	
	/***************/
	// mouse position
	int xpos;
	int ypos;
	
	// left-clicked mouse position
	int xpos_fix;
	int ypos_fix;
	
	// selected query
	String query_type;
	
	// selected circle - query 2
	int r = 50;
	
	// selected polygon - query 3
	ArrayList<Integer> xpos_p = new ArrayList<>();
	ArrayList<Integer> ypos_p = new ArrayList<>();
	ArrayList<Integer> xpos_p_fix = new ArrayList<>();
	ArrayList<Integer> ypos_p_fix = new ArrayList<>();
	
	// selected checkbox
	boolean c1;
	boolean c2;
	boolean c3;
	
	// submitted queries
	ArrayList<String> queries = new ArrayList<String>();	
	
	// GUI objects
	Image img;
	Checkbox check1; 
    Checkbox check2; 
    Checkbox check3;
    Checkbox radio1; 
    Checkbox radio2; 
    Checkbox radio3;
    Checkbox radio4;
    Checkbox radio5;
    CheckboxGroup radioGroup;    
    Button submitButton; 
    Button clear;
    TextArea showField;
    Label feature = new Label("Active Feature");
    Label query = new Label("Query");
    Label position = new Label("Mouse Position | Selected");
    Label submitted = new Label("Submitted Query");
    Label pos = new Label("");
    
    
    public void init()  
    {
    	Frame c = (Frame)this.getParent().getParent();
    	c.setTitle("Yun Ling 9184777342");
    	
    	img = null;
    	setLayout(null);
   	
        submitButton = new Button("Submit"); 

        showField = new TextArea("Your Submitted Area");
        check1 = new Checkbox("Building", true); 
        check2 = new Checkbox("Students", true); 
        check3 = new Checkbox("Annoucements", true);
        radioGroup = new CheckboxGroup(); 
        radio1 = new Checkbox("Whole Region", radioGroup,true); 
        radio2 = new Checkbox("Point Query", radioGroup,false); 
        radio3 = new Checkbox("Range Query", radioGroup,false); 
        radio4 = new Checkbox("Surrounding Studnets", radioGroup,false); 
        radio5 = new Checkbox("Emergency Query", radioGroup,false); 
        //pos.setForeground(Color.red);
        
        feature.setBounds(830,0,100,40);
        check1.setBounds(830,40,100,20); 
        check2.setBounds(830,60,100,20); 
        check3.setBounds(830,80,100,20); 
        query.setBounds(830,120,100,40);
        radio1.setBounds(830,160,170,20);
        radio2.setBounds(830,180,170,20);
        radio3.setBounds(830,200,170,20);
        radio4.setBounds(830,220,170,20);
        radio5.setBounds(830,240,170,20);
        position.setBounds(830,280,170,20);
        pos.setBounds(830,300,170,20);
        submitButton.setBounds(830,340,100,40);
        submitted.setBounds(830,400,100,20);
        showField.setBounds(830,430,160,150);
        Font myFont = new Font("Calibra",Font.BOLD,12);
        Font myFont2 = new Font("Calibra",Font.ITALIC,11);
        feature.setFont(myFont);
        query.setFont(myFont);
        position.setFont(myFont);
        submitted.setFont(myFont);
        submitButton.setFont(myFont);
        showField.setFont(myFont2);
        
        /* add */
        add(feature);
        add(query);
        add(position);
        add(submitted);
        add(check1);
        add(check2);
        add(check3);
        add(radio1);
        add(radio2);
        add(radio3);
        add(radio4);
        add(radio5);
        add(pos);
        add(submitButton);
        add(showField);
        radio1.addMouseListener(this);
        radio2.addMouseListener(this);
        radio3.addMouseListener(this);
        radio4.addMouseListener(this);
        radio5.addMouseListener(this);
        submitButton.addActionListener(this); 
        addMouseMotionListener(this); 
        addMouseListener(this); 
    }

    /* MouseMotionListener */
    public void mouseMoved(MouseEvent me) {  
         xpos = me.getX(); 
         ypos = me.getY(); 
         pos.setText("("+xpos+","+ypos+") | ("+xpos_fix+","+ypos_fix+")");
    }
    public void mouseDragged(MouseEvent me) {}
    
    /* MouseListener */
    public void mouseClicked(MouseEvent me) {
    	// left click to change radio button
    	System.out.println(me.getSource().toString());
    	if (me.getButton()==1 & me.getSource().getClass().equals(java.awt.Checkbox.class)) {
    		Graphics g = getGraphics();
    		paint(g);
    	}
    	// left click in range
    	if (me.getButton()==1 & me.getSource().getClass().equals(hw2.class)) {
	    	xpos_fix = xpos;
	    	ypos_fix = ypos;
	    	if (xpos_fix<=820 & ypos_fix<=580) {
		    	pos.setText("("+xpos+","+ypos+") | ("+xpos_fix+","+ypos_fix+")");
		    	if (radioGroup.getSelectedCheckbox().getLabel()=="Point Query") {
		    		Graphics g = getGraphics();
		    		paint(g);
		        	g.setColor(Color.red);
		        	g.fillRect(xpos_fix-2, ypos_fix-2, 5, 5);
		        	g.drawOval(xpos_fix-r, ypos_fix-r, 2*r, 2*r);
		    	}
		    	if (radioGroup.getSelectedCheckbox().getLabel()=="Range Query") {
		    		Graphics g = getGraphics();
		    		paint(g);
		    		xpos_p.add(xpos_fix);
		    		ypos_p.add(ypos_fix);
		    		for (int i=1; i<xpos_p.size(); i++) {
		    			g.setColor(Color.red);
		    			g.drawLine(xpos_p.get(i),ypos_p.get(i),xpos_p.get(i-1),ypos_p.get(i-1));
		    		}
		    	}
		    	if (radioGroup.getSelectedCheckbox().getLabel()=="Surrounding Studnets") {
		    		Graphics g = getGraphics();
		    		paint(g);
		    		g.setColor(Color.red);
		    		g.fillRect(xpos_fix-2, ypos_fix-2, 5, 5);	
		    	}
		    	if (radioGroup.getSelectedCheckbox().getLabel()=="Emergency Query") {
		    		Graphics g = getGraphics();
		    		paint(g);
		    		g.setColor(Color.red);
		    		g.fillRect(xpos_fix-2, ypos_fix-2, 5, 5);	
		    	}
	    	}
    	}
    	// right click
    	if (me.getButton()==3) {
    		if (radioGroup.getSelectedCheckbox().getLabel()=="Range Query") {
    			Graphics g = getGraphics();
        		//paint(g);
    			g.setColor(Color.red);
    			System.out.println("polygon:");
    			System.out.println(xpos_p.size());
    			for (int i=0; i<xpos_p.size(); i++) {
    				System.out.println("("+xpos_p.get(i)+","+ypos_p.get(i)+")");
    			}
    			int ntemp = xpos_p.size();
    			int[] xtemp = new int[ntemp];
    			int[] ytemp = new int[ntemp];
    			xpos_p_fix.clear(); ypos_p_fix.clear();
    			for (int i=0; i<ntemp; i++) {
    				xtemp[i] = xpos_p.get(i); xpos_p_fix.add(xtemp[i]);
    				ytemp[i] = ypos_p.get(i); ypos_p_fix.add(ytemp[i]);
    			}
    			g.drawPolygon(xtemp,ytemp,ntemp);
    			xpos_p.clear();
    			ypos_p.clear();
    			System.out.println(xpos_p_fix.size());
    		}
    	}
    }
    private Polygon Polygon(ArrayList<Integer> xpos_polygon2,
			ArrayList<Integer> ypos_polygon2, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	public void mousePressed (MouseEvent me) {}
    public void mouseReleased (MouseEvent me) {} 
    public void mouseExited (MouseEvent me) {}
    public void mouseEntered (MouseEvent me) {}
    
    /* Load Image */
    public void loadImage() {
    	try
    	{ 
    		img = getImage(getDocumentBase(), "map.jpg");
     	}
    	catch(Exception e) { }
    }
 
	public void paint(Graphics g) {
		setSize(1000, 580);
		if (img == null)
			loadImage();
		g.drawImage(img, 0, 0, this);
	}
	
	/* Click Submit Button */
	public void actionPerformed(ActionEvent evt) {
		Graphics g = this.getGraphics();
		paint(g);
		c1 = check1.getState();
		c2 = check2.getState();
		c3 = check3.getState();
		query_type = radioGroup.getSelectedCheckbox().getLabel();
		// 1
		if (query_type == "Whole Region") {
			Query1 e = new Query1(g, c1, c2, c3);
			// show submitted query
			if (c1==true) {String s1="select * from buildings"; queries.add(s1);}
			if (c2==true) {String s2="select * from students"; queries.add(s2);}
			if (c3==true) {String s3="select * from annsystems"; queries.add(s3);}
			String q = "";
			for (int i=0;i<queries.size();i++) {
				if (i==0) {q = "Query"+(int)(i+1)+": "+queries.get(i); }
				else {q = q+"\n"+"Query"+(int)(i+1)+": "+queries.get(i);}
			}
			System.out.println(q);
			showField.setText(q);
		}
		// 2
		if (query_type == "Point Query") {
			Query2 e = new Query2(g, c1, c2, c3, xpos_fix, ypos_fix);
			// show submitted query
			if (c1==true) {String s1="select * from buildings where SDO_GEOM.RELATE(shape, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,4),sdo_ordinate_array("
	        		+(int)(xpos_fix-r)+","+ypos_fix+","+(int)(xpos_fix+r)+","+ypos_fix+","+xpos_fix+","+(int)(ypos_fix+r)+")), 0.005)='TRUE'"; queries.add(s1);}
			if (c2==true) {String s2="select * from students where SDO_GEOM.RELATE(location, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,4),sdo_ordinate_array("
	        		+(int)(xpos_fix-r)+","+ypos_fix+","+(int)(xpos_fix+r)+","+ypos_fix+","+xpos_fix+","+(int)(ypos_fix+r)+")), 0.005)='TRUE'"; queries.add(s2);}
			if (c3==true) {String s3="select * from annsystems where SDO_GEOM.RELATE(circle, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,4),sdo_ordinate_array("
	        		+(int)(xpos_fix-r)+","+ypos_fix+","+(int)(xpos_fix+r)+","+ypos_fix+","+xpos_fix+","+(int)(ypos_fix+r)+")), 0.005)='TRUE'"; queries.add(s3);}
			String q = "";
			for (int i=0;i<queries.size();i++) {
				if (i==0) {q = "Query"+(int)(i+1)+": "+queries.get(i); }
				else {q = q+"\n"+"Query"+(int)(i+1)+": "+queries.get(i);}
			}
			System.out.println(q);
			showField.setText(q);
		}
		// 3
		if (query_type == "Range Query") {
			Query3 e = new Query3(g, c1, c2, c3, xpos_p_fix, ypos_p_fix);
			// show submitted query
			if (c1==true) {
				String search = "select * from students where SDO_GEOM.RELATE(location, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,1),sdo_ordinate_array("; 
		        for (int i=0;i<xpos_p_fix.size();i++) {
		        	if (i<xpos_p_fix.size()-1) {
		        		search = search+xpos_p_fix.get(i)+",";
		        		search = search+ypos_p_fix.get(i)+",";
		        	}
		        	else {
		        		search = search+xpos_p_fix.get(i)+",";
		        		search = search+ypos_p_fix.get(i);
		        	}
		        	
		        }
		        search = search+")), 0.005)='TRUE'";
		        String s1 = search;
				queries.add(s1);
				}
			if (c2==true) {
				String search = "select * from students where SDO_GEOM.RELATE(location, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,1),sdo_ordinate_array("; 
		        for (int i=0;i<xpos_p_fix.size();i++) {
		        	if (i<xpos_p_fix.size()-1) {
		        		search = search+xpos_p_fix.get(i)+",";
		        		search = search+ypos_p_fix.get(i)+",";
		        	}
		        	else {
		        		search = search+xpos_p_fix.get(i)+",";
		        		search = search+ypos_p_fix.get(i);
		        	}
		        	
		        }
		        search = search+")), 0.005)='TRUE'";
				String s2 = search; 
				queries.add(s2);
				}
			if (c3==true) {
				String search = "select * from annsystems where SDO_GEOM.RELATE(circle, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,1),sdo_ordinate_array("; 
		        for (int i=0;i<xpos_p_fix.size();i++) {
		        	if (i<xpos_p_fix.size()-1) {
		        		search = search+xpos_p_fix.get(i)+",";
		        		search = search+ypos_p_fix.get(i)+",";
		        	}
		        	else {
		        		search = search+xpos_p_fix.get(i)+",";
		        		search = search+ypos_p_fix.get(i);
		        	}
		        	
		        }
		        search = search+")), 0.005)='TRUE'";
				String s3 = search;
				queries.add(s3);
				}
			String q = "";
			for (int i=0;i<queries.size();i++) {
				if (i==0) {q = "Query"+(int)(i+1)+": "+queries.get(i); }
				else {q = q+"\n"+"Query"+(int)(i+1)+": "+queries.get(i);}
			}
			System.out.println(q);
			showField.setText(q);
		}
		// 4
		if (query_type == "Surrounding Studnets") {
			Query4 e = new Query4(g, c1, c2, c3, xpos_fix, ypos_fix);
			// show submitted query
			String s3="select * from annsystems a1 where SDO_GEOM.SDO_DISTANCE(a1.circle, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix
			        +",NULL),NULL,NULL),0.005) = (select min(SDO_GEOM.SDO_DISTANCE(a2.circle, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix+",NULL),NULL,NULL),0.005)) from annsystems a2)";
			queries.add(s3);
			String s1="select s.* from students s, annsystems a1 where SDO_GEOM.RELATE(s.location, 'anyinteract', a1.circle, 0.005)='TRUE'"
	        		+" and SDO_GEOM.SDO_DISTANCE(a1.circle, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix+",NULL),NULL,NULL),0.005) "
	        		+"= (select min(SDO_GEOM.SDO_DISTANCE(a2.circle, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix+",NULL),NULL,NULL),0.005)) from annsystems a2)";
			queries.add(s1);
			String q = "";
			for (int i=0;i<queries.size();i++) {
				if (i==0) {q = "Query"+(int)(i+1)+": "+queries.get(i); }
				else {q = q+"\n"+"Query"+(int)(i+1)+": "+queries.get(i);}
			}
			System.out.println(q);
			showField.setText(q);
		}
		// 5
		if (query_type == "Emergency Query") {
			Query5 e = new Query5(g, c1, c2, c3, xpos_fix, ypos_fix);
			// show submitted query
			String s3="select * from annsystems a1 where SDO_GEOM.SDO_DISTANCE(a1.locus, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix
					+",NULL),NULL,NULL),0.005) = (select min(SDO_GEOM.SDO_DISTANCE(a2.locus, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix+",NULL),NULL,NULL),0.005)) from annsystems a2)";
			queries.add(s3);
			String s1="select a3.asid, a3.locus, a3.radius, s.*  from students s, annsystems a1, annsystems a3 where "
	        		+ "SDO_GEOM.SDO_DISTANCE(s.location,a3.circle,0.005) = (select min(SDO_GEOM.SDO_DISTANCE(s.location,a4.circle,0.005)) from annsystems a4 where a4.asid<>a1.asid) "
	        		+ "and SDO_GEOM.RELATE(s.location, 'anyinteract', a1.circle, 0.005)='TRUE' and SDO_GEOM.SDO_DISTANCE(a1.locus, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix+",NULL),NULL,NULL),0.005) "
	        		+ "= (select min(SDO_GEOM.SDO_DISTANCE(a2.locus, sdo_geometry(2001,NULL,sdo_point_type("+xpos_fix+","+ypos_fix+",NULL),NULL,NULL),0.005)) from annsystems a2) and a3.asid<>a1.asid";
			queries.add(s1);
			String q = "";
			for (int i=0;i<queries.size();i++) {
				if (i==0) {q = "Query"+(int)(i+1)+": "+queries.get(i); }
				else {q = q+"\n"+"Query"+(int)(i+1)+": "+queries.get(i);}
			}
			System.out.println(q);
			showField.setText(q);
		}
	}
}

