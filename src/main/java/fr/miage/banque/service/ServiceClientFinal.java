package fr.miage.banque.service;

import fr.miage.banque.dao.Client;
import fr.miage.banque.exposition.Position;
import fr.miage.banque.metier.ClientController;
import fr.miage.banque.metier.CompteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Façade pour le client final
 */
@Service
public class ServiceClientFinal {
    @Autowired
    CompteController compteController;
    @Autowired
    ClientController clientController;

    /**
     * Virement de compte à compte
     * @param id1 id du compte sur lequel on débite la somme
     * @param id2 id du compte sur lequel on crédite la somme
     * @param somme la somme
     */
    @Transactional
    public void virementCompteACompte(long id1, long id2, double somme) {
        compteController.debiterCompte(id1, somme);
        compteController.crediterCompte(id2, somme);
    }

    /**
     * Retourne la position d'un compte
     * @param id id du compte
     * @return la position
     */
    public Position consulterPosition(long id) {
        return compteController.consulterPosition(id);
    }

    /**
     * Retourne un client
     * @param id id du client
     * @return le client
     */
    public Client getClientById(Long id) {
        return clientController.getClientById(id);
    }

    /**
     * Récupèration de la liste d'id de comptes d'un client
     * @param id id du client
     * @return la liste d'id de comptes
     */
    public Iterable<Long> getListeIdComptes(Long id) {
        return clientController.getListeIdComptes(id);
    }

}
