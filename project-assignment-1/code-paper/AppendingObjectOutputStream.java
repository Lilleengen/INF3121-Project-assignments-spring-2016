package hangman;

//Credit to Andreas_D @ Stack Overflow: http://stackoverflow.com/a/1195078
//Licensed under CC BY-SA (https://creativecommons.org/licenses/by-sa/2.0/) as according to the Stack Overflow ToS (https://stackexchange.com/legal/terms-of-service)
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AppendingObjectOutputStream extends ObjectOutputStream {

    public AppendingObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override protected void writeStreamHeader() throws IOException {
        reset();
    }

}