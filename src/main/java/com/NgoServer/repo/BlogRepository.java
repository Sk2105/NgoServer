package com.NgoServer.repo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.NgoServer.dto.BlogResponseDTO;
import com.NgoServer.exceptions.BlogNotFoundException;
import com.NgoServer.models.Blog;
import com.NgoServer.models.Comment;
import com.NgoServer.models.User;
import com.NgoServer.utils.Role;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

        @Query("""
                        Select b.id, b.title, b.content, b.image, b.createdAt, b.updatedAt, u.id, u.username, u.email, u.phoneNumber, u.createdAt, u.role
                        FROM Blog b
                        JOIN b.author u
                        WHERE b.title = :title ORDER BY b.id DESC
                        """)
        List<Object[]> findByTitleObjects(String title);

        default BlogResponseDTO findByTitle(String title) {
                return findByTitleObjects(title).stream()
                                .map(this::toBlogResponseDTO)
                                .findFirst()
                                .orElse(null);
        }

        @Query(value = """
                        SELECT b.id, b.title, b.content, b.image, b.created_at,b.updated_at,
                               u.id, u.username, u.email, u.phone_number, u.created_at, u.role
                        FROM blogs b
                        JOIN users u ON b.author_id = u.id ORDER BY b.id DESC
                        """, nativeQuery = true)
        List<Object[]> findAllBlogsObjects();

        default List<BlogResponseDTO> findAllBlogs() {
                return findAllBlogsObjects().stream()
                                .map(this::toBlogResponseDTO)
                                .collect(Collectors.toList()); // `collect(Collectors.toList())` is more idiomatic in
                                                               // Java
        }

        private BlogResponseDTO toBlogResponseDTO(Object[] objects) {
                User user = new User();
                user.setId((Long) objects[6]);
                user.setUsername((String) objects[7]);
                user.setEmail((String) objects[8]);
                user.setPhoneNumber((String) objects[9]);

                user.setCreatedAt(convertToLocalDateTime(objects[10]));

                user.setRole(Role.valueOf((String) objects[11]));

                return new BlogResponseDTO(
                                (Long) objects[0],
                                (String) objects[1],
                                (String) objects[2],
                                (String) objects[3],
                                convertToLocalDateTime(objects[4]),
                                convertToLocalDateTime(objects[5]),
                                user);

        }

        private LocalDateTime convertToLocalDateTime(Object obj) {
                if (obj instanceof Timestamp ts) {
                        return ts.toLocalDateTime();
                } else if (obj instanceof LocalDateTime ldt) {
                        return ldt; // No conversion needed
                }
                return null;
        }

        private Blog toBlog(Object[] objects) {
                Long id = (Long) objects[0];
                String title = (String) objects[1];
                String content = (String) objects[2];
                String image = (String) objects[3];
                LocalDateTime createdAt = convertToLocalDateTime(objects[4]);
                LocalDateTime updatedAt = convertToLocalDateTime(objects[5]);

                Long authorId = (Long) objects[6];
                String authorUsername = (String) objects[7];
                String authorEmail = (String) objects[8];
                String authorPhoneNumber = (String) objects[9];
                LocalDateTime authorCreatedAt = convertToLocalDateTime(objects[10]);
                Role authorRole = (Role) objects[11];

                User author = new User();
                author.setId(authorId);
                author.setUsername(authorUsername);
                author.setEmail(authorEmail);
                author.setPhoneNumber(authorPhoneNumber);
                author.setCreatedAt(authorCreatedAt);
                author.setRole(authorRole);

                Blog blog = new Blog();
                blog.setId(id);
                blog.setTitle(title);
                blog.setContent(content);
                blog.setImage(image);
                blog.setCreatedAt(createdAt);
                blog.setUpdatedAt(updatedAt);
                blog.setAuthor(author);
                return blog;
        }

        @Query(value = """
                        SELECT b.id, b.title, b.content,b.image, b.createdAt,b.updatedAt, u.id, u.username, u.email, u.phoneNumber, u.createdAt, u.role
                        FROM Blog b
                        JOIN b.author u
                        WHERE b.id = :id ORDER BY b.id DESC
                        """)
        List<Object[]> findBlogByIdObjects(long id);

        default Blog findBlogById(long id) {
                List<Comment> comments = findCommentsByBlogId(id);
                System.out.println(comments);
                return findBlogByIdObjects(id).stream()
                                .map(obj -> {
                                        Blog blog = toBlog(obj);
                                        blog.setComments(comments);
                                        return blog;
                                })
                                .findFirst()
                                .orElseThrow(() -> new BlogNotFoundException("Blog with id=" + id + " does not exist"));
        }

        private Comment toComment(Object[] objects) {
                Comment comment = new Comment();
                comment.setId((Long) objects[0]);
                comment.setContent((String) objects[1]);
                comment.setCreatedAt(convertToLocalDateTime(objects[2]));

                User user = new User();
                user.setId((Long) objects[3]);
                user.setUsername((String) objects[4]);
                user.setEmail((String) objects[5]);
                user.setPhoneNumber((String) objects[6]);
                user.setCreatedAt(convertToLocalDateTime(objects[7]));
                user.setRole((Role) objects[8]);

                comment.setUser(user);

                return comment;
        }

        @Query("""
                                SELECT c.id, c.content, c.createdAt, u.id, u.username, u.email, u.phoneNumber, u.createdAt, u.role
                                FROM Comment c
                                JOIN c.blog b
                                JOIN b.author u
                                WHERE b.id = :blogId ORDER BY c.id DESC
                        """)
        List<Object[]> findCommentsByBlogIdObjects(@Param("blogId") Long blogId);

        default List<Comment> findCommentsByBlogId(Long blogId) {
                return findCommentsByBlogIdObjects(blogId).stream()
                                .map(this::toComment)
                                .collect(Collectors.toList());
        }

}
