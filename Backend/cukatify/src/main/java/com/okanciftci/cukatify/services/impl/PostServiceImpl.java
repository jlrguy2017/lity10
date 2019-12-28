package com.okanciftci.cukatify.services.impl;

import com.okanciftci.cukatify.entities.mongo.Post;
import com.okanciftci.cukatify.persistence.mongo.PostRepository;
import com.okanciftci.cukatify.services.abstr.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;


    @Override
    public List<Post> takeAllPostsApproved() {
        return this.postRepository.findByisApprovedIsTrue();
    }

    @Override
    public List<Post> takeAllPostsByCategory(String id) {
        return this.postRepository.findByCategoryId(id).stream().collect(Collectors.toList());
    }

    @Override
    public Post findById(String id) {
        return this.postRepository.findById(id).orElse(null);
    }


}
