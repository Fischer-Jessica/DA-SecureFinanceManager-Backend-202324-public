package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Category;
import at.htlhl.financialoverview.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all categories")
    public List<Category> getCategories() {
        return categoryRepository.getCategories();
    }

    @GetMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one category")
    public Category getCategory(@PathVariable("categoryId") int categoryId) {
        return categoryRepository.getCategory(categoryId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new category")
    public int addCategory(@RequestParam("categoryName") byte[] categoryName,
                           @RequestParam("categoryDescription") byte[] categoryDescription,
                           @RequestParam("categoryColourId") int categoryColourId) {
        return categoryRepository.addCategory(categoryName, categoryDescription, categoryColourId);
    }

    @PatchMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing category")
    public void updateCategory(@PathVariable("categoryId") int categoryId, @RequestBody Category category) {
        categoryRepository.updateCategory(categoryId, category);
    }

    @DeleteMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a category")
    public void deleteCategory(@PathVariable("categoryId") int categoryId, @RequestParam byte[] password) {
        categoryRepository.deleteCategory(categoryId, password);
    }
}
