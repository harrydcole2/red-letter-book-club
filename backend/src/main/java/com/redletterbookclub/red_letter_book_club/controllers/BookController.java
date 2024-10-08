package com.redletterbookclub.red_letter_book_club.controllers;

import com.redletterbookclub.red_letter_book_club.dtos.BookDTO;
import com.redletterbookclub.red_letter_book_club.dtos.ReviewDTO;
import com.redletterbookclub.red_letter_book_club.models.Book;
import com.redletterbookclub.red_letter_book_club.models.Review;
import com.redletterbookclub.red_letter_book_club.models.User;
import com.redletterbookclub.red_letter_book_club.repositories.BookRepository;
import com.redletterbookclub.red_letter_book_club.repositories.ReviewRepository;
import com.redletterbookclub.red_letter_book_club.repositories.UserRepository;
import com.redletterbookclub.red_letter_book_club.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenUtil tokenUtil;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam String type) {
        List<Book> books;

        if (type.equals("featured")) {
            books = bookRepository.findByArchivedFalseOrderByCreatedAtDesc();
        } else if (type.equals("archived")) {
            books = bookRepository.findByArchivedTrueOrderByCreatedAtDesc();
        } else if (type.equals("all")) {
            books = bookRepository.findAll();
        } else {
            return ResponseEntity.badRequest().build();
        }

        List<BookDTO> bookDTOs = books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookDTOs);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Book newBook, @RequestParam String token) {
        // TODO: Server-side validation of created book
        String role = tokenUtil.extractRole(token);
        if (!role.equals("admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Book createdBook = bookRepository.save(newBook);
        BookDTO bookDTO = convertToDTO(createdBook);
        return ResponseEntity.status(201).body(bookDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(b -> ResponseEntity.ok(convertToDTO(b)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id, @RequestParam String token) {
        String role = tokenUtil.extractRole(token);
        if (!role.equals("admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody Book updatedBook, @RequestParam String token) {
        String role = tokenUtil.extractRole(token);
        if (!role.equals("admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (bookRepository.existsById(id)) {
            updatedBook.setId(id);
            Book book = bookRepository.save(updatedBook);
            BookDTO bookDTO = convertToDTO(book);
            return ResponseEntity.ok(bookDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/{id}/review")
    public ResponseEntity<?> addReviewToBook(@PathVariable Long id, @RequestParam String token, @RequestBody Review newReview) {

        String email = tokenUtil.extractEmail(token);
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            Optional<Book> book = bookRepository.findById(id);
            if (book.isPresent()) {
                newReview.setBook(book.get());
                newReview.setUser(user);
                Review createdReview = reviewRepository.save(newReview);
                ReviewDTO reviewDTO = convertToDTO(createdReview);
                return ResponseEntity.status(201).body(reviewDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Create an account first");
    }

    private BookDTO convertToDTO(Book book) {
        List<ReviewDTO> reviews = reviewRepository.findByBookId(book.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setArchived(book.isArchived());
        bookDTO.setDatePublished(book.getDatePublished());
        bookDTO.setGenre(book.getGenre());
        bookDTO.setPictureUrl(book.getPictureUrl());
        bookDTO.setReviews(reviews);
        return bookDTO;
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setBookId(review.getBook().getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setContent(review.getContent());
        if (review.getUser() != null) {
            reviewDTO.setUserId(review.getUser().getId());
            reviewDTO.setReviewer(review.getUser().getPreferredName());
        }
        return reviewDTO;
    }
}
