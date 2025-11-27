package com.example.bookmark.service;

import com.example.bookmark.entity.Bookmark;
import com.example.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    public Bookmark createBookmark(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    public Bookmark getBookmarkById(Long id) {
        return bookmarkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bookmark not found: " + id));
    }

    // Update an existing bookmark
    public Bookmark updateBookmark(Long id, Bookmark updated) {
        return bookmarkRepository.findById(id)
                .map(existing -> {
                    existing.setUrl(updated.getUrl());
                    existing.setTitle(updated.getTitle());
                    existing.setDescription(updated.getDescription());
                    return bookmarkRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Bookmark not found: " + id));
    }

    // Delete a bookmark by id
    public void deleteBookmark(Long id) {
        if (!bookmarkRepository.existsById(id)) {
            throw new IllegalArgumentException("Bookmark not found: " + id);
        }
        bookmarkRepository.deleteById(id);
    }
}
