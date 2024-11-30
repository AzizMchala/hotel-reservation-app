<?php
include '../config/config.php'; // Inclure le fichier de configuration avec les paramètres de la DB

header("Content-Type: application/json"); // Définit le type de contenu comme JSON

// Vérifier si la requête est POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Récupérer les données envoyées dans le corps de la requête
    $input = json_decode(file_get_contents('php://input'), true);

    // Vérifier si toutes les données nécessaires sont présentes
    if (
        isset($input['id_user']) &&
        isset($input['id_hotel']) &&
        isset($input['check_in_date']) &&
        isset($input['check_out_date']) &&
        isset($input['total_price'])
    ) {
        $id_user = $input['id_user'];
        $id_hotel = $input['id_hotel'];
        $check_in_date = $input['check_in_date'];
        $check_out_date = $input['check_out_date'];
        $total_price = $input['total_price'];

        try {
            // Préparer l'insertion dans la table 'reservation'
            $query = $conn->prepare("
                INSERT INTO reservation (id_user, id_hotel, check_in_date, check_out_date, total_price) 
                VALUES (:id_user, :id_hotel, :check_in_date, :check_out_date, :total_price)
            ");

            // Exécuter la requête avec les données fournies
            $query->execute([
                ':id_user' => $id_user,
                ':id_hotel' => $id_hotel,
                ':check_in_date' => $check_in_date,
                ':check_out_date' => $check_out_date,
                ':total_price' => $total_price
            ]);

            // Vérifier si l'insertion a réussi
            if ($query->rowCount() > 0) {
                echo json_encode([
                    "success" => true,
                    "message" => "Réservation ajoutée avec succès"
                ], JSON_UNESCAPED_SLASHES);
            } else {
                echo json_encode([
                    "success" => false,
                    "message" => "Échec de l'ajout de la réservation"
                ], JSON_UNESCAPED_SLASHES);
            }
        } catch (PDOException $e) {
            // Gérer les erreurs de la base de données
            echo json_encode([
                "success" => false,
                "message" => "Erreur : " . $e->getMessage()
            ], JSON_UNESCAPED_SLASHES);
        }
    } else {
        // Les données nécessaires ne sont pas présentes
        echo json_encode([
            "success" => false,
            "message" => "Données de réservation manquantes"
        ], JSON_UNESCAPED_SLASHES);
    }
} else {
    // Si la requête n'est pas POST
    echo json_encode([
        "success" => false,
        "message" => "Méthode non autorisée"
    ], JSON_UNESCAPED_SLASHES);
}
?>
