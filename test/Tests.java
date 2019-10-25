import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Tests {

    ByteArrayOutputStream consoleOutput;
    Inspector inspector = new Inspector();

    @BeforeEach
    public void beforeEach() {
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
    }

    @AfterEach
    public void afterEach() {
        consoleOutput.reset();
    }

    @Test
    public void testSuperclass() throws Exception {
        ClassB classB = new ClassB();
        inspector.inspect(classB, false);

        assert (consoleOutput.toString().contains("Superclass name: ClassC"));
    }

    //since we ignore the object class, there should be no superclass
    @Test
    public void testNoSuperclass() {
        ClassA classA = new ClassA();
        inspector.inspect(classA, false);

        assert (consoleOutput.toString().contains("No superclass exists"));

    }

    @Test
    public void testInterface() {
        ClassA classA = new ClassA();
        inspector.inspect(classA, false);

        assert (consoleOutput.toString().contains("Interface name: Runnable"));
    }

    @Test
    public void testConstructorDetails() {
        ClassA classA = new ClassA();
        inspector.inspect(classA, false);

        assert (consoleOutput.toString().contains("Constructor name: ClassA"));
        assert (consoleOutput.toString().contains("Constructor parameter types: (int)"));
        assert (consoleOutput.toString().contains("Constructor modifiers: public"));
    }

    @Test
    public void testMethodDetails() {
        ClassA classA = new ClassA();
        inspector.inspect(classA, false);

        assert (consoleOutput.toString().contains("Method name: run"));
        assert (consoleOutput.toString().contains("Method return type: void"));
        assert (consoleOutput.toString().contains("Method modifiers: public"));
    }

    @Test
    public void testFieldDetails() {
        ClassA classA = new ClassA();
        inspector.inspect(classA, false);

        assert (consoleOutput.toString().contains("Field name: val"));
        assert (consoleOutput.toString().contains("Field type: int"));
        assert (consoleOutput.toString().contains("Field modifiers: private"));
    }

    @Test
    public void testArray() {
        ClassD classD = new ClassD();
        inspector.inspect(classD, false);

        assert (consoleOutput.toString().contains("Field is array"));
        assert (consoleOutput.toString().contains("Array component type: ClassA"));
        assert (consoleOutput.toString().contains("Array length: 10"));
        assert (consoleOutput.toString().contains("Array content: [null,null,null,null,null,null,null,null,null,null]"));
    }

    @Test
    public void testRecursion() throws Exception {
        ClassB classB = new ClassB();
        inspector.inspect(classB, false);

        assert (consoleOutput.toString().contains("Class name: ClassD"));
    }
}