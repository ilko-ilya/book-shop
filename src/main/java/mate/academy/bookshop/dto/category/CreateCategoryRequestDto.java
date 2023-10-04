package mate.academy.bookshop.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotNull
    private String name;
    private String description;
}
