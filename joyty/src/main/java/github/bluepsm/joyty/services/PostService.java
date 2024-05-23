package github.bluepsm.joyty.services;

import java.util.Set;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

import github.bluepsm.joyty.models.Post;
import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.payload.request.CreatePostRequest;
import github.bluepsm.joyty.models.Tag;
import github.bluepsm.joyty.repositories.PostRepository;
import github.bluepsm.joyty.repositories.TagRepository;
import github.bluepsm.joyty.repositories.UserRepository;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;
    
    public Post createPost(Long userId, CreatePostRequest createPostRequest) {
        Optional<User> user = userRepository.findById(userId);
        
        Set<Tag> tags = new HashSet<Tag>();

        for (Long tagId : createPostRequest.getTags()) {
            Tag tag = tagRepository.findById(tagId).get();
            tags.add(tag);
        }
        
        String body = createPostRequest.getBody();
        String placeName = createPostRequest.getPlaceName();
        String placeAddress = createPostRequest.getPlaceAddress();
        Double placeLatitude = createPostRequest.getPlaceLatitude();
        Double placeLongtitude = createPostRequest.getPlaceLongtitude();
        Date meetingDatetime = createPostRequest.getMeetingDatetime();
        Integer partySize = createPostRequest.getPartySize();
        BigDecimal costEstimate = createPostRequest.getCostEstimate();
        Boolean costShare = createPostRequest.getCostShare();
        
        Post post = new Post(body, 
			        		partySize, 
			        		placeName, 
			        		placeAddress, 
			        		placeLatitude, 
			        		placeLongtitude, 
			        		meetingDatetime, 
			        		costEstimate, 
			        		costShare);
        
        post.setAuthor(user.get());
        post.setTags(tags);
        post.setJoinner(0);
        post.setMeetingDone(false);
        
        return postRepository.save(post);
    }
    
    public Optional<List<Post>> getAllPosts(String[] sort) {
    	List<Order> orders = new ArrayList<Order>();

        if (sort[0].contains(",")) {
          // will sort more than 2 columns
          for (String sortOrder : sort) {
            // sortOrder="column, direction"
            String[] _sort = sortOrder.split(",");
            orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
          }
        } else {
          // sort=[column, direction]
          orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }
        
        List<Post> posts = postRepository.findAll(Sort.by(orders));
        return Optional.of(posts);
    }
    
    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }
    
    private Sort.Direction getSortDirection(String direction) {
    	if (direction.equals("desc")) {
    		return Sort.Direction.DESC;
    	} else if (direction.equals("asc")) {
    		return Sort.Direction.ASC;
    	}
    	
    	return Sort.Direction.DESC;
    }
    
    public Optional<Post> updatePost(Long postId, CreatePostRequest updatePostRequest) {
    	Optional<Post> postOpt = postRepository.findById(postId);
    	
    	if(!postOpt.isPresent()) {
    		return Optional.empty();
    	}
    	
    	Post post = postOpt.get();
        
        Set<Tag> tags = new HashSet<Tag>();

        for (Long tagId : updatePostRequest.getTags()) {
            Tag tag = tagRepository.findById(tagId).get();
            tags.add(tag);
        }
        
        post.setBody(updatePostRequest.getBody());
        post.setPlaceName(updatePostRequest.getPlaceName());
        post.setPlaceAddress(updatePostRequest.getPlaceAddress());
        post.setPlaceLatitude(updatePostRequest.getPlaceLatitude());
        post.setPlaceLongtitude(updatePostRequest.getPlaceLongtitude());
        post.setMeetingDatetime(updatePostRequest.getMeetingDatetime());
        post.setPartySize(updatePostRequest.getPartySize());
        post.setCostEstimate(updatePostRequest.getCostEstimate());
        post.setCostShare(updatePostRequest.getCostShare());
        post.setTags(tags);
        
        return Optional.of(postRepository.save(post));
    }
    
    public boolean deletePostById(Long id) {
        try {
            postRepository.deleteById(id);
            return true;
        } catch(EmptyResultDataAccessException err) {
            return false;
        }
    }
    
    public Window<Post> getScrollPosts(Long latestPost) {
    	OffsetScrollPosition offset = ScrollPosition.offset(latestPost);
    	Window<Post> posts = postRepository.findFirst5AllByOrderByCreatedAtDesc(offset);
    	
    	return posts;
    }
}
