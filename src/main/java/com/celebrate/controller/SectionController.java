package com.celebrate.controller;

import com.celebrate.dto.input.SectionInput;
import com.celebrate.dto.response.SectionResponse;
import com.celebrate.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @QueryMapping
    public List<SectionResponse> sections() {
        return sectionService.getAllSections();
    }

    @MutationMapping
    public SectionResponse createSection(@Argument("section") SectionInput section) {
        return sectionService.createSection(section);
    }

    @MutationMapping
    public SectionResponse editSection(@Argument("section") SectionInput section) {
        return sectionService.editSection(section);
    }

    @MutationMapping
    public boolean deleteSection(@Argument String id) {
        return sectionService.deleteSection(id);
    }
}
