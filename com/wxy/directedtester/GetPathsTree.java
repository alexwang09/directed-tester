package com.wxy.directedtester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.android.chimpchat.adb.AdbChimpDevice;

public class GetPathsTree {

	private static ArrayList<ClickViewNode> curTabNodeList;

	public static void getPathsTree(String filePath, AdbChimpDevice device)
			throws IOException {

		String id = null;
		
		String mainAct = PathRecorder.getMainAct();
		int num = mainAct.indexOf("/");
		String packageName = mainAct.substring(0, num);
		System.out.println(packageName);
		ArrayList<String[]> pathsArrayList = new ArrayList<>();
		// 获取seed Paths
		pathsArrayList = getSeedPaths(filePath, device,mainAct,packageName);
		boolean flag = false;

		// 获取全路径树
		ClickViewNode rootNode = new ClickViewNode();
		TreeNode root = new TreeNode(rootNode);
		TreeNode currentNode = new TreeNode();
		root.setNodePath(root);
		// 添加安卓应用启动权限
		String action = "android.intent.action.MAIN";
		Collection<String> categories = new ArrayList<String>();
		categories.add("android.intent.category.LAUNCHER");

		// System.out.println(pathsArrayList.size());
		
		for (int i = 0; i < pathsArrayList.size(); i++) {
			if (i > 0) {
				// 重新进入应用
				device.startActivity(null,action,null,null,categories,new HashMap<String, Object>(),mainAct,0);
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			ArrayList<TreeNode> currentNodeList = new ArrayList<TreeNode>();
			currentNodeList.add(root);
			int pathsNum = 0;

			for (int j = 0; j < pathsArrayList.get(i).length; j++) {
				if (pathsArrayList.get(i)[j] != null) {
					pathsNum++;
				}
			}

			for (int j = 0; j < pathsNum; j++) {
				if (pathsArrayList.get(i)[j] == null) {
					break;
				}
				ArrayList<TreeNode> nextNodeList = new ArrayList<TreeNode>();
				int currentNodeNum = currentNodeList.size();
				for (int m = 0; m < currentNodeNum; m++) {
					currentNode = currentNodeList.get(m);
					ArrayList<Double> nodePath = currentNode.getNodePath();
					// 进入currentNode的页面
					if (currentNode != root) {
						for (int n = 0; n < nodePath.size() - 1; n = n + 2) {
							int time = 0;
							device.touch(
									nodePath.get(n).intValue(),
									nodePath.get(n + 1).intValue(),
									com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
							while (id.equals(PathRecorder.getCurrentAcId())
									&& time < 4) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								time++;
							}
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							//错误处理
							/*boolean errFlag = PathRecorder.isError();
							System.out.println(errFlag);
							if(!errFlag){
								device.touch(495,566,com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
					        	try {
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								n=0;
							}*/
							id = PathRecorder.getCurrentAcId();
						}
					}
					String type = pathsArrayList.get(i)[j];
					ArrayList<ClickViewNode> clickNodeList = new ArrayList<ClickViewNode>();
					ArrayList<ClickViewNode> tabNodeList = new ArrayList<ClickViewNode>();
					// 获取当前页面的可点击控件
					if (currentNode.getNode().isTab() == true) {
						if (flag == false) {
						
							clickNodeList = PathRecorder.getView();
							 System.out.println("first Tab");
							curTabNodeList = clickNodeList;
							flag = true;
						} else {
						
							tabNodeList = PathRecorder.getView();
							System.out.println("non-first Tab");
							for (int p = 0; p < tabNodeList.size(); p++) {
								for (int q = 0; q < curTabNodeList.size(); q++) {
									if (tabNodeList
											.get(p)
											.getId()
											.equals(curTabNodeList.get(q)
													.getId())) {
										// System.out.println("remove"+" "+tabNodeList.get(p).getId());
										tabNodeList.remove(p);
										p--;
										break;
									}
								}
							}
							clickNodeList = tabNodeList;
							curTabNodeList = PathRecorder.getView();
						}
					} else {
						clickNodeList = PathRecorder.getView();
					}
					// 找到type类型的控件构建路径树
					for (int k = 0; k < clickNodeList.size(); k++) {
						ClickViewNode clickNode = clickNodeList.get(k);
						if (clickNode.getType().equals(type)) {
							TreeNode node = new TreeNode(clickNode);
							currentNode.addChild(node);
							node.setParent(currentNode);
							// 每个节点生成路径
							node.setNodePath(currentNode);
							nextNodeList.add(node);
						}
					}
					id = PathRecorder.getCurrentAcId();
					int nodePathSize = 0;
					// System.out.println(j);
					// 在遍历倒数第二层节点的同时，依次遍历其子节点（最后一层节点）
					if (j == pathsNum - 1) {
						for (int k = 0; k < currentNode.getChildren().size(); k++) {
							int time = 0;
							ArrayList<Double> lastNodePath = currentNode
									.getChildren().get(k).getNodePath();
							device.touch(
									lastNodePath.get(2 * j).intValue(),
									lastNodePath.get(2 * j + 1).intValue(),
									com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
							while (id.equals(PathRecorder.getCurrentAcId())
									&& time < 4) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								time++;
							}
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							/*boolean errFlag = PathRecorder.isError();
							System.out.println(errFlag);
							if(!errFlag){
								try {
									Thread.sleep(4000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								device.touch(496,566,com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
					        	try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								k=0;
								continue;
							}*/
							id = PathRecorder.getCurrentAcId();
							time = 0;

							device.press(
									"KEYCODE_BACK",
									com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
							while (id.equals(PathRecorder.getCurrentAcId())
									&& time < 4) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								time++;
							}
							try {
								Thread.sleep(3500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							id = PathRecorder.getCurrentAcId();
						}
					}

					// 返回首页
					if (nodePath.size() > 0) {
						nodePathSize = nodePath.size();
						for (int p = 0; p < nodePathSize / 2; p++) {
							int time = 0;
							device.press(
									"KEYCODE_BACK",
									com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
							while (id.equals(PathRecorder.getCurrentAcId())
									&& time < 4) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								time++;
							}
							try {
								Thread.sleep(3500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				currentNodeList = nextNodeList;
			}
			// 退出应用
			System.out.println("退出应用");
			String cmd = "adb shell am force-stop " + packageName;
			CMDUtils.runCMD(cmd, null);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//清楚数据和缓存
			  device.startActivity(null, action, null, null, categories,
			     		new HashMap<String, Object>(),"com.android.settings/com.android.settings.Settings", 0);
        	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
         
        	device.touch(121,966,com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
        	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	device.touch(136,409,com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
        	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	device.touch(435,586,com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
        	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	device.touch(489,618,com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
        	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
        	cmd = "adb shell am force-stop " + "com.android.settings";
			CMDUtils.runCMD(cmd, null);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	//	if (device != null) {
	//		device.dispose();
	//		device = null;
	//	}
		// 遍历节点树
		System.out.println(0 + "  root");
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		treeNodes.add(root);
		ParseTree.parseTree(treeNodes, 0);
		// System.out.println(line);
	}

	public static ArrayList<String[]> getSeedPaths(String filePath,
			AdbChimpDevice device, String mainAct, String packageName) throws NumberFormatException, IOException {
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String[]> pathsArrayList = new ArrayList<>();
		// 添加安卓应用启动权限
		String action = "android.intent.action.MAIN";
		Collection<String> categories = new ArrayList<String>();
		categories.add("android.intent.category.LAUNCHER");

		device.startActivity(null,action,null,null,categories,new HashMap<String, Object>(),mainAct,0);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String line = "";
		String id = null;
		String[] pathTypeStrings = new String[100];
		int typeNum = 0;
		while ((line = br.readLine()) != null) {
			int l = line.indexOf("|");
			if (l != -1) {
				String type = line.substring(0, l);
				// System.out.println(type);
				switch (type) {
				case "TOUCH":
					double touchX = Double.parseDouble(line.substring(
							line.indexOf("'x'") + 4, line.indexOf("'y'") - 1));
					double touchY = Double
							.parseDouble(line.substring(
									line.indexOf("'y'") + 4,
									line.indexOf("'type'") - 1));
					String touchType = line.substring(
							line.indexOf("'type'") + 8, line.indexOf("}") - 2);
					// System.out.println(touchX+" "+touchY+" "+touchType);
					ArrayList<ClickViewNode> clickNodeList = new ArrayList<ClickViewNode>();
					int time1 = 0;
					// 获取当前页面的可点击控件
					id = PathRecorder.getCurrentAcId();
					clickNodeList = PathRecorder.getView();
					// 匹配控件，得到控件类型
					// System.out.println(clickNodeList.size());
					int num = -1;
					for (int i = 0; i < clickNodeList.size(); i++) {
						if (touchX >= clickNodeList.get(i).getMyAbsoluteLeft()
								&& touchX <= clickNodeList.get(i)
										.getMyAbsoluteLeft()
										+ clickNodeList.get(i).getMyWidth()
								&& touchY >= clickNodeList.get(i)
										.getMyAbsoluteTop()
								&& touchY <= clickNodeList.get(i)
										.getMyAbsoluteTop()
										+ clickNodeList.get(i).getMyHeight()) {
							if (num == -1) {
								num = i;
							} else {
								if (clickNodeList.get(i).getMyWidth() < clickNodeList
										.get(num).getMyAbsoluteLeft()
										+ clickNodeList.get(num).getMyWidth()
										|| clickNodeList.get(i).getMyHeight() < clickNodeList
												.get(num).getMyHeight()) {
									num = i;
								}
							}
						}
					}
					String pathType = clickNodeList.get(num).getType();
					pathTypeStrings[typeNum] = pathType;
					typeNum++;
					if (touchType.equals("downAndUp")) {
						device.touch(
								(int) touchX,
								(int) touchY,
								com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
						while (id.equals(PathRecorder.getCurrentAcId())
								&& time1 < 4) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							time1++;
						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					break;

				case "WAIT":
					/*int time2 = 0;
					for (int i = 0; i < typeNum; i++) {
						id = PathRecorder.getCurrentAcId();
						device.press(
								"KEYCODE_BACK",
								com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
						while (id.equals(PathRecorder.getCurrentAcId())
								&& time2 < 4) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							time2++;
						}
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						line = br.readLine();
					}*/
					String cmd = "adb shell am force-stop " + packageName;
					System.out.println("退出app");
					CMDUtils.runCMD(cmd, null);
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 重新进入应用
					device.startActivity(null,action,null,null,categories,new HashMap<String, Object>(),mainAct,0);
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					pathsArrayList.add(pathTypeStrings);
					pathTypeStrings = new String[100];
					typeNum = 0;
					break;
				}
			}
		}
		br.close();
		System.out.println(pathsArrayList.size());
		System.out.println(pathsArrayList.get(0)[0]+" "+pathsArrayList.get(0)[1]);
		System.out.println(pathsArrayList.get(1)[0]+" "+pathsArrayList.get(1)[1]);
		return pathsArrayList;
	}
}
