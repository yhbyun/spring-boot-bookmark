package com.example.bookmark.service;

import com.example.bookmark.entity.Bookmark;
import com.example.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    @Transactional
    public Bookmark createBookmark(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }
}
