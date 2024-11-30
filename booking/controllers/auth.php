<?php
include '../config/config.php'; // Inclure le fichier de configuration

header("Content-Type: application/json"); // Définit le type de contenu comme JSON

// Récupérer le corps JSON de la requête
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, true);

$email = $input['email'] ?? null;
$password = $input['password'] ?? null;

// Vérifier si l'email et le mot de passe sont présents
if (empty($email) || empty($password)) {
    echo json_encode(["success" => false, "message" => "Email ou mot de passe manquant"]);
    exit;
}

// Préparation de la requête SQL pour vérifier l'utilisateur
$query = $conn->prepare("SELECT id_user, name, email FROM users WHERE email = :email AND password = :password");
$query->execute(['email' => $email, 'password' => $password]);
$user = $query->fetch(PDO::FETCH_ASSOC);

// Vérification et réponse
if ($user) {
    echo json_encode(["success" => true, "message" => "Connexion réussie", "data" => $user]);
} else {
    echo json_encode(["success" => false, "message" => "Email ou mot de passe incorrect"]);
}
?>
