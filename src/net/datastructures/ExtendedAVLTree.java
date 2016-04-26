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
            this.x = x; //(x,y) is the position of the internal node. 
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
            this.x = x; //(x,y) is the position of the external node. 
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
    /*Q1:Clone an AVLTree and return a reference to the cloned new AVLTree object.
     */
    
    /*
     * The method clondSubTress is a recursive function. According to the algorithm, every node in the original tree would be accessed once. And the operations
     * used to modify the pointers of parent and children take constant amount of time. Therefore, the time complexity of clone is O(n) where n is the size of
     * the original tree.
     */
	public static <K, V> void cloneSubTrees(BTPosition<Entry<K,V>> old_root, BTPosition<Entry<K,V>> cloned_root)
	{
		BTPosition<Entry<K,V>> old_leftChild = old_root.getLeft();
		BTPosition<Entry<K,V>> old_rightChild = old_root.getRight();
		
		if(old_leftChild!=null)
		{
			AVLNode<K,V> cloned_leftchild = new AVLNode<K,V>(old_leftChild.element(),null,null,null);
			cloned_root.setLeft(cloned_leftchild);
			cloned_leftchild.setParent(cloned_root);
			cloneSubTrees(old_leftChild, cloned_root.getLeft());
			
		}
		if(old_rightChild!=null)
		{
			AVLNode<K,V> cloned_rightchild = new AVLNode<K,V>(old_rightChild.element(),null,null,null);
			cloned_root.setRight(cloned_rightchild);
			cloned_rightchild.setParent(cloned_root);
			cloneSubTrees(old_rightChild, cloned_root.getRight());
		}	
		
	}

	public static <K, V> AVLTree<K, V> clone(AVLTree<K,V> tree)
	{
		AVLTree<K,V> cloned_tree = new AVLTree<K,V>();
		cloned_tree.addRoot(tree.root().element());
		cloneSubTrees(tree.root,cloned_tree.root);
		cloned_tree.size = tree.size();
		((AVLNode<K,V>)cloned_tree.root).height = ((AVLNode<K,V>)tree.root).height; //maintain the height of tree, because we use this to adjust the nodes and edges in print().
		return cloned_tree;

	}
	
	/*Q2:Merge two AVLTree and return the reference of the merged tree. 
	 */
	
	/*
	 * Analyzation of the time complexity of our algorithm. Assume the size of the tree tress are 
	 * m and n repectively. Firstly, we use inorder traversal to get a sorted list of the elements 
	 * in two AVL trees. The time complexity for this is O(m+n). Then, we merge the two lists into 
	 * an sorted array. The time complexity is similar as we need to scan both the two lists. So it's
	 * O(m+n) again. Finally, we use the sorted array to generate a new AVLTree as the merged tree. 
	 * Each element in the sorted array would be accessed once. And the time used for maintaining the 
	 * pointers of nodes is a constant. So the complexity is also O(m+n). Overall, the time complexity 
	 * of our algorithm is O(m+n). 
	 */
	public static <K, V> AVLTree<K, V> merge(AVLTree<K,V> tree1, AVLTree<K,V> tree2)
	{
		AVLTree<K,V> merged_tree = new AVLTree<K,V>();
		NodePositionList<Position<Entry<K,V>>> tree1List = new NodePositionList<Position<Entry<K,V>>>();
		NodePositionList<Position<Entry<K,V>>> tree2List = new NodePositionList<Position<Entry<K,V>>>();
		//add the nodes of these two trees into nodePositionList, inorder traversal would gurantee the order of the nodes
		
		tree1.inorderPositions(tree1.root(),tree1List);
		tree2.inorderPositions(tree2.root(),tree2List);

		ArrayList<Entry<K,V>> merged_array = new ArrayList<Entry<K,V>>();

		
		while((!tree1List.isEmpty())&&(!tree2List.isEmpty()))
		{
			//we firstly remove all external nodes from the two lists. 
			//still need to check whether treelist1 is empty again, because remove may make it empty. 
			while(!tree1List.isEmpty()&&(tree1List.first().element().element() == null))
			{
				tree1List.remove(tree1List.first());
			}
			//still need to check whether treelist1 is empty again, because remove may make it empty. 
			while(!tree2List.isEmpty()&&(tree2List.first().element().element() == null))
			{
				tree2List.remove(tree2List.first());
			}
			//double check if tree1list and tree2list are empty, because they might be empty after the last external node is deleted. 
			if(!tree1List.isEmpty()&&!tree2List.isEmpty()&&Integer.parseInt(tree1List.first().element().element().getKey().toString()) < Integer.parseInt(tree2List.first().element().element().getKey().toString()))
			{
				merged_array.add(tree1List.first().element().element());
				tree1List.remove(tree1List.first());
			}
			else if(!tree1List.isEmpty()&&!tree2List.isEmpty())
			{	
				if(!tree2List.isEmpty()){
				merged_array.add(tree2List.first().element().element());
				tree2List.remove(tree2List.first());}
			}

		}
		//if tree1list is empty, just copy tree2list to the end of the merged_list
		if(tree1List.isEmpty())
		{
			Iterator<Position<Entry<K,V>>> it2 = tree2List.iterator();
			while(it2.hasNext()){
				Entry<K,V> tmp_element2 = it2.next().element();
				if(tmp_element2 == null){
					continue;
				}
				merged_array.add(tmp_element2);
				}
			
		}
		//if tree2list is empty, just copy tree1list to the end of the merged_list
		else if(tree2List.isEmpty())
		{
			
			Iterator<Position<Entry<K,V>>> it1 = tree1List.iterator();
			
			while(it1.hasNext()){
				Entry<K,V> tmp_element1 = it1.next().element();
				if(tmp_element1 == null){
					continue;
				}
				merged_array.add(tmp_element1);
				}
		}
		int merged_tree_size = merged_array.size();
		//merged_tree = merged_tree_size;
		int height = 0;
		merged_tree.root = MergedArray2AVLTree(0,(merged_array.size()-1), merged_array,merged_tree,height);
		merged_tree.numEntries = tree1.size() + tree2.size();


		//assign null to tree1 and tree 2 for garbage collection.
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
		//the following code is used to maintain the height of AVLtree. 
		if((leftChild == null) && (rightChild == null))
		{
			parent.setLeft(merged_tree.createNode(null, parent, null, null));
			parent.setRight(merged_tree.createNode(null, parent, null, null));
			((AVLNode<K,V>)(parent)).setHeight(1);
		}
		else if(leftChild == null)
		{
			parent.setLeft(merged_tree.createNode(null, parent, null, null));
			((AVLNode<K,V>)(parent)).setHeight(1+((AVLNode<K,V>)(rightChild)).height);
		}
		else if(rightChild == null)
		{
			((AVLNode<K,V>)(parent)).setHeight(1+((AVLNode<K,V>)(leftChild)).height);
			parent.setRight(merged_tree.createNode(null, parent, null, null));
		}
		else
		{
			((AVLNode<K,V>)(parent)).setHeight(1+Math.max(((AVLNode<K,V>)(leftChild)).height,((AVLNode<K,V>)(rightChild)).height));
		}

		if(leftChild != null)
		{
			leftChild.setParent(parent);
		}
		if(rightChild != null)
		{
			
			rightChild.setParent(parent);
		}

		return parent;	
	}
	/*Q3:Print an AVLtree.
	 */
	
	/*
	 * Time complexity analysis. We use a recursive algorithm to print the AVLtree. The time for printing each node in the AVLtree is a constant.  
	 * So the time complexity of this algorithm is O(n) where n is the number of nodes in the AVLtree. 
	 */
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
		int height_of_tree = ((AVLNode<K,V>)(tree.root)).height;
		int levels_nums = height_of_tree;
		int level_height = (int)((frame_height/levels_nums)*0.8); //control the height of each level according to the AVLtree's height.
		
		drawSubTree(tree.root,treeFrame,0,frame_width,(int)(0.02*frame_height), level_height);
	}
	/*
	 * The parameters of this recursive function are, root is the node to be painted. x_l and x_r is would 
	 * determine the position of root as root would be located in the middle of them. y is the y-axis of 
	 * root. level_height is the height of each level. Because we use height of the tree to adjust the 
	 * positions of the nodes of an AVLtree.
	 */
	public static <K,V> void drawSubTree(BTPosition<Entry<K,V>> root, JFrame frame, int x_l, int x_r, int y,int level_height)
	{
		int mid = (x_l+x_r)/2;
		frame.getContentPane().add(new InternalNode(mid,y,25));
		frame.setVisible(true); //set the frame visable everytime or the new added component would not be displayed.
		frame.getContentPane().add(new Labels((new StringBuilder(root.element().getKey().toString())).append(" ").append(root.element().getValue().toString()).toString(),mid-25,y+13));
		frame.setVisible(true);
		BTPosition<Entry<K,V>> leftChild = root.getLeft();
		BTPosition<Entry<K,V>> rightChild = root.getRight();
		//if leftChild is not external, then draw an internal node on the left.
		if(leftChild.element()!=null)
		{
			frame.getContentPane().add(new Edge(mid+10,y+25,(x_l+mid)/2+10,y+level_height));
			frame.setVisible(true);
			drawSubTree(leftChild,frame,x_l,mid,y+level_height, level_height);
			
			frame.setVisible(true);

		}
		//if leftChild is external, then draw an external node on the left.
		else if(leftChild.element()==null)
		{
			frame.getContentPane().add(new ExternalNode((x_l+mid)/2-10,y+level_height,20,20));
			frame.setVisible(true);
			frame.getContentPane().add(new Edge(mid+10,y+25,(x_l+mid)/2,y+level_height));
			frame.setVisible(true);
		}
		//if rightChild is not external, then draw an internal node on the right.
		if(rightChild.element()!=null)
		{
			frame.getContentPane().add(new Edge(mid+10,y+25,(mid+x_r)/2+10,y+level_height));
			frame.setVisible(true);
			drawSubTree(rightChild,frame,mid,x_r,y+level_height, level_height);
			frame.setVisible(true);

		}
		//if rightChild is external, then draw an external node on the right.
		else if(rightChild.element()==null)
		{
			frame.getContentPane().add(new ExternalNode((mid+x_r)/2-10,y+level_height,20,20));
			frame.setVisible(true);
			frame.getContentPane().add(new Edge(mid+10,y+25,(mid+x_r)/2,y+level_height));

			frame.setVisible(true);
		}
	
	}
}