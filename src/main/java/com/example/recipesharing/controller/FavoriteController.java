package com.example.recipesharing.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.recipesharing.entity.Favorite;
import com.example.recipesharing.entity.Recipe;
import com.example.recipesharing.entity.User;
import com.example.recipesharing.repository.FavoriteRepository;
import com.example.recipesharing.repository.RecipeRepository;
import com.example.recipesharing.service.UserService;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;
    private final UserService userService;

    public FavoriteController(FavoriteRepository favoriteRepository,
                              RecipeRepository recipeRepository,
                              UserService userService) {
        this.favoriteRepository = favoriteRepository;
        this.recipeRepository = recipeRepository;
        this.userService = userService;
    }

    @PostMapping("/add/{id}")
    public String addFavorite(@PathVariable Long id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Recipe recipe = recipeRepository.findById(id).orElseThrow();

        if (!favoriteRepository.existsByUserAndRecipe(user, recipe)) {
            Favorite fav = new Favorite();
            fav.setUser(user);
            fav.setRecipe(recipe);
            favoriteRepository.save(fav);
        }
        return "redirect:/view/" + id;
    }

    @GetMapping("/list")
    public String showFavorites(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Favorite> favorites = favoriteRepository.findByUser(user);
        model.addAttribute("favorites", favorites);
        return "favorites"; // ← templates/favorites.html を表示
    }
}