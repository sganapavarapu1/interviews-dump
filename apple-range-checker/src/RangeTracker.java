public class RangeTracker extends BinarySearchTree implements IRangeTracker {

    // Constructor
    public RangeTracker()
    {
        super();
    }

    // -- interface implementation

    public void addRange(int min, int max) {
        if (min > max) return;
        insert(new Range(min, max));
    }

    public boolean queryRange(int min, int max) {
        if (min > max) return false;
        return recursiveQuery(new Range(min, max));
    }

    public boolean deleteRange(int min, int max) {
        if (min > max) return false;
        return recursiveDelete(new Range(min, max));
    }

    public void listRanges()
    {
        inOrderPrint(theRoot);
    }

    // -- range decoration

    /* A recursive function to delete a range */
    private boolean recursiveDelete(Range key)
    {
        /* If the tree is empty, return false */
        if (theRoot == null) {
            return false;
        }

        Node lCurrent = theRoot;
        boolean lIsQuit = false;
        int nextMinVal = 0;
        boolean lIsPartialMatch = false;
        while (!lIsQuit && lCurrent != null) {
            Range r = ((Range) lCurrent.getData());
            int minK = key.getMin(), nodemin = r.getMin();
            int maxK = key.getMax(), nodemax = r.getMax();
            if (minK < nodemin) { // lesser than lCurrent node's range
                lCurrent = lCurrent.left;
            }
            else
            if (minK > nodemax) { // greater than lCurrent node's range
                lCurrent = lCurrent.right;
            }
            // possibly in lCurrent range
            else {
                if (maxK <= nodemax) {
                    // completely in range

                    // boundaries are same
                    if (minK == nodemin && maxK == nodemax) {
                        remove(key);
                    }
                    else
                        // only right boundary is same
                    if (minK > nodemin && maxK == nodemax) {
                        r.max = minK - 1;
                    }
                    else
                        // only left boundary is same
                    if (minK == nodemin && maxK < nodemax) {
                        r.min = maxK + 1;
                    }
                    else
                        // neither boundary is same
                    if (minK > nodemin && maxK < nodemax) {
                        int tmp = r.max;
                        r.max = minK - 1; // fix lCurrent node range
                        // new node for left over range on right
                        insert(new Range(maxK + 1, tmp));
                    }

                    return true; // done
                }
                else {
                    // data is found partially in lCurrent range
                    // delete that part from lCurrent range and search rest of it
                    if (minK == nodemin) { // partial data covers full node
                        // delete this node
                        remove(r);
                    }
                    else { // partial data covers partial node
                        nextMinVal = r.max + 1;
                        r.max = minK - 1;
                    }
                    lIsQuit = true;
                    lIsPartialMatch = true;
                }
            }
        }

        //-------

        Node rCurrent = theRoot;
        boolean rIsQuit = false;
        int nextMaxVal = 0;
        boolean rIsPartialMatch = false;
        while (!rIsQuit && rCurrent != null) {
            Range r = ((Range) rCurrent.getData());
            int minK = key.getMin(), nodemin = r.getMin();
            int maxK = key.getMax(), nodemax = r.getMax();
            if (maxK < nodemin) { // lesser than rCurrent node's range
                rCurrent = rCurrent.left;
            }
            else
            if (maxK > nodemax) { // greater than rCurrent node's range
                rCurrent = rCurrent.right;
            }
            // possibly in rCurrent range
            else {
                if (minK >= nodemin) {
                    // completely in range

                    // boundaries are same
                    if (minK == nodemin && maxK == nodemax) {
                        remove(key);
                    }
                    else
                        // only right boundary is same
                        if (minK > nodemin && maxK == nodemax) {
                            r.max = minK - 1;
                        }
                        else
                            // only left boundary is same
                            if (minK == nodemin && maxK < nodemax) {
                                r.min = maxK + 1;
                            }
                            else
                                // neither boundary is same
                                if (minK > nodemin && maxK < nodemax) {
                                    int tmp = r.max;
                                    r.max = minK - 1; // fix rCurrent node range
                                    // new node for left over range on right
                                    insert(new Range(maxK + 1, tmp));
                                }

                    return true; // done
                }
                else {
                    // data is found partially in rCurrent range
                    // delete that part from rCurrent range and search rest of it
                    if (maxK == nodemax) { // partial data covers full node
                        // delete this node
                        remove(r);
                    }
                    else { // partial data covers partial node
                        nextMaxVal = r.min - 1;
                        r.min = maxK + 1;
                    }
                    rIsQuit = true;
                    rIsPartialMatch = true;
                }
            }
        }

        // -- partial match processing

        if (lIsPartialMatch) {
            recursiveDelete( new Range(nextMinVal, key.getMax()) );
        }

        if (rIsPartialMatch) {
            recursiveDelete( new Range(key.getMin(), nextMaxVal) );
        }

        return (lIsQuit || rIsQuit);
    }

    /* A recursive function to query a range */
    private boolean recursiveQuery(Range key)
    {
        /* If the tree is empty, return false */
        if (theRoot == null) {
            return false;
        }

        Node current = theRoot;
        boolean isQuit = false;
        boolean isPartiallyFound = false;
        while (!isQuit && current != null) {
            Range r = ((Range) current.getData());
            int minK = key.getMin(), nodemin = r.getMin();
            int maxK = key.getMax(), nodemax = r.getMax();
            if (minK < nodemin) { // lesser than current node's range
                current = current.left;
            }
            else
            if (minK > nodemax) { // greater than current node's range
                current = current.right;
            }
            // possibly in current range
            else {
                if (maxK <= nodemax) {
                    // completely in range
                    return true;
                }
                else {
                    // partially in current range
                    // search rest of it
                    isQuit = true;
                    isPartiallyFound = true;
                }
            }
        }

        if (isPartiallyFound) {
            final Range r = ((Range) current.getData());
            isQuit = recursiveQuery( new Range(r.getMax()+1, key.getMax()) );
        }

        return isQuit;
    }
}
