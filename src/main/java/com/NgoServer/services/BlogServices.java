package com.NgoServer.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.BlogDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.models.Blog;
import com.NgoServer.models.User;
import com.NgoServer.repo.AuthRepository;
import com.NgoServer.repo.BlogRepository;

@Service
public class BlogServices {

    @Autowired
    private BlogRepository repository;

    @Autowired
    private AuthRepository authRepository;


    public List<Blog> getAllBlogs() {
        return repository.findAllBlogs();
    }


    public Blog getBlogById(long id) {
        Blog blog = repository.findBlogById(id);
        if(blog == null){
            throw new RuntimeException("Blog not found");
        }
        return blog;
    }


    public ResponseDTO createBlog(BlogDTO blogDTO) {
        if(blogDTO.title().isEmpty() || blogDTO.content().isEmpty() || blogDTO.image().isEmpty()){
            throw new RuntimeException("All fields are required");
        }

        if(repository.findByTitle(blogDTO.title()) != null){
            throw new RuntimeException("Blog with this title already exists");
        }

        Blog blog = mapBlogDTOToBlog(blogDTO);
        User user = getCurrentUserDetails();
        blog.setAuthor(user);
        repository.save(blog);
        return new ResponseDTO("Blog created successfully", 200);
    }

    public User getCurrentUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        return authRepository.findByEmail(principal.getUsername()).get();
    }

    public ResponseDTO updateBlog(long id, BlogDTO blogDTO) {
        Blog blog = repository.findBlogById(id);
        if(blog == null){
            throw new RuntimeException("Blog not found");
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
        if(blog == null){
            throw new RuntimeException("Blog not found");
        }
        repository.delete(blog);
        return new ResponseDTO("Blog deleted successfully", 200);
    }


    private Blog mapBlogDTOToBlog(BlogDTO blogDTO) {
        Blog blog = new Blog();
        blog.setTitle(blogDTO.title());
        blog.setContent(blogDTO.content());
        blog.setImage(blogDTO.image());
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        return blog;
    }


}
