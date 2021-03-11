package fr.miage.banque.dao;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository pour les comptes
 */
public interface CompteRepository extends CrudRepository<Compte, Long> {
}
