
import com.fizzed.blaze.Task;

public class hello {
    
    @Task(order=2, value="Prints hello world")
    public void main() {
        System.out.println("Hello World!");
    }
    
    @Task(order=1, value="Prints java version")
    public void version() {
        System.out.println(System.getProperty("java.version"));
    }
    
}
