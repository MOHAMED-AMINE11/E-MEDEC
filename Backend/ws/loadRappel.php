<?php
include_once '../racine.php';
include_once RACINE . '/service/RappelService.php';

$rs = new RappelService();  // Création d'une instance du service Rappel
$rappels = $rs->getAllRappels();  // Récupération de tous les rappels
$response = [];  // Tableau pour stocker la réponse

// Parcourir tous les rappels trouvés
foreach ($rappels as $rappel) {
    $response[] = [
        "id" => $rappel->getId(),
        "utilisateur_id" => $rappel->getUtilisateurId(),
        "titre" => $rappel->getTitre(),
        "description" => $rappel->getDescription(),
        "date_heure" => $rappel->getDateHeure(),
        "etat" => $rappel->getEtat(),
        "created_at" => $rappel->getCreatedAt(),
        "medicament" => $rappel->getMedicament()
    ];
}

// Définir l'en-tête de la réponse comme JSON
header('Content-Type: application/json');

// Envoyer la réponse JSON
echo json_encode($response);
?>

