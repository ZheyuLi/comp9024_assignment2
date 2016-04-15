/*
 * COMP 9024 assignment 2
 * Author:Huijun Wu(z5055605)
 * Date:15/04/2016
*/
package datastructures;
import java.util.Stack;


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
		
		return cloned_tree;

	}
	

	
}