package fr.miage.banque.exposition;

import lombok.*;

/**
 * Infos pour l'ouverture d'un compte (la somme initiale)
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
public class OuvertureCompte {
    private double somme;
}
