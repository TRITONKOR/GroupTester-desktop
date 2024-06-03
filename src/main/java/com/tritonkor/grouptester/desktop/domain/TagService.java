package com.tritonkor.grouptester.desktop.domain;

import com.tritonkor.grouptester.desktop.net.controller.TagController;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

/**
 * Service class for managing tags in the GroupTester desktop application.
 */
@Service
public class TagService {

    /**
     * Retrieves all tags.
     *
     * @return The HTTP response containing all tags.
     */
    public static HttpResponse<String> getAllTags() {
        return TagController.getAllTestsByUser();
    }
}
