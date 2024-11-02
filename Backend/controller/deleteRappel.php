<?php
include_once '../racine.php';
include_once RACINE . '/service/RappelService.php';

// Vérifier si l'ID du rappel est fourni
if (isset($_GET['id'])) {
    $id = $_GET['id'];
    $rs = new RappelService();

    // Recherche du rappel par ID
    $rappel = $rs->findById($id);

    if ($rappel) {
        // Suppression du rappel trouvé
        $rs->delete($rappel);
        echo json_encode(["success" => true, "message" => "Rappel supprimé avec succès."]);
    } else {
        // Si le rappel n'est pas trouvé
        echo json_encode(["success" => false, "message" => "Rappel introuvable."]);
    }
} else {
    // Si l'ID n'est pas fourni dans la requête
    echo json_encode(["success" => false, "message" => "ID manquant."]);
}
?>