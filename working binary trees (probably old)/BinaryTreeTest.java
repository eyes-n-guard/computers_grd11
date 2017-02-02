import java.io.*;

public class BinaryTreeTest
{
	public static void main(String[]args)
	{
		BinaryTree tree = new BinaryTree();
		
		
		
		tree.insert(42);
    	tree.insert(60);
		tree.insert(47);
		tree.insert(83);
		tree.insert(50);
		tree.insert(35);
		tree.insert(102);
		tree.insert(104);
		tree.insert(74);
		tree.print();
		System.out.println();
		tree.delete(102);
		tree.print();
		if(tree.root.getLeft().isLeaf())
			System.out.println("leaf");
	}
    
    
}