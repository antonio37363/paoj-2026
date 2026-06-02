package com.pao.proiect.fooddelivery.repository;

import java.util.List;
import java.util.Optional;

// Repository este o interfata generica. Am definit metodele de baza pentru operatiile CRUD, unde T este tipul entității, iar ID este tipul id-ulu
public interface Repository<T, ID> {
    void save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    void update(T entity);

    void delete(ID id);
}