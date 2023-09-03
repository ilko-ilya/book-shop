package mate.academy.bookshop.service;

import java.util.List;
import mate.academy.bookshop.dto.BookDto;
import mate.academy.bookshop.dto.BookSearchParameters;
import mate.academy.bookshop.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> getAll();

    BookDto updateBookById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters);
}
