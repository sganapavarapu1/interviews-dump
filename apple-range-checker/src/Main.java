public class Main {
    // Driver Program to test above functions
    public static void main(String[] args)
    {
        IRangeTracker tracker = new RangeTracker();
        tracker.addRange(100, 110);
        queryRange( tracker, 100, 100, true );
        queryRange( tracker, 100, 101, true );
        queryRange( tracker, 110, 110, true );
        queryRange( tracker, 109, 110, true );
        queryRange( tracker, 101, 109, true );
        queryRange( tracker, 99, 100, false );
        queryRange( tracker, 110, 111, false );
        queryRange( tracker, 90, 110, false );
        queryRange( tracker, 100, 120, false );
        queryRange( tracker, 90, 105, false );
        queryRange( tracker, 105, 120, false );
        queryRange( tracker, 90, 120, false );
        tracker.addRange(50, 98);
        queryRange( tracker, 50, 98, true );
        queryRange( tracker, 98, 100, false );
        tracker.addRange(145, 150);
        tracker.addRange(130, 140);
        queryRange( tracker, 135, 140, true );
        tracker.addRange(160, 170);
        queryRange( tracker, 135, 150, false );
        tracker.addRange(111, 114);
        tracker.addRange(185, 190);
        tracker.addRange(115, 120);
        tracker.addRange(175, 180);
        tracker.addRange(195, 200);
        queryRange( tracker, 112, 118, true );
        queryRange( tracker, 102, 118, true );
        deleteRange( tracker, 130, 140, true );

        deleteRange( tracker, 66, 104, true );
        deleteRange( tracker, 151, 155, false );
        deleteRange( tracker, 63, 113, true );
        deleteRange( tracker, 59, 118, true );

        // TODO: uncomment line 46 and fix the failing test
        // NOTE: for reproducing failing case in line 46, it's required to comment lines 36-39
        // WARN: Test result shows PASS, but data is not cleaned properly between range 57-148
        // 57-98 is cleaned up, but nodes 110-118, 111-114 needs deletion & 115-120 => 119-120
        // deleteRange( tracker, 57, 148, true );
    }

    public static void deleteRange(final IRangeTracker tracker, final int min, final int max, boolean expected) {
        System.out.println("\nDeleting the range:\t[" + min + " - " + max + "] ...");
        System.out.print("\tBEFORE:\t\t");
        tracker.listRanges();
        boolean actual = tracker.deleteRange(min, max);
        System.out.print("\n\tAFTER:\t\t");
        tracker.listRanges();
        System.out.println("\nDelete: [" + min + " - " + max + "]\t\texpected: " + expected + "," + (expected? "\t" : "") +
                "\tactual: " + actual + "\tResult:\t" + (actual == expected ? "PASS": "FAIL"));
    }

    public static void queryRange(final IRangeTracker tracker, final int min, final int max, boolean expected) {
        boolean actual = tracker.queryRange(min, max);
        System.out.println("Query: [" + min + " - " + max + "]\t\texpected: " + expected + "," + (expected? "\t" : "") +
                "\tactual: " + actual + "\tResult:\t" + (actual == expected ? "PASS": "FAIL"));
    }
}
