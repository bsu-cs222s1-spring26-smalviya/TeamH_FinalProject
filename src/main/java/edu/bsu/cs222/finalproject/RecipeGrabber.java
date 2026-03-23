package edu.bsu.cs222.finalproject;
//import java.util.Scanner;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class RecipeGrabber {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URLConnection connection = connectToMealDb();
        String data =readJsonAsStringFrom(connection);
        //printData(data);

    }

    public static URLConnection connectToMealDb() throws IOException, URISyntaxException{
        //Scanner scanner = new Scanner(System.in);
        //System.out.print("Enter ingredient: ");
        //String input = scanner.nextLine();

        String encodedUrlString = "https://www.themealdb.com/api/json/v1/1/filter.php?i=" +
                URLEncoder.encode("chicken_breast", Charset.defaultCharset()) +
                "&rvprop=timestamp" + URLEncoder.encode("|",Charset.defaultCharset()) + "user&rvlimit=4&redirects";
        URI uri = new URI(encodedUrlString);
        URLConnection connection = uri.toURL().openConnection();
        connection.setRequestProperty("User-Agent",
                "FinalProjectCs222/0.1 (academic use; rj.martin@bsu.edu)");
        connection.connect();
        return connection;
    }
    public static String readJsonAsStringFrom(URLConnection connection) throws IOException {
        return new String(connection.getInputStream().readAllBytes(), Charset.defaultCharset());
    }
    public static void printData(String data){
        System.out.println(data);
    }
}
