package com.example.cachewithredisdemo.repository;

import com.example.cachewithredisdemo.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
