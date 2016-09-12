package sensitiveWord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WordFilter{
    private static Node rootNode = new Node();  
    
	/**
	 * construct the tree recursively by inserting nodes
	 * @param node
	 * @param cs
	 * @param index
	 */
	private void insertNode(Node node, char[] cs, int index) {  
        Node n = node.find(String.valueOf(cs[index]));  
        if (n == null)
            node.insert( cs[index] );
        
        // flag if this is the ending of the sensitive word 
        if (index == (cs.length-1))  
            n.setFlag(Constant.LEAF_NODE);  
               
        index++;
        // if not, continue to insert nodes recursively
        if (index < cs.length)  
            insertNode(n, cs, index);  
    }


    public void load(String filename) {
        synchronized (rootNode) {
            if(rootNode != null) {
                rootNode.clear();
            }

            try {
                File badwords = new File(filename); 
                BufferedReader r = new BufferedReader(new FileReader(badwords));
 
                String line = "";
                while((line = r.readLine()) != null) {
                    char[] chars = line.toCharArray();  
                    if(chars.length > 0)  
                        insertNode(rootNode, chars, 0);  
                }
                r.close();
            }catch(Exception e){
                System.out.println("Exception in loading sensitive words from file");
            }
        }
    }
   
    public void update(Set<String> words) {
        synchronized ( rootNode ) {
            if (rootNode != null) {
                rootNode.clear();
            }

            try {  
                for(String word : words) {  
                    if(word==null || word.length() < 1)
                        continue;
                    word = word.toLowerCase();
                    //    
                    word = new String(word.getBytes("iso-8859-1"),"UTF-8");
                    char[] chars = word.toCharArray();  
                    if(chars.length > 0)  
                        insertNode(rootNode, chars, 0);  
                } 
            }catch(Exception e){
                System.out.println("Exception in updating sensitive words");
            }
        }
    }
   
    public List<String> searchWord(String data){
    synchronized(rootNode){      
 
       char[] chars = data.toCharArray();  
       List<String> word = new ArrayList<String>();
       List<String> words = new ArrayList<String>();
       
       Node node = rootNode;  
       int index = 0;
       
       while(index<chars.length) {  
           node = node.find(String.valueOf(chars[index]));      
           if(node == null) {  
               node = rootNode;  
               index = index - word.size();  
               word.clear();  
           } else if(node.getFlag() == 1) {  
               word.add(String.valueOf(chars[index]));  
               StringBuffer sb = new StringBuffer();  
               for(String str : word) {  
                   sb.append(str);  
               }  
               words.add(sb.toString());  
               index = index - word.size() + 1;  
               word.clear();  
               node = rootNode;  
           } else {  
               word.add(String.valueOf(chars[index]));  
           }  
           index++;  
       }  
       word.clear();
       return words;
    }
    }  
         
    public boolean checkWord(String data) {  
        data = data.toLowerCase();
        char[] chars = data.toCharArray();  
        List<String> word = new ArrayList<String>();
       
        Node node = rootNode;  
        int index = 0;
       
        while(index<chars.length) {  
            node = node.find(String.valueOf(chars[index]));      
            if(node == null) {  
                node = rootNode;  
                index = index - word.size();  
                word.clear();  
            } else if(node.getFlag() == 1) {  
                word.clear();  
                return true;  
            } else {  
                word.add(String.valueOf(chars[index]));  
            }  
            index++;  
        }  
        word.clear();
        return false;
    }      
}
