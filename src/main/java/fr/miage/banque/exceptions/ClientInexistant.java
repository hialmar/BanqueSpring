package fr.miage.banque.exceptions;

/**
 * Exception générée en cas de client n'existant pas
 * Note : on en fait une RuntimeException pour les gérer avec des ExceptionHandler dans les RestController
 */
public class ClientInexistant extends RuntimeException {
    /**
     * constructeur avec message d'erreur
     * @param s le message d'erreur
     */
    public ClientInexistant(String s) {
        super(s);
    }
}
