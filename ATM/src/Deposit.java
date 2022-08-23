import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Deposit {
    static void deposit(String[] credentials,Connection conn){
        int balance = Integer.parseInt(credentials[3]);
        int amount;
        System.out.print("Enter amount to deposit : ");
        String s = ScannerFix.sc.nextLine();
        amount = Integer.parseInt(s);
        balance = balance + amount;
        credentials[3] = String.valueOf(balance);
        String query = "update `users` set `balance`=? WHERE userid = ?;";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, balance);
            pst.setString(2, credentials[0]);
            pst.executeUpdate();
            TransactionHistory.updateRecord(conn, credentials[0], " deposited ", String.valueOf(amount), credentials[3],"",1);
            System.out.println(amount+" succesfully deposited to your account.");
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
}
