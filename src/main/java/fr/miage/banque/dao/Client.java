package fr.miage.banque.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Client de la banque
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
public class Client {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String nom;
    private String prenom;
    // ajout du eager pour les tests, à virer en production
    // ne pas oublier le mappedBy
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<Compte> comptes;

    /**
     * pour affichage (attention à ne pas faire de boucle infinie...)
     * @return la chaine d'affichage
     */
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", comptes=" + comptes +
                '}';
    }
}
