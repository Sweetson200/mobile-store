package com.learning.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.learning.config.PersistenceProperties;
import com.learning.entities.UserDetail;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Component
public class UserDetailRepository {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;


    @PostConstruct
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory(
                "mobile_store", PersistenceProperties.fromEnvironment());
        entityManager = entityManagerFactory.createEntityManager();
    }

    @PreDestroy
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }


    public List<UserDetail> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM UserDetail u", UserDetail.class).getResultList();
    }

    public UserDetail findByEmailAndPassword(String email, String password) {
        List<UserDetail> users = entityManager.createQuery(
                "SELECT u FROM UserDetail u WHERE u.email = :email AND u.password = :password", UserDetail.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList();

        return users.isEmpty() ? null : users.get(0);
    }
}
