package com.wxy.pathRecorder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.python.antlr.PythonParser.return_stmt_return;

import com.android.chimpchat.adb.AdbChimpDevice;


public class GetPathsTree {

	public static void getPathsTree(String filePath,AdbChimpDevice device) throws IOException {
		
		String id=null;
		 ArrayList<String[] >pathsArrayList=new ArrayList<>();
		 //获取seed Paths
		 pathsArrayList=getSeedPaths(filePath,device);
		 
		 //paths的预处理，合并相同项
		 //tab切换
		 
		 
		 
	//	for(int i=0;i<pathsArrayList.size();i++){
	//		System.out.println(pathsArrayList.get(i)[0]+" "+pathsArrayList.get(i)[1]);
	//	}
		//获取全路径树
	    ClickViewNode rootNode=new ClickViewNode();
	    TreeNode root=new TreeNode(rootNode);
	    TreeNode currentNode=new TreeNode();
	    root.setNodePath(root);
	    //添加安卓应用启动权限
        String action = "android.intent.action.MAIN";   
         Collection<String> categories = new ArrayList<String>();   
         categories.add("android.intent.category.LAUNCHER");
	    
       //  System.out.println(pathsArrayList.size());
		   for(int i=0;i<pathsArrayList.size();i++){
			   ArrayList<TreeNode> currentNodeList=new ArrayList<TreeNode>();
			    currentNodeList.add(root);
			 for(int j=0;j<pathsArrayList.get(i).length;j++){
			   //System.out.println(type);
				 if(pathsArrayList.get(i)[j]==null)
					 break;
					 
				
				 
				  ArrayList<TreeNode> nextNodeList=new ArrayList<TreeNode>();
				 int currentNodeNum=currentNodeList.size();
				// System.out.println(currentNodeList.size());
				 for(int m=0;m<currentNodeNum;m++){
					 //进入首页
					 //device.startActivity(null, action, null, null, categories,
					   //   		new HashMap<String, Object>(),"com.hiapk.marketpho/com.baidu.appsearch.MainTabActivity", 0); 
					 
					 currentNode=currentNodeList.get(m);
					ArrayList<Double> nodePath = currentNode.getNodePath();
				
					//进入currentNode的页面
					if(currentNode!=root){
						//System.out.println("nonroot");
			     for(int n=0;n<nodePath.size()-1;n=n+2){
			    	   int time=0;
			    	 device.touch( nodePath.get(n).intValue(),nodePath.get(n+1).intValue(),com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
			    	 while(id.equals(PathRecorder.getCurrentAcId())&&time<4){
						 try {
					          Thread.sleep(1000);
					      } catch (InterruptedException e) {
					          e.printStackTrace(); 
					      }
						 time++;
					}
					id=PathRecorder.getCurrentAcId();
			     }
			}
				 String type=pathsArrayList.get(i)[j];
		   		 ArrayList<ClickViewNode> clickNodeList=new ArrayList<ClickViewNode>();
				
				//获取当前页面的可点击控件
				clickNodeList=PathRecorder.getView();
				
				//找到type类型的控件构建路径树	
			    	for(int k=0;k<clickNodeList.size();k++){
			    		
			    		ClickViewNode clickNode = clickNodeList.get(k);
			    		
						if(clickNode.getType().equals(type)){
							 TreeNode node=new TreeNode(clickNode);
							 currentNode.addChild(node);
							 node.setParent(currentNode); 
							 //每个节点生成路径
							 node.setNodePath(currentNode);
							// for(int p=0;p<node.getNodePath().size();p++){
							//	 System.out.print(node.getNodePath().get(p)+"  ");
							// }
							// System.out.print("\n");
							 nextNodeList.add(node);
						}		
					}	 
			    	
			    	id=PathRecorder.getCurrentAcId();
					int nodePathSize=0;
		
					if(nodePath.size()>0){
					   nodePathSize=nodePath.size();
						for(int p=0;p<nodePathSize/2;p++){
							device.press("KEYCODE_BACK", com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
							try {
		  				          Thread.sleep(2000);
		  				      } catch (InterruptedException e) {
		  				          e.printStackTrace(); 
		  				      }
						}
					}
					 try {
				          Thread.sleep(2000);
				      } catch (InterruptedException e) {
				          e.printStackTrace(); 
				      }
		     }
				 
				 //。。
				 currentNodeList=nextNodeList;
		  }
		 }
		   //遍历节点树
		   System.out.println(0+"  root");
		   List<TreeNode> treeNodes=new ArrayList<TreeNode>();
		   treeNodes.add(root);
		   ParseTree.parseTree(treeNodes,0);
				
	     //  System.out.println(line);
        
}
	
	
	@SuppressWarnings("null")
	public static ArrayList<String[]> getSeedPaths(String filePath, AdbChimpDevice device) throws NumberFormatException, IOException {
		
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
	    ArrayList<String[] >pathsArrayList=new ArrayList<>();
	    
	    //添加安卓应用启动权限
        String action = "android.intent.action.MAIN";   
         Collection<String> categories = new ArrayList<String>();   
         categories.add("android.intent.category.LAUNCHER");
         
  	   String line="";
  	   String id=null;
  	   String[] pathTypeStrings = new String[100];
	   int typeNum = 0;
	   while((line=br.readLine())!=null){
		   int l=line.indexOf("|");
		   if(l!=-1){
		   String type=line.substring(0,l);
		   //System.out.println(type);
		   switch (type) {
		   
		case "TOUCH": 
			double touchX=Double.parseDouble(line.substring(line.indexOf("'x'")+4,line.indexOf("'y'")-1));
			double touchY=Double.parseDouble(line.substring(line.indexOf("'y'")+4,line.indexOf("'type'")-1));
			String touchType=line.substring(line.indexOf("'type'")+8,line.indexOf("}")-2);
			//System.out.println(touchX+" "+touchY+" "+touchType);
		
			ArrayList<ClickViewNode> clickNodeList=new ArrayList<ClickViewNode>();
			int time1=0;
			//获取当前页面的可点击控件
			
				id=PathRecorder.getCurrentAcId();
		    	clickNodeList=PathRecorder.getView();
			
			
			//匹配控件，得到控件类型
			//System.out.println(clickNodeList.size());
			int num=-1;
			for(int i=0;i<clickNodeList.size();i++){
				if(touchX>=clickNodeList.get(i).getMyAbsoluteLeft()&&touchX<=clickNodeList.get(i).getMyAbsoluteLeft()+clickNodeList.get(i).getMyWidth()
						&&touchY>=clickNodeList.get(i).getMyAbsoluteTop()&&touchY<=clickNodeList.get(i).getMyAbsoluteTop()+clickNodeList.get(i).getMyHeight()){
				if(num==-1){
	                  num=i;
				} 
				else {
					if(clickNodeList.get(i).getMyWidth()<clickNodeList.get(num).getMyAbsoluteLeft()+clickNodeList.get(num).getMyWidth()
							||clickNodeList.get(i).getMyHeight()<clickNodeList.get(num).getMyHeight()){
						num=i;
					}
				}
			}        
			}
		//	System.out.print(num);
			
			String pathType=clickNodeList.get(num).getType();
			//System.out.print(pathType);
			pathTypeStrings[typeNum]=pathType;
			typeNum++;
			
		//..
		//System.out.println(touchType);
		if(touchType.equals("downAndUp")){
			//System.out.println("success touch");
		 device.touch( (int) touchX,(int)touchY,com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
		 
		 try {
	          Thread.sleep(2000);
	      } catch (InterruptedException e) {
	          e.printStackTrace(); 
	      }
		 
		 while(id.equals(PathRecorder.getCurrentAcId())&&time1<4){
			 try {
		          Thread.sleep(1000);
		      } catch (InterruptedException e) {
		          e.printStackTrace(); 
		      }
			 time1++;
		}
		 
		}
			break;
			
		 case "WAIT":
			 
			 int time2=0;
             for(int i=0;i<typeNum;i++){
            	 id=PathRecorder.getCurrentAcId();
            		device.press("KEYCODE_BACK", com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
            		 
            		while(id.equals(PathRecorder.getCurrentAcId())&&time2<4){
   					 try {
   				          Thread.sleep(1000);
   				      } catch (InterruptedException e) {
   				          e.printStackTrace(); 
   				      }
   					 time2++;
   				}
            		line=br.readLine();
			 }
			typeNum=0;
			pathsArrayList.add(pathTypeStrings);
			pathTypeStrings=new String[100];
		//	 device.startActivity(null, action, null, null, categories,
			//      		new HashMap<String, Object>(),"com.hiapk.marketpho/com.baidu.appsearch.MainTabActivity", 0); 
			 
			
			 
			 break;
		   }
		 }
	   }
	   
	   try {
	          Thread.sleep(4000);
	      } catch (InterruptedException e) {
	          e.printStackTrace(); 
	      }
	   
	   br.close();
	   return pathsArrayList;
	}
}
