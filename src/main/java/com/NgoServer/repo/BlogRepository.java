package com.NgoServer.repo;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.NgoServer.models.Blog;
import com.NgoServer.models.User;
import com.NgoServer.utils.Role;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

        @Query("""
                        Select b.id, b.title, b.content, b.image, b.createdAt, u.id, u.username, u.email, u.phoneNumber, u.createdAt, u.role
                        FROM Blog b
                        JOIN b.author u
                        WHERE b.title = :title
                        """)
        List<Object[]> findByTitleObjects(String title);

        default Blog findByTitle(String title) {
                return findByTitleObjects(title).stream()
                                .map(this::toBlog)
                                .findFirst()
                                .orElse(null);
        }

        @Query(value = """
                        SELECT b.id, b.title, b.content, b.image, b.created_at,
                               u.id, u.username, u.email, u.phone_number, u.created_at, u.role
                        FROM blogs b
                        JOIN users u ON b.author_id = u.id
                        """, nativeQuery = true)
        List<Object[]> findAllBlogsObjects();

        default List<Blog> findAllBlogs() {
                return findAllBlogsObjects().stream()
                                .map(this::toBlog)
                                .collect(Collectors.toList()); // `collect(Collectors.toList())` is more idiomatic in
                                                               // Java
        }

        default Blog toBlog(Object[] objects) {
                Blog blog = new Blog();
                blog.setId((Long) objects[0]);
                blog.setTitle((String) objects[1]);
                blog.setContent((String) objects[2]);
                blog.setImage((String) objects[3]);

                if (objects[4] instanceof Timestamp timestamp) {
                        blog.setCreatedAt(timestamp.toLocalDateTime());
                }

                User user = new User();
                user.setId((Long) objects[5]);
                user.setUsername((String) objects[6]);
                user.setEmail((String) objects[7]);
                user.setPhoneNumber((String) objects[8]);

                if (objects[9] instanceof Timestamp timestamp) {
                        user.setCreatedAt(timestamp.toLocalDateTime());
                }

                if (objects[10] instanceof String roleStr) {
                        try {
                                user.setRole(Role.valueOf(roleStr));
                        } catch (IllegalArgumentException e) {
                                throw new RuntimeException("Invalid role value: " + roleStr, e);
                        }
                }

                blog.setAuthor(user);
                return blog;
        }

        @Query(value = """
                        SELECT b.id, b.title, b.content,b.image, b.createdAt, u.id, u.username, u.email, u.phoneNumber, u.createdAt, u.role
                        FROM Blog b
                        JOIN b.author u
                        WHERE b.id = :id
                        """)
        List<Object[]> findBlogByIdObjects(long id);

        default Blog findBlogById(long id) {
                return findBlogByIdObjects(id).stream()
                                .map(this::toBlog)
                                .findFirst()
                                .orElse(null);
        }

}
