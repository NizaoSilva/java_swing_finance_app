package Forms;

import Classes.*;
import java.sql.ResultSet;
import javax.swing.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class Operations extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Operations.class.getName());
    protected JLabel lblDestination, lblInterestRate, lblInitialFee;
    private JFormattedTextField fdDestination;
    private HomePage homePage;

    public Operations() {
        initComponents();
    }
    
    public Operations(HomePage homePage) {
        this();
        this.homePage = homePage;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        cbOperation = new javax.swing.JComboBox<>();
        lblValue = new javax.swing.JLabel();
        fdValue = new javax.swing.JFormattedTextField();
        bttConfirm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        cbOperation.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        cbOperation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Deposit", "Withdraw", "Transfer", "Loan" }));
        cbOperation.setSelectedIndex(1);
        cbOperation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOperationActionPerformed(evt);
            }
        });

        lblValue.setText("Value");

        fdValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

        bttConfirm.setText("Confirm");
        bttConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttConfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbOperation, 0, 163, Short.MAX_VALUE)
                    .addComponent(fdValue)
                    .addComponent(bttConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(cbOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fdValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bttConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cbOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOperationActionPerformed
        panel.setLayout(null);
        int offsetY = 60;
        String op = cbOperation.getSelectedItem().toString();
        
        // limpa campos extras
        if (lblDestination != null) { panel.remove(lblDestination); lblDestination = null; }
        if (fdDestination != null) { panel.remove(fdDestination); fdDestination = null; }
        if (lblInterestRate != null) { panel.remove(lblInterestRate); lblInterestRate = null; }
        if (lblInitialFee != null) { panel.remove(lblInitialFee); lblInitialFee = null; }

        int height = 238, btnY = 136;

        if (op.equals("Transfer")) {
            lblDestination = new JLabel("Destination:");
            lblDestination.setBounds(lblValue.getX(), lblValue.getY() + offsetY, lblValue.getWidth(), lblValue.getHeight());
            panel.add(lblDestination);
            NumberFormatter intFormatter = new NumberFormatter(new DecimalFormat("#0"));
            intFormatter.setAllowsInvalid(false);
            fdDestination = new JFormattedTextField(new DefaultFormatterFactory(intFormatter));
            fdDestination.setBounds(fdValue.getX(), fdValue.getY() + offsetY, fdValue.getWidth(), fdValue.getHeight());
            panel.add(fdDestination);
            height = 298; btnY = 196;
        }
        if (op.equals("Loan")) {
            DataBase db = new DataBase();
            try {ResultSet rs = db.executeQuery("SELECT * FROM settings LIMIT 1");
                rs.next();
                lblInterestRate = new JLabel("Interest Rate: " + String.format("%.2f%%", Double.parseDouble(rs.getString("lo_interest_rate")) * 100) + " /day");
                lblInitialFee = new JLabel("Initial Fee: "   + String.format("%.2f%%", Double.parseDouble(rs.getString("lo_initial_fee")) * 100));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            lblInterestRate.setBounds(lblValue.getX(), lblValue.getY() + 60, lblValue.getWidth(), lblValue.getHeight());
            lblInitialFee.setBounds(lblValue.getX(), lblValue.getY() + 90, lblValue.getWidth(), lblValue.getHeight());
            panel.add(lblInterestRate); panel.add(lblInitialFee);
            height = 298; btnY = 196;
        }

        bttConfirm.setLocation(bttConfirm.getX(), btnY);
        this.setSize(this.getWidth(), height);
        panel.revalidate(); panel.repaint();
    }//GEN-LAST:event_cbOperationActionPerformed

    private void bttConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttConfirmActionPerformed
        if (((Number) fdValue.getValue()).doubleValue() < 0) {
            JOptionPane.showMessageDialog(this, "The value must be positive!","Error", JOptionPane.ERROR_MESSAGE);}
        String op = cbOperation.getSelectedItem().toString();
        try {
            if (("Withdraw".equals(op) || "Transfer".equals(op)) && ((Number) fdValue.getValue()).doubleValue() > homePage.capital.getBalance()) {
                JOptionPane.showMessageDialog(this, "This capital does not have enough money!","Error", JOptionPane.ERROR_MESSAGE);
            } else if ("Deposit".equals(op)) {
                Deposit.create(((Number) fdValue.getValue()).doubleValue(), homePage.capital.getId());
            } else if ("Withdraw".equals(op)) {
                Withdraw.create(((Number) fdValue.getValue()).doubleValue(), homePage.capital.getId());
            } else if ("Transfer".equals(op)) {
                if (JOptionPane.showConfirmDialog(this,
                    "Confirm transfer to " + Capital.fromId(((Number) fdDestination.getValue()).intValue()) + "?",
                    "Transfer confirm",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) return;
                Transfer.create(((Number) fdValue.getValue()).doubleValue(), homePage.capital.getId(), ((Number) fdDestination.getValue()).intValue());
            } else if ("Loan".equals(op)) {
                if (JOptionPane.showConfirmDialog(this,
                    "Confirm loan to " + homePage.capital + "?",
                    "Loan confirm",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) return;
                DataBase db = new DataBase();
                ResultSet rs = db.executeQuery("SELECT * FROM settings LIMIT 1");
                rs.next();
                Loan lo = Loan.create(rs.getDouble("lo_interest_rate"), ((Number) fdValue.getValue()).doubleValue(),
                    rs.getDouble("lo_initial_fee"), homePage.client.getId(), homePage.capital.getId());
                homePage.cbCapitalPerform(lo);
            }
            homePage.cbCapitalPerform();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_bttConfirmActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Operations().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttConfirm;
    protected javax.swing.JComboBox<String> cbOperation;
    private javax.swing.JFormattedTextField fdValue;
    private javax.swing.JLabel lblValue;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
