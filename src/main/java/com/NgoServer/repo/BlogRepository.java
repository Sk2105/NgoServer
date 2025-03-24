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
                        WHERE b.title = :title
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
                        JOIN users u ON b.author_id = u.id
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
                                ((Timestamp) objects[4]).toLocalDateTime(),
                                ((Timestamp) objects[5]).toLocalDateTime(),
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
                Blog blog = new Blog();
                blog.setId((Long) objects[0]);
                blog.setTitle((String) objects[1]);
                blog.setContent((String) objects[2]);
                blog.setImage((String) objects[3]);
                blog.setCreatedAt(convertToLocalDateTime(objects[4]));
                blog.setUpdatedAt(convertToLocalDateTime(objects[5]));

                User author = new User();
                author.setId((Long) objects[6]);
                author.setUsername((String) objects[7]);
                author.setEmail((String) objects[8]);
                author.setPhoneNumber((String) objects[9]);
                author.setCreatedAt(convertToLocalDateTime(objects[10]));
                author.setRole((Role) objects[11]);

                blog.setAuthor(author);
                return blog;
        }

        @Query(value = """
                        SELECT b.id, b.title, b.content,b.image, b.createdAt,b.updatedAt, u.id, u.username, u.email, u.phoneNumber, u.createdAt, u.role
                        FROM Blog b
                        JOIN b.author u
                        WHERE b.id = :id
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

                User author = new User();
                author.setId((Long) objects[3]);
                author.setUsername((String) objects[4]);
                author.setEmail((String) objects[5]);
                author.setPhoneNumber((String) objects[6]);
                author.setCreatedAt(convertToLocalDateTime(objects[7]));
                author.setRole((Role) objects[8]);

                comment.setUser(author);

                return comment;
        }

        @Query("""
                                SELECT c.id, c.content, c.createdAt, u.id, u.username, u.email, u.phoneNumber, u.createdAt, u.role
                                FROM Comment c
                                JOIN c.blog b
                                JOIN b.author u
                                WHERE b.id = :blogId
                        """)
        List<Object[]> findCommentsByBlogIdObjects(@Param("blogId") Long blogId);

        default List<Comment> findCommentsByBlogId(Long blogId) {
                return findCommentsByBlogIdObjects(blogId).stream()
                                .map(this::toComment)
                                .collect(Collectors.toList());
        }

}
