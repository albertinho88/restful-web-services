package ec.clicka.rest.webservices.restfulwebservices.user;

import ec.clicka.rest.webservices.restfulwebservices.jpa.PostRepository;
import ec.clicka.rest.webservices.restfulwebservices.jpa.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserResourceJpa {

    private UserRepository repository;

    private PostRepository postRepository;

    public UserResourceJpa(UserRepository repository, PostRepository postRepository) {
        super();
        this.repository = repository;
        this.postRepository = postRepository;
    }

    @GetMapping(path = "/jpa/users")
    public List<User> retrieveAllUsers() {
        return repository.findAll();
    }

    @GetMapping(path = "/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable Integer id) {
        User user = repository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("id: " + id);
        }

        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }

    @DeleteMapping(path = "/jpa/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = repository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id) {
        User user = repository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("id: " + id);
        }
        return user.getPosts();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Post> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post) {
        User user = repository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("id: " + id);
        }
        post.setUser(user);
        Post savedPost = postRepository.save(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "jpa/users/{userId}/posts/{id}")
    public EntityModel<Post> retrievePost(@PathVariable Integer userId, @PathVariable Integer id) {
        User user = repository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User with id: " + userId + " not found.");
        }

        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            throw new EntityNotFoundException("Post with id " + id + " not found.");
        }

        EntityModel<Post> entityModel = EntityModel.of(post);
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrievePostsForUser(userId));
        entityModel.add(link.withRel("posts-per-user"));
        return entityModel;
    }

}
