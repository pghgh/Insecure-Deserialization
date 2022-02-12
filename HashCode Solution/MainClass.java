import java.io.*;

public class MainClass {

    public static void main(String[] args) {

        /*
        Trying the example - steps:

        Part I: The insecure version

        1. The "User" object should be serialized (uncomment section A)
        2. The (.txt) file in which the object was serialized should be modified using a
        hex editor (a plugin offered by the IDE was used); one can modify the role "user1"
        to "admin"
        3. The "User" object should now be deserialized (uncomment section B); the role of
        the user has been changed, and this fact has not been recognized/reported; the solution
        to this problem is presented in Part II

        Part II: The secure version

        1. The "User" object should be serialized (uncomment section C)
        2. The (.txt) file in which the object was serialized should be modified using a
        hex editor (a plugin offered by the IDE was used); one can modify the role "user1"
        to "admin"
        3. The "User" object should now be deserialized (uncomment section D); if a solution
        such as using hash codes or digital signatures has been implemented, then an error
        will appear (since the object has been modified); otherwise the modification will not
        be recognized/reported to the terminal
        */

        // Section A
        /*
        User user = new User("user1");
        user.simpleSerializeObject("serializedUserData.txt");
        */

        // Section B
        /*
        User deserializedUserObject = User.simpleDeserializeObject("serializedUserData.txt");
        System.out.println("The role of the user is: "+ deserializedUserObject.getRole());
        */

        // Section C
        /*
        User user = new User("user1");
        user.serializeObjectWithHashCode("serializedUserData.txt");
        */

        // Section D
	/*
        User deserializedUserObject = User.deserializeObjectWithHashCode("serializedUserData.txt");
        System.out.println("The role of the user is: "+ deserializedUserObject.getRole());
	*/

    }


}
