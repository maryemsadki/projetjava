package javaproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// Classe principale représentant la fenêtre de connexion/inscription
public class Login extends JFrame {

    // Champs de saisie pour l'utilisateur et le mot de passe
    private JTextField utilisateur;
    private JPasswordField password;

    // Boutons pour se connecter ou s'inscrire
    private JButton boutonConnexion, boutonInscription;

    // Constructeur : crée et affiche l'interface graphique
    public Login() {
        setTitle("Connexion/Inscription"); // Titre de la fenêtre
        setSize(350, 300); // Taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme l'application à la fermeture de la fenêtre
        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran

        // Création d'un panel principal avec une disposition verticale
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 228, 225)); // Couleur de fond rose clair
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Marges autour du contenu

        // Titre de l'interface
        JLabel titre = new JLabel("Authentification");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setForeground(new Color(139, 0, 0)); // Couleur rouge foncé
        panel.add(titre);
        panel.add(Box.createVerticalStrut(20)); // Espace vertical

        // Champ "Utilisateur"
        JLabel labelUser = new JLabel("Utilisateur :");
        utilisateur = new JTextField(); // Champ texte
        panel.add(labelUser);
        panel.add(Box.createVerticalStrut(5));
        panel.add(utilisateur);
        panel.add(Box.createVerticalStrut(15));

        // Champ "Mot de passe"
        JLabel labelPass = new JLabel("Mot de passe :");
        password = new JPasswordField(); // Champ de mot de passe (masqué)
        panel.add(labelPass);
        panel.add(Box.createVerticalStrut(5));
        panel.add(password);
        panel.add(Box.createVerticalStrut(20));

        // Bouton de connexion
        boutonConnexion = new JButton("Connexion");
        boutonConnexion.setBackground(new Color(178, 34, 34)); // Rouge bordeaux
        boutonConnexion.setForeground(Color.WHITE);
        boutonConnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(boutonConnexion);

        // Bouton d'inscription
        boutonInscription = new JButton("S'inscrire");
        boutonInscription.setBackground(new Color(70, 130, 180)); // Bleu acier
        boutonInscription.setForeground(Color.WHITE);
        boutonInscription.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(15));
        panel.add(boutonInscription);

        // Définition des actions associées aux boutons
        boutonConnexion.addActionListener(e -> handleConnexion());
        boutonInscription.addActionListener(e -> handleInscription());

        // Ajout du panel à la fenêtre et affichage
        add(panel);
        setVisible(true);
    }

    // Méthode appelée lors du clic sur "Connexion"
    private void handleConnexion() {
        String user = utilisateur.getText().trim();
        String mdp = new String(password.getPassword()).trim(); // Conversion sécurisée du mot de passe

        // Vérifie que les champs ne sont pas vides
        if (user.isEmpty() || mdp.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifie si les identifiants sont valides
        if (validateCredentials(user, mdp)) {
            dispose(); // Ferme la fenêtre actuelle
            JOptionPane.showMessageDialog(null, "Connexion réussie !");
            new contact(); // Lance la fenêtre principale de l'application
        }
    }

    // Méthode appelée lors du clic sur "S'inscrire"
    private void handleInscription() {
        String user = utilisateur.getText().trim();
        String mdp = new String(password.getPassword()).trim();

        // Vérifie que les champs ne sont pas vides
        if (user.isEmpty() || mdp.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifie si l'utilisateur existe déjà
        if (utilisateurExiste(user)) {
            JOptionPane.showMessageDialog(null, "Cet utilisateur existe déjà !");
            dispose();
            new contact(); // Ouvre quand même la page principale (ce comportement est discutable)
        } else {
            // Si l'utilisateur n'existe pas, on l'ajoute à la base
            if (insererUtilisateur(user, mdp)) {
                JOptionPane.showMessageDialog(null, "Inscription réussie !");
                dispose();
                new contact(); // Lancement de la fenêtre principale après inscription
            }
        }
    }

    // Vérifie dans la base de données si les identifiants sont corrects
    private boolean validateCredentials(String username, String password) {
        try (Connection conn = connexionDB.getConnection()) {
            String sql = "SELECT * FROM login WHERE user = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeQuery().next(); // Renvoie true si un résultat est trouvé
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur de base de données : " + ex.getMessage());
            return false;
        }
    }

    // Vérifie si un utilisateur existe déjà (inscription)
    private boolean utilisateurExiste(String username) {
        try (Connection conn = connexionDB.getConnection()) {
            String sql = "SELECT * FROM login WHERE user = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur de vérification : " + ex.getMessage());
            return false;
        }
    }

    // Insère un nouvel utilisateur dans la base de données
    private boolean insererUtilisateur(String username, String password) {
        try (Connection conn = connexionDB.getConnection()) {
            String sql = "INSERT INTO login (user, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            int result = stmt.executeUpdate(); // Retourne 1 si l'insertion a réussi
            return result > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur d'inscription : " + ex.getMessage());
            return false;
        }
    }

    // Point d'entrée du programme
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login(); // Crée une instance de la fenêtre Login
        });
    }
}
