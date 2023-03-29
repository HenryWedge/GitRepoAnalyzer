package output;

public class ConsoleOutput implements Output {

    @Override
    public void println(final Object object) {
        System.out.println(object);
    }
}
