package com.wxy.pathRecorder;

import java.util.ArrayList;
import java.util.List;

import com.kenai.jffi.Array;

public class ParseTree {

	static public void parseTree(List<TreeNode> treeNodes,int num) {
		List<TreeNode> childs=new ArrayList<>();
	    
	    num++;
		if(treeNodes.size()>0){
			for (int i = 0, len = treeNodes.size(); i < len; i++) {
                    for(int j=0;j<treeNodes.get(i).getChildren().size();j++){
	                     childs.add(treeNodes.get(i).getChildren().get(j));
	                     System.out.print(num+"  ");
                         System.out.println(treeNodes.get(i).getChildren().get(j).getNode().getType()+" "+
                         treeNodes.get(i).getChildren().get(j).getNode().getId());
                    }
	            if(childs.size()>0){
	                 parseTree(childs,num);
	            }
		}
	}
	}
}
