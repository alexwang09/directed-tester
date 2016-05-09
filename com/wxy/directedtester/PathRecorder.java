package com.wxy.directedtester;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import com.wxy.node.ClickViewNode;
import com.share.util.ShareUtil;
import com.wxy.node.ViewNode;

public class PathRecorder {
	public static boolean preConfig() {
		boolean flag = false;
		// .......
		String deviceId = ShareUtil.deviceId;
		String port = "4939";
		String cmd = "adb -s " + deviceId + " forward tcp:" + port
				+ " tcp:4939";

		CMDUtils.runCMD(cmd, null);
		cmd = "adb -s " + deviceId + " shell service call window 3";
		String result = CMDUtils.runCMD(cmd, null);
		int index = result.indexOf("1");
		if (index > -1) {
			flag = true;
		} else {
			cmd = "adb -s " + deviceId + " shell service call window 1 i32 "
					+ port;
			result = CMDUtils.runCMD(cmd, null);
			index = result.indexOf("1");
			if (index > -1) {
				flag = true;
			}
		}
		return flag;
	}

	public static boolean isError(){
		String id = null;
		String context = "";
		String line;
		boolean flag = true;
		int num;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("127.0.0.1", 4939), 40000);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "utf-8"));

			out.write("LIST");
			out.newLine();
			out.flush();

			// receive response from viewserver
			while ((line = in.readLine()) != null) {
				if ("DONE.".equalsIgnoreCase(line)) { //$NON-NLS-1$
					break;
				}

				if (line.contains(".")) {
					num = line.indexOf(":");
					if(num>=0){
						String str = line.substring(0,num);
						if(str.contains("Application Error")){
							flag = false;
							break;
						}
					}
					
				}
				 num=-1;
			}
			in.close();
			out.close();
		} catch (IOException e) {  
            e.printStackTrace();  
        } 
		
		return flag;
	}

	public static String getMainAct(){
		String mainAct = null;
		String context = "";
		String line;
		int num;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("127.0.0.1", 4939), 40000);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "utf-8"));

			out.write("LIST");
			out.newLine();
			out.flush();

			// receive response from viewserver
			while ((line = in.readLine()) != null) {
				if ("DONE.".equalsIgnoreCase(line)) { //$NON-NLS-1$
					break;
				}

				if (line.contains(".")) {
					num = line.indexOf(" ");
					mainAct = line.substring(num+1);
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mainAct;
	}
	
	public static String getCurrentAcId() {
		String id = null;
		String context = "";
		String line;
		int num;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("127.0.0.1", 4939), 40000);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "utf-8"));

			out.write("LIST");
			out.newLine();
			out.flush();

			// receive response from viewserver
			while ((line = in.readLine()) != null) {
				if ("DONE.".equalsIgnoreCase(line)) { //$NON-NLS-1$
					break;
				}

				if (line.contains(".")) {
					num = line.indexOf(" ");
					id = line.substring(0, num);
				}

				// System.out.println(str);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;

	}

	public static ArrayList<ClickViewNode> getView() {

		// String context="";
		String context1 = "";
		String line;
		/*
		 * try{ Socket socket = new Socket(); socket.connect(new
		 * InetSocketAddress("127.0.0.1", 4939),40000); BufferedWriter out = new
		 * BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		 * BufferedReader in = new BufferedReader(new
		 * InputStreamReader(socket.getInputStream(), "utf-8"));
		 * 
		 * out.write("LIST"); out.newLine(); out.flush();
		 * 
		 * //receive response from viewserver while ((line = in.readLine()) !=
		 * null) { if ("DONE.".equalsIgnoreCase(line)) { //$NON-NLS-1$ break; }
		 * context+=line+"\r\n"; }
		 * 
		 * } catch ( Exception e ) { e.printStackTrace(); }
		 */

		line = "";
		int i, index1;
		char c1;
		// ArrayList<String> idArray = new ArrayList<String>();
		ArrayList<ViewNode> nodeList = new ArrayList<ViewNode>();
		ArrayList<ClickViewNode> clickNodeList = new ArrayList<ClickViewNode>();
		ArrayList<ClickViewNode> tabNodeList = new ArrayList<ClickViewNode>();
		ArrayList<Integer> clickNodeIndex = new ArrayList<Integer>();
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("127.0.0.1", 4939), 40000);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "utf-8"));
			out.write("DUMP ffffffff");
			out.newLine();
			out.flush();

			int j = 0, k = 0;
			while ((line = in.readLine()) != null) {
				if ("DONE.".equalsIgnoreCase(line)) { //$NON-NLS-1$
					break;
				}
				
				String sub = line.substring(0, line.indexOf("@"));
				int typeStart = sub.lastIndexOf(".") + 1;
				String type = sub.substring(typeStart);
				
				index1 = line.indexOf("isClickable()=");
				c1 = line.charAt(index1 + 14);
				if (c1 == '4'||type.equals("TabWidget")) {
					// index2=line.indexOf("@");
					// idArray.add(line.substring(index2+1,index2+9));
					ViewNode node = new ViewNode(line, true);
					nodeList.add(node);
					clickNodeIndex.add(j);
				} else {
					ViewNode node = new ViewNode(line, false);
					nodeList.add(node);
				}
				//context1 += line + "\r\n";
				j++;
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.print(context);
		// System.out.print(context1);

		// for(i=0;i<nodeList.size();i++){
		// System.out.print(nodeList.get(i).layer+" "+nodeList.get(i).type+" "+nodeList.get(i).myX+" "+nodeList.get(i).myY+" "+nodeList.get(i).myWidth+" "+nodeList.get(i).myHeight+" "+(nodeList.get(i).myLeft-nodeList.get(i).myScrollX)+" "+(nodeList.get(i).myTop-nodeList.get(i).myScrollY)+" "+"\n");
		// }

		// for(i=0;i<clickNodeIndex.size();i++){
		// System.out.println(clickNodeIndex.get(i));
		// }

		for (i = 0; i < clickNodeIndex.size(); i++) {

			int index2 = clickNodeIndex.get(i);
			int layer = nodeList.get(index2).getLayer();
			double absoluteLeft = nodeList.get(index2).getMyLeft()
					- nodeList.get(index2).getMyScrollX();
			double absoluteTop = nodeList.get(index2).getMyTop()
					- nodeList.get(index2).getMyScrollY();

			for (int j = index2 - 1; j >= 0; j--) {
				if (nodeList.get(j).getLayer() == layer - 1) {
					absoluteLeft += nodeList.get(j).getMyLeft()
							- nodeList.get(j).getMyScrollX();
					absoluteTop += nodeList.get(j).getMyTop()
							- nodeList.get(j).getMyScrollY();
					layer--;
				}
				if (layer <= 0)
					break;
			}
			ClickViewNode clickNode = new ClickViewNode(nodeList.get(index2));
			clickNode.setMyAbsoluteLeft(absoluteLeft);
			clickNode.setMyAbsoluteTop(absoluteTop);
			clickNode.setMyXCenter();
			clickNode.setMyYCenter();
			clickNodeList.add(clickNode);
		}
        //过滤view
		double tabAbsoluteLeft = -1,tabAbsoluteTop = -1,tabWidth = -1,tabHeight = -1;
		for(i=0;i<clickNodeList.size();i++){
			if(clickNodeList.get(i).getType().equals("TabWidget")){
				tabAbsoluteLeft = clickNodeList.get(i).getMyAbsoluteLeft();
				tabAbsoluteTop=clickNodeList.get(i).getMyAbsoluteTop();
				tabWidth=clickNodeList.get(i).getMyWidth();
				tabHeight=clickNodeList.get(i).getMyHeight();
				break;
			}
		}
		
		//除去与tab栏冲突的和超出屏幕范围的view
	
		for(i=0;i<clickNodeList.size();i++){
			double  myXCenter = clickNodeList.get(i).getMyXCenter();
			double myYCenter=clickNodeList.get(i).getMyYCenter();
			
			if(myXCenter>=ShareUtil.sceenXSize || myXCenter<=0 || myYCenter<=0 || myYCenter>=ShareUtil.sreenYSize){
				clickNodeList.remove(i);
				i--;
				continue;
			}
			
			if(tabAbsoluteLeft>=0){
				double myXLeft = clickNodeList.get(i).getMyAbsoluteLeft();
				double myXRight = clickNodeList.get(i).getMyAbsoluteLeft()+ clickNodeList.get(i).getMyWidth();
				double myYTop = clickNodeList.get(i).getMyAbsoluteTop();
				double myYBottom= clickNodeList.get(i).getMyAbsoluteTop()+clickNodeList.get(i).getMyHeight();
				
				if(myXCenter>=tabAbsoluteLeft&&myXCenter<=(tabAbsoluteLeft+tabWidth)&&myYCenter>=tabAbsoluteTop&&myYCenter<=(tabAbsoluteTop+tabHeight)){
					if(myXLeft<tabAbsoluteLeft||myXRight>tabAbsoluteLeft+tabWidth||myYTop<tabAbsoluteTop||myYBottom>tabAbsoluteTop+tabHeight){
						clickNodeList.remove(i);
						i--;
					}else{
						clickNodeList.get(i).setTab(true);
					}
				}
			}
		}

		return clickNodeList;
	}

}