package br.com.het.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.het.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Person p set p.enabled = false where p.id =:id")
  void disablePerson(@Param("id") Long id);

  @Query("SELECT p From Person p where p.firstName LIKE LOWER(CONCAT('%',:firstName,'%'))")
  Page<Person> findPeopleByName(@Param("firstName") String firstName, Pageable pageable);
}
