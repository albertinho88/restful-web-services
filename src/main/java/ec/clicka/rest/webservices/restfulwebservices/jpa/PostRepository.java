package ec.clicka.rest.webservices.restfulwebservices.jpa;

import ec.clicka.rest.webservices.restfulwebservices.user.Post;
import ec.clicka.rest.webservices.restfulwebservices.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
