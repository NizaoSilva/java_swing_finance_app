package Forms;

import Classes.Admin;
import Classes.DataBase;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Settings extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Settings.class.getName());
    private HomePage homePage;

    public Settings() {
        initComponents();
    }
    
    public Settings(HomePage homePage) {
        this();
        this.homePage = homePage;
        
        DataBase db = new DataBase();
        try {
            ResultSet rs = db.executeQuery("SELECT * FROM settings LIMIT 1");
            rs.next();
            
            fdSzInterestRate.setValue(rs.getDouble("sz_interest_rate"));
            fdLoInterestRate.setValue(rs.getDouble("lo_interest_rate"));
            fdLoInitialFee.setValue(rs.getDouble("lo_initial_fee"));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        lblSzInterestRate = new javax.swing.JLabel();
        fdSzInterestRate = new javax.swing.JFormattedTextField();
        bttConfirm = new javax.swing.JButton();
        lblLoInterestRate = new javax.swing.JLabel();
        fdLoInterestRate = new javax.swing.JFormattedTextField();
        lblLoInitialFee = new javax.swing.JLabel();
        fdLoInitialFee = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        lblSzInterestRate.setText("SafeZone Interest Rate");

        fdSzInterestRate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.000000"))));
        fdSzInterestRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fdSzInterestRateActionPerformed(evt);
            }
        });

        bttConfirm.setText("Confirm");
        bttConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttConfirmActionPerformed(evt);
            }
        });

        lblLoInterestRate.setText("Loan Interest Rate");

        fdLoInterestRate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.000000"))));

        lblLoInitialFee.setText("Loan Initial Fee");

        fdLoInitialFee.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fdLoInitialFee, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLoInitialFee, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fdLoInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLoInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fdSzInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bttConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSzInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblSzInterestRate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fdSzInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblLoInterestRate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fdLoInterestRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblLoInitialFee)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fdLoInitialFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
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

    private void bttConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttConfirmActionPerformed
        try { //((Number) fdValue.getValue()).doubleValue()
            DataBase db = new DataBase();
            db.executeUpdate("UPDATE settings SET sz_interest_rate=?, lo_interest_rate=?, lo_initial_fee=?", fdSzInterestRate.getValue(),
                    fdLoInterestRate.getValue(), fdLoInitialFee.getValue());
            if (homePage.operationsForm != null) {
                if (homePage.operationsForm.cbOperation.getSelectedItem().toString().equals("Loan")) {
                    homePage.operationsForm.lblInterestRate.setText(
                        "Interest Rate: " + String.format("%.2f%%", ((Number) fdLoInterestRate.getValue()).doubleValue() * 100) + " /day");
                    homePage.operationsForm.lblInitialFee.setText(
                        "Initial Fee: "   + String.format("%.2f%%", ((Number) fdLoInitialFee.getValue()).doubleValue() * 100));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bttConfirmActionPerformed

    private void fdSzInterestRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fdSzInterestRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fdSzInterestRateActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Settings().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttConfirm;
    private javax.swing.JFormattedTextField fdLoInitialFee;
    private javax.swing.JFormattedTextField fdLoInterestRate;
    private javax.swing.JFormattedTextField fdSzInterestRate;
    private javax.swing.JLabel lblLoInitialFee;
    private javax.swing.JLabel lblLoInterestRate;
    private javax.swing.JLabel lblSzInterestRate;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
