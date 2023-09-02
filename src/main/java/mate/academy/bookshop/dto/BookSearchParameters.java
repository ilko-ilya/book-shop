package mate.academy.bookshop.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookSearchParameters {
    private String[] titles;
    private String[] authors;
}
