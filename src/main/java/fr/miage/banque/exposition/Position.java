package fr.miage.banque.exposition;

import fr.miage.banque.dao.Operation;
import lombok.*;

import java.util.Date;

/**
 * Poistion d'un compte : id, solde, actif ou inactif, date de consultation et liste d'opérations
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
@ToString
public class Position {
    private long id;
    private double solde;
    private boolean actif;
    private Date date;
    private Iterable<Operation> operations;
}
