package br.com.het.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.het.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
