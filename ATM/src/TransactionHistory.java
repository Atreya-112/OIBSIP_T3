import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionHistory {
    static String timeStamp(){
        long millis = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(millis);
        return timestamp.toString().substring(0, timestamp.toString().length()-4);
    }
    static void updateRecord(Connection conn,String Sender,String Receiver,String Amount,String BalanceR,String BalanceS,int Self){
        String query = "INSERT INTO `transactions`(`Sender`, `Receiver`, `Amount`, `Timestamp`, `BalanceR`, `BalanceS`, `Self`) VALUES (?,?,?,?,?,?,?);";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, Sender);
            pst.setString(2, Receiver);
            pst.setString(3, Amount);
            pst.setString(4, timeStamp());
            pst.setString(5, BalanceR);
            pst.setString(6, BalanceS);
            pst.setInt(7, Self);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void fetchHistory(Connection conn,String User){
        String query = "SELECT * FROM `transactions` WHERE sender = ? OR receiver = ?;";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, User);
            pst.setString(2, User);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                if(rs.getInt(7)==1){
                    System.out.println(rs.getString(4)+" "+rs.getString(3)+rs.getString(2)+"Resultant Balance : "+rs.getString(5));
                }
                else if(rs.getString(1).equals(User)){
                    System.out.println(rs.getString(4)+" Sent "+rs.getString(3)+" to : "+ rs.getString(2)+" Resulting Balance : "+rs.getString(6));
                }
                else{
                    System.out.println(rs.getString(4)+" Received "+rs.getString(3)+" from : "+ rs.getString(1)+" Resulting Balance : "+rs.getString(5));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
