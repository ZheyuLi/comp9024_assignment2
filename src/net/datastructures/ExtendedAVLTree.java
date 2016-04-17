/*
 * COMP 9024 assignment 2
 * Author:Huijun Wu(z5055605)
 * Date:15/04/2016
*/
package datastructures;
import java.util.*;

import javax.swing.*;

import datastructures.AVLTree.AVLNode;

import java.awt.*;

/*Q1:Clone an AVLTree and return a reference to the cloned new AVLTree object.
 */
public class ExtendedAVLTree<K,V> extends AVLTree<K,V>
{
	/**
	 * As the draw methods can only be called by extending JComponent, we define some classes for nodes,
	 * edges and node contents. JComponent is extending java.awt.component as well. 
	 */
	// Define graphical class for internal nodes. 
    protected static class InternalNode extends JComponent {

        private static final long serialVersionUID = 1L; //for serializable class
        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;
        
        InternalNode(int x, int y, int d) {
            this.x = x; //use the top of internal node as the position rather than middle point
            this.y = y;
            this.w = d; //major axis for the parameters of drawOval
            this.h = d; //minor axis for the parameters of drawOval
        }
        //override the original paint function of component to draw a circle for each internal node.
        @Override
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);
            g.drawOval(x, y, w, h);
        }
    }

    // Define graphical class for external nodes. 
    protected static class ExternalNode extends JComponent {

        private static final long serialVersionUID = 1L;
        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;
        
        ExternalNode(int x, int y, int w, int h) {
            this.x = x; //use the top of internal node as the position rather than middle point
            this.y = y;
            this.w = w; //length for the external node 
            this.h = h; //height for the external node
        }
        //override the paint function to draw a rectangle for external nodes. 
        @Override
        protected void paintComponent(Graphics g) {
            g.drawRect(x, y, w, h);
        }
    }
    
    // Define edges class for connecting nodes.
    protected static class Edge extends JComponent {

        private static final long serialVersionUID = 1L;
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        //(x1,y1) (x2,y2) are the two end of the line.
        Edge(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
        //override the paint function to draw a line
        @Override
        protected void paintComponent(Graphics g) {
            g.drawLine(x1, y1, x2, y2);
        }
    }

    // Define labels for the content of internal nodes.
    protected static class Labels extends JComponent {
        private static final long serialVersionUID = 1L;
        String s; 
        int x = 0;
        int y = 0;
        
        Labels(String s, int x, int y) {
            this.s = s;
            this.x = x;
            this.y = y;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            g.drawString(s, x, y);
        }
    }

	public static <K, V> void cloneSubTrees(BTPosition<Entry<K,V>> old_root, BTPosition<Entry<K,V>> cloned_root)
	{
		BTPosition<Entry<K,V>> old_leftChild = old_root.getLeft();
		BTPosition<Entry<K,V>> old_rightChild = old_root.getRight();
		
		if(old_leftChild!=null)
		{
			AVLNode<K,V> cloned_leftchild = new AVLNode<K,V>(old_leftChild.element(),null,null,null);
			cloned_root.setLeft(cloned_leftchild);
			cloned_leftchild.setParent(cloned_root);
			cloneSubTrees(cloned_leftchild, cloned_root.getLeft());
			
		}
		if(old_rightChild!=null)
		{
			AVLNode<K,V> cloned_rightchild = new AVLNode<K,V>(old_rightChild.element(),null,null,null);
			cloned_root.setRight(cloned_rightchild);
			cloned_rightchild.setParent(cloned_root);
			cloneSubTrees(cloned_rightchild, cloned_root.getRight());
		}	
		
	}

	public static <K, V> AVLTree<K, V> clone(AVLTree<K,V> tree)
	{
		AVLTree<K,V> cloned_tree = new AVLTree<K,V>();
		cloned_tree.addRoot(tree.root().element());
		cloneSubTrees(tree.root,cloned_tree.root);
		cloned_tree.size = tree.size();
		
		return cloned_tree;

	}
	
	/*Q2:Merge two AVLTree and return the reference of the merged tree. 
	 */
	public static <K, V> AVLTree<K, V> merge(AVLTree<K,V> tree1, AVLTree<K,V> tree2)
	{
		AVLTree<K,V> merged_tree = new AVLTree<K,V>();
		NodePositionList<Position<Entry<K,V>>> tree1List = new NodePositionList<Position<Entry<K,V>>>();
		NodePositionList<Position<Entry<K,V>>> tree2List = new NodePositionList<Position<Entry<K,V>>>();
		//add the nodes of these two trees into nodePositionList, inorder traversal would gurantee the order of the nodes
		
		tree1.inorderPositions(tree1.root(),tree1List);
		tree2.inorderPositions(tree2.root(),tree2List);
		
		/*System.out.println("traversal tree1list");
		Iterator<Position<Entry<K,V>>> it11 = tree1List.iterator();
		while(it11.hasNext())
		{
			Entry<K,V> tmp1 = it11.next().element();
			if(tmp1 == null)
			{
				System.out.println("null");
			}
			else
			{
				System.out.println(tmp1.getKey().toString());
			}
		}
		
		System.out.println("traversal tree2list");
		Iterator<Position<Entry<K,V>>> it12 = tree2List.iterator();
		while(it12.hasNext())
		{
			Entry<K,V> tmp2 = it12.next().element();
			if(tmp2 == null)
			{
				System.out.println("null");
			}
			else
			{
			//	System.out.println(tmp2.getKey().toString());
			}
		}*/
		ArrayList<Entry<K,V>> merged_array = new ArrayList<Entry<K,V>>();

		
		while((tree1List.size()>0)&&(tree2List.size()>0))
		{
		//	Entry<K,V> tree1first = tree1List.first().element().element();

		//	Entry<K,V> tree2first = tree2List.first().element().element();
			
			while(!tree1List.isEmpty()&&(tree1List.first().element().element() == null))
			{

				tree1List.remove(tree1List.first());
			//	System.out.println("first of tree1First is null, delete and the size now is");
			//	System.out.print(tree1List.size());
			}
			while(!tree2List.isEmpty()&&(tree2List.first().element().element() == null))
			{

				tree2List.remove(tree2List.first());
			
			//	System.out.println("first of tree2First is null, delete and the size now is");
			//	System.out.print(tree2List.size());
			}
			if(!tree1List.isEmpty()&&!tree2List.isEmpty()&&Integer.parseInt(tree1List.first().element().element().getKey().toString())< Integer.parseInt(tree2List.first().element().element().getKey().toString()))
			{
				merged_array.add(tree1List.first().element().element());
			//	System.out.print(tree1List.first().element().element().getKey());
				tree1List.remove(tree1List.first());
			//	System.out.println("add element and delete, The size of tree1List is");
			
			//	System.out.print(tree1List.size());
			}
			else
			{	
				if(!tree2List.isEmpty()){
				merged_array.add(tree2List.first().element().element());
		//		System.out.print(tree2List.first().element().element().getKey());
				tree2List.remove(tree2List.first());}
		//		System.out.println("add element and delete, The size of tree2List is");
			//	System.out.print(tree2List.size());
			}

		}

		if(tree1List.size() == 0)
		{
			Iterator<Position<Entry<K,V>>> it2 = tree2List.iterator();
			while(it2.hasNext())
			{	
				Entry<K,V> tmp_element = it2.next().element();
				if(tmp_element != null){
					merged_array.add(it2.next().element());}
			}
			
		}
		else if(tree2List.size() == 0)
		{
			Iterator<Position<Entry<K,V>>> it1 = tree1List.iterator();
			Entry<K,V> tmp_element1 = it1.next().element();
			if(tmp_element1 != null){
				merged_array.add(it1.next().element());}
		}
		int merged_tree_size = merged_array.size();
		//merged_tree = merged_tree_size;
		int height = 0;
		merged_tree.root = MergedArray2AVLTree(0,(merged_array.size()-1), merged_array,merged_tree,height);
		merged_tree.numEntries = tree1.size() + tree2.size();
		
	
	//	System.out.println("the height of the merged_tree is ");
	//	System.out.print(merged_tree.height(merged_tree.root()));

		//assign null to tree1 and tree 2 for GC
		tree1 = null;
		tree2 = null;
	
		
		return merged_tree;
		
	}
	
	protected static <K,V>  BTPosition<Entry<K,V>>  MergedArray2AVLTree(int start, int end, ArrayList<Entry<K,V>> merged_array, AVLTree<K,V> merged_tree,int height)
	{
		if(start > end)
		{
			return null;
		}
		
		int mid = start + (end - start)/2;
		Entry<K,V> parent_element = merged_array.get(mid);
		BTPosition<Entry<K,V>> leftChild = MergedArray2AVLTree(start,mid-1, merged_array,merged_tree,height);
		BTPosition<Entry<K,V>> rightChild = MergedArray2AVLTree(mid+1,end, merged_array,merged_tree,height);
		BTPosition<Entry<K,V>> parent = merged_tree.createNode(parent_element, null, leftChild, rightChild);
		parent.setLeft(leftChild);
		parent.setRight(rightChild);
		merged_tree.size = merged_tree.size+2;
		
		if((leftChild == null) && (rightChild == null))
		{
			((AVLNode<K,V>)(parent)).setHeight(1);
		}
		else if(leftChild == null)
		{
			((AVLNode<K,V>)(parent)).setHeight(1+((AVLNode<K,V>)(rightChild)).height);
		}
		else if(rightChild == null)
		{
			((AVLNode<K,V>)(parent)).setHeight(1+((AVLNode<K,V>)(leftChild)).height);
		}
		else
		{
			((AVLNode<K,V>)(parent)).setHeight(1+Math.max(((AVLNode<K,V>)(leftChild)).height,((AVLNode<K,V>)(rightChild)).height));
		}

		//System.out.println("the size of merged_tree is");
		//System.out.print(merged_tree.size);
		if(leftChild != null)
		{
			leftChild.setParent(parent);
		}
		if(rightChild != null)
		{
			
			rightChild.setParent(parent);
		}
		
		/*if(parent.element() ==  null)
		{
			System.out.println("null");
		}
		else
		{
			System.out.println(parent.element().getKey().toString());
		}*/
		return parent;	
	}
	
	public static <K, V> void print(AVLTree<K, V> tree)
	{
		JFrame treeFrame = new JFrame();
		JPanel treePanel = new JPanel();
		//treeFrame.getContentPane().add(treePanel);
		int frame_width = 900;
		int frame_height = 800;
		treeFrame.setSize(frame_width, frame_height);
		treeFrame.setTitle("COMP9024 Assignment Two - Question3");
		treeFrame.setVisible(true);
		treeFrame.setResizable(false); 
		int height_of_tree = ((AVLNode<K,V>)(tree.root)).height;
		System.out.println("the height of the whole tree is:");
		System.out.print(height_of_tree);
		int levels_nums = height_of_tree + 1;
		int level_height = (int)((frame_height/levels_nums)*0.8);
		
		drawSubTree(tree.root,treeFrame,frame_width/2,(int)(0.02*frame_height), level_height);
	}
	
	public static <K,V> void drawSubTree(BTPosition<Entry<K,V>> root, JFrame frame, int x, int y,int level_height)
	{
		frame.getContentPane().add(new InternalNode(x,y,20));
		frame.setVisible(true);
		//frame.getContentPane().add(new Labels((new StringBuilder(root.element().getKey().toString())).append(" ").append(root.element().getValue().toString()).toString(),x,y));
		//frame.setVisible(true);
		BTPosition<Entry<K,V>> leftChild = root.getLeft();
		BTPosition<Entry<K,V>> rightChild = root.getRight();
		if(leftChild.element()!=null)
		{
			drawSubTree(leftChild,frame,x/2,y+(int)(0.02*frame.getHeight())+level_height, level_height);
			
			frame.setVisible(true);
		}
		else if(leftChild.element()==null)
		{
			frame.getContentPane().add(new ExternalNode(x/2,y+(int)(0.02*frame.getHeight())+level_height,20,20));
			frame.setVisible(true);
		}
		
		if(rightChild.element()!=null)
		{
			drawSubTree(rightChild,frame,(frame.getWidth())/2+x/2,y+(int)(0.02*frame.getHeight())+level_height, level_height);
			frame.setVisible(true);
		}
		else if(rightChild.element()==null)
		{
			frame.getContentPane().add(new ExternalNode((frame.getWidth())/2+x/2,y+(int)(0.02*frame.getHeight())+level_height,20,20));
			frame.setVisible(true);
		}
	
	}
}