package com.example.recipesharing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.recipesharing.entity.Favorite;
import com.example.recipesharing.entity.Recipe;
import com.example.recipesharing.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    boolean existsByUserAndRecipe(User user, Recipe recipe);
    
    Favorite findByUserAndRecipe(User user, Recipe recipe);
}