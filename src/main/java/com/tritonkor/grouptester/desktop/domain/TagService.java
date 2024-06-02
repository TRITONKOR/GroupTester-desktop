package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.net.controller.TagController;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    public static HttpResponse<String> getAllTags() {
        return TagController.getAllTestsByUser();
    }
}
