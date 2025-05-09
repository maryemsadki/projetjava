package javaproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// Classe permettant d'ajouter un contact via une interface graphique
public class ajoutercontact extends JFrame {

    // Champs de texte pour saisir les informations du contact
    private JTextField nomField, prenomField, telephoneField;
    private JButton okButton, annulerButton;

    // Constructeur : crée et affiche la fenêtre "Ajouter un Contact"
    public ajoutercontact(JFrame parent) {
        setTitle("Ajouter un Contact"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme uniquement cette fenêtre, pas toute l'application
        setLocationRelativeTo(parent); // Centre la fenêtre par rapport à la fenêtre parent

        // Panel principal contenant tous les éléments de la fenêtre
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Disposition verticale des composants
        panel.setBackground(new Color(255, 228, 225)); // Couleur de fond rose clair
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Marge intérieure du panel

        // Titre centré en haut
        JLabel titre = new JLabel("Ajouter un Contact");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(new Color(139, 0, 0)); // Rouge foncé
        titre.setAlignmentX(Component.CENTER_ALIGNMENT); // Centre horizontalement
        panel.add(titre);
        panel.add(Box.createVerticalStrut(20)); // Espace vertical après le titre

        // Champ pour le nom
        JLabel labelNom = new JLabel("Nom :");
        nomField = new JTextField();
        nomField.setPreferredSize(new Dimension(200, 30));
        panel.add(labelNom);
        panel.add(Box.createVerticalStrut(5)); // Petit espace
        panel.add(nomField);
        panel.add(Box.createVerticalStrut(15)); // Espace entre les champs

        // Champ pour le prénom
        JLabel labelPrenom = new JLabel("Prénom :");
        prenomField = new JTextField();
        prenomField.setPreferredSize(new Dimension(200, 30));
        panel.add(labelPrenom);
        panel.add(Box.createVerticalStrut(5));
        panel.add(prenomField);
        panel.add(Box.createVerticalStrut(15));

        // Champ pour le numéro de téléphone
        JLabel labelTel = new JLabel("Numéro de téléphone :");
        telephoneField = new JTextField();
        telephoneField.setPreferredSize(new Dimension(200, 30));
        panel.add(labelTel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(telephoneField);
        panel.add(Box.createVerticalStrut(20));

        // Création des boutons "OK" et "Annuler"
        okButton = new JButton("OK");
        annulerButton = new JButton("Annuler");

        // Panel contenant les deux boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 228, 225)); // Même fond que le panel principal
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0)); // Centrage horizontal
        buttonPanel.add(okButton);
        buttonPanel.add(annulerButton);
        panel.add(buttonPanel); // Ajout des boutons au panel principal

        // Action du bouton "OK" : ajouter le contact
        okButton.addActionListener(e -> ajouterContact());

        // Action du bouton "Annuler" : ferme la fenêtre
        annulerButton.addActionListener(e -> dispose());

        // Ajout du panel principal à la fenêtre
        add(panel);
        setVisible(true); // Affiche la fenêtre
    }

    // Méthode appelée quand on clique sur "OK"
    private void ajouterContact() {
        // Récupération des valeurs saisies
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String telephone = telephoneField.getText();

        // Connexion à la base de données et insertion du contact
        try (Connection conn = connexionDB.getConnection()) {
            String query = "INSERT INTO contacts (nom, prénom, tel) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nom);       // Remplace le premier ? par le nom
                stmt.setString(2, prenom);    // Remplace le deuxième ? par le prénom
                stmt.setString(3, telephone); // Remplace le troisième ? par le téléphone
                stmt.executeUpdate();         // Exécute l'instruction SQL
                JOptionPane.showMessageDialog(this, "Contact ajouté avec succès!");
                dispose(); // Ferme la fenêtre après l’ajout
            }
        } catch (SQLException ex) {
            // Affiche un message d'erreur en cas d’échec de la requête
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du contact: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}

