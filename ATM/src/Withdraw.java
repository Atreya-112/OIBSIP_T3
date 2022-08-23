import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Withdraw {
    static void withdraw(String[] credentials,Connection conn){
        int balance = Integer.parseInt(credentials[3]);
        int amount;
        System.out.print("Enter amount to withdraw : ");
        String s = ScannerFix.sc.nextLine();
        amount = Integer.parseInt(s);
        if(amount > balance) System.out.println("Insufficient Balance !!");
        else{
            balance = balance - amount;
            credentials[3] = String.valueOf(balance);
            String query = "update `users` set `balance`=? WHERE userid = ?;";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, balance);
                pst.setString(2, credentials[0]);
                pst.executeUpdate();
                TransactionHistory.updateRecord(conn, credentials[0], " withdrawn ", String.valueOf(amount), credentials[3],"0",1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
