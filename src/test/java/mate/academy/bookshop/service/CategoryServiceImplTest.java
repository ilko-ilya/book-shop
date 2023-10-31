package mate.academy.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.bookshop.dto.category.CategoryDto;
import mate.academy.bookshop.dto.category.CreateCategoryRequestDto;
import mate.academy.bookshop.exception.EntityNotFoundException;
import mate.academy.bookshop.mapper.CategoryMapper;
import mate.academy.bookshop.model.Category;
import mate.academy.bookshop.repository.category.CategoryRepository;
import mate.academy.bookshop.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category category1;
    private Category category2;
    private CategoryDto categoryDto1;
    private CategoryDto categoryDto2;
    private CreateCategoryRequestDto requestDto;

    @BeforeEach
    public void setUp() {
        category1 = new Category();
        category1.setId(1L);
        category1.setName("Fantasy");
        category1.setDescription("Unreal movies");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Criminal");
        category2.setDescription("Really dangerous movies");

        categoryDto1 = new CategoryDto();
        categoryDto1.setId(category1.getId());
        categoryDto1.setName(category1.getName());
        categoryDto1.setDescription(category1.getDescription());

        categoryDto2 = new CategoryDto();
        categoryDto2.setId(category2.getId());
        categoryDto2.setName(category2.getName());
        categoryDto2.setDescription(category2.getDescription());

        requestDto = new CreateCategoryRequestDto();
        requestDto.setName(category1.getName());
        requestDto.setDescription(category1.getDescription());
    }

    @Test
    @DisplayName("Save valid category with valid requestDto")
    public void saveCategory_WithValidData_ShouldReturnSavedCategory() {
        when(categoryMapper.toEntity(requestDto)).thenReturn(category1);
        when(categoryRepository.save(category1)).thenReturn(category1);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        CategoryDto actual = categoryService.save(requestDto);
        assertEquals(categoryDto1, actual);
    }

    @Test
    @DisplayName("Verify list of categories is returned")
    public void getAllCategories_ValidPageable_ShouldReturnListOfCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categoryList = List.of(category1, category2);
        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, categoryList.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        List<CategoryDto> categoryDtoList = categoryService.findAll(pageable);

        assertFalse(categoryDtoList.isEmpty());
        assertEquals(2, categoryDtoList.size());
        assertEquals(categoryDto1, categoryDtoList.get(0));
        assertEquals(categoryDto2, categoryDtoList.get(1));
    }

    @Test
    @DisplayName("Verify valid category by ID")
    public void getCategoryById_WithValidId_ShouldReturnCategory() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category1));
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        CategoryDto actual = categoryService.getById(categoryId);
        assertEquals(categoryDto1, actual);
    }

    @Test
    @DisplayName("Verify delete category by valid ID")
    public void deleteCategoryById_WithValidId_ShouldDeleteCategory() {
        Long categoryId = 1L;
        categoryService.deleteById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    @DisplayName("Verify than an exception is thrown due to a non-existent ID")
    public void getCategory_WithNotExistingId_ShouldThrowException() {
        Long categoryId = 5L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(categoryId));
    }
}
