package mate.academy.bookshop.service.book;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.BookDto;
import mate.academy.bookshop.dto.BookDtoWithoutCategoryIds;
import mate.academy.bookshop.dto.BookSearchParameters;
import mate.academy.bookshop.dto.CreateBookRequestDto;
import mate.academy.bookshop.exception.EntityNotFoundException;
import mate.academy.bookshop.mapper.BookMapper;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.model.Category;
import mate.academy.bookshop.repository.book.BookRepository;
import mate.academy.bookshop.repository.book.BookSpecificationBuilder;
import mate.academy.bookshop.repository.category.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Set<Category> categorySet = categoryRepository
                .findCategoriesByIdIn(requestDto.getCategoryIds());
        Book book = bookMapper.toModel(requestDto);
        book.setCategories(categorySet);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto requestDto) {
        Set<Category> categorySet = categoryRepository
                .findCategoriesByIdIn(requestDto.getCategoryIds());
        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        book.setCategories(categorySet);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getAllBooksByCategoryId(Long categoryId) {
        return bookRepository.findAllBooksByCategoryId(categoryId)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
