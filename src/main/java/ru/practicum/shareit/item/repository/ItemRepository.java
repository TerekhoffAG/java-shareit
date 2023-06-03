package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerId(Integer userId);


    @Query(value = "select i from Item i where lower(i.name) like %?1% or lower(i.description) like %?1% " +
            "and i.available=true")
    List<Item> findByNameOrDescription(String text);
}