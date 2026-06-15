package br.com.het.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.het.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
