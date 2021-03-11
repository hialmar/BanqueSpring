package fr.miage.banque.exposition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Infos pour la création d'un client (nom et prénom)
 */
// Utilisation de lombok pour générer constructeurs, getters...
// on veut le constructeur avec TOUS les attributs
@AllArgsConstructor
// on veut le constructeur SANS argument
@NoArgsConstructor
// on veut les setters pour TOUS les attributs
@Setter
// on veut les getters pour TOUS les attributs
@Getter
public class NomPrenom {
    private String nom;
    private String prenom;
}
