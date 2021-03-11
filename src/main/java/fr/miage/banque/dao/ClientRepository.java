package fr.miage.banque.dao;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository pour les clients
 */
public interface ClientRepository extends CrudRepository<Client, Long> {
    /**
     * permet de chercher un client en donnant son nom et son prénom (sensé être unique)
     * @param nom le nom
     * @param prenom le prénom
     * @return le client ou null
     */
    public Client findClientByNomAndPrenom(String nom, String prenom);
}
