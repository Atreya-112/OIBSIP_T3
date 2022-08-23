import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transfer {
    static void transfer(String[] credentials,Connection conn){
        int balance = Integer.parseInt(credentials[3]);
        String accountID;
        int amountSender;
        int amountReceiver;
        String receiver;
        System.out.print("Enter account ID of receiver : ");
        accountID = ScannerFix.sc.nextLine();
        if(accountID.equals(credentials[0])){
            System.out.println("You are not allowed make a self transfer use deposit function.");
            return;
        }
        System.out.print("Enter amount to transfer : ");
        String s = ScannerFix.sc.nextLine();
        amountSender = Integer.parseInt(s);
        if(balance < amountSender){
            System.out.println("Insufficient balance.");
            return;
        }
        balance = balance - amountSender;
        credentials[3] = String.valueOf(balance);
        String query = "select * from `users` where userid = ?;";
        String query_1 = "update `users` set `balance`=? WHERE userid = ?;";
        String query_2 = "update `users` set `balance`=? WHERE userid = ?;";
        try (PreparedStatement pst = conn.prepareStatement(query_1)) {
            //Reducing sender's balance.
            pst.setInt(1, balance);
            pst.setString(2, credentials[0]);
            pst.executeUpdate();

            //Retrieving reciever's balance.
            PreparedStatement pst1 = conn.prepareStatement(query);
            pst1.setString(1, accountID);
            ResultSet rs = pst1.executeQuery();
            rs.next();
            amountReceiver = Integer.parseInt(rs.getString("balance"));
            receiver = rs.getString("username");
            
            //Updating reciever's balance.
            amountReceiver = amountReceiver + amountSender;
            PreparedStatement pst2 = conn.prepareStatement(query_2);
            pst2.setInt(1, amountReceiver);
            pst2.setString(2, accountID);
            pst2.executeUpdate();
            
            TransactionHistory.updateRecord(conn,credentials[0], accountID,String.valueOf(amountSender), String.valueOf(amountReceiver),credentials[3],0);
            System.out.println( amountSender+" succesfully transferred to "+receiver);
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
