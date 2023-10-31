package mate.academy.bookshop.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import mate.academy.bookshop.dto.category.CategoryDto;
import mate.academy.bookshop.dto.category.CreateCategoryRequestDto;
import mate.academy.bookshop.service.category.CategoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category")
    public void createCategory_WithValidData_Success() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("my new category number1");
        requestDto.setDescription("description number1");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName(requestDto.getName());
        categoryDto.setDescription(requestDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);

        EqualsBuilder.reflectionEquals(categoryDto, actual);
    }

    @WithMockUser(username = "test", password = "test", roles = {"USER", "ADMIN"})
    @Test
    @DisplayName("Get all available categories")
    public void getAllCategories_ShouldReturnAllCategories_Success() throws Exception {
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(categoryService.getById(1L));
        expected.add(categoryService.getById(2L));
        expected.add(categoryService.getById(3L));
        expected.add(categoryService.getById(4L));
        expected.add(categoryService.getById(5L));
        expected.add(categoryService.getById(6L));
        expected.add(categoryService.getById(7L));
        expected.add(categoryService.getById(8L));

        MvcResult mvcResult =
                mockMvc.perform(get("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        List<CategoryDto> actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<>() {
                    });
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "test", password = "test", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category")
    public void updateCategory_WithValidCategoryDto_Success() throws Exception {
        CategoryDto savedCategory = categoryService.getById(1L);
        savedCategory.setName("Updated category name");

        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto();
        categoryRequestDto.setName(savedCategory.getName());

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        mockMvc.perform(put("/api/categories/{id}", savedCategory.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(equalTo("Updated category name"))));
    }
}
