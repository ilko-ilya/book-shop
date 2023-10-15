package mate.academy.bookshop.mapper;

import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.order.CreateOrderItemRequestDto;
import mate.academy.bookshop.dto.order.OrderItemDto;
import mate.academy.bookshop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    OrderItem toModel(CreateOrderItemRequestDto requestDto);
}
