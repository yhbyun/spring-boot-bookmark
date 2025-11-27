package com.example.bookmark.controller;

import com.example.bookmark.aop.Idempotent;
import com.example.bookmark.entity.Bookmark;
import com.example.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public List<Bookmark> getAllBookmarks() {
        return bookmarkService.getAllBookmarks();
    }

    @Idempotent
    @PostMapping
    public Bookmark createBookmark(@RequestBody Bookmark bookmark) {
        return bookmarkService.createBookmark(bookmark);
    }

    @PutMapping("/{id}")
    public Bookmark updateBookmark(@PathVariable Long id, @RequestBody Bookmark bookmark) {
        return bookmarkService.updateBookmark(id, bookmark);
    }

    @DeleteMapping("/{id}")
    public void deleteBookmark(@PathVariable Long id) {
        bookmarkService.deleteBookmark(id);
    }
}
