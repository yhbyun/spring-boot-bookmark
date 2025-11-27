package com.example.bookmark.controller;

import com.example.bookmark.entity.Bookmark;
import com.example.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkViewController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public String getBookmarkList(Model model) {
        model.addAttribute("bookmarks", bookmarkService.getAllBookmarks());
        return "bookmark-list";
    }

    @PostMapping
    public String addBookmark(@ModelAttribute Bookmark bookmark) {
        bookmarkService.createBookmark(bookmark);
        return "redirect:/bookmarks";
    }
}
