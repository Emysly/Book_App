package com.emysilva.demo.controller;

import com.emysilva.demo.exception.ResourceNotFoundException;
import com.emysilva.demo.model.Book;
import com.emysilva.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
@CrossOrigin
public class BookController {

	@Autowired
	BookRepository bookRepository;

	@GetMapping("/books")
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}

	@PostMapping("/books")
	public Book createBook(@Valid @RequestBody Book book) {
		return bookRepository.save(book);
	}

	@GetMapping("books/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No book with id: " + id));

		return ResponseEntity.ok(book);
	}

	@PutMapping("books/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable("id") Long id,@Valid @RequestBody Book book) {
		Book bookToUpdate = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No book with id: " + id));

		bookToUpdate.setTitle(book.getTitle());
		bookToUpdate.setAuthor(book.getAuthor());
		bookToUpdate.setDescription(book.getDescription());
		bookToUpdate.setRating(book.getRating());
		bookToUpdate.setImageUrl(book.getImageUrl());
		bookToUpdate.setPrice(book.getPrice());
		bookToUpdate.setNumberOfCopies(book.getNumberOfCopies());

		Book updatedBook = bookRepository.save(bookToUpdate);

		return ResponseEntity.ok(updatedBook);
	}

	@DeleteMapping("books/{id}")
	public void deletBookById(@PathVariable("id") Long id) {
		Book bookToDelete = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No book with id: " + id));

		bookRepository.delete(bookToDelete);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", true);
		ResponseEntity.ok(response);
	}

}
