package com.NgoServer.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.BlogBodyDTO;
import com.NgoServer.dto.BlogResponseDTO;
import com.NgoServer.dto.CommentBodyDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.exceptions.BlogAlreadyExistsException;
import com.NgoServer.exceptions.BlogNotFoundException;
import com.NgoServer.exceptions.FoundEmptyElementException;
import com.NgoServer.models.Blog;
import com.NgoServer.models.Comment;
import com.NgoServer.models.User;
import com.NgoServer.repo.BlogRepository;
import com.NgoServer.repo.CommentRepository;

@Service
public class BlogServices {

    @Autowired
    private BlogRepository repository;

    @Autowired
    private AuthServices authServices;

    @Autowired
    private CommentRepository commentRepository;

    public List<BlogResponseDTO> getAllBlogs() {
        return repository.findAllBlogs();
    }

    public Blog getBlogById(long id) {
        Blog blog = repository.findBlogById(id);
        if (blog == null) {
            throw new BlogNotFoundException("Blog not found");
        }
        return blog;
    }

    public ResponseDTO createBlog(BlogBodyDTO blogDTO) {
        if (blogDTO.title().isEmpty() || blogDTO.content().isEmpty() || blogDTO.image().isEmpty()) {
            throw new FoundEmptyElementException("All fields are required");
        }
        BlogResponseDTO b = repository.findByTitle(blogDTO.title());

        if (b != null) {
            throw new BlogAlreadyExistsException("Blog with this title already exists");
        }

        Blog blog = mapBlogDTOToBlog(blogDTO);
        User user = getCurrentUserDetails();
        blog.setAuthor(user);
        repository.save(blog);
        return new ResponseDTO("Blog created successfully", 200);
    }

    private User getCurrentUserDetails() {
        return authServices.getCurrentUserDetails();
    }

    public ResponseDTO updateBlog(long id, BlogBodyDTO blogDTO) {
        Blog blog = repository.findBlogById(id);
        if (blog == null) {
            throw new BlogNotFoundException("Blog not found");
        }
        blog.setTitle(blogDTO.title());
        blog.setContent(blogDTO.content());
        blog.setImage(blogDTO.image());
        blog.setUpdatedAt(LocalDateTime.now());
        repository.save(blog);
        return new ResponseDTO("Blog updated successfully", 200);
    }

    public ResponseDTO deleteBlog(long id) {
        Blog blog = repository.findBlogById(id);
        if (blog == null) {
            throw new BlogNotFoundException("Blog not found");
        }
        repository.delete(blog);
        return new ResponseDTO("Blog deleted successfully", 200);
    }

    private Blog mapBlogDTOToBlog(BlogBodyDTO blogDTO) {
        Blog blog = new Blog();
        blog.setTitle(blogDTO.title());
        blog.setContent(blogDTO.content());
        blog.setImage(blogDTO.image());
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        return blog;
    }

    public ResponseDTO addComment(long blogId, CommentBodyDTO body) {
        Blog blog = repository.findBlogById(blogId);
        if (blog == null) {
            throw new BlogNotFoundException("Blog not found");
        }

        Comment comment = new Comment();
        comment.setContent(body.content());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(getCurrentUserDetails());
        comment.setBlog(blog);
        blog.getComments().add(comment);
        commentRepository.save(comment);

        return new ResponseDTO("Comment added successfully", 200);
    }



}
