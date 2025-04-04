package com.example.myblog.domain.model;


import jakarta.validation.constraints.AssertTrue;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedParams {
    protected Integer offset;
    protected Integer limit;

    @AssertTrue
    protected boolean getValidPage() {
        return (offset != null && limit != null) || (offset == null && limit == null);
    }
}
