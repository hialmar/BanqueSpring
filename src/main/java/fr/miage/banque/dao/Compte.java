package fr.miage.banque.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Compte d'un client
 */
// Utilisation de lombok pour générer constructeurs, getters...
// on veut le constructeur avec TOUS les attributs
@AllArgsConstructor
// on veut les setters pour TOUS les attributs
@Setter
// on veut les getters pour TOUS les attributs
@Getter
@Entity
public class Compte {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private double solde;
    private boolean actif;

    // pour éviter une boucle infinie quand on renvoie le client en JSON
    @JsonIgnore
    @ManyToOne
    private Client client;

    public Compte() {
        actif = true;
    }

    /**
     * pour affichage (attention à ne pas faire de boucle infinie...)
     * @return la chaine
     */
    @Override
    public String toString() {
        return "Compte{" +
                "id=" + id +
                ", solde=" + solde +
                ", actif=" + actif +
                '}';
    }
}
