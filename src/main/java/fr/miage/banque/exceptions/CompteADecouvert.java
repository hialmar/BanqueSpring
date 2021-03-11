package fr.miage.banque.exceptions;

/**
 * Exception générée en cas de risque de découvert
 * Note : on en fait une RuntimeException pour les gérer avec des ExceptionHandler dans les RestController
 */
public class CompteADecouvert extends RuntimeException {
    /**
     * constructeur avec message d'erreur
     * @param s le message d'erreur
     */
    public CompteADecouvert(String s) {
        super(s);
    }
}
