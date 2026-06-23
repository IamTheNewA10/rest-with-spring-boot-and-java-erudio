package br.com.het.unittests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.het.data.dto.BookDTO;
import br.com.het.exception.RequiredObjectIsNullException;
import br.com.het.mocks.MockBook;
import br.com.het.model.Book;
import br.com.het.repository.BookRepository;
import br.com.het.service.BookService;

public class BookServiceTest {

        MockBook input;

        @InjectMocks
        private BookService service;

        @Mock
        BookRepository repository;

        @BeforeEach
        void setUp() {
                input = new MockBook();
                MockitoAnnotations.openMocks(this);
        }

        @Test
        void testFindById() {
                Book book = input.mockEntity(1);
                book.setId(1L);
                when(repository.findById(1L)).thenReturn(Optional.of(book));
                var result = service.findById(1L);

                // verfica se o id e os links não são nulos
                assertNotNull(result);
                assertNotNull(result.getId());
                assertNotNull(result.getLinks());

                // verfica se os links hateoas estao batendo
                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("self")
                                                && link.getHref().endsWith("/api/book/v1/1")
                                                && link.getType().equals("GET")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("findAll")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("GET")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("create")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("POST")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("update")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("PUT")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("delete")
                                                && link.getHref().endsWith("/api/book/v1/1")
                                                && link.getType().equals("DELETE")));

                // verfica se o metodo esta retornando os campos corretamente
                assertEquals("Some Author1", result.getAuthor());
                assertEquals(25D, result.getPrice());
                assertEquals("Some Title1", result.getTitle());
                assertNotNull(result.getLaunchDate());
        }

        @Test
        void testCreate() {
                Book book = input.mockEntity(1);
                Book persisted = book;
                persisted.setId(1L);

                BookDTO dto = input.mockDTO(1);

                when(repository.save(book)).thenReturn(persisted);
                var result = service.create(dto);

                // verfica se o id e os links não são nulos
                assertNotNull(result);
                assertNotNull(result.getId());
                assertNotNull(result.getLinks());

                // verfica se os links hateoas estao batendo
                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("self")
                                                && link.getHref().endsWith("/api/book/v1/1")
                                                && link.getType().equals("GET")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("findAll")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("GET")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("create")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("POST")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("update")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("PUT")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("delete")
                                                && link.getHref().endsWith("/api/book/v1/1")
                                                && link.getType().equals("DELETE")));

                // verfica se o metodo esta retornando os campos corretamente
                assertEquals("Some Author1", result.getAuthor());
                assertEquals(25D, result.getPrice());
                assertEquals("Some Title1", result.getTitle());
                assertNotNull(result.getLaunchDate());
        }

        @Test
        void testCreateWithNullBook() {
                Exception exception = assertThrows(RequiredObjectIsNullException.class,
                                () -> {
                                        service.create(null);
                                });

                String expectedMessage = "It is not allowed to persist a null object!";
                String actualMessage = exception.getMessage();

                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void testUpdate() {
                Book book = input.mockEntity(1);
                Book persisted = book;
                persisted.setId(1L);

                BookDTO dto = input.mockDTO(1);

                when(repository.findById(1L)).thenReturn(Optional.of(book));
                when(repository.save(book)).thenReturn(persisted);
                var result = service.create(dto);

                // verfica se o id e os links não são nulos
                assertNotNull(result);
                assertNotNull(result.getId());
                assertNotNull(result.getLinks());

                // verfica se os links hateoas estao batendo
                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("self")
                                                && link.getHref().endsWith("/api/book/v1/1")
                                                && link.getType().equals("GET")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("findAll")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("GET")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("create")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("POST")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("update")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("PUT")));

                assertNotNull(result.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("delete")
                                                && link.getHref().endsWith("/api/book/v1/1")
                                                && link.getType().equals("DELETE")));

                // verfica se o metodo esta retornando os campos corretamente
                assertEquals("Some Author1", result.getAuthor());
                assertEquals(25D, result.getPrice());
                assertEquals("Some Title1", result.getTitle());
                assertNotNull(result.getLaunchDate());
        }

        @Test
        void testUpdateWithNullBook() {
                Exception exception = assertThrows(RequiredObjectIsNullException.class,
                                () -> {
                                        service.update(null);
                                });

                String expectedMessage = "It is not allowed to persist a null object!";
                String actualMessage = exception.getMessage();

                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void testDelete() {
                Book book = input.mockEntity();
                book.setId(1L);
                when(repository.findById(1L)).thenReturn(Optional.of(book));
                service.delete(1L);
                verify(repository, times(1)).findById(1L);
                verify(repository, times(1)).delete(any(Book.class));
        }

        @Test
        @Disabled("REASON: Still Under Development")
        void testFindAll() {
                List<Book> list = input.mockEntityList();
                when(repository.findAll()).thenReturn(list);
                List<BookDTO> books = new ArrayList<>();// service.findAll();

                assertNotNull(books);
                assertEquals(14, books.size());

                var bookOne = books.get(1);

                // verfica se o id e os links não são nulos
                assertNotNull(bookOne);
                assertNotNull(bookOne.getId());
                assertNotNull(bookOne.getLinks());

                // verfica se os links hateoas estao batendo
                assertNotNull(bookOne.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("self")
                                                && link.getHref().endsWith("/api/book/v1/1")
                                                && link.getType().equals("GET")));

                assertNotNull(bookOne.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("findAll")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("GET")));

                assertNotNull(bookOne.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("create")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("POST")));

                assertNotNull(bookOne.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("update")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("PUT")));

                assertNotNull(bookOne.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("delete")
                                                && link.getHref().endsWith("/api/book/v1/1")
                                                && link.getType().equals("DELETE")));

                // verfica se o metodo esta retornando os campos corretamente
                assertEquals("Some Author1", bookOne.getAuthor());
                assertEquals(25D, bookOne.getPrice());
                assertEquals("Some Title1", bookOne.getTitle());
                assertNotNull(bookOne.getLaunchDate());

                var bookFive = books.get(5);

                // verfica se o id e os links não são nulos
                assertNotNull(bookFive);
                assertNotNull(bookFive.getId());
                assertNotNull(bookFive.getLinks());

                // verfica se os links hateoas estao batendo
                assertNotNull(bookFive.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("self")
                                                && link.getHref().endsWith("/api/book/v1/5")
                                                && link.getType().equals("GET")));

                assertNotNull(bookFive.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("findAll")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("GET")));

                assertNotNull(bookFive.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("create")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("POST")));

                assertNotNull(bookFive.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("update")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("PUT")));

                assertNotNull(bookFive.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("delete")
                                                && link.getHref().endsWith("/api/book/v1/5")
                                                && link.getType().equals("DELETE")));

                // verfica se o metodo esta retornando os campos corretamente
                assertEquals("Some Author5", bookFive.getAuthor());
                assertEquals(25D, bookFive.getPrice());
                assertEquals("Some Title5", bookFive.getTitle());
                assertNotNull(bookFive.getLaunchDate());

                var bookNine = books.get(9);

                // verfica se o id e os links não são nulos
                assertNotNull(bookNine);
                assertNotNull(bookNine.getId());
                assertNotNull(bookNine.getLinks());

                // verfica se os links hateoas estao batendo
                assertNotNull(bookNine.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("self")
                                                && link.getHref().endsWith("/api/book/v1/9")
                                                && link.getType().equals("GET")));

                assertNotNull(bookNine.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("findAll")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("GET")));

                assertNotNull(bookNine.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("create")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("POST")));

                assertNotNull(bookNine.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("update")
                                                && link.getHref().endsWith("/api/book/v1")
                                                && link.getType().equals("PUT")));

                assertNotNull(bookNine.getLinks().stream()
                                .anyMatch(link -> link.getRel().value().equals("delete")
                                                && link.getHref().endsWith("/api/book/v1/9")
                                                && link.getType().equals("DELETE")));

                // verfica se o metodo esta retornando os campos corretamente
                assertEquals("Some Author9", bookNine.getAuthor());
                assertEquals(25D, bookNine.getPrice());
                assertEquals("Some Title9", bookNine.getTitle());
                assertNotNull(bookNine.getLaunchDate());

        }

}
