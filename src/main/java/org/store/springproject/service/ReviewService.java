package org.store.springproject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.springproject.exception.ReviewNotFoundException;
import org.store.springproject.exception.ShoeNotFoundException;
import org.store.springproject.exception.UserNotFoundException;
import org.store.springproject.model.Review;
import org.store.springproject.model.Shoe;
import org.store.springproject.model.User;
import org.store.springproject.repository.ReviewRepository;
import org.store.springproject.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ShoeService shoeService;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<Review> findByUser_Id(Integer userId){
        if(!userRepository.existsById(userId))
        {
            throw new ReviewNotFoundException("The Review of the user does not exist");
        }
        return reviewRepository.findByUser_Id(userId);
    }
    public List<Review> findByMinRating(int minRating){
        return reviewRepository.findByMinRating(minRating);
    }
    public void deleteReviewById(Integer reviewId) throws ReviewNotFoundException {
        reviewRepository.deleteById(reviewId);
    }
    public Review addReview(Integer userId, Long shoeId, Review review)
    {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Shoe shoe = shoeService.findById(shoeId).orElseThrow();
        review.setUser(user);
        review.setShoe(shoe);
        return reviewRepository.save(review);
    }
}
