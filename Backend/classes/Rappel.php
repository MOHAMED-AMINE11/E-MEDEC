<?php
class Rappel {
    private $id;
    private $utilisateur_id;
    private $titre;
    private $description;
    private $date_heure;
    private $etat;
    private $created_at;
    private $medicament;

    // Constructeur avec des paramètres optionnels pour certains attributs
    public function __construct($id = null, $utilisateur_id, $titre, $description, $date_heure, $etat, $created_at, $medicament) {
        $this->id = $id;
        $this->utilisateur_id = $utilisateur_id;
        $this->titre = $titre;
        $this->description = $description;
        $this->date_heure = $date_heure;
        $this->etat = $etat;
        $this->created_at = $created_at;
        $this->medicament = $medicament;
    }

    // Getters et Setters pour chaque propriété
    public function getId() {
        return $this->id;
    }

    public function setId($id) {
        $this->id = $id;
    }

    public function getUtilisateurId() {
        return $this->utilisateur_id;
    }

    public function setUtilisateurId($utilisateur_id) {
        $this->utilisateur_id = $utilisateur_id;
    }

    public function getTitre() {
        return $this->titre;
    }

    public function setTitre($titre) {
        $this->titre = $titre;
    }

    public function getDescription() {
        return $this->description;
    }

    public function setDescription($description) {
        $this->description = $description;
    }

    public function getDateHeure() {
        return $this->date_heure;
    }

    public function setDateHeure($date_heure) {
        $this->date_heure = $date_heure;
    }

    public function getEtat() {
        return $this->etat;
    }

    public function setEtat($etat) {
        $this->etat = $etat;
    }

    public function getCreatedAt() {
        return $this->created_at;
    }

    public function setCreatedAt($created_at) {
        $this->created_at = $created_at;
    }

    public function getMedicament() {
        return $this->medicament;
    }

    public function setMedicament($medicament) {
        $this->medicament = $medicament;
    }
}
?>
