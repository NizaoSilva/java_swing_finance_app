package Forms;

import Classes.*;
import java.awt.Component;
import javax.swing.*;
import java.util.*;
import java.sql.SQLException;
import java.sql.ResultSet;
        
public class NewUser extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(NewUser.class.getName());
    private int moment=  0;
    private Map<String, String[]> steps;
    private Map<String, String> userDate = new HashMap<>();

    public NewUser() {
        initComponents();
        setLocationRelativeTo(null);

        pnBackGround.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        steps = new LinkedHashMap<>();
        steps.put("Your Identify:", new String[]{"Document"});
        steps.put("Address:", new String[]{"Street","Number","Neighborhood","City","UF","Country","Zip Code"});
        steps.put("Contact:", new String[]{"Name","Email","Telephone","Date of birth"});
        steps.put("Client:", new String[]{"Username","Password"});

        showContent();
    }

    private void showContent() {
        pnBackGround.removeAll();
        String chave = (String) steps.keySet().toArray()[moment];
        String[] campos = steps.get(chave);

        pnBackGround.setLayout(new BoxLayout(pnBackGround, BoxLayout.Y_AXIS));

        JLabel subtitulo = new JLabel(chave);
        subtitulo.setFont(lblSubTitle.getFont());
        subtitulo.setForeground(lblSubTitle.getForeground());
        pnBackGround.add(subtitulo);
        pnBackGround.add(Box.createVerticalStrut(10)); 

        for (String campo : campos) {
            JLabel lbl = new JLabel(campo + ":");
            JTextField fd;
            if (campo.equals("Password")) {
                fd = new JPasswordField(20);
            } else {
                fd = new JTextField(20);
            }
            fd.setName(campo);
            // se já digitou antes, recarrega
            if (userDate.containsKey(campo)) {
                fd.setText(userDate.get(campo));
            }
            pnBackGround.add(lbl);
            pnBackGround.add(Box.createVerticalStrut(5)); 
            pnBackGround.add(fd);
            pnBackGround.add(Box.createVerticalStrut(10)); 
        }

        JButton bttProximo = new JButton(moment == steps.size()-1 ? "Finish" : "Next");
        bttProximo.addActionListener(e -> {
            try {
                nextStep();
            } catch (SQLException ex) {
                logger.severe("Erro: " + ex.getMessage());
            }
        });
        pnBackGround.add(bttProximo);

        pnBackGround.revalidate();
        pnBackGround.repaint();
        this.pack();
        this.setLocationRelativeTo(null); 
    }

private void nextStep() throws SQLException {
    // coleta campos da etapa atual
    Component[] comps = pnBackGround.getComponents();
    for (Component c : comps) {
        if (c instanceof JTextField fd) {
            userDate.put(fd.getName(), fd.getText().trim());
        }
    }

    String chave = (String) steps.keySet().toArray()[moment];
    DataBase db = new DataBase();
    
    switch (chave) {
        case "Your Identify:": {
            String document = userDate.get("Document");
            try (ResultSet rs = db.executeQuery( "SELECT id FROM contact WHERE cpf = ?",document)) {
                if (rs.next()) {
                    int option = JOptionPane.showConfirmDialog(this,
                            "This document already exists. Do you want to create another account anyway?",
                            "Document already registered", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        
                        moment = steps.size() - 1; // pula direto para Cliente
                    } else {
                        return; // fica na mesma etapa
                    }
                } else {
                    moment++;
                }
                break;
            } 
        }

        case "Address:": {
            moment++;
            break;
        }

        case "Contact:": {
            String email = userDate.get("Email");
            try (ResultSet rs = db.executeQuery( "SELECT id FROM contact WHERE email = ?",email)) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "This email already exists!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    moment++;
                    break;
                }
            }
        }

        case "Client:": {
            String username = userDate.get("Username");
            try (ResultSet rs = db.executeQuery( "SELECT id FROM client WHERE username = ?",username)) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "This username already exists!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            Contact contact;
            contact = Contact.fromCpf(userDate.get("Document"));
            if (null == contact) {
                Address address = Address.create(
                        userDate.get("Street"),
                        userDate.get("Number"),
                        userDate.get("Neighborhood"),
                        userDate.get("City"),
                        userDate.get("UF"),
                        userDate.get("Country"),
                        userDate.get("Zip Code"));
    //            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    //            LocalDate birth = LocalDate.parse(dadosUsuario.get("Date of birth"), fmt);
                contact = Contact.create(
                        userDate.get("Name"),
                        userDate.get("Email"),
                        userDate.get("Telephone"),
                        userDate.get("Date of birth"),
                        userDate.get("Document"),
                        address.getId());
            }

            int qtdClientes = 0;
            try (ResultSet rs = db.executeQuery("SELECT COUNT(*) AS total FROM client")) {
                if (rs.next()) {
                    qtdClientes = rs.getInt("total");
                }
            }

            if (qtdClientes == 0) {
                // cria admin
                Admin.create(userDate.get("Username"), userDate.get("Password"), contact.getId());
            } else {
                // cria client
                Client.create(userDate.get("Username"), userDate.get("Password"), contact.getId());
            }

            JOptionPane.showMessageDialog(this, "User registered successfully!");
            dispose();
            Credentials homepage = new Credentials();
            homepage.setVisible(true);
            break;
        }
    }
    showContent();
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bttHomePage = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        pnBackGround = new javax.swing.JPanel();
        lblSubTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        bttHomePage.setText("Home Page");
        bttHomePage.setMinimumSize(new java.awt.Dimension(75, 44));
        bttHomePage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttHomePageActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitle.setText("Become a User");

        pnBackGround.setBackground(new java.awt.Color(255, 255, 255));
        pnBackGround.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout pnBackGroundLayout = new javax.swing.GroupLayout(pnBackGround);
        pnBackGround.setLayout(pnBackGroundLayout);
        pnBackGroundLayout.setHorizontalGroup(
            pnBackGroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnBackGroundLayout.setVerticalGroup(
            pnBackGroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 27, Short.MAX_VALUE)
        );

        lblSubTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSubTitle.setForeground(new java.awt.Color(102, 102, 102));
        lblSubTitle.setText("if you are not yet");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnBackGround, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTitle)
                            .addComponent(lblSubTitle))
                        .addGap(31, 31, 31)
                        .addComponent(bttHomePage, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSubTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bttHomePage, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnBackGround, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void bttHomePageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttHomePageActionPerformed
        this.dispose();
        new Credentials().setVisible(true);;
    }//GEN-LAST:event_bttHomePageActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new NewUser().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttHomePage;
    private javax.swing.JLabel lblSubTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnBackGround;
    // End of variables declaration//GEN-END:variables
}
