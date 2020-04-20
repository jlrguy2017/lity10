import postrecapi from '../api/postrecapi'
import {GET_REC_POSTS} from './types'

export const getRecommendedPosts = (id) => 
      async (dispatch) => {
        try{
            const response = await postrecapi.get(`recommend/${id}`)
                dispatch({
                    type : GET_REC_POSTS,
                    payload : response.data.posts
            })
        }catch(err){
            dispatch({
                type: GET_REC_POSTS,
                payload: []
              });
        }
    }

