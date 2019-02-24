public class Node {
    INodeData data;
    Node left, right;

    public Node(INodeData item) {
        data = item;
        left = right = null;
    }

    public INodeData getData() {
        return data;
    }
}
