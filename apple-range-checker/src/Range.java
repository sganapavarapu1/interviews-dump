public class Range implements INodeData {
    int min;
    int max;

    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "[" + min + "-" + max + "]";
    }

    /**
     * Compare's current range's min boundary with passed in range's min boundary
     * Returns whether current range's min is lower boundary value
     *
     * @param rhs
     * @return boolean true if this range's min is less than other range's min
     */
    public boolean isLessThan(final INodeData rhs) {
        if (rhs instanceof Range) {
            final Range other = (Range) rhs;
            return this.min < other.min;
        }
        throw new IllegalArgumentException("Expecting Range object");
    }

    /**
     * Compare's current range's min boundary with passed in range's min boundary
     * Returns whether current range's min is upper boundary value
     *
     * @param rhs
     * @return boolean true if this range's min is greater than other range's min
     */
    public boolean isGreaterThan(final INodeData rhs) {
        if (rhs instanceof Range) {
            final Range other = (Range) rhs;
            return this.min > other.min;
        }
        throw new IllegalArgumentException("Expecting Range object");
    }

    /**
     *
     * @param value
     * @return boolean true if the value is in range, else false
     */
    /*
    public boolean isPartOfRangeInclusive(int value) {
        return (value >= min && value <= max);
    }
    */

    /**
     * Trims current range to a specific value, offset (exclusive).
     * Returns slice off range.
     * @param offset value, beginning which will be excluded from current range
     *               i.e. current range is trimmed to one less than it.
     * @return Range of the sliced off (excluded) range from current range.
     */
    /*
    public Range trim(final int offset) {
        // if out of range, cannot split current range
        if (offset < min || offset > max) {
            return null;
        }
        // if current range has only one number, then cannot split
        if (min == max) {
            return null;
        }
        // if split at one of the boundaries, then adjust boundary and
        // return a new range object with single value
        if (offset == min) {
            // e.g. if range is 5-10, and offset = 5, then,
            // current range will be adjusted to 6-10, and a new range object with 5-5 is returned
            int tmp1 = min;
            min++;
            return new Range(tmp1, tmp1);
        }
        if (offset == max) {
            // e.g. if range is 5-10, and offset = 10, then,
            // current range will be adjusted to 5-9, and a new range object with 10-10 is returned
            int tmp2 = max;
            max--;
            return new Range(tmp2, tmp2);
        }
        // split at a point inside the range
        // e.g. if range is 5-10 and offset is 6, then,
        // current range is adjusted to 5-5, and new range object 6-10 is returned
        // e.g. if range is 5-10 and offset is 9, then,
        // current range is adjusted to 5-8, and new range object 9-10 is returned
        int tmp3 = max;
        max = offset - 1;
        return new Range(offset, tmp3);
    }
    */
}
