package com.miftahunajat.util;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.*;

public class ThreadUtil {
    static boolean UseStdStreams = false;
    static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();


    public static Object readObjectFromStream(DataInputStream inputStream) throws IOException, ClassNotFoundException {
        if ( UseStdStreams ) {
            ObjectInputStream in = new ObjectInputStream(inputStream);
            return in.readObject();
        } else {
            int len = inputStream.readInt();
            byte buffer[] = new byte[len]; // this could be reused !
            while (len > 0)
                len -= inputStream.read(buffer, buffer.length - len, len);
            return ThreadUtil.conf.getObjectInput(buffer).readObject();
        }
    }

    public static void writeObjectToStream(DataOutputStream outputStream, Object toWrite) throws IOException {
        if ( UseStdStreams ) {
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(toWrite);
            out.flush();
        } else {
            // write object
            FSTObjectOutput objectOutput = conf.getObjectOutput(); // could also do new with minor perf impact
            // write object to internal buffer
            objectOutput.writeObject(toWrite);
            // write length
            outputStream.writeInt(objectOutput.getWritten());
            // write bytes
            outputStream.write(objectOutput.getBuffer(), 0, objectOutput.getWritten());

            objectOutput.flush(); // return for reuse to conf
        }
    }
}
