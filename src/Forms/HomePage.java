package Forms;

import Classes.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class HomePage extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(HomePage.class.getName());
    protected Client client;
    protected Capital capital;
    private boolean isInitializing = true;
    protected Operations operationsForm = null;
    private Settings settingsForm = null;
    private Charts chartsForm = null;
    
    public HomePage() {initComponents();}

    public HomePage(Client client){
        this();
        this.client = client;
        
        try {
            lblSubTitle.setText("Welcome, " + this.client.getContact().getName() + "!");
            if (client instanceof Admin) {
                bttSupport.setText("Settings");
            }
            client.refreshCapitals();
            if (client.getCapitals().isEmpty()){
                Current.create(client.getId());
            //    client.getCapitals().add();
            }
            cbCapital.removeAllItems();
            for (Capital cap : client.getCapitals()) {
                cbCapital.addItem(cap.toString());
            }
            capital = client.getCapitals().get(0);
            lblBalance.setText(!(capital instanceof SafeZone)
                ? "$ " + String.format("%.2f", capital.getBalance())
                : "$ " + String.format("%.2f", capital.getBalance()) + " +" +
                  String.format("%.2f%%", SafeZone.fromId(capital.getId()).getInterestRate() * 100) + " /day");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        isInitializing = false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        cbCapital = new javax.swing.JComboBox<>();
        lblBalance = new javax.swing.JLabel();
        bttExit = new javax.swing.JButton();
        lblSubTitle = new javax.swing.JLabel();
        bttOperations = new javax.swing.JButton();
        bttCharts = new javax.swing.JButton();
        bttSupport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitle.setText("Finance Bank");

        cbCapital.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Current 1", "SafeZone 2", "Loan 3" }));
        cbCapital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCapitalActionPerformed(evt);
            }
        });

        lblBalance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblBalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBalance.setText("Balance: 00,00");

        bttExit.setText("Exit");
        bttExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttExitActionPerformed(evt);
            }
        });

        lblSubTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSubTitle.setForeground(new java.awt.Color(102, 102, 102));
        lblSubTitle.setText("Welcome username");

        bttOperations.setText("Operations");
        bttOperations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttOperationsActionPerformed(evt);
            }
        });

        bttCharts.setText("Charts");
        bttCharts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttChartsActionPerformed(evt);
            }
        });

        bttSupport.setText("Suport");
        bttSupport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttSupportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lblSubTitle)
                            .addComponent(lblTitle)
                            .addComponent(bttCharts, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bttOperations, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(bttSupport, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bttExit, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbCapital, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblBalance, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbCapital, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBalance)
                    .addComponent(lblSubTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bttOperations, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bttSupport, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bttCharts, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bttExit, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbCapitalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCapitalActionPerformed
        cbCapitalPerform();
    }//GEN-LAST:event_cbCapitalActionPerformed

    protected void cbCapitalPerform(){
        if (isInitializing) {return;}
        Object item = cbCapital.getSelectedItem(); 
        if (item == null) {
            lblBalance.setText("$ 0.00"); 
            return; 
        }
        String selectedItem = (String) item;
        try {
            String idString = selectedItem.split("\\.")[0].trim();
            int id = Integer.parseInt(idString);

            capital = Capital.fromId(id);
            this.capital = capital;
            
            lblBalance.setText(!(capital instanceof SafeZone)
                ? "$ " + String.format("%.2f", capital.getBalance())
                : "$ " + String.format("%.2f", capital.getBalance()) + " +" +
                  String.format("%.2f%%", SafeZone.fromId(capital.getId()).getInterestRate() * 100) + " /day");
            if (chartsForm != null) {chartsForm.showOperations();}
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) { // Trata erros de split/parse
            JOptionPane.showMessageDialog(this, "Erro: Não foi possível ler o ID da Capital selecionada.", 
                "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no Banco de Dados: " + ex.getMessage(), "Erro SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    protected void cbCapitalPerform(Capital capital){
        cbCapital.addItem(capital.toString());
    }

    
    private void bttExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttExitActionPerformed
        if (operationsForm != null) {operationsForm.dispose();}
        if (settingsForm != null) {settingsForm.dispose();}
        if (chartsForm != null) {chartsForm.dispose();}
        this.dispose();
        new Credentials().setVisible(true);
    }//GEN-LAST:event_bttExitActionPerformed

    private void bttOperationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttOperationsActionPerformed
        if (operationsForm != null) {operationsForm.dispose();}
        operationsForm = new Operations(this); //if (chartsForm == null) {}

        // Obtém a posição e tamanho do form atual
        int currentX = this.getX();
        int currentY = this.getY();
        int currentWidth = this.getWidth();

        // Define a posição do novo form ao lado direito do atual
        int newX = currentX + currentWidth; // "+10" é um pequeno espaço entre eles
        int newY = currentY; // mesma altura vertical

        operationsForm.setLocation(newX, newY);
        operationsForm.setVisible(true);
    }//GEN-LAST:event_bttOperationsActionPerformed
    
    private void bttChartsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttChartsActionPerformed
        if (chartsForm != null) {chartsForm.dispose();}
        chartsForm = new Charts(this); //if (chartsForm == null) {}

        // Obtém a posição e tamanho do form atual
        int currentX = this.getX();
        int currentY = this.getY();
        int currentWidth = this.getWidth();

        // Define a posição do novo form ao lado direito do atual
        int newX = currentX + currentWidth; // "+10" é um pequeno espaço entre eles
        int newY = currentY; // mesma altura vertical

        chartsForm.setLocation(newX, newY);
        chartsForm.setVisible(true);
    }//GEN-LAST:event_bttChartsActionPerformed

    private void bttSupportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttSupportActionPerformed
        if (client instanceof Admin) {
            if (settingsForm == null) {settingsForm = new Settings(this);}

            // Obtém a posição e tamanho do form atual
            int currentX = this.getX();
            int currentY = this.getY();
            int currentWidth = this.getWidth();

            // Define a posição do novo form ao lado direito do atual
            int newX = currentX + currentWidth; // "+10" é um pequeno espaço entre eles
            int newY = currentY; // mesma altura vertical

            settingsForm.setLocation(newX, newY);
            settingsForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Safezone account request sent.");
            try {
                DataBase db = new DataBase();
                ResultSet rs = db.executeQuery("SELECT * FROM settings LIMIT 1");
                rs.next();
                if (!client.getCapitals().stream().anyMatch(c -> c instanceof SafeZone)) {
                    cbCapitalPerform(SafeZone.create(rs.getDouble("sz_interest_rate"), client.getId()));
                    JOptionPane.showMessageDialog(this, "Safezone created successfully!!");
                } else {
                    JOptionPane.showMessageDialog(this, "You already have a safezone account!!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_bttSupportActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new HomePage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttCharts;
    private javax.swing.JButton bttExit;
    private javax.swing.JButton bttOperations;
    private javax.swing.JButton bttSupport;
    private javax.swing.JComboBox<String> cbCapital;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblSubTitle;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables

    private void cbCapitalActionPerformed() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
