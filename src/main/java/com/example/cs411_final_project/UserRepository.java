package com.example.cs411_final_project;

import com.example.cs411_final_project.entity.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer>{
}
