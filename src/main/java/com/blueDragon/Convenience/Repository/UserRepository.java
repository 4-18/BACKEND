package com.blueDragon.Convenience.Repository;

import com.blueDragon.Convenience.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
}
