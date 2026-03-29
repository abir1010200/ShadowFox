import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LibraryGUI {

    static final String DB_URL = "jdbc:sqlite:library.db";
    static int currentUserId = -1;

    JFrame frame;
    CardLayout cardLayout;
    JPanel mainPanel;

    JTextField usernameField, passwordField;

    public static void main(String[] args) {
        new LibraryGUI();
    }

    public LibraryGUI() {
        createTables();

        frame = new JFrame("Library System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "login");
        mainPanel.add(registerPanel(), "register");
        mainPanel.add(menuPanel(), "menu");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // ---------------- DATABASE ----------------
    void createTables() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT UNIQUE, password TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS books (id INTEGER PRIMARY KEY, title TEXT, author TEXT, available INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS borrowed (user_id INTEGER, book_id INTEGER)");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM books");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO books VALUES (1,'Java Basics','James Gosling',1)");
                stmt.execute("INSERT INTO books VALUES (2,'Python Guide','Guido van Rossum',1)");
                stmt.execute("INSERT INTO books VALUES (3,'DSA','Mark Weiss',1)");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- LOGIN ----------------
    JPanel loginPanel() {
        JPanel panel = new JPanel(new GridLayout(4,1));

        usernameField = new JTextField();
        passwordField = new JTextField();

        JButton loginBtn = new JButton("Login");
        JButton goRegister = new JButton("Register");

        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);

        panel.add(loginBtn);
        panel.add(goRegister);

        loginBtn.addActionListener(e -> login());
        goRegister.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        return panel;
    }

    void login() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?");

            ps.setString(1, usernameField.getText());
            ps.setString(2, passwordField.getText());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                currentUserId = rs.getInt("id");
                JOptionPane.showMessageDialog(frame, "Login Success");
                cardLayout.show(mainPanel, "menu");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Login");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- REGISTER ----------------
    JPanel registerPanel() {
        JPanel panel = new JPanel(new GridLayout(4,1));

        JTextField user = new JTextField();
        JTextField pass = new JTextField();

        JButton registerBtn = new JButton("Register");
        JButton back = new JButton("Back");

        panel.add(new JLabel("New Username"));
        panel.add(user);
        panel.add(new JLabel("New Password"));
        panel.add(pass);

        panel.add(registerBtn);
        panel.add(back);

        registerBtn.addActionListener(e -> {
            try {
                Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO users(username,password) VALUES(?,?)");

                ps.setString(1, user.getText());
                ps.setString(2, pass.getText());
                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Registered!");
                cardLayout.show(mainPanel, "login");

                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "User exists!");
            }
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        return panel;
    }

    // ---------------- MENU ----------------
    JPanel menuPanel() {
        JPanel panel = new JPanel(new GridLayout(5,1));

        JButton view = new JButton("View Books");
        JButton borrow = new JButton("Borrow Book");
        JButton returnB = new JButton("Return Book");
        JButton rec = new JButton("Recommendations");
        JButton logout = new JButton("Logout");

        panel.add(view);
        panel.add(borrow);
        panel.add(returnB);
        panel.add(rec);
        panel.add(logout);

        view.addActionListener(e -> viewBooks());
        borrow.addActionListener(e -> borrowBook());
        returnB.addActionListener(e -> returnBook());
        rec.addActionListener(e -> recommend());
        logout.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        return panel;
    }

    // ---------------- FUNCTIONS ----------------
    void viewBooks() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");

            String data = "";
            while (rs.next()) {
                data += rs.getInt("id") + ". " +
                        rs.getString("title") + " (" +
                        (rs.getInt("available")==1?"Available":"Not Available") + ")\n";
            }

            JOptionPane.showMessageDialog(frame, data);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void borrowBook() {
        String id = JOptionPane.showInputDialog("Enter Book ID");

        try {
            Connection conn = DriverManager.getConnection(DB_URL);

            PreparedStatement check = conn.prepareStatement(
                    "SELECT available FROM books WHERE id=?");
            check.setInt(1, Integer.parseInt(id));
            ResultSet rs = check.executeQuery();

            if (rs.next() && rs.getInt(1)==1) {

                PreparedStatement borrow = conn.prepareStatement(
                        "INSERT INTO borrowed VALUES (?,?)");
                borrow.setInt(1, currentUserId);
                borrow.setInt(2, Integer.parseInt(id));
                borrow.executeUpdate();

                PreparedStatement update = conn.prepareStatement(
                        "UPDATE books SET available=0 WHERE id=?");
                update.setInt(1, Integer.parseInt(id));
                update.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Borrowed!");
            } else {
                JOptionPane.showMessageDialog(frame, "Not available!");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void returnBook() {
        String id = JOptionPane.showInputDialog("Enter Book ID");

        try {
            Connection conn = DriverManager.getConnection(DB_URL);

            PreparedStatement delete = conn.prepareStatement(
                    "DELETE FROM borrowed WHERE user_id=? AND book_id=?");
            delete.setInt(1, currentUserId);
            delete.setInt(2, Integer.parseInt(id));
            delete.executeUpdate();

            PreparedStatement update = conn.prepareStatement(
                    "UPDATE books SET available=1 WHERE id=?");
            update.setInt(1, Integer.parseInt(id));
            update.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Returned!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void recommend() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT title FROM books WHERE available=1 LIMIT 3");

            ResultSet rs = ps.executeQuery();

            String data = "Recommended:\n";
            while (rs.next()) {
                data += "- " + rs.getString("title") + "\n";
            }

            JOptionPane.showMessageDialog(frame, data);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}