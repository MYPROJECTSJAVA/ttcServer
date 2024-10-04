package Serverside;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class Game {
    String p1,p2,winner;
    String [] Board=new String[9];
    boolean turn =false;
    public void addPlayer(String s){
        if(p1==null)p1=s;
        else if(p2==null)p2=s;
        else{System.out.println("Too many players");}
    }
    public String makeMove(String i){
        turn=!turn;
        int point=parseInt(i)-1;
        if(point>9){
            return "";
        }
        if(Board[point]==null){
            Board[point]=(turn?p1:p2);
            winner=logic(Board);
            return (point+1)+(turn?p1:p2);
        }

        return "";
    }
    public String logic(String[] arr) {
        //row
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        for (int i : new int[]{0, 3, 6}) {
            if (arr[i] != null && Objects.equals(arr[i], arr[i + 1]) && Objects.equals(arr[i], arr[i + 2])) {
                System.out.println("I am here1");
                winner=arr[i];
                return arr[i];
            }
        }
        //column
        for (int i : new int[]{0, 1, 2}) {
            if (arr[i] != null && Objects.equals(arr[i], arr[i + 3]) && Objects.equals(arr[i], arr[i + 6])) {
                System.out.println("I am here2");
                winner=arr[i];
                return arr[i];
            }
        }
        //diagonal
        if (arr[4] != null && Objects.equals(arr[4], arr[0]) && Objects.equals(arr[4], arr[8])) {
            System.out.println("I am here3");
            winner=arr[4];
            return arr[4];
        }
        if (arr[4] != null && Objects.equals(arr[4], arr[2]) && Objects.equals(arr[4], arr[6])) {
            System.out.println("I am here4");
            winner=arr[4];
            return arr[4];

        }
        System.out.println("The logic winner is");
        return "";
    }

}
