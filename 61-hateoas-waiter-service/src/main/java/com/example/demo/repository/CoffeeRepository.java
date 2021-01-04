package com.example.demo.repository;

import com.example.demo.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * 继承JpaRepository
 * 默认path为coffee
 */
@RepositoryRestResource(path = "/coffee")
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

    /**
     * 通过id排序
     * http://localhost:8080/coffee/search/findByNameInOrderById?list=mocha,espresso
     * @param list
     * @return
     */
    List<Coffee> findByNameInOrderById(List<String> list);

    /**
     * 通过name查找coffee
     * http://localhost:8080/coffee/search/findByName?name=mocha
     * @param name
     * @return
     */
    Coffee findByName(String name);
}
