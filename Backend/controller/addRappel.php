<?php
// Activer le rapport d'erreurs pour le débogage
error_reporting(E_ALL);
ini_set('display_errors', 0); // Désactiver l'affichage des erreurs sur le navigateur

include_once '../racine.php';
include_once RACINE . '/service/RappelService.php';

// Définir l'en-tête de la réponse comme JSON
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $rappelService = new RappelService();

    // Récupération des données du formulaire
    $utilisateur_id = 1; // Modifiez ceci selon vos besoins
    $titre = $_POST['titre'] ?? null;
    $description = $_POST['description'] ?? null;
    $date_heure = $_POST['date_heure'] ?? null;
    $etat = $_POST['etat'] ?? null;
    $medicament = $_POST['medicament'] ?? null;

    // Vérifier que les champs obligatoires sont fournis
    if (is_null($utilisateur_id) || is_null($titre) || is_null($description) || is_null($date_heure) || is_null($etat)) {
        echo json_encode([
            "success" => false,
            "message" => "Tous les champs sont requis."
        ]);
        exit();
    }

    // Création de l'objet Rappel
    $rappel = new Rappel(
        null,
        $utilisateur_id,
        $titre,
        $description,
        $date_heure,
        $etat,
        date('Y-m-d H:i:s'), // Date de création actuelle
        $medicament
    );

    try {
        // Appeler la méthode create du RappelService
        $rappelService->create($rappel);

        echo json_encode([
            "success" => true,
            "message" => "Rappel ajouté avec succès.",
            "rappel" => [
                "titre" => $rappel->getTitre(),
                "description" => $rappel->getDescription(),
                "date_heure" => $rappel->getDateHeure(),
                "etat" => $rappel->getEtat(),
                "medicament" => $rappel->getMedicament(),
            ]
        ]);
    } catch (Exception $e) {
        echo json_encode([
            "success" => false,
            "message" => "Erreur lors de l'ajout du rappel: " . $e->getMessage()
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Méthode non autorisée."
    ]);
}
?>
