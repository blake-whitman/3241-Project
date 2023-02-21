import java.util.Scanner;
import java.util.*;

class Equipment {
    String type;
    String description;
    int modelNumber;
    int year;
    int serialNumber;
    int inventoryId;
    String arrivalDate;
    String warrantyExpiration;
    String manufacturer;
    int weight;
    int size;
    
    public Equipment() {

    }
}

class Warehouse {
    String city;
    String address;
    String phone;
    String managerName;
    int storageCapacity;
    int droneCapacity;

    public Warehouse() {

    }
}

class Member {
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

public class inventory {

    public static int displayOptions(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("1. Add new records (warehouse, member, or equipment)");
        System.out.println("2. Edit/delete existing records (warehouse, member, or equipment)");
        System.out.println("3. Search for records (warehouse, member, or equipment)");
        System.out.print("Enter a number to select one of the three options or enter 'q' to exit: ");
        if (s.hasNextInt()) {
            int choice = s.nextInt();
            s.nextLine();
            return choice;
        }
        return -1;
    }

    public static Warehouse addWarehouse(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
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
        return w;
    }

    public static int removeWarehouse(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Here are the current warehouses in our system: ");
        int num = 1;

        for (Warehouse w : warehouses) {
            System.out.println(num + ": " + w.city + " | " + w.address + " | " + w.phone + " | " + w.managerName + " | " + w.storageCapacity + " | " + w.droneCapacity);
            num++;
        }

        System.out.println("");
        System.out.print("Enter the number of the warehouse that you wish to edit or remove, or enter 'q' to return home: ");
        if (s.hasNextInt()) {
            int index = s.nextInt();
            s.nextLine();
            System.out.print("Enter 'e' to edit or 'r' to remove this entry: ");
            String in = s.nextLine();
            if (in.equals("r")) {
                return index - 1;
            }
            Warehouse edited = editWarehouse(warehouses.get(index - 1), s);
            warehouses.set(index - 1, edited);
            return -1;
        }
        return Integer.MIN_VALUE;
    }

    public static Warehouse editWarehouse(Warehouse w, Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Enter the edited version of this warehouse (current information in parantheses)");

        Warehouse edited = new Warehouse();
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
        return edited;
    }

    public static Member addMember(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
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
        return m;
    }

    public static int removeMember(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Here are the current members in our system: ");
        int num = 1;

        for (Member m : members) {
            System.out.println(num + ": " + m.id + " | " + m.fName + " | " + m.lName + " | " + m.address + " | " + m.phone + " | " + m.email + " | " + m.startDate + " | " + m.warehouseDistance);
            num++;
        }

        System.out.println("");
        System.out.print("Enter the number of the member that you wish to edit or remove, or enter 'q' to return home: ");
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
            return -1;
        }
        return Integer.MIN_VALUE;
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
        return edited;
    }


    public static Equipment addEquipment(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        Equipment e = new Equipment();
        System.out.print("Equipment type: ");
        e.type = s.nextLine();
        System.out.print("Equipment description: ");
        e.description = s.nextLine();
        System.out.print("Equipment model number: ");
        e.modelNumber = s.nextInt();
        s.nextLine();
        System.out.print("Equipment year: ");
        e.year = s.nextInt();
        s.nextLine();
        System.out.print("Equipment serial number: ");
        e.serialNumber = s.nextInt();
        s.nextLine();
        System.out.print("Equipment inventory id: ");
        e.inventoryId = s.nextInt();
        s.nextLine();
        System.out.print("Equipment arrival date: ");
        e.arrivalDate = s.nextLine();
        System.out.print("Equipment warranty expiration: ");
        e.warrantyExpiration = s.nextLine();
        System.out.print("Equipment manufacturer: ");
        e.manufacturer = s.nextLine();
        System.out.print("Equipment weight: ");
        e.weight = s.nextInt();
        s.nextLine();
        System.out.print("Equipment size: ");
        e.size = s.nextInt();
        s.nextLine();
        return e;
    }

    public static int removeEquipment(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Here is the current equipment in our system: ");
        int num = 1;

        for (Equipment e : equip) {
            System.out.println(num + ": " + e.type + " | " + e.description + " | " + e.modelNumber + " | " + e.year + " | " + e.serialNumber + " | " + e.inventoryId + " | " + e.arrivalDate + " | " + e.warrantyExpiration + " | " + e.manufacturer + " | " + e.weight + " | " + e.size);
            num++;
        }

        System.out.println("");
        System.out.print("Enter the number of the equipment that you wish to edit or remove, or enter 'q' to return home: ");
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
            return -1;
        }
        return Integer.MIN_VALUE;
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
        return edited;
    }


    public static void addRecord(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Which type of record are you seeking to add to the system?: ");
        System.out.println("1. Warehouse");
        System.out.println("2. Member");
        System.out.println("3. Equipment");
        System.out.println("4. Back to home screen");
        System.out.print("Enter choice here: ");
        if (s.hasNextInt()) {
            int choice = s.nextInt();
            s.nextLine();
            if (choice != 4) {
                if (choice == 1) {
                    Warehouse w = addWarehouse(s);
                    if (w != null) {
                        warehouses.add(w);
                    }
                } else if (choice == 2) {
                    Member m = addMember(s);
                    if (m != null) {
                        members.add(m);
                    }
                } else {
                    Equipment e = addEquipment(s);
                    if (e != null) {
                        equip.add(e);
                    }
                }
            }
        }
    }

    public static void editRemoveRecord(Scanner s) {
        System.out.println("\n---------------------------------------------------------------------------------------\n");
        System.out.println("Which type of record are you seeking to edit/remove from the system?: ");
        System.out.println("1. Warehouse");
        System.out.println("2. Member");
        System.out.println("3. Equipment");
        System.out.println("4. Back to home screen");
        System.out.print("Enter choice here: ");
        if (s.hasNextInt()) {
            int choice = s.nextInt();
            s.nextLine();
            if (choice != 4) {
                if (choice == 1) {
                    int remIndex = removeWarehouse(s);
                    if (remIndex != -1) {
                        warehouses.remove(remIndex);
                    }
                } else if (choice == 2) {
                    int remIndex = removeMember(s);
                    if (remIndex != -1) {
                        members.remove(remIndex);
                    }
                } else {
                    int remIndex = removeEquipment(s);
                    if (remIndex != -1) {
                        equip.remove(remIndex);
                    }
                }
            }
        }
    }

    public static List<Warehouse> warehouses = new ArrayList<>();
    public static List<Member> members = new ArrayList<>();
    public static List<Equipment> equip = new ArrayList<>();
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("\n\nWelcome to our inventory management system. Enter a number below to add to, edit, delete, or search through our records.\n");
        int selection = displayOptions(s);

        while (selection != -1) {
            if (selection == 1) {
                addRecord(s);
            } else if (selection == 2) {
                editRemoveRecord(s);
            } else {
                // searchRecord();
            }
            selection = displayOptions(s);
        }

        System.out.println(warehouses);
    }
 }