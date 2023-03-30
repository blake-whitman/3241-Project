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
        System.out.println("3. Search for records\n");
        System.out.print("Enter a number to select one of the three options or enter 'q' to exit: ");
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

    public static int removeWarehouse(Scanner s) {
        if (warehouses.isEmpty()) {
            System.out.println("\nThere are currently no warehouses to edit or remove!");
            return -1;
        }
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Here are the current warehouses in our system\n");
        int num = 1;

        for (warehouse w : warehouses) {
            System.out.println(num + ": " + w.city + " | " + w.address + " | " + w.phone + " | " + w.managerName + " | " + w.storageCapacity + " | " + w.droneCapacity);
            num++;
        }

        System.out.println("");
        System.out.print("Enter the number of the warehouse that you wish to edit or remove: ");
        if (s.hasNextInt()) {
            int index = s.nextInt();
            s.nextLine();
            System.out.print("Enter 'e' to edit or 'r' to remove this entry: ");
            String in = s.nextLine();
            if (in.equals("r")) {
                return index - 1;
            }
            warehouse edited = editWarehouse(warehouses.get(index - 1), s);
            warehouses.set(index - 1, edited);
        }
        return -1;
    }

    public static warehouse editWarehouse(warehouse w, Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the edited version of this warehouse (current information in parantheses)");

        warehouse edited = new warehouse();
        System.out.print("Warehouse city (currently '" + w.city + "'): ");
        edited.city = s.nextLine();
        System.out.print("Warehouse address (currently '" + w.address + "'): ");
        edited.address = s.nextLine();
        System.out.print("Warehouse phone (currently '" + w.phone + "'): ");
        edited.phone = s.nextLine();
        System.out.print("Warehouse manager name (currently '" + w.managerName + "'): ");
        edited.managerName = s.nextLine();
        System.out.print("Warehouse storage capacity (currently '" + w.storageCapacity + "'): ");
        edited.storageCapacity = s.nextInt();
        s.nextLine();
        System.out.print("Warehouse drone capacity (currently '" + w.droneCapacity + "'): ");
        edited.droneCapacity = s.nextInt();
        s.nextLine();
        if (w.address != edited.address) {
            warehouseMap.put(edited.address, edited);
        }
        return edited;
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
        m.id = s.nextLine();
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

    public static int removeMember(Scanner s) {
        if (members.isEmpty()) {
            System.out.println("\nThere are currently no members to edit or remove!");
            return -1;
        }
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Here are the current members in our system\n");
        int num = 1;

        for (Member m : members) {
            System.out.println(num + ": " + m.id + " | " + m.fName + " | " + m.lName + " | " + m.address + " | " + m.phone + " | " + m.email + " | " + m.startDate + " | " + m.warehouseDistance);
            num++;
        }

        System.out.println("");
        System.out.print("Enter the number of the member that you wish to edit or remove: ");
        if (s.hasNextInt()) {
            int index = s.nextInt();
            s.nextLine();
            System.out.print("Enter 'e' to edit or 'r' to remove this entry: ");
            String in = s.nextLine();
            if (in.equals("r")) {
                return index - 1;
            }
            Member edited = editMember(members.get(index - 1), s);
            members.set(index - 1, edited);
        }
        return -1;
    }


    public static Member editMember(Member m, Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the edited version of this member (current information in parantheses)");
        Member edited = new Member();

        System.out.print("Member id (currently '" + m.id + "''): ");
        edited.id = s.nextInt();
        s.nextLine();
        System.out.print("Member first name (currently '" + m.fName + "''): ");
        edited.fName = s.nextLine();
        System.out.print("Member last name (currently '" + m.lName + "''): ");
        edited.lName = s.nextLine();
        System.out.print("Member address (currently '" + m.address + "''): ");
        edited.address = s.nextLine();
        System.out.print("Member phone (currently '" + m.phone + "''): ");
        edited.phone = s.nextLine();
        System.out.print("Member email (currently '" + m.email + "''): ");
        edited.email = s.nextLine();
        System.out.print("Member start date (currently '" + m.startDate + "''): ");
        edited.startDate = s.nextLine();
        System.out.print("Member warehouse distance (currently '" + m.warehouseDistance + "''): ");
        edited.warehouseDistance = s.nextInt();
        s.nextLine();
        if (m.id != edited.id) {
            memberMap.put(edited.id, edited);
        }
        return edited;
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

    public static int removeEquipment(Scanner s) {
        if (equip.isEmpty()) {
            System.out.println("\nThere is currently no equipment to edit or remove!");
            return -1;
        }
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Here is the current equipment in our system\n");
        int num = 1;

        for (Equipment e : equip) {
            System.out.println(num + ": " + e.type + " | " + e.description + " | " + e.modelNumber + " | " + e.year + " | " + e.serialNumber + " | " + e.inventoryId + " | " + e.arrivalDate + " | " + e.warrantyExpiration + " | " + e.manufacturer + " | " + e.weight + " | " + e.size);
            num++;
        }

        System.out.println("");
        System.out.print("Enter the number of the equipment that you wish to edit or remove: ");
        if (s.hasNextInt()) {
            int index = s.nextInt();
            s.nextLine();
            System.out.print("Enter 'e' to edit or 'r' to remove this entry: ");
            String in = s.nextLine();
            if (in.equals("r")) {
                return index - 1;
            }
            Equipment edited = editEquipment(equip.get(index - 1), s);
            equip.set(index - 1, edited);
        }
        return -1;
    }

    public static Equipment editEquipment(Equipment e, Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the edited version of this equipment (current information in parantheses)");
        Equipment edited = new Equipment();

        System.out.print("Equipment type (currently '" + e.type + "''): ");
        edited.type = s.nextLine();
        System.out.print("Equipment description (currently '" + e.description + "''): ");
        edited.description = s.nextLine();
        System.out.print("Equipment model number (currently '" + e.modelNumber + "''): ");
        edited.modelNumber = s.nextInt();
        s.nextLine();
        System.out.print("Equipment year (currently '" + e.year + "''): ");
        edited.year = s.nextInt();
        s.nextLine();
        System.out.print("Equipment serial number (currently '" + e.serialNumber + "''): ");
        edited.serialNumber = s.nextInt();
        s.nextLine();
        System.out.print("Equipment inventory id (currently '" + e.inventoryId + "''): ");
        edited.inventoryId = s.nextInt();
        s.nextLine();
        System.out.print("Equipment arrival date (currently '" + e.arrivalDate + "''): ");
        edited.arrivalDate = s.nextLine();
        System.out.print("Equipment warranty expiration (currently '" + e.warrantyExpiration + "''): ");
        edited.warrantyExpiration = s.nextLine();
        System.out.print("Equipment manufacturer (currently '" + e.manufacturer + "''): ");
        edited.manufacturer = s.nextLine();
        System.out.print("Equipment weight (currently '" + e.weight + "''): ");
        edited.weight = s.nextInt();
        s.nextLine();
        System.out.print("Equipment size (currently '" + e.size + "''): ");
        edited.size = s.nextInt();
        s.nextLine();
        if (e.inventoryId != edited.inventoryId) {
            equipmentMap.put(edited.inventoryId, edited);
        }
        return edited;
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
        s.nextLine;
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

    public static void editRemoveRecord(Scanner s) {
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
                    int remIndex = removeWarehouse(s);
                    if (remIndex != -1) {
                        warehouse w = warehouses.get(remIndex);
                        warehouses.remove(remIndex);
                        warehouseMap.remove(w.address);
                    }
                } else if (choice == 2) {
                    int remIndex = removeMember(s);
                    if (remIndex != -1) {
                        Member m = members.get(remIndex);
                        members.remove(remIndex);
                        memberMap.remove(m.id);
                    }
                } else {
                    int remIndex = removeEquipment(s);
                    if (remIndex != -1) {
                        Equipment e = equip.get(remIndex);
                        equip.remove(remIndex);
                        equipmentMap.remove(e.inventoryId);
                    }
                }
            }
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

    public static List<warehouse> warehouses = new ArrayList<>();
    public static List<Member> members = new ArrayList<>();
    public static List<Equipment> equip = new ArrayList<>();
    public static Map<String, warehouse> warehouseMap = new HashMap<>();
    public static Map<Integer, Member> memberMap = new HashMap<>();
    public static Map<Integer, Equipment> equipmentMap = new HashMap<>();

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

    public static void main(String[] args) {
        Connection conn = initializeDB(database);
        Scanner s = new Scanner(System.in);
        System.out.println("\n\nWelcome to our inventory management system. Enter a number below to add to, edit, delete, or search through our records.\n");
        int selection = displayOptions(s);

        while (selection != -1) {
            if (selection == 1) {
                addRecord(conn, s);
            } else if (selection == 2) {
                editRemoveRecord(s);
            } else {
                searchRecord(s, conn);
            }
            selection = displayOptions(s);
        }
    }
 }
