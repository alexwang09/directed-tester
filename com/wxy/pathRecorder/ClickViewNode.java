package com.wxy.pathRecorder;


public class ClickViewNode {
	
	private String type;
	private double myAbsoluteLeft;
	private double myAbsoluteTop;
	private double myWidth;
	private double myHeight;
	private double myXCenter;
	private double myYCenter;
	private String id;	
	
	 
	public double getMyXCenter() {
		return myXCenter;
	}

	public void setMyXCenter() {
		this.myXCenter = this.myAbsoluteLeft+this.myWidth/2;
	}

	public double getMyYCenter() {
		return myYCenter;
	}

	public void setMyYCenter() {
		this.myYCenter = this.myAbsoluteTop+this.myHeight/2;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


      
      public  ClickViewNode(ViewNode node){
    	  this.type=node.getType();
    	  this.myWidth=node.getMyWidth();
    	  this.myHeight=node.getMyHeight();
    	  this.id=node.getId();
      }
      
      public  ClickViewNode(){
    	  
      }

	public double getMyAbsoluteLeft() {
		return myAbsoluteLeft;
	}

	public void setMyAbsoluteLeft(double myAbsoluteLeft) {
		this.myAbsoluteLeft = myAbsoluteLeft;
	}

	public double getMyAbsoluteTop() {
		return myAbsoluteTop;
	}

	public void setMyAbsoluteTop(double myAbsoluteTop) {
		this.myAbsoluteTop = myAbsoluteTop;
	}


}
