package stuff;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class UnzippingJUnitTest {
    
    public UnzippingJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testIfCompressionIsShorter() {
        String text = "aaabbbaaacaccbbaaabbbaaacaccbbaaabbbaaacaccbb";
        String compressed = unzipping.LZ77.compressStr(text);
        assertTrue("compressed file is longer than before compression",
                   compressed.length() <= text.length());
    }
    
  
    
    
    @Test
    public void testIfDecompressionInvertCompression() {
        String text = "aaabbbaaacaccbbaaabbbaaacaccbbaaabbbaaacaccbb";
        String compressed = unzipping.LZ77.compressStr(text);
        String decompressed = unzipping.LZ77.decompressStr(compressed);
        assertEquals("Decompression does not invert compression",
                     text, decompressed);
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}