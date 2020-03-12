
import {GET_POSTS, GET_SELECTED_POST,SAVE_POST} from '../actions/types' 

const initialState = {
    posts: [],
    selectedPost: {
        rating : 0,
        category : {
            name : "Noname"
        }
    },
    savedPost : {}
  };
  

export default (state = initialState,action) => {

    switch(action.type) {
        case GET_POSTS:
            return {
                ...state,
                posts : [...action.payload]
            };
        case GET_SELECTED_POST:
            return {
                ...state,
                selectedPost : action.payload
            };
        case SAVE_POST:
            return {
                ...state,
                savedPost : action.payload
            };
        default:
            return state;
    }
}