package javaproject;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

// Fenêtre pour modifier un contact existant
public class modifierContactt extends JFrame {
    // Champs de saisie
    private JTextField nomField, prenomField, telephoneField;

    // ID du contact à modifier
    private int contactId;

    // Référence à la fenêtre principale pour pouvoir la rafraîchir
    private contact parent;

    // Constructeur prenant les infos du contact sélectionné
    public modifierContactt(contact parent, int id, String nom, String prenom, String telephone) {
        this.parent = parent;
        this.contactId = id;

        setTitle("Modifier un Contact");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent); // Centre la fenêtre par rapport à la fenêtre principale

        // Création d'un panneau principal avec disposition verticale
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 228, 225)); // Couleur de fond rose pâle
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Marge intérieure

        // Titre
        JLabel titre = new JLabel("Modifier un Contact");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(new Color(139, 0, 0)); // Rouge foncé
        titre.setAlignmentX(Component.CENTER_ALIGNMENT); // Centré horizontalement
        panel.add(titre);
        panel.add(Box.createVerticalStrut(20)); // Espace vertical

        // Champ Nom
        panel.add(new JLabel("Nom :"));
        nomField = new JTextField(nom); // Pré-rempli avec l’ancien nom
        panel.add(nomField);

        // Champ Prénom
        panel.add(new JLabel("Prénom :"));
        prenomField = new JTextField(prenom);
        panel.add(prenomField);

        // Champ Téléphone
        panel.add(new JLabel("Téléphone :"));
        telephoneField = new JTextField(telephone);
        panel.add(telephoneField);

        // Boutons
        JButton okButton = new JButton("Modifier");
        JButton annulerButton = new JButton("Annuler");

        // Action pour modifier le contact en base
        okButton.addActionListener(e -> modifierContact());

        // Ferme la fenêtre si on annule
        annulerButton.addActionListener(e -> dispose());

        // Panneau des boutons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(255, 228, 225));
        btnPanel.add(okButton);
        btnPanel.add(annulerButton);

        // Ajout final au panneau principal
        panel.add(Box.createVerticalStrut(20)); // Espace avant les boutons
        panel.add(btnPanel);

        add(panel); // Ajoute le panneau à la fenêtre
        setVisible(true); // Affiche la fenêtre
    }

    // Méthode qui exécute la requête SQL pour modifier les données du contact
    private void modifierContact() {
        // Récupération des valeurs saisies
        String nom = nomField.getText();
        String prénom = prenomField.getText();
        String tel = telephoneField.getText();

        // Connexion à la base et exécution de la requête UPDATE
        try (Connection conn = connexionDB.getConnection()) {
            String sql = "UPDATE contacts SET nom = ?, prénom = ?, tel = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setString(2, prénom); // Assurez-vous que la colonne s'appelle bien "prénom" en base
            stmt.setString(3, tel);
            stmt.setInt(4, contactId); // On utilise l’ID pour identifier le bon enregistrement
            stmt.executeUpdate();

            // Message de confirmation
            JOptionPane.showMessageDialog(this, "Contact modifié avec succès !");
            
            // Rafraîchit la table dans la fenêtre principale
            parent.rafraichirTableau();

            // Ferme la fenêtre
            dispose();
        } catch (SQLException ex) {
            // Affiche un message d'erreur si la modification échoue
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }
}
