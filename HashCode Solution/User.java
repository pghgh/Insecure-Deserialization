import java.io.*;
import java.util.Scanner;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String role;
    private static String hashValuesFileName = "hashValues.txt";

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

    public void serializeObjectWithHashCode(String serializationDataName) {

        // overwrite the content of the file if it already exists
        File file = new File(serializationDataName);
        if (file.exists()) {
            file.delete();
        }

        try {
            // serialize the "User" object
            ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(serializationDataName, true));
            writer.writeObject(this);
            writer.close();
        } catch (Exception e) {
            System.err.println("Error when serializing the data: " + e.getMessage());
            System.exit(1);
        }

        // save the hash code in a file
        try {

            // CODE TAKEN FROM START <3>
            // The idea of using hash codes and saving them in a file was inspired by an answer-entry in the following forum:
            // https://stackoverflow.com/questions/22578017/check-whether-a-java-object-has-been-modified

            FileWriter fileWriter = new FileWriter(hashValuesFileName);
            fileWriter.write(Integer.toString(this.hashCode()));
            fileWriter.close();
            System.out.println("Successfully wrote the hash code to the file.");

            // CODE TAKEN FROM END <3>
        } catch (IOException e) {
            System.out.println("An error occurred when writing to the hashValuesFile: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("The serialization process has been successfully completed");
    }

    @Override
    public int hashCode() {
        // CODE TAKEN FROM START <4>
        // The implementation of this function was written was based on the following recommendations from this forum:
        // https://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method-for-a-collection

        int result = 37;
        int c = (role != null ? role.hashCode() : 0);
        result = 37 * result + c;
        return result;
        // CODE TAKEN FROM END <4>
    }

    public static User deserializeObjectWithHashCode(String deserializationDataName) {

        User u = new User("");
        try {
            // CODE TAKEN FROM START <5>
            // The implementation of this function was written was based on the following recommendations from this forum:
            // https://stackoverflow.com/questions/22578017/check-whether-a-java-object-has-been-modified

            ObjectInputStream reader;
            reader = new ObjectInputStream(new FileInputStream(deserializationDataName));
            User deserializedUserObject = (User) reader.readObject();
            int hashCodeDeserialized = deserializedUserObject.hashCode();
            int savedHashCode = 0;
            // obtain the original hash code (from the saved file)
            try {
                File hashValuesFile = new File(hashValuesFileName);
                Scanner scanner = new Scanner(hashValuesFile);
                if (scanner.hasNextLine()) {
                    String hashCode = scanner.nextLine();
                    savedHashCode = Integer.parseInt(hashCode);
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred when scanning the hashValuesFile:" + e.getMessage());
                e.printStackTrace();
            }

            // if the hash codes (the one saved in the hashValues file and the one from the received object)
            // do not match, then the object has been modified
            if (hashCodeDeserialized != savedHashCode) {
                System.out.println("hashCodeDeserialized = " + hashCodeDeserialized + " is not equal to " + "savedHashCode = " + savedHashCode);
                throw new RuntimeException("The object has been modified");
            }
            else
                u = deserializedUserObject; // the object has not been modified, so it is safe to return it
            reader.close();
            // CODE TAKEN FROM END <5>
        } catch (Exception e) {
            System.err.println("Error when deserializing the data: " + e.getMessage());
            System.exit(1);
        }

        return u;

    }
}