package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.dto.BlogBodyDTO;
import com.NgoServer.dto.CommentBodyDTO;
import com.NgoServer.services.BlogServices;

@RestController
@RequestMapping("blogs")
public class BlogController {

    @Autowired
    private BlogServices blogService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAllBlogs() {
        return ResponseEntity.ok().body(blogService.getAllBlogs());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getBlogById(@PathVariable Long id) {
        return ResponseEntity.ok().body(blogService.getBlogById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBlog(@RequestBody BlogBodyDTO blogDTO) {
        return ResponseEntity.ok().body(blogService.createBlog(blogDTO));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody BlogBodyDTO blogDTO) {
        return ResponseEntity.ok().body(blogService.updateBlog(id, blogDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        return ResponseEntity.ok().body(blogService.deleteBlog(id));
    }

    @PostMapping("/{id}/comments")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody CommentBodyDTO body) {
        return ResponseEntity.ok().body(blogService.addComment(id, body));
    }

}
