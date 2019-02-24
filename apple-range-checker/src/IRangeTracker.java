public interface IRangeTracker {
    void addRange(int min, int max);
    boolean queryRange(int min, int max);
    boolean deleteRange(int min, int max);
    void listRanges(); // in-order print
}
