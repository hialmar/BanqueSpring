package fr.miage.banque.service;

import fr.miage.banque.dao.Client;
import fr.miage.banque.exposition.Position;
import fr.miage.banque.metier.ClientController;
import fr.miage.banque.metier.CompteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Façade pour le collaborateur (expose toutes les méthodes)
 */
@Service
public class ServiceCollaborateur {
    @Autowired
    CompteController compteController;

    @Autowired
    ClientController clientController;

    /**
     * Création d'un client
     * @param nom son nom
     * @param prenom son prénom
     * @return l'id
     */
    public long creerClient(String nom, String prenom) {
        return clientController.creerClient(nom, prenom);
    }

    /**
     * Retourne un client à partir de son id
     * @param idClient son id
     * @return le client
     */
    public Client getClientById(long idClient) {
        return clientController.getClientById(idClient);
    }

    /**
     * Retourne un client à partir de son nom et de son prénom
     * @param nom
     * @param prenom
     * @return le client
     */
    public Client getClientByNomPrenom(String nom, String prenom) {
        return clientController.getClientByNomPrenom(nom, prenom);
    }

    /**
     * Retourne la liste des ids de comptes d'un client
     * @param idClient l'id du client
     * @return la liste
     */
    public List<Long> getListeIdComptes(long idClient) {
        return clientController.getListeIdComptes(idClient);
    }

    /**
     * Ouverture de compte pour un client
     * @param idClient id du client
     * @param somme la somme initiale
     * @return id du compte
     */
    public long ouvrirCompte(long idClient, double somme) {
        return compteController.ouvrirCompte(idClient, somme);
    }

    /**
     * Opération de crédit
     * @param id l'id du compte
     * @param somme la somme
     */
    public void crediterCompte(long id, double somme) {
        compteController.crediterCompte(id, somme);
    }

    /**
     * Opération de débit
     * @param id l'id du compte
     * @param somme la somme
     */
    public void debiterCompte(long id, double somme) {
        compteController.debiterCompte(id, somme);
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
     * Fermeture de compte
     * @param id id du compte
     */
    public void cloturerCompte(long id) {
        compteController.cloturerCompte(id);
    }

    /**
     * Virement de compte à compte
     * @param id1 id du compte débiteur
     * @param id2 id du compte créditeur
     * @param somme la somme
     */
    @Transactional
    public void virementCompteACompte(long id1, long id2, double somme) {
        compteController.debiterCompte(id1, somme);
        compteController.crediterCompte(id2, somme);
    }
}
