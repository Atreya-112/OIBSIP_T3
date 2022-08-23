import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws Exception {
        Connection conn = ConnectDB.getConnection();
        Menu(verifyCredentials(startUP(), conn),conn);
    }
    static String[] startUP(){
        String[] credentials = new String[5];
        credentials[4] = "0";
        System.out.println("Welcome To ATM");
        System.out.print("Enter UserID : ");
        credentials[0] = ScannerFix.sc.nextLine();
        System.out.print("Enter Pin : ");
        credentials[2] = ScannerFix.sc.nextLine();
        return credentials;
    }
    static String[] verifyCredentials(String[] credentials,Connection conn) {
        String query = "select * from users where userid = ?;";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, credentials[0]);
            ResultSet rs = pst.executeQuery();
            rs.next();
            credentials[1] = rs.getString("username");
            credentials[3] = rs.getString("balance");
            if(rs.getString("userpin").equals(credentials[2])) credentials[4] = "1";
        } catch (SQLException e) {
            //
        }
        return credentials;
    }
    static void Menu(String[] credentials,Connection conn){
        if(credentials[4].equals("1")){
            System.out.println("Welcome "+credentials[1]+" you have following choices : ");
            while (true) {
                String choice;
                System.out.println("1. Transaction History");
                System.out.println("2. Withdraw");
                System.out.println("3. Deposit");
                System.out.println("4. Transfer");
                System.out.println("5. Exit");
                System.out.print("Enter Serial Number : ");
                choice = ScannerFix.sc.nextLine();
                // sc.close();
                switch (choice) {
                    case "1": 
                        TransactionHistory.fetchHistory(conn, credentials[0]);   
                        break;
                    case "2":
                        Withdraw.withdraw(credentials, conn);
                        break;
                    case "3":
                        Deposit.deposit(credentials, conn);
                        break;
                    case "4":
                        Transfer.transfer(credentials, conn);
                        break;
                    case "5":
                        return;
                    default:
                        return;
                }
            }
        }
        else{
            System.out.println("User ID or PIN is wrong.");
            return; 
        }
    }
}
