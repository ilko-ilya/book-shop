package mate.academy.bookshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mate.academy.bookshop.dto.BookDto;
import mate.academy.bookshop.dto.CreateBookRequestDto;
import mate.academy.bookshop.service.book.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book")
    public void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Lucky Gilmore");
        requestDto.setAuthor("Mike Tyson");
        requestDto.setPrice(BigDecimal.valueOf(10.99));
        requestDto.setIsbn("123456789-04");
        requestDto.setCoverImage("my_image");
        requestDto.setDescription("Description");
        requestDto.setCategoryIds(Set.of(1L));

        BookDto expected = new BookDto();
        expected.setId(7L);
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setPrice(requestDto.getPrice());
        expected.setIsbn(requestDto.getIsbn());
        expected.setCoverImage(requestDto.getCoverImage());
        expected.setDescription(requestDto.getDescription());
        expected.setCategoryIds(requestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected, actual, "id");
    }

    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    @DisplayName("Get all books")
    @Test
    void getAll_ShouldReturnAllBooks_Success() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(bookService.getBookById(1L));
        expected.add(bookService.getBookById(2L));
        expected.add(bookService.getBookById(3L));

        Pageable pageable = PageRequest.of(0, 3);
        MvcResult mvcResult =
                mockMvc.perform(get("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(
                                pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                ).andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<BookDto> actual =
                objectMapper.readValue(jsonResponse, new TypeReference<>() {
                });
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Update a book")
    public void updateBook_WithValidDate_Success() throws Exception {
        BookDto expected = bookService.getBookById(1L);
        expected.setTitle("New title");
        expected.setAuthor("New author");

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Lucky Gilmore");
        requestDto.setAuthor("Mike Tyson");
        requestDto.setPrice(BigDecimal.valueOf(10.99));
        requestDto.setIsbn("123456789-05");
        requestDto.setCoverImage("my_image");
        requestDto.setDescription("Description");
        requestDto.setCategoryIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/api/books/{id}", expected.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
