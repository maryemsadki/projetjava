package javaproject; // Déclaration du package dans lequel se trouve cette classe

import java.sql.Connection;
import java.sql.DriverManager;

// Classe utilitaire pour établir une connexion avec la base de données MySQL
public class connexionDB {

    // Méthode statique pour obtenir une connexion à la base de données
    public static Connection getConnection() {
        try {
            // Déclaration des informations nécessaires à la connexion
            String url = "jdbc:mysql://localhost:3306/contact"; // URL de la base de données (ici, la base "contact" sur le serveur local)
            String user = "root"; // Nom d'utilisateur MySQL (par défaut "root" pour XAMPP ou WAMP)
            String password = ""; // Mot de passe de l'utilisateur (souvent vide en local)

            // Chargement du driver JDBC de MySQL
            // Cette ligne permet de s'assurer que le driver est bien chargé dans la JVM
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Création et retour de la connexion à la base de données
            // Si tout est correct, cette ligne renvoie un objet Connection qu'on pourra utiliser pour exécuter des requêtes SQL
            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            // En cas d'erreur (driver non trouvé, mauvais identifiants, base inaccessible...), afficher l'erreur dans la console
            e.printStackTrace();

            // Retourner null pour indiquer qu'aucune connexion n'a pu être établie
            return null;
        }
    }
}




