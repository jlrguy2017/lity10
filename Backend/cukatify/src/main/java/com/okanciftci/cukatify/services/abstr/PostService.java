package com.okanciftci.cukatify.services.abstr;

import com.okanciftci.cukatify.common.service.abstr.ValidationInterface;
import com.okanciftci.cukatify.entities.mongo.Post;
import com.okanciftci.cukatify.models.mongo.PostModel;

import java.util.List;

public interface PostService extends ValidationInterface<PostModel> {

    List<Post> takeAllPostsApproved();

    List<Post> takeAllPostsByCategory(String id);

    Post findById(String id);

    PostModel savePost(PostModel postModel);

    Post findPostTotalRating(Post post);

}
