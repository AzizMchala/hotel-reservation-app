<?php
include '../config/config.php'; // Inclure le fichier de configuration avec les paramètres de la DB

header("Content-Type: application/json"); // Définit le type de contenu comme JSON

// Préparation de la requête SQL pour récupérer tous les hôtels, y compris les chemins des images
$query = $conn->prepare("SELECT id_hotel, name, location, description, price, image_path FROM hotels");
$query->execute();

// Récupérer les résultats de la requête
$hotels = $query->fetchAll(PDO::FETCH_ASSOC);

// Vérifier si des hôtels ont été trouvés
if ($hotels) {
    // Parcourir les hôtels et ajouter l'URL complète de l'image
    foreach ($hotels as &$hotel) {
        // Vérifier si l'image_path est présent et valide
        if ($hotel['image_path']) {
            // Si l'image existe, construire l'URL complète de l'image
            $hotel['image'] = 'http://192.168.1.11/booking/' . $hotel['image_path'];
        } else {
            // Si pas d'image, fournir une image par défaut
            $hotel['image'] = 'http://192.168.1.11/booking/photo/default.jpg'; // Image par défaut
        }
    }

    // Retourner la réponse JSON avec les informations des hôtels et des images sans échapper les barres obliques
    echo json_encode([
        "success" => true,
        "message" => "Hôtels récupérés avec succès",
        "data" => $hotels
    ], JSON_UNESCAPED_SLASHES);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Aucun hôtel trouvé"
    ], JSON_UNESCAPED_SLASHES);
}
?>
