package mate.academy.bookshop.mapper;

import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookshop.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "cartItems", target = "cartItems")
    })
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
