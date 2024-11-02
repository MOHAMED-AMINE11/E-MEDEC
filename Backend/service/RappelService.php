<?php
include_once 'C:\Users\LENOVO$\Desktop\Backend/racine.php'; 
include_once RACINE . '/classes/Rappel.php';
include_once RACINE . '/connexion/Connexion.php';
include_once RACINE . '/dao/IDao.php';

class RappelService implements IDao {
    private $connexion;

    public function __construct() {
        $this->connexion = new Connexion();
    }

    // Méthode create adaptée pour accepter un objet $o
    public function create($o) {
        // Utiliser :utilisateur_id dans la requête SQL
        $query = "INSERT INTO rappels (utilisateur_id, titre, description, date_heure, etat, created_at, medicament) 
                  VALUES (:utilisateur_id, :titre, :description, :date_heure, :etat, :created_at, :medicament);";
        $req = $this->connexion->getConnexion()->prepare($query);
    
        // Stocker la date de création actuelle
        $created_at = date('Y-m-d H:i:s');
    
        // Lier les paramètres à partir de l'objet $o
        $req->bindParam(':utilisateur_id', $o->getUtilisateurId());
        $req->bindParam(':titre', $o->getTitre());
        $req->bindParam(':description', $o->getDescription());
        $req->bindParam(':date_heure', $o->getDateHeure());
        $req->bindParam(':etat', $o->getEtat());
        $req->bindParam(':created_at', $created_at);
        $req->bindParam(':medicament', $o->getMedicament());
    
        // Exécuter la requête
        $req->execute() or die('Erreur SQL lors de l\'ajout du rappel.');
    }

    public function delete($o) {
        $query = "DELETE FROM rappels WHERE id = :id";
        $req = $this->connexion->getConnexion()->prepare($query);

        // Lier l'ID de l'objet $o
        $req->bindParam(':id', $o->getId(), PDO::PARAM_INT);

        $req->execute() or die('Erreur SQL lors de la suppression du rappel.');
    }

    public function update($o) {
        try{
        $query = "UPDATE rappels SET titre = :titre, description = :description, 
                  date_heure = :date_heure, etat = :etat, medicament = :medicament 
                  WHERE id = :id";
        $req = $this->connexion->getConnexion()->prepare($query);

        // Lier les paramètres à partir de l'objet $o
        $req->bindParam(':titre', $o->getTitre());
        $req->bindParam(':description', $o->getDescription());
        $req->bindParam(':date_heure', $o->getDateHeure());
        $req->bindParam(':etat', $o->getEtat());
        $req->bindParam(':medicament', $o->getMedicament());
        $req->bindParam(':id', $o->getId(), PDO::PARAM_INT);

        $req->execute() or die('Erreur SQL lors de la mise à jour du rappel.');
      
    }catch (PDOException $e) {
            die('Erreur SQL : ' . $e->getMessage());
        }
    }

    public function getAllRappels() {
        $rappels = array();
        $query = "SELECT * FROM rappels";
        $req = $this->connexion->getConnexion()->prepare($query);
        $req->execute();

        while ($r = $req->fetch(PDO::FETCH_OBJ)) {
            $rappels[] = new Rappel(
                $r->id,
                $r->utilisateur_id,
                $r->titre,
                $r->description,
                $r->date_heure,
                $r->etat,
                $r->created_at,
                $r->medicament
            );
        }
        return $rappels;
    }

    public function findById($id) {
        $query = "SELECT * FROM rappels WHERE id = :id";
        $req = $this->connexion->getConnexion()->prepare($query);
        $req->bindParam(':id', $id, PDO::PARAM_INT);
        $req->execute();

        if ($r = $req->fetch(PDO::FETCH_OBJ)) {
            return new Rappel(
                $r->id,
                $r->utilisateur_id,
                $r->titre,
                $r->description,
                $r->date_heure,
                $r->etat,
                $r->created_at,
                $r->medicament
            );
        }
        return null; // Si aucun rappel trouvé
    }
}
?>

