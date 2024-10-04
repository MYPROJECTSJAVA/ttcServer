package ClientSide;
import javax.swing.*;
import java.sql.*;


public class Player {
    public String letter;
    String name;
    Connection connectDB;
    public Player(String L) {

        letter = L;
        connectDB= DatabaseConnector.connectDatabase();
    }
    // authentication method
    void authenticateUser(){
        // just take names for now
        name=JOptionPane.showInputDialog(null,"Enter your name","");
        String sql="select password from users where name = ?";
        try{
            PreparedStatement preparedStatement=connectDB.prepareStatement(sql);
            preparedStatement.setString(1,name);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                String pass=resultSet.getString("password");
                String passInput=JOptionPane.showInputDialog(null,"Enter your password","");
                while(pass.compareTo(passInput)==0){
                    passInput=JOptionPane.showInputDialog(null,"Wrong Password!! Re-enter your password again","");
                }
            }
            else{
                int response = JOptionPane.showConfirmDialog(null, "Username doesnot exist.Do you want to add yourself?", "Select an Option",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(response==JOptionPane.YES_OPTION){
                    this.addNewUser();

                }
                else{
                    //exit
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

    }
    //add new user to the database
    private void addNewUser(){
        String sql="insert into users (name,password) values (?,?)";
        try {
            //set statement
            PreparedStatement statement = connectDB.prepareStatement(sql);
            statement.setString(1,JOptionPane.showInputDialog(null,"Enter to Register your name",""));
            statement.setString(2,JOptionPane.showInputDialog(null,"Enter your new password",""));


            int affectedRows= statement.executeUpdate();
        }catch(Exception e){System.out.println(e);}

    }

    public static void main(String[] args){

    }
}
