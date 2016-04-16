/*
 * COMP 9024 assignment 2
 * Author:Huijun Wu(z5055605)
 * Date:15/04/2016
*/
package datastructures;
import java.util.*;


/*Q1:Clone an AVLTree and return a reference to the cloned new AVLTree object.
 */
public class ExtendedAVLTree<K,V> extends AVLTree<K,V>
{

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
			if(!tree1List.isEmpty()&&Integer.parseInt(tree1List.first().element().element().getKey().toString())< Integer.parseInt(tree2List.first().element().element().getKey().toString()))
			{
				merged_array.add(tree1List.first().element().element());
			//	System.out.print(tree1List.first().element().element().getKey());
				tree1List.remove(tree1List.first());
			//	System.out.println("add element and delete, The size of tree1List is");
			
			//	System.out.print(tree1List.size());
			}
			else
			{
				merged_array.add(tree2List.first().element().element());
		//		System.out.print(tree2List.first().element().element().getKey());
				tree2List.remove(tree2List.first());
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
		merged_tree.root = MergedArray2AVLTree(0,(merged_array.size()-1), merged_array,merged_tree);
		merged_tree.numEntries = tree1.size() + tree2.size();
		//assign null to tree1 and tree 2 for GC
		tree1 = null;
		tree2 = null;
	
		
		return merged_tree;
		
	}
	
	protected static <K,V>  BTPosition<Entry<K,V>>  MergedArray2AVLTree(int start, int end, ArrayList<Entry<K,V>> merged_array, AVLTree<K,V> merged_tree)
	{
		if(start > end)
		{
			return null;
		}
		
		int mid = start + (end - start)/2;
		Entry<K,V> parent_element = merged_array.get(mid);
		BTPosition<Entry<K,V>> leftChild = MergedArray2AVLTree(start,mid-1, merged_array,merged_tree);
		BTPosition<Entry<K,V>> rightChild = MergedArray2AVLTree(mid+1,end, merged_array,merged_tree);
		BTPosition<Entry<K,V>> parent = merged_tree.createNode(parent_element, null, leftChild, rightChild);
		parent.setLeft(leftChild);
		parent.setRight(rightChild);
		merged_tree.size = merged_tree.size+2;
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

	
}