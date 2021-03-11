package fr.miage.banque;

import fr.miage.banque.dao.Client;
import fr.miage.banque.dao.Compte;
import fr.miage.banque.exceptions.ClientExistant;
import fr.miage.banque.exceptions.CompteADecouvert;
import fr.miage.banque.exceptions.CompteCloture;
import fr.miage.banque.exceptions.CompteInexistant;
import fr.miage.banque.metier.ClientController;
import fr.miage.banque.metier.CompteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

/**
 * L'application
 */
@SpringBootApplication
public class BanqueApplication {

    /**
     * Loggeur
     */
    private static final Logger log = LoggerFactory.getLogger(BanqueApplication.class);

    /**
     * Le main
     * @param args arguments du programme
     */
    public static void main(String[] args) {
        SpringApplication.run(BanqueApplication.class, args);
    }

    /**
     * CommandLineRunner pour tester les méthodes métier
     * Note : on aurait aussi pu faire des tests unitaires
     * @param clientController traitements métiers sur les clients
     * @param compteController traitements métiers sur les comptes
     * @return
     */
    @Bean
    @Transactional
    public CommandLineRunner banqueStarter(ClientController clientController, CompteController compteController) {
        return (args) -> {

            long idClient;
            long id1, id2;
            try {
                idClient = clientController.creerClient("Dupond", "Jean");
                id1 = compteController.ouvrirCompte(idClient, 1000);
                id2 = compteController.ouvrirCompte(idClient, 1000);
                long id3 = compteController.ouvrirCompte(idClient, 1000);
                long id4 = compteController.ouvrirCompte(idClient,1000);
            } catch (ClientExistant ex) {
                log.info("client déjà présent");
                Client c = clientController.getClientByNomPrenom("Dupond", "Jean");
                idClient = c.getId();
                id1 = c.getComptes().get(0).getId();
                id2 = c.getComptes().get(0).getId();
            }


            // fetch all Comptes
            log.info("Comptes found with findAll():");
            log.info("-------------------------------");
            for (Compte c : compteController.getAllComptes()) {
                log.info(c.toString());
            }
            log.info("");

            log.info(clientController.getClientById(idClient).toString());

            log.info(clientController.getListeIdComptes(idClient).toString());

            log.info(compteController.consulterPosition(id1).toString());
            try {
                compteController.crediterCompte(id1, 100);
            } catch (CompteCloture e) {
                log.info("Cloturé");
            }
            log.info(compteController.consulterPosition(id1).toString());
            log.info(compteController.consulterPosition(id2).toString());
            try {
                compteController.debiterCompte(id2, 100);
            } catch (CompteADecouvert e) {
                log.info("A découvert");
            } catch (CompteCloture e) {
                log.info("Cloturé");
            }
            log.info(compteController.consulterPosition(id2).toString());
            try {
                compteController.debiterCompte(1000, 100);
            } catch (CompteInexistant e) {
                log.info("bien inexistant");
            }
            try {
                compteController.debiterCompte(id1, 100000);
            } catch (CompteADecouvert e) {
                log.info("bien à découvert");
            } catch (CompteCloture e) {
                log.info("Cloturé");
            }

            try {
                compteController.cloturerCompte(id1);
                compteController.debiterCompte(id1, 100);
            } catch (CompteCloture e) {
                log.info("bien cloturé");
            }

            Client c = clientController.getClientById(idClient);
            log.info(c.toString());
        };
    }
}
