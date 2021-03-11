package fr.miage.banque.dao;

import org.springframework.data.repository.CrudRepository;

/**
 * Répository pour les opérations
 */
public interface OperationRepository extends CrudRepository<Operation,Long> {
    /**
     * Renvoie toutes les opérations d'un compte
     * Juste pour l'exemple (on aurait pu faire avec un lien OneToMany)
     * @param c le compte
     * @return les opérations ou une liste vide
     */
    Iterable<Operation> getOperationsByCompte(Compte c);
}
