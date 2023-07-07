package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Category;
import at.htlhl.financialoverview.model.User;
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
    public List<Category> getCategories(@RequestBody User loggedInUser) {
        return categoryRepository.getCategories(loggedInUser);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one category")
    public Category getCategory(@RequestParam int categoryId, @RequestBody User loggedInUser) {
        return categoryRepository.getCategory(categoryId, loggedInUser);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new category")
    public int addCategory(@RequestParam byte[] categoryName,
                           @RequestParam byte[] categoryDescription,
                           @RequestParam int categoryColourId,
                           @RequestBody User loggedInUser) {
        return categoryRepository.addCategory(categoryName, categoryDescription, categoryColourId, loggedInUser);
    }

    @PatchMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing category")
    public void updateCategory(@RequestBody Category updatedCategory, @RequestBody User loggedInUser) {
        categoryRepository.updateCategory(updatedCategory, loggedInUser);
    }

    @PatchMapping("/categories/categoryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing category")
    public void updateCategoryName(@RequestParam int categoryId, @RequestParam byte[] updatedCategoryName, @RequestBody User loggedInUser) {
        categoryRepository.updateCategoryName(categoryId, updatedCategoryName, loggedInUser);
    }

    @PatchMapping("/categories/categoryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the Description of an existing category")
    public void updateCategoryDescription(@RequestParam int categoryId, @RequestParam byte[] updatedCategoryDescription, @RequestBody User loggedInUser) {
        categoryRepository.updateCategoryDescription(categoryId, updatedCategoryDescription, loggedInUser);
    }

    @PatchMapping("/categories/categoryColourId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing category")
    public void updateCategoryColourId(@RequestParam int categoryId, @RequestParam int updatedCategoryColourId, @RequestBody User loggedInUser) {
        categoryRepository.updateCategoryColourId(categoryId, updatedCategoryColourId, loggedInUser);
    }

    @DeleteMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a category")
    public void deleteCategory(@RequestParam int categoryId, @RequestBody User loggedInUser) {
        categoryRepository.deleteCategory(categoryId, loggedInUser);
    }
}
