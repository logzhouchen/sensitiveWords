package sensitiveWord;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RootNodeGCTest {

    @Before
    public void setUp() throws Exception {
        wf = new WordFilter();
    }

    @Test
    public void test() {
        new checkTread().start();
        
        while( true ) {
            long now  = System.currentTimeMillis();
            int half = (int) (now % 2);
            wf.load(fileName[half]); 
            
            Node root = wf.getRoot();
            System.out.println(root.toString() + " constructed!");
            WeakReference<Node> reference = new WeakReference<Node>(root);
            queue.add(reference);
            root = null;
            
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private class checkTread extends Thread {
        @Override
        public void run(){ 
            while ( true ) {
                System.out.println("size " + queue.size());
                for(WeakReference<Node> item : queue) {
                    if ( item.get() == null ) {
                        System.out.println("object is collected!");
                    } else {
                        System.out.println( item.get().toString() + " is Not collected");
                    }
                }
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } 
    }
    
    private static WordFilter wf = null;
    
    private static final String[] fileName = new String[]{"sensitiveWords1.txt", "sensitiveWords2.txt"};

    List<WeakReference<Node>> queue = new ArrayList<WeakReference<Node>>();
}
