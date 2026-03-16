import java.util.Scanner;

public class enhancedcalculator {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Enhanced Calculator ===");
            System.out.println("1. Addition");
            System.out.println("2. Subtraction");
            System.out.println("3. Multiplication");
            System.out.println("4. Division");
            System.out.println("5. Square Root");
            System.out.println("6. Power (x^y)");
            System.out.println("7. Temperature Conversion (C ↔ F)");
            System.out.println("8. Currency Conversion (INR → USD)");
            System.out.println("9. Exit");

            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            if (choice == 9) break;

            double a, b, result;

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter two numbers: ");
                        a = sc.nextDouble();
                        b = sc.nextDouble();
                        System.out.println("Result = " + (a + b));
                        break;

                    case 2:
                        System.out.print("Enter two numbers: ");
                        a = sc.nextDouble();
                        b = sc.nextDouble();
                        System.out.println("Result = " + (a - b));
                        break;

                    case 3:
                        System.out.print("Enter two numbers: ");
                        a = sc.nextDouble();
                        b = sc.nextDouble();
                        System.out.println("Result = " + (a * b));
                        break;

                    case 4:
                        System.out.print("Enter two numbers: ");
                        a = sc.nextDouble();
                        b = sc.nextDouble();
                        if (b == 0)
                            System.out.println("Error: Cannot divide by zero");
                        else
                            System.out.println("Result = " + (a / b));
                        break;

                    case 5:
                        System.out.print("Enter number: ");
                        a = sc.nextDouble();
                        System.out.println("Square Root = " + Math.sqrt(a));
                        break;

                    case 6:
                        System.out.print("Enter base and exponent: ");
                        a = sc.nextDouble();
                        b = sc.nextDouble();
                        System.out.println("Result = " + Math.pow(a, b));
                        break;

                    case 7:
                        System.out.print("Enter temperature in Celsius: ");
                        a = sc.nextDouble();
                        result = (a * 9 / 5) + 32;
                        System.out.println("Fahrenheit = " + result);
                        break;

                    case 8:
                        System.out.print("Enter amount in INR: ");
                        a = sc.nextDouble();
                        result = a * 0.012; // Example rate
                        System.out.println("USD = " + result);
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }

        sc.close();
        System.out.println("Calculator Closed.");
    }
}