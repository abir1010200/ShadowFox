import java.util.*;
class Contact {
    String name;
    String phone;
    String email;

    Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}

public class ContactManager {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Contact> contacts = new ArrayList<>();

        while (true) {
            System.out.println("\n=== Contact Management ===");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Update Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");

            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {

                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Phone: ");
                    String phone = sc.nextLine();

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    contacts.add(new Contact(name, phone, email));
                    System.out.println("Contact Added!");
                    break;

                case 2:
                    if (contacts.isEmpty()) {
                        System.out.println("No contacts available.");
                    } else {
                        for (int i = 0; i < contacts.size(); i++) {
                            Contact c = contacts.get(i);
                            System.out.println((i + 1) + ". " + c.name +
                                    " | " + c.phone + " | " + c.email);
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter contact number to update: ");
                    int u = sc.nextInt() - 1;
                    sc.nextLine();

                    if (u >= 0 && u < contacts.size()) {
                        Contact c = contacts.get(u);

                        System.out.print("New Name: ");
                        c.name = sc.nextLine();

                        System.out.print("New Phone: ");
                        c.phone = sc.nextLine();

                        System.out.print("New Email: ");
                        c.email = sc.nextLine();

                        System.out.println("Contact Updated!");
                    } else {
                        System.out.println("Invalid selection.");
                    }
                    break;

                case 4:
                    System.out.print("Enter contact number to delete: ");
                    int d = sc.nextInt() - 1;

                    if (d >= 0 && d < contacts.size()) {
                        contacts.remove(d);
                        System.out.println("Contact Deleted!");
                    } else {
                        System.out.println("Invalid selection.");
                    }
                    break;

                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}