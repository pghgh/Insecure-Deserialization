import java.io.*;
import java.security.*;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String role;
    private static String publicKeyFileName = "publicKeyValue.txt";
    private static String privateKeyFileName = "privateKeyValue.txt";


    public User(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void simpleSerializeObject(String serializationDataName) {

        // overwrite the content of the files if they already exist
        File file = new File(serializationDataName);
        if (file.exists()) {
            file.delete();
        }

        try {
            ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(serializationDataName, true));

            // serialize the "User" Object
            writer.writeObject(this);
            writer.close();
        }
        catch (Exception e) {
            System.err.println("Error when serializing the data: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("The serialization process has been successfully completed");
    }

    public static User simpleDeserializeObject(String deserializationDataName) {

        User deserializedUserObject = new User("");
        try {
            ObjectInputStream reader;

            // get the signed User object
            reader = new ObjectInputStream(new FileInputStream(deserializationDataName));
            deserializedUserObject = (User) reader.readObject();

        } catch (Exception e) {
            System.err.println("Error when deserializing the data: " + e.getMessage());
            System.exit(1);
        }

        return deserializedUserObject;
    }

    public void serializeObjectWithSignature(String serializationDataName) {

        // overwrite the content of the files if they already exist
        File file = new File(serializationDataName);
        if (file.exists()) {
            file.delete();
        }

        file = new File(publicKeyFileName);
        if (file.exists()) {
            file.delete();
        }

        file = new File(privateKeyFileName);
        if (file.exists()) {
            file.delete();
        }


        try {

            // CODE TAKEN FROM START <1>
            // The following code part has been written based on various sources (tutorials and the Java Documentation about digital signatures):
            // https://docs.oracle.com/javase/8/docs/api/java/security/SignedObject.html
            // https://docs.oracle.com/javase/tutorial/security/apisign/gensig.html
            // https://docs.oracle.com/javase/tutorial/security/apisign/versig.html
            // http://www.java2s.com/Tutorial/Java/0490__Security/SigningaJavaObject.htm

            // used algorithm: DSA (Digital Signature Algorithm)
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
            // keysize: 1024 bits
            keyGen.initialize(1024);
            // obtain the keypair
            KeyPair keypair = keyGen.genKeyPair();

            // we save/serialize the keys in 2 files
            ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(publicKeyFileName, true));
            writer.writeObject(keypair.getPublic());

            writer = new ObjectOutputStream(new FileOutputStream(privateKeyFileName, true));
            writer.writeObject(keypair.getPrivate());

            PrivateKey privateKey = keypair.getPrivate();
            Serializable o = this;
            // obtain a signature object which implements the chosen (DSA) algorithm
            Signature sig = Signature.getInstance(privateKey.getAlgorithm());
            // obtain a SignedObject based on the Serializable object o (i.e. the "User" object)
            SignedObject so = new SignedObject(o, privateKey, sig);

            // serialize the SignedObject
            writer = new ObjectOutputStream(new FileOutputStream(serializationDataName, true));
            writer.writeObject(so);
            writer.close();

            // CODE TAKEN FROM END <1>
        }
        catch (Exception e) {
            System.err.println("Error when serializing the data: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("The serialization process has been successfully completed");
    }

    public static User deserializeObjectWithSignature(String deserializationDataName) {

        User u = new User("");
        try {
            // CODE TAKEN FROM START <2>

            // The following code part has been written based on various sources (tutorials and the Java Documentation about digital signatures:
            // https://docs.oracle.com/javase/8/docs/api/java/security/SignedObject.html
            // https://docs.oracle.com/javase/tutorial/security/apisign/gensig.html
            // https://docs.oracle.com/javase/tutorial/security/apisign/versig.html
            // http://www.java2s.com/Tutorial/Java/0490__Security/SigningaJavaObject.htm

            ObjectInputStream reader;

            // get the signed User object
            reader = new ObjectInputStream(new FileInputStream(deserializationDataName));
            SignedObject deserializedUserObject = (SignedObject) reader.readObject();

            // get the previously saved public key
            reader = new ObjectInputStream(new FileInputStream(publicKeyFileName));
            PublicKey publicKey = (PublicKey) reader.readObject();
            reader.close();

            // obtain the digital signature of the received file for verification purpose
            Signature sig = Signature.getInstance(publicKey.getAlgorithm());

            // verify the digital signature
            boolean verification = deserializedUserObject.verify(publicKey, sig);
            if (verification) {
                // the object is safe to deserialize and we will return it
                Serializable o = (User) deserializedUserObject.getObject();
                u = (User) o;
            }
            else // the object has been modified
                throw new RuntimeException("The object has been modified");
            
            // CODE TAKEN FROM END <2>
        } catch (Exception e) {
            System.err.println("Error when deserializing the data: " + e.getMessage());
            System.exit(1);
        }

        return u;
    }
}