package edu.bsu.cs222.finalproject;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class UserStorageAttemptTwo {
    static StringBuffer stringBufferOfData = new StringBuffer();
    static String filename = null;
    static ArrayList<String> loginData = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static String answer;
    static String username;
    static String password;
    static String checkUsername;
    static String checkPassword;
    static String recipeName;

    static boolean permission = true;//if/else should be gates added to check if user is logged in when moved to javafx

    public static void main(String[] args){
        boolean fileRead = readFile();

        if (fileRead) {
            replacement();

            writeToFile();
        }
        System.exit(0);

    }

    private static boolean readFile() {
        System.out.println("Create Account (c) login to account (l) or save recipe (r)");
        answer = sc.nextLine();
        if (answer.equalsIgnoreCase("c")){
            System.out.println("Enter username: ");
            username = sc.nextLine();
            if (username.contains(" ")){
                System.out.println("Spaces are not allowed");
                System.exit(0);//this does not need to stay once in javafx
            }
            System.out.println("Enter password: ");
            password = sc.nextLine();
            if (password.contains(" ")){
                System.out.println("Spaces are not allowed");
                System.exit(0);//does not need to stay once in javafx
            }
            //createAccount(username, password);
        }
        else if (answer.equalsIgnoreCase("l")){
            System.out.println("Enter username: ");
            checkUsername = sc.nextLine();
            System.out.println("Enter password: ");
            checkPassword = sc.nextLine();
            //signIn(checkUsername, checkPassword);
        }
        else if (answer.equalsIgnoreCase("r")){
            if (!permission){
                System.out.println("Please log into an account before saving recipes");
                System.exit(0);//does not need to stay once in javafx
            } else{
                System.out.println("Enter username: ");//this line and the one checking for password will not be necessary in javafx. user will stay logged in
                username = sc.nextLine();
                System.out.println("Enter password: ");//this line and the one checking for username will not be needed once in javafx
                password = sc.nextLine();
                System.out.println("Enter recipe name: ");
                recipeName = sc.nextLine();
                if (recipeName.contains(" ")){
                    recipeName = recipeName.replace(" ", "_");
                    System.exit(0);//does not need to stay once in javafx
                }

                //addRecipe(username, password, recipeName);
            }
        }
        filename = "C:\\Users\\graff\\IdeaProjects\\TeamH_FinalProject\\src\\main\\java\\edu\\bsu\\cs222\\finalproject\\logins";
        try (Scanner fileToRead = new Scanner(new File(filename))) {

            for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null; ) {
                //System.out.println(line);
                loginData.add(line);

                stringBufferOfData.append(line).append("\r\n");
            }

            fileToRead.close();
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println("The file " + filename + " could not be found! " + ex.getMessage());
            return false;

        } finally {
            return true;
        }
    }

    private static void writeToFile() {
        try {
            BufferedWriter buffwriter = new BufferedWriter(new FileWriter(filename));
            buffwriter.write(stringBufferOfData.toString());
            buffwriter.close();

        } catch (Exception e) {
            System.out.println("Error occured while attempting to write to file: " + e.getMessage());
        }
    }



    private static void replacement(){
        if (answer.equalsIgnoreCase("c")){
            String replacementText = username + " " + password + "\n";
            stringBufferOfData.append(replacementText);

        } else if (answer.equalsIgnoreCase("l")) {

            boolean failedLogin = true;

            for (String loginDatum : loginData) {
                if (loginDatum.equals(checkUsername + " " + checkPassword)) {
                    permission = true;
                    System.out.println("You're logged in!");
                    failedLogin = false;
                    break;
                }
            }
            if (failedLogin) {
                System.out.println("Invalid username or login");
            }
        } else{
            String lineToEdit = username + " " + password;
            String replacementText = username + " " + password + " " + recipeName;
            int startIndex = stringBufferOfData.indexOf(lineToEdit);
            int endIndex = startIndex + lineToEdit.length();

            stringBufferOfData.replace(startIndex, endIndex, replacementText);
        }
    }

    /*public static void createAccount(String username, String password){
        String replacementText = username + " " + password + "\n";
        stringBufferOfData.append(replacementText);
    }
    public static void signIn(String username, String password){
        for (String loginDatum : loginData) {
            if (loginDatum.equals(username + " " + password)) {
                permission = true;
                System.out.println("You're logged in!");
                break;
            } else {
                System.out.println("Invalid username or login");
            }
        }
    }
    public static void addRecipe(String username, String password, String recipe){
        String lineToEdit = username + " " + password;
        String replacementText = username + " " + password + " " + recipe;
        int startIndex = stringBufferOfData.indexOf(lineToEdit);
        int endIndex = startIndex + lineToEdit.length();

        stringBufferOfData.replace(startIndex, endIndex, replacementText);
    }

     */
}
