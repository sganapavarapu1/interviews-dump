package sixdegrees;

import java.util.*;

/**
 * This class has only one member variable that's a reference to SocialNetworkService 
 * instance to fetch friends of a person, by personaId.
 * 
 * All other data structures used to find shortest path are not member variables so as
 * to separate state management by request e.g. visited nodes should not be a shared
 * member data for it varies by request / iteration.
 */
public class FriendFinder {

	private final SocialNetworkService sns;

	FriendFinder(SocialNetworkService socialNetworkService) {

		sns = socialNetworkService;
	}
	
	/**
	 * Returns an ordered list of connectivity if the given personAId has a
	 * connection of friend relationships to personZId within the specified
	 * degreesOfSeparation, otherwise return null.
	 * 
	 * This method implements bi-directional BFS.
	 * 
	 * Data structures used:
	 * For every direction, 
	 * 1) A queue (LinkedList) is required to keep track of next set of persons whose friends need to be fetched
	 * 2) A set (HashSet) is required to keep track of persons whose parent person is known
	 *    (clarification: here, parent indicates the personId from which current person is reached)
	 * 3) A map (HashMap) is required to map a personId to its parentId
	 *  
	 * E.g. if A to Z path should be found, 
	 * this algorithm shall search path from A and path Z, bi-directionally, 
	 * same time till the intersection is found.
	 * This algorithm follows BFS where each iteration goes processes immediate friends (adjacent) to that person.
	 * Every iteration shall have 2 BFS function calls, one for searching from A (direction) and another from Z (direction).
	 *  
	 * Initial state:
	 * Each queue is initialized with the first personId from that direction, 
	 * Each set is initialized with the first personId from that direction, 
	 * Each map is initialized with the first personId from that direction and its parent as null
	 * 
	 * Algorithm:
	 * Iterate while both queues are not empty, 
	 * For every iteration
	 * 1) invoke BFS for both directions (one after the other) with corresponding data structures passed to BFS function
	 *    In the BFS function
	 *    a) head of the queue is retrieved (removed from queue) - let's call this one as head-personId
	 *    b) friends of the head are fetched
	 *    c) now that the friends' parent (head-personId) is known:
	 *       - put each friendId as key with this head-personId as value 
	 *       - add friendId to visited set because its parent is known
	 *    d) add each friendId to queue for next pass of BFS in that direction
	 * 2) check whether the visited sets of both directions intersect
	 * 3) if they do, 
	 *    a) construct path from A to intersection
	 *    b) construct path from intersection to Z
	 *    c) check whether the number of edges is within the passed degreesOfSeparation
	 *       if so, return the path from A-to-Z (concatenated), 
	 *       else, return null
	 * 4) else, continue to next iteration
	 * 
	 */
	public List<String> findShortestPath(String personAId, String personZId, int degreesOfSeparation) {

		Queue<String> queueA = new LinkedList<>();
		Queue<String> queueZ = new LinkedList<>();
		
		Set<String> visitedA = new HashSet<>();
		Set<String> visitedZ = new HashSet<>();
		
		HashMap<String, String> parentMapA = new HashMap<>();
		HashMap<String, String> parentMapZ = new HashMap<>();

		queueA.add(personAId);
		visitedA.add(personAId);

		queueZ.add(personZId);
		visitedZ.add(personZId);

		parentMapA.put(personAId, null);
		parentMapZ.put(personZId, null);

		while (!queueA.isEmpty() && !queueZ.isEmpty()) {
			
			BFS(queueA, visitedA, parentMapA);
			
			BFS(queueZ, visitedZ, parentMapZ);
			
			Set<String> intersection = new HashSet<String>(visitedA);
			intersection.retainAll(visitedZ);
			
			if (!intersection.isEmpty()) {
				if (intersection.size() == 1) {
					String[] ids = intersection.toArray(new String[1]);

					List<String> pathFromA = getPath(parentMapA, ids[0], true);
					{
						List<String> path4mInt = getPath(parentMapZ, ids[0], false);
						List<String> pathToZ = path4mInt.subList(1, path4mInt.size());
						pathFromA.addAll(pathToZ);
					}
					if (degreesOfSeparation < pathFromA.size()-1) {
						return null;
					}
					return pathFromA;
				}
				else {
					// TODO: log error
					return null;
				}
			}
		}
		
		return null;
	}

	/**
	 * 
	 * @param queue
	 * @param visited
	 * @param parentMap
	 */
	private void BFS(Queue<String> queue, Set<String> visited, 
			HashMap<String, String> parentMap) {

		if (queue.isEmpty()) return;

		String personId = queue.remove();
		Collection<String> friends = sns.findFriends(personId);
		for (String friendId : friends) {
			if (!visited.contains(friendId)) {
				parentMap.put(friendId, personId);
				visited.add(friendId);
				queue.add(friendId);
			}
		}
	}
	
	/**
	 * 
	 * @param parentMap that belongs to this direction
	 * @param intersectPersonId personId of the intersection
	 * @param direction true when fetching path from A to intersection, false otherwise
	 * @return List<String> list of personIds from A to intersection or intersection to Z (as dictated by direction)
	 */
	private List<String> getPath(HashMap<String, String> parentMap, String intersectPersonId, 
			boolean direction) {
		List<String> path = new ArrayList<>();
		String s = intersectPersonId;
		while (s != null) {
			path.add(s);
			s = parentMap.get(s);
		}
		if (direction) {
			// if direction is from A to intersection, 
			// the path constructed needs to be reversed because
			// the first node that started this list here is intersection node
			// where as it should be A
			Collections.reverse(path);
		}
		return path;
	}

}
