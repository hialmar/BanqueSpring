package fr.miage.banque.metier;

import fr.miage.banque.dao.*;
import fr.miage.banque.exceptions.ClientInexistant;
import fr.miage.banque.exceptions.CompteADecouvert;
import fr.miage.banque.exceptions.CompteCloture;
import fr.miage.banque.exceptions.CompteInexistant;
import fr.miage.banque.exposition.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Traitements métiers pour les comptes.
 */
@Controller
@Transactional
public class CompteController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CompteRepository compteRepository;
    @Autowired
    OperationRepository operationRepository;

    /**
     * ouverture d'un compte
     * @param idClient l'id du client
     * @param valeur la somme placée sur le compte à l'ouverture
     * @return l'id du compte
     */
    public long ouvrirCompte(long idClient, double valeur) {
        // on cherche le client
        Optional<Client> opt = clientRepository.findById(idClient);
        if(!opt.isPresent()) {
            throw new ClientInexistant("Le client d'id " + idClient + " n'existe pas.");
        }
        Client cli = opt.get();
        // création du compte
        Compte c = new Compte();
        c.setClient(cli);
        c.setSolde(valeur);
        // création en base de donnée
        c = compteRepository.save(c);
        // on crée une opération correspondant à l'ouverture du compte
        creerOperation(valeur, c, "ouverture");
        // on retourne l'id
        return c.getId();
    }

    /**
     * Création d'une opération
     * @param valeur somme
     * @param c le compte
     * @param type type d'opération
     */
    private void creerOperation(double valeur, Compte c, String type) {
        // création de l'opération
        Operation o = new Operation();
        o.setCompte(c);
        o.setType(type);
        o.setDate(new Date());
        o.setMontant(valeur);
        // ajout en base de donnée
        operationRepository.save(o);
    }

    /**
     * Crédite le compte
     * @param id l'id du compte
     * @param somme la somme à créditer
     */
    public void crediterCompte(long id, double somme) {
        // on récupère le compte
        Compte c = getCompte(id);
        if (!c.isActif()) {
            throw new CompteCloture("Le compte d'id "+id+" est cloturé.");
        }
        // on modifie le solde
        c.setSolde(c.getSolde()+somme);
        // on crée une opération correspondante
        creerOperation(somme, c, "credit");
    }

    /**
     * Récupère un compte à partir de son id
     * @param id l'id du compte
     * @return le compte ou une exception
     */
    private Compte getCompte(long id) {
        // tentative de récupérer le compte
        Optional<Compte> opt = compteRepository.findById(id);
        if (!opt.isPresent()) {
            throw new CompteInexistant("Le compte d'id "+ id +" n'existe pas");
        }
        Compte c = opt.get();
        return c;
    }

    /**
     * Débite le compte d'une somme
     * @param id l'id du compte
     * @param somme la sommeà débiter
     */
    public void debiterCompte(long id, double somme) {
        // tente de récupérer le compte
        Compte c = getCompte(id);
        if (!c.isActif()) {
            throw new CompteCloture("Le compte d'id "+id+" est cloturé.");
        }
        // modifie le solde
        c.setSolde(c.getSolde()-somme);
        if(c.getSolde()<0) {
            // Le fait de faire un throw annule l'opération précédente à cause de la transaction
            // si on voulait quand même faire l'opération il faudrait commit la transaction avant le throw
            // cf. https://stackoverflow.com/questions/24338150/how-to-manually-force-a-commit-in-a-transactional-method
            throw new CompteADecouvert("Le compte d'id "+id+" serait à découvert.");
        }
        // création d'une opération
        creerOperation(somme, c, "debit");
    }

    /**
     * Récupère la position (solde, date et liste d'opération) d'un compte
     * @param id l'id du compte
     * @return la position
     */
    public Position consulterPosition(long id) {
        Compte c = getCompte(id);
        Position p = new Position(c.getId(),c.getSolde(),c.isActif(),new Date(), operationRepository.getOperationsByCompte(c));
        return p;
    }

    /**
     * Ferme un compte
     * @param id id du compte
     */
    public void cloturerCompte(long id) {
        // tentative de récupération du compte
        Compte c = getCompte(id);
        if (!c.isActif()) {
            throw new CompteCloture("Le compte d'id "+id+" est cloturé.");
        }
        // on le ferme
        c.setActif(false);
        // on crée une opération correspondante
        creerOperation(0,c,"cloture");
    }

    /**
     * Pour les tests, retourne tous les comptes de la banque
     * @return
     */
    public Iterable<Compte> getAllComptes() {
        return compteRepository.findAll();
    }
}
