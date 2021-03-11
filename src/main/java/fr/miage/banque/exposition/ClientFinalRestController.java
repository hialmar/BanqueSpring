package fr.miage.banque.exposition;

import fr.miage.banque.dao.Client;
import fr.miage.banque.dao.Compte;
import fr.miage.banque.exceptions.CompteADecouvert;
import fr.miage.banque.exceptions.CompteCloture;
import fr.miage.banque.exceptions.CompteInexistant;
import fr.miage.banque.service.ServiceClientFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * API REST pour le client final
 */
@RestController
@RequestMapping("/api_client")
public class ClientFinalRestController {
    @Autowired
    ServiceClientFinal serviceClientFinal;

    /**
     * Récupération du client en fonction de son id
     * @param c le client (en fait son id, transformé en client automatiquement)
     * @return le client
     */
    @GetMapping("/clients/{idClient}")
    public Client getClient(@PathVariable("idClient") Client c) {
        return serviceClientFinal.getClientById(c.getId());
    }

    /**
     * Renvoie la liste des id des comptes d'un client donné
     * @param c le client (en fait son id, transformé en client automatiquement)
     * @return la liste d'id
     */
    @GetMapping("/clients/{idClient}/comptes")
    public Iterable<Long> getComptesClient(@PathVariable("idClient") Client c) {
        Iterable<Long> comptes = serviceClientFinal.getListeIdComptes(c.getId());
        return comptes;
    }

    /**
     * Renvoie la position d'un compte
     * @param c le compte (en fait son id
     * @return
     */
    @GetMapping("/comptes/{id}")
    public Position getById(@PathVariable("id") Compte c) {
        return serviceClientFinal.consulterPosition(c.getId());
    }

    /**
     * Opération de crédit ou de débit
     * @param c le compte (en fait son id)
     * @param virement détails de l'opération
     * @return
     */
    @PutMapping("/comptes/{id}/virement")
    public Position debiterOuCrediterCompte(@PathVariable("id") Compte c, @RequestBody Virement virement) {
        serviceClientFinal.virementCompteACompte(c.getId(), virement.getIdCompteDestinataire(), virement.getSomme());
        return serviceClientFinal.consulterPosition(c.getId());
    }

    /**
     * Gestionnaire d'exception qui gère les erreurs d'id de compte ou de client
     * @param req infos sur la requête HTTP
     * @param ex l'exception
     * @return une erreur HTTP
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErreurRest> gereCompteInexistant(HttpServletRequest req, MissingPathVariableException ex) {
        return new ResponseEntity<ErreurRest>(new ErreurRest("Compte ou Client inexistant"), HttpStatus.NOT_FOUND);
    }

    /**
     * Gestionnaire d'exception qui gère les erreurs métiers
     * @param req infos sur la requête HTTP
     * @param ex l'exception
     * @return une erreur HTTP
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErreurRest> gereRuntimeException(HttpServletRequest req, RuntimeException ex) {
        if(ex instanceof CompteADecouvert) {
            return new ResponseEntity<ErreurRest>(new ErreurRest(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } else if (ex instanceof CompteInexistant) {
            return new ResponseEntity<ErreurRest>(new ErreurRest(ex.getMessage()), HttpStatus.NOT_FOUND);
        } else if (ex instanceof CompteCloture) {
            return new ResponseEntity<ErreurRest>(new ErreurRest(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<ErreurRest>(new ErreurRest(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
