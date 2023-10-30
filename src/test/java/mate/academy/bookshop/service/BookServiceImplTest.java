package mate.academy.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookshop.dto.BookDto;
import mate.academy.bookshop.dto.CreateBookRequestDto;
import mate.academy.bookshop.mapper.BookMapper;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.model.Category;
import mate.academy.bookshop.repository.book.BookRepository;
import mate.academy.bookshop.repository.category.CategoryRepository;
import mate.academy.bookshop.service.book.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    private static final Long NEGATIVE_BOOK_ID = -10L;
    private static final Long NOT_EXISTING_BOOK_ID = 10L;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;
    private Book book1;
    private Book book2;
    private CreateBookRequestDto requestDto;
    private BookDto bookDto;
    private BookDto bookDto2;

    @BeforeEach
    public void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Comedy");

        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("War of the worlds");
        book1.setAuthor("Bill Gates");
        book1.setIsbn("123456789-01");
        book1.setPrice(BigDecimal.valueOf(39.99));
        book1.setCategories(Set.of(category));

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Rich dad, poor dad");
        book2.setAuthor("Jacky Chan");
        book2.setIsbn("123456789-02");
        book2.setCategories(Set.of(category));
        book2.setPrice(BigDecimal.valueOf(35.55));

        requestDto = new CreateBookRequestDto();
        requestDto.setTitle(book1.getTitle());
        requestDto.setAuthor(book1.getAuthor());
        requestDto.setIsbn(book1.getIsbn());
        requestDto.setCategoryIds(Set.of(1L));
        requestDto.setPrice(book1.getPrice());

        bookDto = new BookDto();
        bookDto.setId(book1.getId());
        bookDto.setTitle(book1.getTitle());
        bookDto.setAuthor(book1.getAuthor());
        bookDto.setIsbn(book1.getIsbn());
        bookDto.setCategoryIds(Set.of(1L));
        bookDto.setPrice(book1.getPrice());

        bookDto2 = new BookDto();
        bookDto2.setId(book2.getId());
        bookDto2.setTitle(book2.getTitle());
        bookDto2.setAuthor(book2.getAuthor());
        bookDto2.setIsbn(book2.getIsbn());
        bookDto2.setCategoryIds(Set.of(1L));
        bookDto2.setPrice(book2.getPrice());
    }

    @Test
    @DisplayName("Verify the correct book was returned when book exists")
    public void getBookById_WithValidId_ShouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        BookDto expectedDto = bookDto;

        when(bookMapper.toDto(book1)).thenReturn(expectedDto);

        BookDto actual = bookService.getBookById(1L);
        assertEquals(expectedDto, actual);
    }

    @Test
    @DisplayName("Verify that an exception is thrown a negative ID is passed")
    public void getBook_WithNegativeBookId_ShouldThrowException() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.getBookById(NEGATIVE_BOOK_ID));

        assertEquals("Can't find book by id: " + NEGATIVE_BOOK_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Verify that book with such ID doesn't exist")
    public void getBook_WithNonExistingId_ShouldThrowException() {
        when(bookRepository.findById(NOT_EXISTING_BOOK_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.getBookById(NOT_EXISTING_BOOK_ID));

        assertEquals("Can't find book by id: " + NOT_EXISTING_BOOK_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Find all books")
    public void getAllBooks_ValidPageable_ShouldReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        when(bookMapper.toDto(book1)).thenReturn(bookDto);
        when(bookMapper.toDto(book2)).thenReturn(bookDto2);

        List<BookDto> bookDtos = bookService.getAll(pageable);

        assertFalse(bookDtos.isEmpty());
        assertEquals(2, bookDtos.size());
        assertEquals(bookDto, bookDtos.get(0));
        assertEquals(bookDto2, bookDtos.get(1));
    }

    @Test
    @DisplayName("Save a book")
    public void saveBook_WithValidBook_ShouldReturnSavedBook() {
        when(bookMapper.toModel(requestDto)).thenReturn(book1);
        when(bookRepository.save(book1)).thenReturn(book1);
        when(bookMapper.toDto(book1)).thenReturn(bookDto);

        BookDto actual = bookService.save(requestDto);
        Assertions.assertEquals(bookDto, actual);
    }
}
