package mate.academy.bookshop.mapper;

import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.cartitem.CartItemDto;
import mate.academy.bookshop.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookshop.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "book", ignore = true)
    CartItem toEntity(CreateCartItemRequestDto requestDto);

    @Mapping(target = "book", ignore = true)
    CartItem toEntity(CartItemUpdateDto updateDto);

    @Mappings({
            @Mapping(source = "book.id", target = "bookId"),
            @Mapping(source = "book.title", target = "bookTitle")
    })
    CartItemDto toDto(CartItem cartItem);

    @AfterMapping
    default void setBook(@MappingTarget CartItem cartItem,
                         CreateCartItemRequestDto requestDto) {
        Book book = new Book();
        book.setId(requestDto.getBookId());
        cartItem.setBook(book);
    }
}
