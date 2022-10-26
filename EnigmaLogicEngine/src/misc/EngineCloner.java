package misc;

import engine.EncryptionMachineEngine;

import java.io.*;

public class EngineCloner {
    public EncryptionMachineEngine cloneEngine(EncryptionMachineEngine engine) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(engine);

        ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (EncryptionMachineEngine) in.readObject();

    }
}
