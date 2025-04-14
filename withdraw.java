import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Withdraw extends javax.swing.JFrame {

    private Connection con;
    private PreparedStatement insert, update;
    private ResultSet rs;

    public Withdraw() {
        initComponents();
        date();
    }

    private void initComponents() {
        // Swing component initialization code remains as-is
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            con.setAutoCommit(false);

            String accno = txtacc.getText();
            String cust_id = jLabel13.getText();
            String firstname = jLabel14.getText();
            String lastname = jLabel15.getText();
            String date = jLabel16.getText();
            String balance = jLabel6.getText();
            String amount = txtamount.getText();

            insert = con.prepareStatement("insert into withdraw(acc_id,cust_id,date,balance,withdraw) values(?,?,?,?,?)");
            insert.setString(1, accno);
            insert.setString(2, cust_id);
            insert.setString(3, date);
            insert.setString(4, balance);
            insert.setString(5, amount);
            insert.executeUpdate();

            update = con.prepareStatement("update Account set bal = bal - ? where account_no = ?");
            update.setString(1, amount);
            update.setString(2, accno);
            update.executeUpdate();

            JOptionPane.showMessageDialog(this, "Withdrawn Successfully");
            con.commit();
        } catch (ClassNotFoundException | SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Withdraw.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(Withdraw.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Withdrawal failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void date() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        jLabel16.setText(date);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        dispose();
    }

    private void findAccount(String accno) {
        try {
            insert = con.prepareStatement("select c.cust_id, c.firstname, c.lastname, a.bal from customer c, account a where c.cust_id = a.customerid and a.account_no = ?");
            insert.setString(1, accno);
            rs = insert.executeQuery();

            if (rs.next()) {
                String id = rs.getString(1);
                String firstname = rs.getString(2);
                String lastname = rs.getString(3);
                String balance = rs.getString(4);

                jLabel13.setText(id.trim());
                jLabel14.setText(firstname.trim());
                jLabel15.setText(lastname.trim());
                jLabel6.setText(balance.trim());
            } else {
                JOptionPane.showMessageDialog(this, "Account not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Withdraw.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error finding account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variables declaration - do not modify
    // Variable declarations for Swing components remain as-is
    // End of variables declaration
}
