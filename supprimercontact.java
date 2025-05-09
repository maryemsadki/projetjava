package javaproject;

import javax.swing.*;
import java.sql.*;

// Classe responsable de la suppression d’un contact sélectionné
public class supprimercontact {

    // Constructeur appelé lorsqu'on veut supprimer un contact
    public supprimercontact(contact parent) {

        // Récupération du tableau affichant les contacts depuis la fenêtre principale
        JTable table = parent.getTable();

        // Vérifie si une ligne (contact) est sélectionnée
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            // Récupère l’ID du contact sélectionné (colonne 0)
            int id = (int) parent.getModel().getValueAt(selectedRow, 0);

            // Récupère le nom du contact sélectionné (colonne 1) pour l’affichage
            String nom = (String) parent.getModel().getValueAt(selectedRow, 1);

            // Affiche une boîte de dialogue de confirmation
            int confirm = JOptionPane.showConfirmDialog(parent,
                    "Voulez-vous vraiment supprimer le contact \"" + nom + "\" ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION);

            // Si l'utilisateur clique sur "Oui"
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connexionDB.getConnection()) {
                    // Préparation de la requête SQL pour supprimer le contact
                    String sql = "DELETE FROM contacts WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, id); // Remplace le ? par l’ID du contact
                    stmt.executeUpdate(); // Exécute la suppression

                    // Message de succès
                    JOptionPane.showMessageDialog(parent, "Contact supprimé avec succès !");
                    
                    // Rafraîchit le tableau pour afficher la liste mise à jour
                    parent.rafraichirTableau();

                } catch (SQLException ex) {
                    // En cas d’erreur SQL, affiche un message d’erreur
                    JOptionPane.showMessageDialog(parent,
                            "Erreur lors de la suppression : " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } else {
            // Si aucun contact n’est sélectionné
            JOptionPane.showMessageDialog(parent, "Veuillez sélectionner un contact à supprimer.");
        }
    }
}




