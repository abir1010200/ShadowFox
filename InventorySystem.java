import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class InventorySystem extends JFrame {

    // Components
    private JTextField idField, nameField, quantityField, priceField;
    private JTable table;
    private DefaultTableModel model;

    // Data storage
    private ArrayList<Item> inventory = new ArrayList<>();

    // Item class
    class Item {
        String id, name;
        int quantity;
        double price;

        Item(String id, String name, int quantity, double price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
    }

    public InventorySystem() {
        setTitle("Inventory Management System");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel (Form)
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        panel.add(quantityField);

        panel.add(new JLabel("Price:"));
        priceField = new JTextField();
        panel.add(priceField);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        panel.add(addBtn);
        panel.add(updateBtn);

        add(panel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Quantity", "Price"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions

        // ADD
        addBtn.addActionListener(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                int qty = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());

                Item item = new Item(id, name, qty, price);
                inventory.add(item);

                model.addRow(new Object[]{id, name, qty, price});

                clearFields();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        // UPDATE
        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow >= 0) {
                try {
                    String id = idField.getText();
                    String name = nameField.getText();
                    int qty = Integer.parseInt(quantityField.getText());
                    double price = Double.parseDouble(priceField.getText());

                    Item item = inventory.get(selectedRow);
                    item.id = id;
                    item.name = name;
                    item.quantity = qty;
                    item.price = price;

                    model.setValueAt(id, selectedRow, 0);
                    model.setValueAt(name, selectedRow, 1);
                    model.setValueAt(qty, selectedRow, 2);
                    model.setValueAt(price, selectedRow, 3);

                    clearFields();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a row first!");
            }
        });

        // DELETE
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow >= 0) {
                inventory.remove(selectedRow);
                model.removeRow(selectedRow);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Select a row first!");
            }
        });

        // TABLE CLICK → Fill fields
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();

                idField.setText(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                quantityField.setText(model.getValueAt(row, 2).toString());
                priceField.setText(model.getValueAt(row, 3).toString());
            }
        });

        setVisible(true);
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventorySystem());
    }
}