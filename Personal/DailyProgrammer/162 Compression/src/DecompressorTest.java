import java.io.File;

/**
 * Victor Kwak
 * CS141, section 01
 * (name of assignment here)
 * May 27, 2014
 */
public class DecompressorTest {
    public static void main(String[] args) {
        Decompressor decompressor = new Decompressor();
        decompressor.readFile(new File("input.txt"));
        decompressor.decompress();
        decompressor.writeFile(new File("output.txt"));
    }
}
