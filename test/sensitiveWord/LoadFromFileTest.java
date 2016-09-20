package sensitiveWord;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LoadFromFileTest {
    
    @Before
    public void setUp() throws Exception {
        wf = new WordFilter();
    }

    @Test
    public void test() {
        wf.load(fileName);
        assertEquals(wf.checkWord("shit"), true);
        assertEquals(wf.checkWord("江泽民"), true);
        assertEquals(wf.checkWord("shen尼玛"), true);
    }


    private static WordFilter wf = null;
    
    private static final String fileName = "sensitiveWords.txt";
}
