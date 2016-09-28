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
        if ( index >= cs.length ) {
            return;
        }

        Node tmp = node.find(String.valueOf(cs[index]));  
        if (tmp == null)
            tmp = node.insert( cs[index] );
        
        // flag if this is the ending of the sensitive word 
        if ( index == (cs.length-1) ) {
            tmp.setFlag(Constant.LEAF_NODE);  
        } else {
            // if not, continue to insert nodes recursively
            index ++;
            insertNode(tmp, cs, index); 
        }
    }

    /**
     * load sensitive words from a file
     * @param filename  the name of the file containing the sensitive words
     */
    public void load(String filename) {
        Node root = new Node();
        try {
            File badwords = new File(filename); 
            BufferedReader r = new BufferedReader(new FileReader(badwords));

            String line = "";
            while((line = r.readLine()) != null) {
                char[] chars = line.toCharArray();  
                if(chars.length > 0)  
                    insertNode(root, chars, 0);  
            }
            r.close();
            
            // newly construct the tree and let it be the root node 
            rootNode = root;
        }catch(Exception e) {
            System.out.println("Exception in loading sensitive words from file");
        }
    }
   
    public void update(Set<String> words) {
        Node root = new Node();
        try {  
            for(String word : words) {
                if(word==null || word.length() < 1)
                    continue;
                word = word.toLowerCase();
                //iso-8859-1  take one byte as a character
                word = new String(word.getBytes("iso-8859-1"),"UTF-8");
                char[] chars = word.toCharArray();  
                if(chars.length > 0)  
                    insertNode(root, chars, 0);  
            } 
            
            rootNode = root;
        }catch(Exception e){
            System.out.println("Exception in updating sensitive words");
        }
    }
   
    public List<String> searchWord(String data){
        char[] chars = data.toCharArray();  
        List<String> word = new ArrayList<String>();
        List<String> words = new ArrayList<String>();
        
        Node root = rootNode, node = root;
        int index = 0;

        //word.clear() is to release memory immediately 
        while ( index < chars.length ) {  
            node = node.find(String.valueOf(chars[index]));
            if ( node == null ) {  
                node = root;  
                index = index - word.size();  
                word.clear();  
            } else if( node.getFlag() == 1 ) {
                word.add(String.valueOf(chars[index]));  
                StringBuffer sb = new StringBuffer();  
                for(String str : word) {  
                    sb.append(str);  
                }  
                words.add(sb.toString());  
                index = index - word.size() + 1;  
                word.clear();  
                node = root;  
            } else {  
                word.add(String.valueOf(chars[index]));  
            }  
            index++;  
        }  
        word.clear();
        return words;
    }  
         
    
    public boolean checkWord(String data) {  
        data = data.toLowerCase();
        char[] chars = data.toCharArray();
       
        Node root = rootNode, node = root;
        int index = 0;
        int len = 0;
        
        while( index < chars.length ) {  
            node = node.find(String.valueOf(chars[index]));
            if (node == null) {
                node = root;
                index = index - len;
                len = 0;  
            } else if(node.getFlag() == 1) {
                len = 0;  
                return true;  
            } else {
               len ++ ;
            }  
            index ++ ;  
        }
        return false;
    }
    
    public Node getRoot() {
        return rootNode;
    }
}
