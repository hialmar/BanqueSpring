package fr.miage.banque.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Opération d'un compte
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
@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String type;

    private Date date;

    private double montant;

    @ManyToOne
    private Compte compte;

    /**
     * pour affichage (attention à ne pas faire de boucle infinie...)
     * @return la chaine à afficher
     */
    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", montant=" + montant +
                '}';
    }
}
