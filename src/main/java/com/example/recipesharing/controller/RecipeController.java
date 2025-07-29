package com.example.recipesharing.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.recipesharing.entity.Recipe;
import com.example.recipesharing.entity.User;
import com.example.recipesharing.security.CustomUserDetails;
import com.example.recipesharing.service.RecipeService;

@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // ✅ 一覧表示
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "index";
    }

    // ✅ 投稿フォーム表示
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "form";
    }

    // ✅ 投稿を保存（POST）＋ログインユーザーと関連付け
    @PostMapping("/new")
    public String createRecipe(@ModelAttribute Recipe recipe,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login"; // 未ログインならログイン画面へ
        }

        User loginUser = userDetails.getUser();
        recipe.setUser(loginUser); // 投稿者をセット
        recipeService.saveRecipe(recipe); // Service経由で保存

        return "redirect:/";
    }

    // ✅ 検索
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        List<Recipe> results = recipeService.searchByTitle(keyword);
        model.addAttribute("recipes", results);
        return "index";
    }

    // ✅ 詳細表示
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        Recipe recipe = recipeService.getRecipeById(id);
        model.addAttribute("recipe", recipe);

        if (userDetails != null) {
            model.addAttribute("loginUsername", userDetails.getUsername());
        }

        return "view";
    }
    // 編集フォーム表示（GET）
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Recipe recipe = recipeService.getRecipeById(id);

        if (userDetails == null || recipe == null ||
            !recipe.getUser().getId().equals(userDetails.getUser().getId())) {
            return "redirect:/";
        }

        model.addAttribute("recipe", recipe);
        return "form";
    }

    // 編集保存（POST）
    @PostMapping("/edit/{id}")
    public String updateRecipe(@PathVariable Long id, @ModelAttribute Recipe updatedRecipe,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Recipe existing = recipeService.getRecipeById(id);

        if (userDetails == null || existing == null ||
            !existing.getUser().getId().equals(userDetails.getUser().getId())) {
            return "redirect:/";
        }

        existing.setTitle(updatedRecipe.getTitle());
        existing.setRating(updatedRecipe.getRating());
        existing.setIngredients(updatedRecipe.getIngredients());
        existing.setInstructions(updatedRecipe.getInstructions());

        recipeService.saveRecipe(existing); // 更新

        return "redirect:/view/" + id;
    }

    // 削除（POST）
 // ✅ 削除（GET）※安全性確認付き
    @GetMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable Long id,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        Recipe recipe = recipeService.getRecipeById(id);

        if (userDetails == null || recipe == null || 
            !recipe.getUser().getId().equals(userDetails.getUser().getId())) {
            return "redirect:/"; // 不正アクセスは一覧へ
        }

        recipeService.deleteRecipe(id); // ← サービス側のメソッド名と合わせました！
        return "redirect:/"; // 削除後は一覧表示へ
    }
}
