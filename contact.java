package javaproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// Classe principale pour gérer la fenêtre de gestion de contacts
public class contact extends JFrame {

    // Composants de l'interface
    private JTable table; // Tableau pour afficher les contacts
    private DefaultTableModel model; // Modèle de données pour JTable

    // Constructeur : initialise l'interface
    public contact() {
        setTitle("Gestion de Contact"); // Titre de la fenêtre
        setSize(800, 600); // Taille
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme uniquement cette fenêtre
        setLocationRelativeTo(null); // Centre la fenêtre

        // Panel principal avec une bordure et une couleur de fond
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 228, 225));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre affiché en haut
        JLabel titre = new JLabel("Gestion de Contact");
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(new Color(139, 0, 0));
        titre.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titre, BorderLayout.NORTH);

        // Modèle du tableau (colonnes)
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nom");
        model.addColumn("Prénom");
        model.addColumn("Téléphone");

        // Tableau qui utilise ce modèle
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(178, 34, 34)); // Couleur de l'en-tête
        table.getTableHeader().setForeground(Color.WHITE);

        // Ajout du tableau à un scrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Boutons d'action
        JButton ajouter = new JButton("Ajouter");
        JButton modifier = new JButton("Modifier");
        JButton supprimer = new JButton("Supprimer");

        // Style appliqué à chaque bouton
        JButton[] boutons = { ajouter, modifier, supprimer };
        for (JButton btn : boutons) {
            btn.setBackground(new Color(178, 34, 34));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setPreferredSize(new Dimension(120, 35));
        }

        // Action du bouton Ajouter : ouvre une nouvelle fenêtre
        ajouter.addActionListener(e -> new ajoutercontact(this));

        // Action du bouton Modifier : vérifie si une ligne est sélectionnée, puis passe les données à la fenêtre de modification
        modifier.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) model.getValueAt(selectedRow, 0);
                String nom = (String) model.getValueAt(selectedRow, 1);
                String prénom = (String) model.getValueAt(selectedRow, 2);
                String tel = (String) model.getValueAt(selectedRow, 3);
                new modifierContactt(this, id, nom, prénom, tel);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact à modifier.");
            }
        });

        // Action du bouton Supprimer : ouvre la fenêtre de suppression
        supprimer.addActionListener(e -> new supprimercontact(this));

        // Panel pour les boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(new Color(255, 228, 225));
        btnPanel.add(ajouter);
        btnPanel.add(modifier);
        btnPanel.add(supprimer);

        // Ajout du panel des boutons en bas
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // Ajout du panel principal à la fenêtre
        add(mainPanel);

        // Chargement des données depuis la base
        afficherContacts();

        setVisible(true); // Affiche la fenêtre
    }

    // Rafraîchit les données du tableau
    public void rafraichirTableau() {
        afficherContacts();
    }

    // Récupère les données de la base de données et les affiche dans le tableau
    private void afficherContacts() {
        model.setRowCount(0); // Vide le tableau avant de le remplir
        try (Connection conn = connexionDB.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nom, prénom, tel FROM contacts");

            // Parcours des résultats et ajout au tableau
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prénom"),
                    rs.getString("tel")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur de chargement:\n" + e.getMessage(),
                    "Erreur DB",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getters pour accéder à la table et au modèle depuis d'autres classes
    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    // Méthode principale pour lancer directement cette fenêtre
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new contact());
    }
}






