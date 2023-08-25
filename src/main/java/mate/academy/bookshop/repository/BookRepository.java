package mate.academy.bookshop.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.bookshop.model.Book;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> getBookById(Long id);

    List<Book> getAll();

}
