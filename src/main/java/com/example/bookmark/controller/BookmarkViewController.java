package com.example.bookmark.controller;

import com.example.bookmark.entity.Bookmark;
import com.example.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    // Delete a bookmark via GET (simple approach)
    @GetMapping("/delete/{id}")
    public String deleteBookmark(@PathVariable Long id) {
        bookmarkService.deleteBookmark(id);
        return "redirect:/bookmarks";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Bookmark bookmark = bookmarkService.getBookmarkById(id);
        model.addAttribute("bookmark", bookmark);
        return "bookmark-edit";
    }

    // Process edit submission
    @PostMapping("/edit")
    public String updateBookmark(@ModelAttribute Bookmark bookmark) {
        bookmarkService.updateBookmark(bookmark.getId(), bookmark);
        return "redirect:/bookmarks";
    }
}
