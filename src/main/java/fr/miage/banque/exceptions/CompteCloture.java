package fr.miage.banque.exceptions;

/**
 * Exception générée en cas de compte déjà cloturé
 * Note : on en fait une RuntimeException pour les gérer avec des ExceptionHandler dans les RestController
 */
public class CompteCloture extends RuntimeException {
    /**
     * constructeur avec message d'erreur
     * @param s le message d'erreur
     */
    public CompteCloture(String s) {
        super(s);
    }
}
