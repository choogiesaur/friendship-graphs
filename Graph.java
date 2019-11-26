package friends;

import java.util.*;
import java.io.*;
import java.lang.*;

class Person {
	String name;
	String student;
	String schoolName;

	public Person(String name, String student, String schoolName) { // modified S
		this.name = name;
		this.student = student;
		this.schoolName = schoolName;
	}

	public String toString() {
		return name;
	}
}

class Node {
	String data;
	Node next;

	public Node(String data, Node next) {
		this.data = data;
		this.next = next;
	}
}

class Path {
	String via;
	int cost;

	public Path(String via, int cost) {
		this.via = via;
		this.cost = cost;
	}
}

public class Graph {
	/**
	 * @param args
	 */
	public HashMap<String, Node> neighbor = new HashMap<String, Node>();
	public HashMap<String, Node> school = new HashMap<String, Node>(); //
	public HashMap<String, Person> identity = new HashMap<String, Person>();
	public HashMap<String, Node> schooloners = new HashMap<String, Node>();
	public ArrayList<String> connectors = new ArrayList<String>();
	public int countDFS = 1;

	public void buildGraph(String fileName) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));
	
		int numVertices = 0;
		if (sc.hasNext()) {
			numVertices = (int) sc.nextInt();
		}
		sc.nextLine();
		// for the reading of the people
		for (int i = 0; i < numVertices; i++) {
			String line = sc.nextLine();
			// System.out.println(line);
			line = line.toLowerCase(); // CASE INSENSITIVVVEEE
			String[] temp = line.split("\\|", 3);
			Person p;
			if (temp.length == 2) {
				p = new Person(temp[0], "n", null);
			} else {
				p = new Person(temp[0], "y", temp[2]);
			}
			neighbor.put(temp[0], null);
			identity.put(temp[0], p);
			if(p.student.equals("y")){
				if(schooloners.get(p.schoolName) == null){
					schooloners.put(p.schoolName, new Node(p.name, null));
				}else{
					Node tmp = schooloners.get(p.schoolName);
					schooloners.put(p.schoolName, new Node(p.name, tmp));
				}
			}
		}
		// now read the friendships

		while (sc.hasNextLine()) {
			String word = sc.nextLine();
			word = word.toLowerCase(); // CASE INSENSITIVE

			String[] temp = word.split("\\|", 3); 	// modified from john's
			Node neigh0 = neighbor.get(temp[0]); 	// switcheroo
			Node neigh1 = neighbor.get(temp[1]); 	// we are now friends of eachother

			neighbor.put(temp[0], new Node(temp[1], neigh0));
			neighbor.put(temp[1], new Node(temp[0], neigh1));

			if (identity.get(temp[0]).student.equals("y") && identity.get(temp[1]).student.equals("y")) { // if both student & same school add whole string to school hashmap
				if (identity.get(temp[0]).schoolName.equals(identity.get(temp[1]).schoolName)) {
					Node tempp = school.get(identity.get(temp[0]).schoolName);
					school.put(identity.get(temp[0]).schoolName, new Node(word,tempp));
				}
			}

		}
		for (String person : identity.keySet()) {
			String sName = identity.get(person).schoolName;
			boolean flag = false;
			Node ptr = neighbor.get(person);

			while (ptr != null) {
				// ptr.data == persons name, get person object of this name from
				// identity, if its schoolname == sName
				// System.out.println("	Data: " + ptr.data);
				if (identity.get(ptr.data).student.equals("y") && identity.get(ptr.data).schoolName.equals(sName)) {
					flag = true;
					break;
				} else {
					ptr = ptr.next;
				}
			}
			if (flag == false) {
				Node tempp = school.get(sName);
				school.put(sName, new Node(person, tempp));
			}
		}

		sc.close();

	}

	public void subgraph(String place) { // we have to store this shiet in a adj
											// list of identical format to the
											// orig graph!!!
		place = place.toLowerCase();
		if (school.get(place) == null) {
			System.out.println("Ain't no students at this school!"); // modified
			return;
		}

		Node ptr = school.get(place);

		while (ptr != null) {
			System.out.println(ptr.data);
			ptr = ptr.next;
		}

	}

	public void shortestPath(String a, String b) {
		a = a.toLowerCase();
		b = b.toLowerCase();
		if (identity.get(a) == null || identity.get(b) == null) {
			System.out.println("One or both of these students not found");
			return;
		}
		if(a.equals(b)){
			System.out.println("Cain't make no path from a person to themself...");
		}

		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		HashMap<String, String> prev = new HashMap<String, String>();

		for (String s : identity.keySet()) {
			visited.put(s, false);
		}

		Queue<String> queue = new LinkedList<String>();
		queue.add(a);
		visited.put(a, true);
		prev.put(a, a);
		while (queue.isEmpty() == false) {
			String s = queue.poll();
			Node ptr = neighbor.get(s);
			while (ptr != null) { // while still neighbors in list...
				if (visited.get(ptr.data) == false) { // if you didnt visit this neighbor yet:
					visited.put(ptr.data, true); // mark it visited
					prev.put(ptr.data, s); // in the previous array, mark this one's previous as s
					// System.out.println(ptr.data + " visited!");
					queue.add(ptr.data);
				}
				if (ptr.data.equals(b)) {
					ArrayList<String> path = new ArrayList<String>();
					path.add(b); // adds end dude to list
);
					String next = prev.get(b); // sets "next" as the previous of b
					while (true) {
						path.add(0, next); // adds next to FRONT of path list

						if (next.equals(prev.get(next))) { // if next is EQUAl to the previous of next...break(this will be so if it's A)
							break;
						}
						next = prev.get(next); // updates next to next's previous
					}
					System.out.print(a);
					for (int i = 1; i < path.size(); i++) {
						System.out.print("--");
						System.out.print(path.get(i));
					}
					System.out.println();
					return;
				}
				ptr = ptr.next;
			}
		}
		if (visited.get(b) == false) { // if end has not been visited, aint no path
			System.out.println("Ain't no path from " + a + " to " + b);
			return;
		}
	}

	public void cliques(String skool) {
		
		skool = skool.toLowerCase();
		if (school.get(skool) == null) {
			System.out.println("Ain't no students at this school!"); // modified
			return;
		}

		Node ptr = school.get(skool);
		
		System.out.println("Clique 1: ");
		while (ptr != null) {
			System.out.println(ptr.data);
			ptr = ptr.next;
		}
	}

	private void DFS(String name, HashMap<String,Boolean> visited, HashMap<String,Integer> dfsnum, HashMap<String, Integer> back){
        //count is for giving the dfsnum and back
        //this is normal dfs being done
        visited.put(name, true);
        dfsnum.put(name, countDFS);
        back.put(name, countDFS);
        countDFS++;

        for(Node ptr = neighbor.get(name); ptr != null; ptr = ptr.next) {
                if(visited.get(ptr.data) == null) { // if we didnt visit
                        DFS(ptr.data, visited, dfsnum, back);
                        if(dfsnum.get(name) != 1) {
                               
                                        if(back.get(name) > back.get(ptr.data)){
                                        	back.put(name, back.get(ptr.data));
					} if(!connectors.contains(name) && dfsnum.get(name) <= back.get(ptr.data)){
                                        	connectors.add(name);
                                        }
                        }

                } else { //if we did visit
                        if(back.get(name) > dfsnum.get(ptr.data)){//want back(v) = min(back(v),dfsnum(w))
                                back.put(name, dfsnum.get(ptr.data));
                        }
                }
               
        }
       
        if(dfsnum.get(name) == 1)
                return;
       

}

public void printConnectors(){
       
        //if this method wasn't called before, we need to get the connectors list
        if(connectors.size() == 0)
        {
                for(String person : identity.keySet()){
                        countDFS = 1;
                        DFS(person, new HashMap<String,Boolean>(), new HashMap<String,Integer>(), new HashMap<String, Integer>());
                }
        }
       
        System.out.println("The connectors are as followed:");
       
        //if there are none
        if(connectors.size() == 0)
                System.out.println("None!");
       
        //if there is at least one
        for(int i = 0; i < connectors.size() ; i++)
        {
               
                System.out.print(connectors.get(i));
                if(i != connectors.size()-1)
                        System.out.print(",");
               
               
        }
       System.out.println();
       System.out.println();
       
}
	// public HashMap<String, Node> neighbor = new HashMap<String, Node>();
	// public HashMap<String, Node> school = new HashMap<String, Node>();
	// public HashMap<String, Person> identity = new HashMap<String, Person>();
}
