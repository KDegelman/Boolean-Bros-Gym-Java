import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.Map;
import java.net.ServerSocket;


public class Server {
    String DatabaseName;
    String url;
    String username;
    String password;
    Connection DatabaseConnect;
    ResultSet columns;
    ServerSocket serverSocket;


    public Server() {
        Map<String, String> env;
        EnvParser envParser = new EnvParser();
//        try {
//            env = envParser.getEnvVars();
//            for (String pass : env.keySet()) {
//                switch (pass) {
//                    case "DB_URL" -> this.url = env.get(pass);
//                    case "DB_USERNAME" -> this.username = env.get(pass);
//                    case "DB_PASSWORD" -> this.password = env.get(pass);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Error reading and grabbing environment variables. Are they set properly?");
//        }
        //Kolten Change
        this.url = "jdbc:mysql://localhost:3306/sys";
        this.username = "root";
        this.password = "YOUR_PASSWORD";
        try {
            DatabaseName = "";
            assert url != null;
            DatabaseConnect = DriverManager.getConnection(url, username, password);
            PreparedStatement introStmt = DatabaseConnect.prepareStatement("SELECT DATABASE();");
            ResultSet rs = introStmt.executeQuery();
            if (rs.next()) {
                DatabaseName = rs.getString(1);
            }
            DatabaseMetaData metaData = DatabaseConnect.getMetaData();
            columns = metaData.getColumns(DatabaseName, null, "Members", null);
            System.out.println("Database has connected. Database name: " + DatabaseName);
//Kolten Change
            serverSocket = new ServerSocket(1234);
            System.out.println(("Server is listening on port 1234"));
            while (true) {
                Socket client = serverSocket.accept();
                Thread clientHandler = new Thread(new ClientHndlr(client, this));
                clientHandler.start();
            }

        } catch (SQLException | IOException e) {
            System.out.println("Error creating Server class and connecting to database. ERROR: " + e.getMessage());
        }
    }

    public boolean isMemberIDUsed(int MemberId) {
        PreparedStatement stmt;
        try {
            stmt = DatabaseConnect.prepareStatement("SELECT * FROM members WHERE MemberID = ?");
            stmt.setInt(1, MemberId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking if member id is used: " + e.getMessage());
            return true;
        }
    }
    
    
    public void readOneEntry(int MemberID) {
        PreparedStatement stmt;
        try {
            stmt = DatabaseConnect.prepareStatement("SELECT * FROM members WHERE MemberID = " + MemberID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Member found, displaying data");
                System.out.println("MemberID: " + rs.getInt("MemberID") + ", First Name: "
                        + rs.getString("First Name") + " Last Name: " + rs.getString("Last Name")+ "Date of Birth" +
                        rs.getString("Date of Birth") +
                        ", Email: " + rs.getString("Email") + ", Phone Number: " + rs.getString("Phone Number") + ", Gender: "
                        + rs.getInt("Gender") + ", City of Residence: " + rs.getInt("City of Residence"));
            } else {
                System.out.println("Member not found in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error reading entry: " + e.getMessage());
        }
    }
    
    public void readOneEntry(int userId, PrintWriter clientOut) {
        PreparedStatement stmt;
        try {
            stmt = DatabaseConnect.prepareStatement("SELECT * FROM members WHERE MemberID = " + userId);
            ResultSet rs = stmt.executeQuery();
            System.out.println();
            if (rs.next()) {
                System.out.println("Member found, displaying data");
                System.out.println("MemberID: " + rs.getInt("MemberID") + ", First Name: "
                        + rs.getString("First_Name") + " Last Name: " + rs.getString("Last_Name")+ "DateofBirth" +
                        rs.getString("Date_of_Birth") +
                        ", Email: " + rs.getString("Email") + ", Phone Number: " + rs.getString("Phone_Number") + ", Gender: "
                        + rs.getString("Gender") + ", City of Residence: " + rs.getString("City_of_Residence"));
                clientOut.println("MemberID: " + rs.getInt("MemberID") + ", First Name: "
                        + rs.getString("First_Name") + " LastName: " + rs.getString("Last_Name")+ "Date of Birth" +
                        rs.getString("Date_of_Birth") +
                        ", Email: " + rs.getString("Email") + ", Phone Number: " + rs.getString("Phone_Number") + ", Gender: "
                        + rs.getString("Gender") + ", City of Residence: " + rs.getString("City_of_Residence")
                );
                clientOut.println("END");
            } else {
                System.out.println("Member not found in the database.");
                clientOut.println("Member not found in the database.");
                clientOut.println("END");
            }
        } catch (SQLException e) {
            System.out.println("Error reading entry: " + e.getMessage());
            clientOut.println("Error reading entry: " + e.getMessage());
            clientOut.println("END");
        }
    }
    
    public void readAllData() {
        Statement selectAll;
        try {
            selectAll = DatabaseConnect.createStatement();
            ResultSet rs = selectAll.executeQuery("SELECT * FROM members");
            System.out.println("All members:");
            while (rs.next()) {
                System.out.println("MemberID: " + rs.getInt("MemberID") + ", First Name: "
                        + rs.getString("First_Name") + " LastName: " + rs.getString("Last_Name")+ " Date of Birth" +
                        rs.getString("Date_of_Birth") +
                        ", Email: " + rs.getString("Email") + ", Phone Number: " + rs.getString("Phone_Number") + ", Gender: "
                        + rs.getInt("Gender") + ", City of Residence: " + rs.getInt("City_of_Residence")
                );}
        } catch (SQLException e) {
            System.out.println("Error reading all data: " + e.getMessage());
        }
    }
    
    public void readAllData(PrintWriter Out) {
        Statement selectAll;
        try {
            selectAll = DatabaseConnect.createStatement();
            ResultSet rs = selectAll.executeQuery("SELECT * FROM sys.members");
            System.out.println("All Members:");
            while (rs.next()) {
                System.out.println("MemberID: " + rs.getInt("MemberID") + ", First Name: "
                + rs.getString("First_Name") + " LastName: " + rs.getString("Last_Name")+ " Date of Birth" +
                                rs.getString("Date_of_Birth") +
                ", Email: " + rs.getString("Email") + ", Phone Number: " + rs.getString("Phone_Number") + ", Gender: "
                                + rs.getString("Gender") + ", City of Residence: " + rs.getString("City_of_Residence")
                );
                Out.println("MemberID: " + rs.getInt("MemberID") + ", First Name: "
                        + rs.getString("First_Name") + " LastName: " + rs.getString("Last_Name")+ " Date of Birth" +
                        rs.getString("Date_of_Birth") +
                        ", Email: " + rs.getString("Email") + ", Phone Number: " + rs.getString("Phone_Number") + ", Gender: "
                        + rs.getString("Gender") + ", City of Residence: " + rs.getString("City_of_Residence")
                );

            }
            Out.println("END");

        } catch (SQLException e) {
            System.out.println("Error reading all data: " + e.getMessage());
            Out.println("Error reading all data: " + e.getMessage());
            Out.println("END");
        }
    }

    public void createEntry(int MemberID, String firstName, String lastName,String DateOfBirth,
                            String PhoneNmbr, String Email, String Gender, String CityOfRes) {
        PreparedStatement stmt;
        if (isMemberIDUsed(MemberID) || MemberID == 0) {
            System.out.println("Member ID is already used or empty, finding first available id");
            int buffer = 1;
            while (true) {
                if (!isMemberIDUsed(buffer)) {
                    MemberID = buffer;
                    System.out.println("Member id is now " + MemberID);
                    break;
                }
                else {
                    buffer++;
                }
            }
        }

        try {
            stmt = DatabaseConnect.prepareStatement("INSERT INTO members VALUE (?, ?, ?, ?, ?, ?, ?, ?);");
            stmt.setInt(1, MemberID);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, DateOfBirth);
            stmt.setString(5, Email);
            stmt.setString(6, PhoneNmbr);
            stmt.setString(7, Gender);
            stmt.setString(8, CityOfRes);
            if (!isMemberIDUsed(MemberID)) {
                stmt.executeUpdate();
                System.out.println("Member created successfully");
            }
            else {
                System.out.println("Member id is already used.");
            }


        } catch (SQLException e) {
            System.out.println("Error creating entry: " + e.getMessage());
        }
    }

    public void createEntry(int MemberID, String firstName, String lastName, String DateOfBirth,
                            String PhoneNmbr, String Email,
                            String Gender, String CityOfRes, PrintWriter clientOut) {
        PreparedStatement stmt;
        if (isMemberIDUsed(MemberID) || MemberID == 0) {
            System.out.println("Member ID is already used or empty, finding first available id");
            clientOut.println("Member ID is already used or empty, finding first available id");
            int buffer = 1;
            while (true) {
                if (!isMemberIDUsed(buffer)) {
                    MemberID = buffer;
                    System.out.println("Member ID is now " + MemberID);
                    clientOut.println("Member ID is now " + MemberID);
                    break;
                }
                else {
                    buffer++;
                }
            }
        }

        try {
            stmt = DatabaseConnect.prepareStatement("INSERT INTO members VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            stmt.setInt(1, MemberID);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, DateOfBirth);
            stmt.setString(5, Email);
            stmt.setString(6, PhoneNmbr);
            stmt.setString(7, Gender);
            stmt.setString(8, CityOfRes);
            if (!isMemberIDUsed(MemberID)) {
                stmt.executeUpdate();
                System.out.println("Member created successfully");
                clientOut.println("Member created successfully");
                clientOut.println("END");
            }
            else {
                System.out.println("Member ID is already used.");
                clientOut.println("Member ID is already used.");
                clientOut.println("END");

            }


        } catch (SQLException e) {
            System.out.println("Error creating entry: " + e.getMessage());
            clientOut.println("Error creating entry: " + e.getMessage());
            clientOut.println("END");
        }
    }

    public boolean updateEntry(int MemberID, String column, String value) {
        PreparedStatement stmt;
        boolean columnFound = false;
        String oldValue = "";
        try {
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if (columnName.equals(column)) {
                    System.out.println("Column found: " + columnName);
                    columnFound = true;
                    break;
                }
            }
            if (!columnFound) {
                System.out.println("Column not found in the database.");
                return false;
            }
            stmt = DatabaseConnect.prepareStatement("SELECT * FROM members WHERE MemberID = ?");
            stmt.setInt(1, MemberID);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                System.out.println("Member found!");
                String columnName = columns.getString("COLUMN_NAME");
                PreparedStatement stmt2 = DatabaseConnect.prepareStatement("SELECT ? FROM members WHERE MemberID = ?");
                stmt2.setString(1, columnName);
                stmt2.setInt(2, MemberID);

                ResultSet oldValueRS = stmt2.executeQuery();
                if (oldValueRS.next()) oldValue = oldValueRS.getString(columnName);
                if (column.equals("MemberID")) {
                    if (isMemberIDUsed(Integer.parseInt(value))) {
                        System.out.println("Member ID is already used.");
                        return false;
                    }
                }
                PreparedStatement stmt3 = DatabaseConnect.prepareStatement("UPDATE members SET ? = ? WHERE MemberID = ?");
                stmt3.setString(1, columnName);
                stmt3.setString(2, value);
                stmt3.setInt(3, MemberID);
                stmt3.executeUpdate();
                System.out.println("Updated " + columnName + " from " + oldValue + " to " + value);
                return true;
            }
            else {
                System.out.println("Member not found in the database.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating entry: " + e.getMessage());

        }
        return false;

    }

    public boolean updateEntry(int MemberID, String column, String value, PrintWriter clientOut) {
        PreparedStatement stmt;
        boolean columnFound = false;
        String oldValue = "";
        try {
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if (columnName.equals(column)) {
                    System.out.println("Column found: " + columnName);
                    clientOut.println("Column found: " + columnName);
                    columnFound = true;
                    break;
                }
            }
            if (!columnFound) {
                System.out.println("Column not found in the database.");
                clientOut.println("Column not found in the database.");
                clientOut.println("END");
                return false;
            }
            stmt = DatabaseConnect.prepareStatement("SELECT * FROM members WHERE MemberID = " + MemberID);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                System.out.println("Member found!");
                String columnName = columns.getString("COLUMN_NAME");
                PreparedStatement oldValueStmt = DatabaseConnect.prepareStatement("SELECT ? FROM members WHERE MemberID = ?");
                oldValueStmt.setString(1, columnName);
                oldValueStmt.setInt(2, MemberID);
                ResultSet oldValueRS = oldValueStmt.executeQuery();
                if (oldValueRS.next()) oldValue = oldValueRS.getString(columnName);
                if (column.equals("MemberId")) {
                    if (isMemberIDUsed(Integer.parseInt(value))) {
                        System.out.println("member id is already used.");
                        clientOut.println("member id is already used.");
                        clientOut.println("END");
                        return false;
                    }
                }
                PreparedStatement stmt2 = DatabaseConnect.prepareStatement("UPDATE members SET " + columnName + " = ? WHERE MemberID = ?");
                stmt2.setString(1, value);
                stmt2.setInt(2, MemberID);
                stmt2.executeUpdate();
                System.out.println("Updated " + columnName + " from " + oldValue + " to " + value);
                clientOut.println("Updated" + columnName + " from " + oldValue + " to " + value);
                clientOut.println("END");
                return true;
            }
            else {
                System.out.println("Member not found in the database.");
                clientOut.println("Member not found in the database.");
                clientOut.println("END");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating entry: " + e.getMessage());
            clientOut.println("Error updating entry: " + e.getMessage());
            clientOut.println("END");

        }
        return false;

    }


    public void deleteEntry(int MemberID) {
        PreparedStatement stmt;
        try {
            stmt = DatabaseConnect.prepareStatement("DELETE FROM Members WHERE MemberID = ?");
            stmt.setInt(1, MemberID);
            stmt.executeUpdate();
            System.out.println("Member deleted successfully");
        } catch (SQLException e) {
            System.out.println("Error deleting entry: " + e.getMessage());
        }
    }


    public void deleteEntry(int MemberID, PrintWriter Out) {
        PreparedStatement stmt;
        try {
            stmt = DatabaseConnect.prepareStatement("DELETE FROM Members WHERE MemberID = ?");
            stmt.setInt(1, MemberID);
            stmt.executeUpdate();
            System.out.println("Member deleted successfully");
            Out.println("Member deleted successfully");
            Out.println("END");
        } catch (SQLException e) {
            System.out.println("Error deleting entry: " + e.getMessage());
            Out.println("Error deleting entry: " + e.getMessage());
            Out.println("END");
        }
    }
}
