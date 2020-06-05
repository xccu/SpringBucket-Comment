package com.example.redisrepositorydemo.repository;

import com.example.redisrepositorydemo.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
