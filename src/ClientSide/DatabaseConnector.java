package ClientSide;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector{
    public static Connection connectDatabase(){
        //database connection detauils
        String jdbcURL="jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";  // Replace with your database username
        String password = "Avishkar@2004";
        Connection connection = null;
        try{
            connection= DriverManager.getConnection(jdbcURL,username,password);
        }catch(Exception e){
            System.out.println("The exception is: "+ e);
        }
        return connection;

    }

    public static void main(String args[]){


    }
}