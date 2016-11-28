# KMime
Mime detection using kotlin

## Usage:
1. Import jar
2. Use Mime class to match files:

### Example:
```
import com.flassie.KMime.Mime

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        println(Mime.match("E:/123.png"))
    }
}
```
Will show:
```
image/png
```
