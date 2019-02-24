public class BinarySearchTree {

    Node theRoot;

    public BinarySearchTree() {
        theRoot = null;
    }

    protected void insert(INodeData key)
    {
        theRoot = recursiveAdd(theRoot, key);
    }

    protected void remove(INodeData key)
    {
        theRoot = recursiveDelete(theRoot, key);
    }

    protected void inOrderPrint(Node root)
    {
        if (root != null)
        {
            inOrderPrint(root.left);
            System.out.print(root.data + "  ");
            inOrderPrint(root.right);
        }
    }

    /* A recursive function to insert a data */
    private Node recursiveAdd(Node root, INodeData key)
    {
        /* If the tree is empty, return a new node */
        if (root == null) {
            root = new Node(key);
            return root;
        }

        /* Otherwise, recur down the tree */
        if (key.isLessThan(root.data)) {
            root.left = recursiveAdd(root.left, key);
        }
        else
        if (key.isGreaterThan(root.data)) {
            root.right = recursiveAdd(root.right, key);
        }

        /* return the (unchanged) node pointer */
        return root;
    }

    /* A recursive function to remove a data */
    private Node recursiveDelete(Node root, INodeData key)
    {
        /* Base Case: If the tree is empty */
        if (root == null) return root;

        /* Otherwise, recur down the tree */
        if (key.isLessThan(root.data)) {
            root.left = recursiveDelete(root.left, key);
        }
        else
        if (key.isGreaterThan(root.data)) {
            root.right = recursiveDelete(root.right, key);
        }
        // if data is same as root's data, then
        // This is the node to be deleted
        else {
            // node with only one child or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            // node with two children: Get the inorder successor (smallest
            // in the right subtree)
            root.data = getLowestValue(root.right);

            // Delete the inorder successor
            root.right = recursiveDelete(root.right, root.data);
        }

        return root;
    }

    private INodeData getLowestValue(Node root)
    {
        INodeData minv = root.getData();
        while (root.left != null)
        {
            minv = root.left.getData();
            root = root.left;
        }
        return minv;
    }
}
