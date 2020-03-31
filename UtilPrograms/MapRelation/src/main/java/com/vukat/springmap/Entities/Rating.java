package com.vukat.springmap.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document("Rating")
@Getter
@Setter
@NoArgsConstructor
public class Rating {

    @Id
    private String id;

    private String userId;

    private String postId;

    private int rating;


    @Override
    public boolean equals(Object o) {
        Rating other = (Rating) o;

        if(other.getUserId() == this.getUserId() && other.getPostId() == this.getPostId()){
            return true;
        }
        return  false;
    }

}