package rs.ac.bg.fon.molecious.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.molecious.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}