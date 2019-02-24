package sixdegrees;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FriendFinderTest {

    @Test
    public void findRelationshipPathTest1() {

        SNSImpl sns = new SNSImpl();
        sns.addFriend("Kevin", "UserB");
        sns.addFriend("Kevin", "UserS");
        sns.addFriend("UserB", "UserC");
        sns.addFriend("UserA", "UserD");
        sns.addFriend("UserX", "UserC");
        sns.addFriend("UserY", "UserX");
        sns.addFriend("Bacon", "UserY");

        FriendFinder ff = new FriendFinder(sns);

        List<String> d0 = ff.findShortestPath("Kevin", "Bacon", 2);
        Assert.assertEquals(d0, null);

        List<String> d1 = ff.findShortestPath("Kevin", "Bacon", 5);
        Assert.assertEquals(d1,
                Arrays.asList("Kevin", "UserB", "UserC", "UserX", "UserY", "Bacon"));

        // Create a shorter path that will be accessed later in the return collection (list in this test case) of friends
        sns.addFriend("UserS", "Bacon");
        List<String> d2 = ff.findShortestPath("Kevin", "Bacon", 6);
        Assert.assertEquals(d2,
                Arrays.asList("Kevin", "UserS", "Bacon"));
    }

    @Test
    public void findRelationshipPathTest2() {

        SNSImpl sns = new SNSImpl();
        sns.addFriend("Kevin", "UserB");
        sns.addFriend("Kevin", "UserS");
        sns.addFriend("UserB", "UserC");
        sns.addFriend("UserA", "UserD");
        sns.addFriend("UserX", "UserC");
        sns.addFriend("UserY", "UserX");
        sns.addFriend("UserD", "UserC"); // difference from first test
        sns.addFriend("Bacon", "UserY");

        FriendFinder ff = new FriendFinder(sns);

        List<String> d1 = ff.findShortestPath("Kevin", "Bacon", 5);
        Assert.assertEquals(d1,
                Arrays.asList("Kevin", "UserB", "UserC", "UserX", "UserY", "Bacon"));

        // Create a shorter path that will be accessed later in the return collection (list in this test case) of friends
        sns.addFriend("UserS", "Bacon");
        List<String> d2 = ff.findShortestPath("Kevin", "Bacon", 6);
        Assert.assertEquals(d2,
                Arrays.asList("Kevin", "UserS", "Bacon"));
    }
}

