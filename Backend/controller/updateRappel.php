<?php
include_once '../racine.php';
include_once RACINE . '/service/RappelService.php';


// Masquer les erreurs PHP pour éviter toute sortie indésirable
error_reporting(0);
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents("php://input"), true);

    if (isset($data['id'])) {
        $id = $data['id'];
        $rs = new RappelService();
        $rappel = $rs->findById($id);

        if ($rappel) {
            // Mise à jour des champs si fournis
            if (isset($data['utilisateur_id'])) {
                $rappel->setUtilisateurId($data['utilisateur_id']);
            }
            if (isset($data['titre'])) {
                $rappel->setTitre($data['titre']);
            }
            if (isset($data['description'])) {
                $rappel->setDescription($data['description']);
            }
            if (isset($data['date_heure'])) {
                $rappel->setDateHeure($data['date_heure']);
            }
            if (isset($data['etat'])) {
                $rappel->setEtat($data['etat']);
            }
            if (isset($data['medicament'])) {
                $rappel->setMedicament($data['medicament']);
            }

            try {
                $rs->update($rappel);
                echo json_encode(["success" => true, "message" => "Mise à jour réussie."]);
            } catch (Exception $e) {
                echo json_encode(["success" => false, "message" => "Erreur lors de la mise à jour : " . $e->getMessage()]);
            }
        } else {
            echo json_encode(["success" => false, "message" => "Rappel introuvable."]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "ID manquant."]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Méthode non autorisée."]);
}
?>
