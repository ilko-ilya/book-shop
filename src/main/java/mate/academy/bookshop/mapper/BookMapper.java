package mate.academy.bookshop.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.BookDto;
import mate.academy.bookshop.dto.BookDtoWithoutCategoryIds;
import mate.academy.bookshop.dto.CreateBookRequestDto;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> longList = book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(longList);
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
