package fr.miage.banque.metier;

import fr.miage.banque.dao.Client;
import fr.miage.banque.dao.ClientRepository;
import fr.miage.banque.dao.Compte;
import fr.miage.banque.exceptions.ClientExistant;
import fr.miage.banque.exceptions.ClientInexistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Traitements métiers pour les clients.
 */
@Controller
@Transactional
public class ClientController {
    @Autowired
    ClientRepository clientRepository;

    /**
     * Création d'un client
     * @param nom le nom du client
     * @param prenom son prénom
     * @return l'id du client
     */
    public long creerClient(String nom, String prenom) {
        // on vérifie que le client n'existe pas déjà
        Client c = clientRepository.findClientByNomAndPrenom(nom,prenom);
        if(c != null) {
            throw new ClientExistant("Le client de nom "+nom+" et de prénom "+prenom+" existe déjà");
        }
        // on le crée
        c = new Client();
        c.setNom(nom);
        c.setPrenom(prenom);
        // on le rend persistant
        c = clientRepository.save(c);
        // on retourne son id
        return c.getId();
    }

    /**
     * Récupération d'un client à partir de son id
     * @param idClient l'id du client
     * @return le client ou exception
     */
    public Client getClientById(long idClient) {
        // testons si le client existe
        Optional<Client> optionalClient = clientRepository.findById(idClient);
        if(!optionalClient.isPresent()) {
            throw new ClientInexistant("Le client d'id "+idClient+" n'existe pas.");
        }
        return optionalClient.get();
    }

    /**
     * Récupération d'un client à partir de ses nom et prénom
     * @param nom le nom du client
     * @param prenom le prénom
     * @return le client ou exception
     */
    public Client getClientByNomPrenom(String nom, String prenom) {
        Client c = clientRepository.findClientByNomAndPrenom(nom,prenom);
        if(c == null) {
            throw new ClientInexistant("Le client de nom "+nom+" et de prénom "+prenom+" n'existe pas.");
        }
        return c;
    }

    /**
     * Retourne la liste des id des comptes d'un client
     * @param idClient l'id du client
     * @return la liste ou exception
     */
    public List<Long> getListeIdComptes(long idClient) {
        // on crée une liste de longs
        List<Long> liste = new ArrayList<>();
        Client client = getClientById(idClient);
        // on y rec
        for(Compte c : client.getComptes()) {
            liste.add(c.getId());
        }
        return liste;
    }
}
