package com.hanker.hankertaco.repository;

import com.hanker.hankertaco.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
