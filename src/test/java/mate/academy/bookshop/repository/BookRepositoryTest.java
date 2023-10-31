package mate.academy.bookshop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by categoryIВ")
    public void getAllBooksByCategoryId() {
        Long categoryId = 7L;
        List<Book> actual = bookRepository.findAllBooksByCategoryId(categoryId);
        assertEquals(2L, actual.size());
    }
}
