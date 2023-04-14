import java.util.Scanner;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;




public class inventory {

    private static String database = "EquipmentRental.db";

    public static Connection initializeDB(String databaseFileName) {
        /**
         * The "Connection String" or "Connection URL".
         * 
         * "jdbc:sqlite:" is the "subprotocol".
         * (If this were a SQL Server database it would be "jdbc:sqlserver:".)
         */
           String url = "jdbc:sqlite:" + databaseFileName;
           Connection conn = null; // If you create this variable inside the Try block it will be out of scope
           try {
               conn = DriverManager.getConnection(url);
               if (conn != null) {
                // Provides some positive assurance the connection and/or creation was successful.
                   DatabaseMetaData meta = conn.getMetaData();
                   System.out.println("The driver name is " + meta.getDriverName());
                   System.out.println("The connection to the database was successful.");
               } else {
                // Provides some feedback in case the connection failed but did not throw an exception.
                System.out.println("Null Connection");
               }
           } catch (SQLException e) {
               System.out.println(e.getMessage());
               System.out.println("There was a problem connecting to the database.");
           }
           return conn;
    }

    public static class Warehouse {
        String city;
        String address;
        String phone;
        String managerName;
        int storageCapacity;
        int droneCapacity;
    
        public Warehouse() {
        }
    }

    public static class Member {
        int id;
        String fName;
        String lName;
        String address;
        String phone;
        String email;
        String startDate;
        int warehouseDistance;
    
        public Member() {
        }
    }

    public static class Equipment {
        String inventoryID;
        String type;
        String description;
        int modelNumber;
        int year;
        String serialNumber;
        String userID;
        String arrivalDate;
        String warrantyExpiration;
        String manufacturer;
        int weight;
        int size;
        String address;
        int numberUnits;
        String releaseDate;

        public Equipment() {
        }
    }
    
    public static class Drone {
        String name;
        int mnumber;
        int vCapacity;
        int distance;
        String expDate;
        int fleetID;
        String serial;
        String manufacturer;
        String year;
        int wCapacity;
        int speed;
        int status;
        String address;
        
        public Drone() {
        }
    }
    
    public static class Rating{
    
        String userID;
        String inventoryID;
        int rating;
        String comments;
        String date;
    
        public Rating() {
        }
   }
    
    public static class Rents{
    
        String userID;
        String inventoryID;
        String startDate;
        String endDate;
    
        public Rents() {
        }
    }
    
    public static class WarehouseOrder{
    
        String orderID;
        String elementType;
        int numberOrdered;
        int value;
        String date;
        int times;
        int frequency;
        String description;
        String address; 
    
        public WarehouseOrder() {
        }
    }

    public static int displayOptions(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("1. Add new records");
        System.out.println("2. Edit/delete existing records");
        System.out.println("3. Search for records");
        System.out.println("4. Useful Queries\n");
        System.out.print("Enter a number to select one of the four options or enter 'q' to exit: ");
        if (s.hasNextInt()) {
            int choice = s.nextInt();
            s.nextLine();
            return choice;
        }
        return -1;
    }

    public static void addWarehouse(Connection conn, Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter information for the warehouse below.\n");
        Warehouse w = new Warehouse();
        System.out.print("Warehouse city: ");
        w.city = s.nextLine();
        System.out.print("Warehouse address: ");
        w.address = s.nextLine();
        System.out.print("Warehouse phone: ");
        w.phone = s.nextLine();
        System.out.print("Warehouse manager name: ");
        w.managerName = s.nextLine();
        System.out.print("Warehouse storage capacity: ");
        w.storageCapacity = s.nextInt();
        s.nextLine();
        System.out.print("Warehouse drone capacity: ");
        w.droneCapacity = s.nextInt();
        s.nextLine();
        
        sqlQuery(conn, "INSERT INTO WAREHOUSE VALUES ('" + w.address + "', '" + w.city + "', '" + w.phone + 
        "', '" + w.managerName + "', " + w.storageCapacity + ", " + w.droneCapacity + ");");
    }

    public static String removeWarehouse(Scanner s, Connection conn) {
        if (!hasItems(conn, "SELECT * FROM WAREHOUSE")) {
            System.out.println("\nThere are currently no warehouses to edit or remove!");
            return "";
        }
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Here are the current warehouses in our system\n");
        sqlQuery(conn, "SELECT * FROM WAREHOUSE");

        System.out.println("");
        System.out.print("Enter the address of the warehouse that you wish to edit or remove: ");
        if (s.hasNextLine()) {
            String address = s.nextLine();
            System.out.print("Enter 'e' to edit or 'r' to remove this entry: ");
            String in = s.nextLine();
            if (in.equals("r")) {
                return address;
            }
            System.out.print("\n\n");
            if(editWarehouse(conn, s, address)) {
                System.out.println("Successfully edited warehouse at " + address);
            } else {
                System.out.println("Could not edit warehouse at " + address);
            }
        }
        return "";
    }

    public static boolean editWarehouse(Connection conn, Scanner s, String editAddress) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the edited version of this warehouse (current information in parantheses)");

        String address = "";
        String city = "";
        String phone = "";
        String manager = "";
        String storage = "";
        String drone = "";

        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM WAREHOUSE WHERE address = '" + editAddress + "'";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Press enter to keep the same:");
            System.out.print("Warehouse city (currently '" + rs.getString(2) + "'): ");
            city = s.nextLine();
            System.out.print("Warehouse address (currently '" + rs.getString(1) + "'): ");
            address = s.nextLine();
            System.out.print("Warehouse phone (currently '" + rs.getString(3) + "'): ");
            phone = s.nextLine();
            System.out.print("Warehouse manager name (currently '" + rs.getString(4)+ "'): ");
            manager = s.nextLine();
            System.out.print("Warehouse storage capacity (currently '" + rs.getString(5) + "'): ");
            storage = s.nextLine();
            System.out.print("Warehouse drone capacity (currently '" + rs.getString(6) + "'): ");
            drone = s.nextLine();
            String addressToUse = rs.getString(1);
            if (address != "") {
                editItem(conn, "WAREHOUSE", "address", "address", addressToUse, address, false);
                addressToUse = address;
            } 
            if (city != "") {
                editItem(conn, "WAREHOUSE", "city", "address", addressToUse, city, false);
            } 
            if (phone != "") {
                editItem(conn, "WAREHOUSE", "phone_number", "address", addressToUse, phone, false);
            } 
            if (manager != "") {
                editItem(conn, "WAREHOUSE", "manager_name", "address", addressToUse, manager, false);
            } 
            if (storage != "") {
                editItem(conn, "WAREHOUSE", "storage_capacity", "address", addressToUse, storage, true);
            } 
            if (drone != "") {
                editItem(conn, "WAREHOUSE", "drone_capacity", "address", addressToUse, drone, true);
            } 

            System.out.print("\nUPDATED WAREHOUSE\n");
            return true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not edit warehouse at " + editAddress);
            return false;
        }
    }

    public static void searchWarehouse(Scanner s, Connection conn) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the address of the warehouse you are seeking below.\n");
        System.out.print("Address: ");
        String address = s.nextLine();
        String sql = String.format("SELECT * FROM WAREHOUSE WHERE %s = '%s';", "address", address);
        sqlQuery(conn, sql);
    }

public static void addMember(Connection conn, Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter information for the member below.\n");
        Member m = new Member();
        System.out.print("Member id: ");
        m.id = s.nextInt();
        s.nextLine();
        System.out.print("Member first name: ");
        m.fName = s.nextLine();
        System.out.print("Member last name: ");
        m.lName = s.nextLine();
        System.out.print("Member address: ");
        m.address = s.nextLine();
        System.out.print("Member phone: ");
        m.phone = s.nextLine();
        System.out.print("Member email: ");
        m.email = s.nextLine();
        System.out.print("Member start date: ");
        m.startDate = s.nextLine();
        System.out.print("Member warehouse distance: ");
        m.warehouseDistance = s.nextInt();
        s.nextLine();
        System.out.print("Member warehouse address: ");
        m.address = s.nextLine();
        
        sqlQuery(conn,
                "INSERT INTO MEMBER VALUES ('" + m.id + "', '" + m.phone
                        + "', '" + m.email + "', '" + m.startDate + "', "
                        + m.warehouseDistance + ", '" + m.address + "');");
        sqlQuery(conn, "INSERT INTO MEMBER_NAME VALUES ('" + m.phone + "', '"
                + m.email + "', '" + m.fName + "', '" + m.lName + "');");
        
    }

    public static String removeMember(Scanner s, Connection conn) {
        if (!hasItems(conn, "SELECT * FROM MEMBER")) {
            System.out.println("\nThere are currently no members to edit or remove!");
            return "";
        }
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Here are the current members in our system\n");

        sqlQuery(conn, "SELECT * FROM MEMBER");

        System.out.println("");
        System.out.print("Enter the user_id of the member that you wish to edit or remove: ");
        if (s.hasNextLine()) {
            String id = s.nextLine();
            System.out.print("Enter 'e' to edit or 'r' to remove this entry: ");
            String in = s.nextLine();
            if (in.equals("r")) {
                return id;
            }
            if (editMember(s, conn, id)) {
                System.out.println("Successfully edited member at " + id);
            } else {
                System.out.println("Could not edit member that does not exist or a mistake was made.");
            }

        }
        return "";
    }


    public static boolean editMember(Scanner s, Connection conn, String id) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the edited version of this member (current information in parantheses)");
        System.out.println("Press enter to keep old information");

        String userId = "";
        String addr = "";
        String phone = "";
        String email = "";
        String start = "";
        String dist = "";

        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM MEMBER WHERE user_id = '" + id + "'";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.print("Member id (currently '" + rs.getString(1) + "''): ");
            userId = s.nextLine();
            System.out.print("Member phone (currently '" + rs.getString(2) + "''): ");
            phone = s.nextLine();
            System.out.print("Member email (currently '" + rs.getString(3) + "''): ");
            email = s.nextLine();
            System.out.print("Member start date (YYYY-MM-DD) (currently '" + rs.getString(4) + "''): ");
            start = s.nextLine();
            System.out.print("Member warehouse distance (currently '" + rs.getString(5) + "''): ");
            dist = s.nextLine();
            System.out.print("Member warehouse address (currently '" + rs.getString(6) + "''): ");
            addr = s.nextLine();
            String idToUse = rs.getString(1);
            if (userId != "") {
                editItem(conn, "MEMBER", "user_id", "user_id", idToUse, userId, false);
                idToUse = userId;
            } 
            if (phone != "") {
                editItem(conn, "MEMBER", "phone", "user_id", idToUse, phone, false);
            } 
            if (email != "") {
                editItem(conn, "MEMBER", "email", "user_id", idToUse, email, false);
            } 
            if (dist != "") {
                editItem(conn, "MEMBER", "warehouse_distance", "user_id", idToUse, dist, true);
            } 
            if (start != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(start);
                    String newsql = "UPDATE MEMBER SET start_date = ? WHERE user_id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(newsql);
                    pstmt.setDate(1, new java.sql.Date(date.getTime())); 
                    pstmt.setString(2, idToUse);
                    pstmt.executeUpdate();
                } catch(ParseException p) {
                    System.out.print("Date form not accepted: " + start);
                }
            }
            if (addr != "") {
                editItem(conn, "MEMBER", "warehouse_address", "user_id", idToUse, addr, true);
            } 

            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not edit member " + id);
            return false;
        }
    }

    public static void searchMember(Scanner s, Connection conn) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the user id of the member you are seeking below.\n");
        System.out.print("User ID: ");
        String id = s.nextLine();
        String sql = String.format("SELECT * FROM MEMBER WHERE %s = '%s';", "user_id", id);
        sqlQuery(conn, sql);
    }


   public static void addEquipment(Connection conn, Scanner s) {
        System.out.println(
                "\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter information for the equipment below.\n");
        Equipment e = new Equipment();
        System.out.print("Equipment inventory id: ");
        e.inventoryID = s.nextLine();
        System.out.print("Equipment description: ");
        e.description = s.nextLine();
        System.out.print("Equipment year: ");
        e.year = s.nextInt();
        s.nextLine();
        System.out.print("Equipment type: ");
        e.type = s.nextLine();
        System.out.print("Equipment arrival date: ");
        e.arrivalDate = s.nextLine();
        System.out.print("Equipment warranty expiration: ");
        e.warrantyExpiration = s.nextLine();
        System.out.print("Equipment manufacturer: ");
        e.manufacturer = s.nextLine();
        System.out.print("Equipment model number: ");
        e.modelNumber = s.nextInt();
        s.nextLine();
        System.out.print("Equipment serial number: ");
        e.serialNumber = s.nextLine();
        System.out.print("User ID: ");
        e.userID = s.nextLine();
        System.out.print("Warehouse Address: ");
        e.address = s.nextLine();
        System.out.print("Number of Units: ");
        e.numberUnits = s.nextInt();
        s.nextLine();
        System.out.print("Release Date: ");
        e.releaseDate = s.nextLine();
        System.out.print("Weight: ");
        e.weight = s.nextInt();
        s.nextLine();
        System.out.print("Size: ");
        e.size = s.nextInt();
        s.nextLine();
        sqlQuery(conn, "INSERT INTO EQUIPMENT VALUES ('" + e.inventoryID
                + "', '" + e.description + "', '" + e.year + "', '" + e.type
                + "', '" + e.arrivalDate + "', '" + e.warrantyExpiration
                + "', '" + e.manufacturer + "', " + e.modelNumber + ", '"
                + e.serialNumber + "', '" + e.userID + "', '" + e.address
                + "', " + e.numberUnits + ", '" + e.releaseDate + "');");
        sqlQuery(conn, "INSERT INTO MODEL_INFO VALUES (" + e.modelNumber + ", "
                + e.weight + ", " + e.size + ");");
    }

    public static String removeEquipment(Scanner s, Connection conn) {
        if (!hasItems(conn, "SELECT * FROM EQUIPMENT")) {
            System.out.println("\nThere is currently no equipment to edit or remove!");
            return "";
        }
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Here is the current equipment in our system\n");
        sqlQuery(conn, "SELECT * FROM EQUIPMENT");
        System.out.println("");
        System.out.print("Enter the inventory_id of the equipment that you wish to edit or remove: ");
        if (s.hasNextLine()) {
            String id = s.nextLine();
            System.out.print("Enter 'e' to edit or 'r' to remove this entry: ");
            String in = s.nextLine();
            if (in.equals("r")) {
                return id;
            }
            if (editEquipment(s, conn, id)) {
                System.out.println("Successfully edited equipment with id " + id);
            } else {
                System.out.println("Could not edit equipment with id " + id);
            }

        }
        return "";
    }

    public static boolean editEquipment(Scanner s, Connection conn, String id) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the edited version of this equipment (current information in parantheses)");
        
        String inventoryId = "";
        String desc = "";
        String year = "";
        String type = "";
        String arrDate = "";
        String expDate = "";
        String manuf = "";
        String model = "";
        String droneSn = "";
        String userId = "";
        String warehouseAddr = "";
        String numUnits = "";
        String release = "";

        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM EQUIPMENT WHERE inventory_id = '" + id + "'";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.print("Equipment inventoryId (currently '" + rs.getString(1) + "''): ");
            inventoryId = s.nextLine();
            System.out.print("Equipment description (currently '" + rs.getString(2) + "''): ");
            desc = s.nextLine();
            System.out.print("Equipment year (currently '" + rs.getString(3) + "''): ");
            year = s.nextLine();
            System.out.print("Equipment type (currently '" + rs.getString(4) + "''): ");
            type = s.nextLine();
            System.out.print("Equipment arrival date (currently '" + rs.getString(5) + "''): ");
            arrDate = s.nextLine();
            System.out.print("Equipment expiration date (currently '" + rs.getString(6) + "''): ");
            expDate = s.nextLine();
            System.out.print("Equipment manufacturer (currently '" + rs.getString(7) + "''): ");
            manuf = s.nextLine();
            System.out.print("Equipment model number (currently '" + rs.getString(8)+ "''): ");
            model = s.nextLine();
            System.out.print("Equipment drone serial no (currently '" + rs.getString(9) + "''): ");
            droneSn = s.nextLine();
            System.out.print("Equipment user id (currently '" + rs.getString(10) + "''): ");
            userId = s.nextLine();
            System.out.print("Equipment warehouse address (currently '" + rs.getString(11) + "''): ");
            warehouseAddr = s.nextLine();
            System.out.print("Equipment number of units (currently '" + rs.getString(12) + "''): ");
            numUnits = s.nextLine();
            System.out.print("Equipment release date (currently '" + rs.getString(13) + "''): ");
            release = s.nextLine();

            String idToUse = rs.getString(1);
            if (inventoryId != "") {
                editItem(conn, "EQUIPMENT", "inventory_ID", "inventory_ID", idToUse, inventoryId, false);
                idToUse = userId;
            } 
            if (desc != "") {
                editItem(conn, "EQUIPMENT", "description", "inventory_ID", idToUse, desc, false);
            } 
            if (year != "") {
                editItem(conn, "EQUIPMENT", "year", "inventory_ID", idToUse, year, false);
            } 
            if (type != "") {
                editItem(conn, "EQUIPMENT", "type", "inventory_ID", idToUse, type, false);
            } 
            if (arrDate != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(arrDate);
                    String newsql = "UPDATE EQUIPMENT SET arrival_date = ? WHERE inventory_ID = ?";
                    PreparedStatement pstmt = conn.prepareStatement(newsql);
                    pstmt.setDate(1, new java.sql.Date(date.getTime())); 
                    pstmt.setString(2, idToUse);
                    pstmt.executeUpdate();
                } catch(ParseException p) {
                    System.out.print("Date form not accepted: " + arrDate);
                }
            }
            if (expDate != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(expDate);
                    String newsql = "UPDATE EQUIPMENT SET warranty_expiration = ? WHERE inventory_ID = ?";
                    PreparedStatement pstmt = conn.prepareStatement(newsql);
                    pstmt.setDate(1, new java.sql.Date(date.getTime())); 
                    pstmt.setString(2, idToUse);
                    pstmt.executeUpdate();
                } catch(ParseException p) {
                    System.out.print("Date form not accepted: " + expDate);
                }
            } 
            if (manuf != "") {
                editItem(conn, "EQUIPMENT", "manufacturer", "inventory_ID", idToUse, manuf, false);
            } 
            if (model != "") {
                editItem(conn, "EQUIPMENT", "model_number", "inventory_ID", idToUse, model, true);
            } 
            if (droneSn != "") {
                editItem(conn, "EQUIPMENT", "drone_serial_number", "inventory_ID", idToUse, droneSn, false);
            } 
            if (userId != "") {
                editItem(conn, "EQUIPMENT", "user_id", "inventory_ID", idToUse, userId, false);
            } 
            if (warehouseAddr != "") {
                editItem(conn, "EQUIPMENT", "warehouse_address", "inventory_ID", idToUse, warehouseAddr, false);
            } 
            if (numUnits != "") {
                editItem(conn, "EQUIPMENT", "num_units", "inventory_ID", idToUse, numUnits, true);
            } 
            if (release != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(release);
                    String newsql = "UPDATE EQUIPMENT SET release_date = ? WHERE inventory_ID = ?";
                    PreparedStatement pstmt = conn.prepareStatement(newsql);
                    pstmt.setDate(1, new java.sql.Date(date.getTime())); 
                    pstmt.setString(2, idToUse);
                    pstmt.executeUpdate();
                } catch(ParseException p) {
                    System.out.print("Date form not accepted: " + expDate);
                }
            } 



            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not edit member " + id);
            return false;
        }
    }

    public static void searchEquipment(Scanner s, Connection conn) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the inventory id of the equipment you are seeking below.\n");
        System.out.print("Inventory ID: ");
        String id = s.nextLine();
        String sql = String.format("SELECT * FROM EQUIPMENT WHERE %s = '%s';", "inventory_ID", id);
        sqlQuery(conn, sql);
    }
    
    public static void addDrone(Connection conn, Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter information for the drone below.\n");
        Drone d = new Drone();
        System.out.print("Drone name: ");
        d.name = s.nextLine();
        System.out.print("Drone Model number: ");
        d.mnumber = s.nextInt();
        s.nextLine();
        System.out.print("Drone volume capacity: ");
        d.vCapacity = s.nextInt();
        s.nextLine();
        System.out.print("Drone distance autonomy: ");
        d.distance = s.nextInt();
        s.nextLine();
        System.out.print("Drone Warranty Expiration Date: ");
        d.expDate = s.nextLine();
        System.out.print("Drone Fleet ID: ");
        d.fleetID = s.nextInt();
        s.nextLine();
        System.out.print("Drone Serial Number: ");
        d.serial = s.nextLine();
        System.out.print("Drone manufacturer: ");
        d.manufacturer = s.nextLine();
        System.out.print("Drone Year: ");
        d.year = s.nextLine();
        System.out.print("Drone weight capacity: ");
        d.wCapacity = s.nextInt();
        s.nextLine();
        System.out.print("Drone max speed: ");
        d.speed = s.nextInt();
        s.nextLine();
        System.out.print("Drone status: ");
        d.status = s.nextInt();
        s.nextLine();
        System.out.print("Drone warehouse address: ");
        d.address = s.nextLine();
        
        sqlQuery(conn, "INSERT INTO DRONE VALUES ('" + d.name + "', "
                + d.mnumber + ", " + d.vCapacity + ", " + d.distance + ", '"
                + d.expDate + "', " + d.fleetID + ", '" + d.serial + "', '"
                + d.manufacturer + "', '" + d.year + "', " + d.wCapacity + ", "
                + d.speed + ", " + d.status + ", '" + d.address + "')");
    }
    
    public static void addRating(Connection conn, Scanner s){
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter Rating information below.\n");
        Rating r = new Rating();
        System.out.print("Member id: ");
        r.userID = s.nextLine();
        System.out.print("Inventory id: ");
        r.inventoryID = s.nextLine();
        System.out.print("Rating: ");
        r.rating = s.nextInt();
        s.nextLine();
        System.out.print("Enter Comments: ");
        r.comments = s.nextLine();
        System.out.print("Rating Dating: ");
        r.date = s.nextLine();
        
        sqlQuery(conn, "INSERT INTO RATINGS VALUES ('" + r.userID + "', '" + r.inventoryID + "', " +
        r.rating + ", '" + r.comments + "', '" + r.date +"');");
        
    }
    
    public static void addRents(Connection conn, Scanner s){
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter Renting information below.\n");
        Rents p = new Rents();
        System.out.print("Member id: ");
        p.userID = s.nextLine();
        System.out.print("Inventory id: ");
        p.inventoryID = s.nextLine();
        System.out.print("Start Date: ");
        p.startDate = s.nextLine();
        System.out.print("End Date: ");
        p.endDate = s.nextLine();
        
        sqlQuery(conn,
                "INSERT INTO RENTS VALUES ('" + p.userID + "', '"
                        + p.inventoryID + "', '" + p.startDate + "', '"
                        + p.endDate + "');");   
    }
    
    public static void addWarehouseOrder(Connection conn, Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter information for the warehouse below.\n");
        WarehouseOrder o = new WarehouseOrder();
        System.out.print("OrderID: ");
        o.orderID = s.nextLine();
        System.out.print("Warehouse Ordered to: ");
        o.address = s.nextLine();
        System.out.print("Element Type: ");
        o.elementType = s.nextLine();
        System.out.print("Number ordered: ");
        o.numberOrdered = s.nextInt();
        s.nextLine();
        System.out.print("Value: ");
        o.value = s.nextInt();
        s.nextLine();
        System.out.print("Date Arrival: ");
        o.date = s.nextLine();
        System.out.print("Times Ordered: ");
        o.times = s.nextInt();
        s.nextLine();
        System.out.print("Frequency Ordered: ");
        o.frequency = s.nextInt();
        s.nextLine();
        System.out.print("Description: ");
        o.description = s.nextLine();
        
        sqlQuery(conn, "INSERT INTO WAREHOUSE_ORDER VALUES ('" + o.orderID + "', '" + o.elementType + "', " + 
        o.numberOrdered + ", " + o.value + ", '" + o.date + "', " + o.times + ", " + o.frequency + 
        ", '" + o.description + "');" );
        sqlQuery(conn, "INSERT INTO SHIPPED VALUES ('" + o.orderID + "', '" + o.address + "');");   
        
    }


    public static void addRecord(Connection conn, Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Which type of record are you seeking to add to the system?\n");
        System.out.println("1. Warehouse");
        System.out.println("2. Member");
        System.out.println("3. Equipment");
        System.out.println("4. Drone");
        System.out.println("5. Rating");
        System.out.println("6. Rents"); 
        System.out.println("7. Warehouse Order");
        System.out.println("8. Back to home screen\n");
        System.out.print("Enter choice here: ");
        if (s.hasNextInt()) {
            int choice = s.nextInt();
            s.nextLine();
            if (choice != 8) {
                if (choice == 1) {
                    addWarehouse(conn, s);
                } else if (choice == 2) {
                    addMember(conn, s);
                } else if (choice == 3) {
                    addEquipment(conn, s);
                } else if (choice == 4) {
                    addDrone(conn, s);
                } else if (choice == 5) {
                    addRating(conn, s);
                } else if (choice == 6) {
                    addRents(conn, s);
                } else if (choice == 7) {
                    addWarehouseOrder(conn, s);
                }
            }
        }
    }

    public static void editRemoveRecord(Scanner s, Connection conn) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Which type of record are you seeking to edit/remove from the system?\n");
        System.out.println("1. Warehouse");
        System.out.println("2. Member");
        System.out.println("3. Equipment");
        System.out.println("4. Back to home screen\n");
        System.out.print("Enter choice here: ");
        if (s.hasNextInt()) {
            int choice = s.nextInt();
            s.nextLine();
            if (choice != 4) {
                if (choice == 1) {
                    String removeAddress = removeWarehouse(s, conn);
                    if (removeAddress != "") {
                        int rows = removeItem(conn, "WAREHOUSE", "address", removeAddress);
                        if (rows > 0 ){
                            System.out.println("Successfully removed warehouse at " + removeAddress);
                        } else {
                            System.out.println("Could not warehouse at " + removeAddress);
                        }
                    }
                } else if (choice == 2) {
                    String remId = removeMember(s, conn);
                    if (remId != "") {
                        int rows = removeItem(conn, "MEMBER", "user_id", remId);
                        if (rows > 0 ){
                            System.out.println("Successfully removed member " + remId);
                        } else {
                            System.out.println("Could not remove member " + remId);
                        }
                    }
                } else {
                    String remEqId = removeEquipment(s, conn);
                    if (remEqId != "") {
                        int rows = removeItem(conn, "EQUIPMENT", "inventory_id", remEqId);
                        if (rows > 0 ){
                            System.out.println("Successfully removed equipment " + remEqId);
                        } else {
                            System.out.println("Could not remove equipment " + remEqId);
                        }
                    }
                }
            }
        }
    }

    public static int removeItem(Connection conn, String table, String attribute, String primaryKeyValue) {
        try {
            String sql = "DELETE FROM " + table + " WHERE " + attribute + " = '" + primaryKeyValue + "'";
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not perform delete on " + table);
            return 0;
        }
    }

    public static int editItem(Connection conn, String table, String attributeToChange, String primaryKeyAttribute, String primaryKeyValue, String newValueOfAttribute, boolean isInt) {
        int newValInt = -1;
        try {
            if(isInt) {
                newValInt = Integer.parseInt(newValueOfAttribute);
            }
            String sql = "UPDATE " + table + " SET " + attributeToChange + " = ? WHERE " + primaryKeyAttribute + " = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            if(isInt) {
                stmt.setInt(1, newValInt);
            } else {
                stmt.setString(1, newValueOfAttribute);
            }

            stmt.setString(2, primaryKeyValue);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not perform update on " + table);
            return 0;
        }
    }

    public static boolean hasItems(Connection conn, String sql) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next(); // returns true if there is at least one row in the ResultSet
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not perform query " + sql);
            return false;
        }
    }

    public static void searchRecord(Scanner s, Connection conn) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Which type of record are you searching for in the system?\n");
        System.out.println("1. Warehouse");
        System.out.println("2. Member");
        System.out.println("3. Equipment");
        System.out.println("4. Back to home screen\n");
        System.out.print("Enter choice here: ");
        if (s.hasNextInt()) {
            int choice = s.nextInt();
            s.nextLine();
            if (choice != 4) {
                if (choice == 1) {
                    searchWarehouse(s, conn);
                } else if (choice == 2) {
                    searchMember(s, conn);
                } else {
                    searchEquipment(s, conn);
                }
            }
        }
    }

    public static void sqlQuery(Connection conn, String sql){
        try {
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         ResultSetMetaData rsmd = rs.getMetaData();
         int columnCount = rsmd.getColumnCount();
         for (int i = 1; i <= columnCount; i++) {
          String value = rsmd.getColumnName(i);
          System.out.print(value);
          if (i < columnCount) System.out.print(",  ");
         }
        System.out.print("\n");
        while (rs.next()) {
          for (int i = 1; i <= columnCount; i++) {
           String columnValue = rs.getString(i);
              System.out.print(columnValue);
              if (i < columnCount) System.out.print(",  ");
          }
       System.out.print("\n");
         }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void preparedSqlQuery(Connection conn, PreparedStatement stmt)
    {
        try {
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
             String value = rsmd.getColumnName(i);
             System.out.print(value);
             if (i < columnCount) System.out.print(",  ");
            }
           System.out.print("\n");
           while (rs.next()) {
             for (int i = 1; i <= columnCount; i++) {
              String columnValue = rs.getString(i);
                 System.out.print(columnValue);
                 if (i < columnCount) System.out.print(",  ");
             }
          System.out.print("\n");
            }
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    }

    //Useful Reports
        public static void getTotalEquipmentRentedByMember(Connection conn)
        {
        	String prep = "SELECT user_id, count(*)\n"
        			+ "FROM RENTS\n"
        			+ "GROUP BY user_id\n"
        			+ "HAVING user_id=?;";
        	PreparedStatement stmt = null;
        	try {
        		stmt = conn.prepareStatement(prep);
            	System.out.print("Enter user_id of member's equipment count to get: ");
            	//user_3901A2B8
            	Scanner userName = new Scanner(System.in);
            	String user = userName.nextLine();
            	userName.close();
            	stmt.setString(1, user);
            	preparedSqlQuery(conn, stmt);
        	}
        	catch(SQLException e)
        	{
        		System.out.println(e.getMessage());
        	}
        }
        
        public static void getPopularItem(Connection conn)
        {
        	String prep = "SELECT description, e.inventory_ID, count(*) as rented_count, (julianday(return_date)-julianday(start_date)) as rented_duration\n"
        			+ "FROM EQUIPMENT as e, RENTS as r\n"
        			+ "WHERE r.inventory_id=e.inventory_ID\n"
        			+ "GROUP BY e.inventory_id\n"
        			+ "ORDER BY rented_count, rented_duration DESC\n"
        			+ "LIMIT 1;";
        	sqlQuery(conn, prep);
        }
        
        public static void getPopularDrone(Connection conn)
        {
        	String prep = "SELECT d.name, d.serial_number, COUNT(*) AS items_delivered\n"
        			+ "FROM DRONE d\n"
        			+ "JOIN EQUIPMENT e ON d.serial_number = e.drone_serial_number\n"
        			+ "GROUP BY d.name, d.serial_number\n"
        			+ "ORDER BY items_delivered DESC\n"
        			+ "LIMIT 1;";
        	sqlQuery(conn, prep);
        }
        
        public static void getMemberWithMostItems(Connection conn)
        {
        	String prep = "SELECT user_id, count(*) as items_rented\n"
        			+ "FROM RENTS\n"
        			+ "GROUP BY user_id\n"
        			+ "ORDER BY items_rented desc\n"
        			+ "LIMIT 1;";
        	sqlQuery(conn, prep);
        }
        
        public static void getEquipmentByTypeAndReleaseYear(Scanner s, Connection conn)
        {
        	String prep = "SELECT description\n"
        			+ "FROM EQUIPMENT\n"
        			+ "WHERE year < ?";
            System.out.print("Select a year that equipment should have been released before: ");
            int year = s.nextInt();
            PreparedStatement stmt = null;
            try{
                stmt = conn.prepareStatement(prep);
                stmt.setInt(1, year);
                preparedSqlQuery(conn, stmt);
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        	sqlQuery(conn, prep);
        }
        
        public static void getPopularManufacturer(Connection conn)
        {
        		String prep = "SELECT e.manufacturer, COUNT(*) AS total_rented_items\n"
        				+ "FROM EQUIPMENT e\n"
        				+ "JOIN RENTS r ON e.inventory_ID = r.inventory_ID\n"
        				+ "GROUP BY e.manufacturer\n"
        				+ "ORDER BY total_rented_items DESC\n"
        				+ "LIMIT 1;";
            	sqlQuery(conn, prep);
        }

        public static void usefulQueries(Scanner s, Connection conn)
        {
            System.out.println("\nSelect the query you wish to perform"
            +"\n1. Find number of equipment items rented by a user-defined patron"
            +"\n2. Find the most popular item based on renting time and number of times rented"
            +"\n3. Find the manufacturer that is most frequently rented"
            +"\n4. Find the most used drone based on number of deliveries"
            +"\n5. Find the member who has rented the most items and how many items they have rented"
            +"\n6. Find the description of equipment by type released before a user-defined year"
            +"\n7. Return to main menu\n");
            int input = -1;
            while(input == -1)
            {
                System.out.print("Please select one of the above options: ");
                if(s.hasNextInt()) input = s.nextInt();
                s.nextLine();
            }
            switch(input)
            {
                case 1:
                    getTotalEquipmentRentedByMember(conn);
                    break;
                case 2:
                    getPopularItem(conn);
                    break;
                case 3:
                    getPopularManufacturer(conn);
                    break;
                case 4:
                    getPopularDrone(conn);
                    break;
                case 5:
                    getMemberWithMostItems(conn);
                    break;
                case 6:
                    getEquipmentByTypeAndReleaseYear(s, conn);
                    break;
                default:
                	break;
            }
        }
        //End Useful Reports

    public static void main(String[] args) {
        Connection conn = initializeDB(database);
        Scanner s = new Scanner(System.in);
        System.out.println("\n\nWelcome to our inventory management system. Enter a number below to add to, edit, delete, or search through our records.\n");
        int selection = displayOptions(s);

        while (selection != -1) {
            if (selection == 1) {
                addRecord(conn, s);
            } else if (selection == 2) {
                editRemoveRecord(s, conn);
            } else if (selection == 3) {
                searchRecord(s, conn);
            }
            else
            {
                usefulQueries(s, conn);
            }
            selection = displayOptions(s);
        }
    }
 }
