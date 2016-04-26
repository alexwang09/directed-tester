package com.wxy.directedtester;

import java.util.ArrayList;
import java.util.List;


public class TreeNode {

	ClickViewNode node;
	private List<TreeNode> children = new ArrayList<TreeNode>();
	private TreeNode parent;
	private ArrayList<Double> nodePath = new ArrayList<>();

	public ArrayList<Double> getNodePath() {
		return nodePath;
	}

	public void setNodePath(TreeNode currentNode) {
		if (this == currentNode) {

		} else {

			ArrayList<Double> currentNodePathList = currentNode.nodePath;
			// .....
			if (currentNode.nodePath != null) {
				for (int i = 0; i < currentNodePathList.size(); i++) {
					this.nodePath.add(currentNodePathList.get(i));
				}
			}
			// System.out.println(this.node.getMyXCenter());
			this.nodePath.add(this.node.getMyXCenter());
			this.nodePath.add(this.node.getMyYCenter());
		}
	}

	public TreeNode(ClickViewNode node) {
		this.node = node;
	}

	public TreeNode() {

	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public void addChild(TreeNode p) {
		children.add(p);
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public ClickViewNode getNode() {
		return node;
	}

	public void setNode(ClickViewNode node) {
		this.node = node;
	}

}
