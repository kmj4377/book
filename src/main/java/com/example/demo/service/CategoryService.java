package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dao.CategorySubDao;
import com.example.demo.dto.CategorySub;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategorySubDao categorySubDao;

    public Integer findSubCategoryIdByKeyword(String memo) {
        if (memo == null || memo.isBlank()) return null;

        List<CategorySub> subs = categorySubDao.getAll();

        for (CategorySub sub : subs) {
            String[] keywords = sub.getAiKeyword().split(",");

            for (String keyword : keywords) {
                if (memo.contains(keyword.trim())) {
                    return sub.getId();
                }
            }
        }
        return null;
    }
}
