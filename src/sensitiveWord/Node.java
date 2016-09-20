package sensitiveWord;

import java.util.HashMap;
import java.util.Map;

public class Node {
	
	/**
	 *  indicate if the  node is the leaf.   0 : no  1 : yes 
	 */
	private int flag = Constant.BRANCH_NODE; 
	
	private Map<String, Node> has = new HashMap<String, Node>();

	public Node () {
	}
	
	public Node insert(char ch) {
		String key = String.valueOf(ch);
		Node n = new Node();
		has.put(key, n);
		return n;
	}
	
	public Node find (String ch) {
		return has.get(ch);
	}
	
	public void clear() {
	    flag = Constant.BRANCH_NODE; 
	    has.clear();
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
