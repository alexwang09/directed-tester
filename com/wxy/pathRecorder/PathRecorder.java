package com.wxy.pathRecorder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import org.omg.CORBA.PUBLIC_MEMBER;

public class PathRecorder {
	public static boolean preConfig() {
		boolean flag = false;
		// .......
		String deviceId = "192.168.56.101:5555";
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

				index1 = line.indexOf("isClickable()=");
				c1 = line.charAt(index1 + 14);
				if (c1 == '4') {
					// index2=line.indexOf("@");
					// idArray.add(line.substring(index2+1,index2+9));
					ViewNode node = new ViewNode(line, true);
					nodeList.add(node);
					clickNodeIndex.add(j);
				} else {
					ViewNode node = new ViewNode(line, false);
					nodeList.add(node);
				}
				context1 += line + "\r\n";
				j++;
			}
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

		// for(i=0;i<clickNodeList.size();i++){
		// System.out.print(clickNodeList.get(i).getType()+" "+clickNodeList.get(i).getId()+" "+clickNodeList.get(i).getMyAbsoluteLeft()+" "+clickNodeList.get(i).getMyAbsoluteTop()+" "+clickNodeList.get(i).getMyWidth()+" "+clickNodeList.get(i).getMyHeight()+"\n");
		// }
		return clickNodeList;
	}

}