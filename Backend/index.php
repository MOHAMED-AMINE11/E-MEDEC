<!DOCTYPE html>
<?php 
include_once './racine.php'; 
include_once RACINE . '/service/RappelService.php'; 
?> 
<html lang="fr"> 
<head> 
    <meta charset="UTF-8"> 
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter un nouveau rappel</title> 
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ccc;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head> 
<body> 
    <form method="POST" action="controller/addRappel.php"> 
        <fieldset> 
            <legend>Ajouter un nouveau rappel</legend> 
            <table> 
                <tr> 
                    <td><label for="titre">Titre :</label></td> 
                    <td><input type="text" id="titre" name="titre" required /></td> 
                </tr> 
                <tr> 
                    <td><label for="description">Description :</label></td> 
                    <td><textarea id="description" name="description" required></textarea></td> 
                </tr> 
                <tr> 
                    <td><label for="date_heure">Date et Heure :</label></td> 
                    <td><input type="datetime-local" id="date_heure" name="date_heure" required /></td> 
                </tr> 
                <tr> 
                    <td><label for="etat">État :</label></td> 
                    <td> 
                        <select id="etat" name="etat" required> 
                            <option value="en_attente">en_attente</option> 
                            <option value="terminé">terminé</option>
                            <option value="annulé">Annulé</option>
                        </select> 
                    </td> 
                </tr> 
                <tr> 
                    <td><label for="medicament">Médicament :</label></td>
                    <td><input type="text" id="medicament" name="medicament" required /></td>
                </tr> 
                <tr> 
                    <td></td> 
                    <td> 
                        <input type="submit" value="Envoyer" /> 
                        <input type="reset" value="Effacer" /> 
                    </td> 
                </tr> 
            </table> 
        </fieldset> 
    </form> 

    <table> 
        <thead> 
            <tr> 
                <th>ID</th> 
                <th>Titre</th> 
                <th>Description</th> 
                <th>Date et Heure</th> 
                <th>État</th> 
                <th>Médicament</th> 
                <th>Actions</th> 
            </tr> 
        </thead> 
        <tbody> 
            <?php 
            $rs = new RappelService(); 
            foreach ($rs->getAllRappels() as $r): 
                $id = $r->getId();
            ?> 
            <tr> 
                <td><?php echo htmlspecialchars($id ?? 'N/A'); ?></td> 
                <td><?php echo htmlspecialchars($r->getTitre()); ?></td> 
                <td><?php echo htmlspecialchars($r->getDescription()); ?></td> 
                <td><?php echo htmlspecialchars($r->getDateHeure()); ?></td> 
                <td><?php echo htmlspecialchars($r->getEtat()); ?></td> 
                <td><?php echo htmlspecialchars($r->getMedicament()); ?></td> 
                <td>
                    <?php if ($id): ?>
                        <a href="controller/deleteRappel.php?id=<?php echo urlencode($id); ?>">Supprimer</a> 
                        <a href="controller/updateRappel.php?id=<?php echo urlencode($id); ?>">Modifier</a>
                    <?php else: ?>
                        <p>Action indisponible</p>
                    <?php endif; ?>
                </td> 
            </tr> 
            <?php endforeach; ?> 
        </tbody> 
    </table> 
</body> 
</html>
