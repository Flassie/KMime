# KMime
Mime detection using kotlin (uses `magic` file from `shared-mime-info v1.5`)

## Usage:
1. Import jar
2. Use Mime class to match files:

### Example:
#### Kotlin:
```
import com.flassie.KMime.Mime

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        println(Mime.match("E:/123.png"))
        println(Mime.match("E:/123.mp3")) // Same file with another extension
    }
}
```
#### Java:
```
import com.flassie.KMime.Mime;

public class Main {
    public static void main(String[] args) {
        String mime = Mime.match("E:/123.png");
        String mime2 = Mime.match("E:/123.mp3"); // Same file with another extension
        System.out.println(mime);
        System.out.println(mime2);
    }
}
```
#### Output (if `E:/123.png` and `E:/123.mp3` exist):
```
image/png
image/png
```
