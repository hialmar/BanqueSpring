package fr.miage.banque.exceptions;

/**
 * Exception générée en cas de client existant déjà
 * Note : on en fait une RuntimeException pour les gérer avec des ExceptionHandler dans les RestController
 */
public class ClientExistant extends RuntimeException {
    /**
     * constructeur avec message d'erreur
     * @param s le message d'erreur
     */
    public ClientExistant(String s) {
        super(s);
    }
}
