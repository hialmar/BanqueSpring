package fr.miage.banque.exposition;

import fr.miage.banque.dao.Client;
import fr.miage.banque.dao.Compte;
import fr.miage.banque.exceptions.CompteADecouvert;
import fr.miage.banque.exceptions.CompteCloture;
import fr.miage.banque.exceptions.CompteInexistant;
import fr.miage.banque.service.ServiceCollaborateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * API REST pour le collaborateur
 */
@RestController
@RequestMapping("/api")
public class CollaborateurRestController {
    @Autowired
    ServiceCollaborateur serviceCollaborateur;

    /**
     * création d'un client
     * @param nomPrenom infos du client
     * @return le client
     */
    @PostMapping("/clients/")
    public Client creerClient(@RequestBody NomPrenom nomPrenom) {
        long id = serviceCollaborateur.creerClient(nomPrenom.getNom(), nomPrenom.getPrenom());
        return serviceCollaborateur.getClientById(id);
    }

    /**
     * Retourne un client
     * @param c le client (en fait son id)
     * @return le client
     */
    @GetMapping("/clients/{idClient}")
    public Client getClient(@PathVariable("idClient") Client c) {
        return c;
    }

    /**
     * Retourne la liste des ids de comptes d'un client
     * @param c le client (en fait son id)
     * @return la liste des ids de comptes
     */
    @GetMapping("/clients/{idClient}/comptes")
    public Iterable<Long> getComptesClient(@PathVariable("idClient") Client c) {
        Iterable<Long> comptes = serviceCollaborateur.getListeIdComptes(c.getId());
        return comptes;
    }

    /**
     * Retourne la postion d'un compte
     * @param c le compte (en fait son id)
     * @return la position
     */
    @GetMapping("/comptes/{id}")
    public Position getById(@PathVariable("id") Compte c) {
       return serviceCollaborateur.consulterPosition(c.getId());
    }

    /**
     * Ouverture d'un compte
     * @param c le client (en fait son id)
     * @param ouvertureCompte infos pour le compte (somme)
     * @return la position du nouveau compte
     */
    @PostMapping("/clients/{idClient}/comptes")
    public Position ouvrirCompte(@PathVariable("idClient") Client c, @RequestBody OuvertureCompte ouvertureCompte) {
        long id = serviceCollaborateur.ouvrirCompte(c.getId(), ouvertureCompte.getSomme());
        return serviceCollaborateur.consulterPosition(id);
    }

    /**
     * Opération de débit ou de crédit
     * @param c le compte (en fait son id)
     * @param creditDebit infos de l'opération
     * @return la position du compte après l'opération
     */
    @PutMapping("/comptes/{id}")
    public Position debiterOuCrediterCompte(@PathVariable("id") Compte c, @RequestBody CreditDebit creditDebit) {
        if(creditDebit.isCredit()) {
            serviceCollaborateur.crediterCompte(c.getId(), creditDebit.getSomme());
            return serviceCollaborateur.consulterPosition(c.getId());
        } else {
            serviceCollaborateur.debiterCompte(c.getId(), creditDebit.getSomme());
            return serviceCollaborateur.consulterPosition(c.getId());
        }
    }

    /**
     * Fermeture d'un compte
     * @param c le compte (en fait son id)
     * @return la position après fermeture
     */
    @DeleteMapping("/comptes/{id}")
    public Position cloturerCompte(@PathVariable("id") Compte c) {
        serviceCollaborateur.cloturerCompte(c.getId());
        return serviceCollaborateur.consulterPosition(c.getId());
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
            return new ResponseEntity<ErreurRest>(new ErreurRest(ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
