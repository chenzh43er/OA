package com.shop.test;

public class Tree {
    TreeNode root = null;
    public Tree(String name){
        root = new TreeNode(name);
    }

    public static void main(String args[]){

        Tree tree = new Tree("a");
        TreeNode node1 = new TreeNode("b");
        TreeNode node2 = new TreeNode("c");

        node1.add(node2);
        tree.root.add(node1);

        System.out.println("bulid this tree");

        System.out.println(tree);

        System.out.println('د');
    }
}
