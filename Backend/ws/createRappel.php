<?php 
include_once '../racine.php'; 
include_once RACINE . '/service/RappelService.php'; 

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $rappelService = new RappelService();

    // Vérifier que les champs obligatoires sont fournis
    if (!isset($_POST['utilisateur_id'], $_POST['titre'], $_POST['description'], 
              $_POST['date_heure'], $_POST['etat'], $_POST['created_at'], $_POST['medicament'])) {
        header('Content-type: application/json');
        echo json_encode([
            "status" => "error", 
            "message" => "Tous les champs sont requis"
        ]);
        exit();
    }

    try {
        // Créer un nouvel objet Rappel avec les données fournies
        $rappel = new Rappel(
            null,  // ID sera généré automatiquement
            $_POST['utilisateur_id'], 
            $_POST['titre'], 
            $_POST['description'], 
            $_POST['date_heure'], 
            $_POST['etat'], 
            $_POST['created_at'], 
            $_POST['medicament']
        );

        // Ajouter le rappel via le service
        $rappelService->create($rappel);

        // Réponse JSON en cas de succès
        header('Content-type: application/json');
        echo json_encode([
            "status" => "success", 
            "message" => "Rappel ajouté avec succès"
        ]);

    } catch (Exception $e) {
        // Réponse JSON en cas d'erreur
        header('Content-type: application/json');
        echo json_encode([
            "status" => "error", 
            "message" => $e->getMessage()
        ]);
    }
}
?>
