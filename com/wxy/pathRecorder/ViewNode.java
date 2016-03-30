package com.wxy.pathRecorder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewNode {
     
	private int layer;
	private String type;
	private double myWidth;
	private double myHeight;	
	private double myScrollX;
	private double myScrollY;
	private double myLeft;
	private double myTop;
	private String id;
	
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getMyWidth() {
		return myWidth;
	}

	public void setMyWidth(double myWidth) {
		this.myWidth = myWidth;
	}

	public double getMyHeight() {
		return myHeight;
	}

	public void setMyHeight(double myHeight) {
		this.myHeight = myHeight;
	}

	public double getMyScrollX() {
		return myScrollX;
	}

	public void setMyScrollX(double myScrollX) {
		this.myScrollX = myScrollX;
	}

	public double getMyScrollY() {
		return myScrollY;
	}

	public void setMyScrollY(double myScrollY) {
		this.myScrollY = myScrollY;
	}

	public double getMyLeft() {
		return myLeft;
	}

	public void setMyLeft(double myLeft) {
		this.myLeft = myLeft;
	}

	public double getMyTop() {
		return myTop;
	}

	public void setMyTop(double myTop) {
		this.myTop = myTop;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
     
	public  ViewNode(String line,boolean a){
    	   char[] ch=line.toCharArray();
	        int k=0;
	       while(ch[k]==' '){
	        	k++;
	        }
	       this.layer=k;
	          
	       String sub=line.substring(0,line.indexOf("@"));
	       int typeStart=sub.lastIndexOf(".")+1;
	       this. type=sub.substring(typeStart);
	      
	      String idString=line.substring(line.indexOf("@")+1,line.indexOf("@")+9);
	      this.id=idString;
	   /*   int xStart=line.indexOf("getX()=")+9;
	      int xEnd=xStart;
	      while(ch[xEnd]!=' '){
	    	  xEnd++;
	      }
	      this. myX=Double.parseDouble(line.substring(xStart, xEnd));
	      
	      
	      int yStart=line.indexOf("getY()=")+9;
	      int yEnd=yStart;
	      while(ch[yEnd]!=' '){
	    	  yEnd++;
	      }
	      this. myY=Double.parseDouble(line.substring(yStart, yEnd));
	      */

	      int scrollXStart=line.indexOf("mScrollX=")+11;
	      int scrollXEnd=scrollXStart;
	      while(ch[scrollXEnd]!=' '){
	    	  scrollXEnd++;
	      }
	     
	      this. myScrollX=Double.parseDouble(line.substring(scrollXStart, scrollXEnd));
	      
	      int scrollYStart=line.indexOf("mScrollY=")+11;
	      int scrollYEnd=scrollYStart;
	      while(ch[scrollYEnd]!=' '){
	    	  scrollYEnd++;
	      }
	      this. myScrollY=Double.parseDouble(line.substring(scrollYStart, scrollYEnd));
	      
	      int myLeftStart=line.indexOf("mLeft=")+8;
	      int myLeftEnd=myLeftStart;
	      while(ch[myLeftEnd]!=' '){
	    	  myLeftEnd++;
	      }
	      this. myLeft=Double.parseDouble(line.substring(myLeftStart, myLeftEnd));
	      
	      int mTopStart=line.indexOf("mTop=")+7;
	      int mTopEnd=mTopStart;
	      while(ch[mTopEnd]!=' '){
	    	  mTopEnd++;
	      }
	      this. myTop=Double.parseDouble(line.substring(mTopStart, mTopEnd));
	      
	      if(a){
	      int widthStart=line.indexOf("mMeasuredWidth=")+17;
	      int widthEnd=widthStart;
	      while(ch[widthEnd]!=' '){
	    	  widthEnd++;
	      }
	      this. myWidth=Double.parseDouble(line.substring(widthStart,widthEnd));
	      
	      
	      int heightStart=line.indexOf("mMeasuredHeight=")+18;
	      int heightEnd=heightStart;
	      while(ch[heightEnd]!=' '){
	    	  heightEnd++;
	      }
	      this. myHeight=Double.parseDouble(line.substring(heightStart, heightEnd));
	     
	      }
       }
	
	
}
