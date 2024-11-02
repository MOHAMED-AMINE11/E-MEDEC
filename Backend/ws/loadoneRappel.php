<?php
include_once '../racine.php';
include_once RACINE . '/service/RappelService.php';

// Vérifier si un ID est passé dans la requête GET
if (isset($_GET['id'])) {
    $id = $_GET['id'];
    $rs = new RappelService();  // Instance du service Rappel

    // Recherche du rappel par son ID
    $rappel = $rs->findById($id);

    if ($rappel) {
        // Préparation de la réponse
        $response = [
            "id" => $rappel->getId(),
            "utilisateur_id" => $rappel->getUtilisateurId(),
            "titre" => $rappel->getTitre(),
            "description" => $rappel->getDescription(),
            "date_heure" => $rappel->getDateHeure(),
            "etat" => $rappel->getEtat(),
            "created_at" => $rappel->getCreatedAt(),
            "medicament" => $rappel->getMedicament()
        ];

        // Envoi de la réponse JSON
        header('Content-Type: application/json');
        echo json_encode($response);
    } else {
        // Si le rappel n'est pas trouvé
        http_response_code(404);
        echo json_encode(["message" => "Rappel non trouvé."]);
    }
} else {
    // Si l'ID n'est pas fourni
    http_response_code(400);
    echo json_encode(["message" => "ID manquant."]);
}
?>