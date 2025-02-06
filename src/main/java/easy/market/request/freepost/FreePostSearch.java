package easy.market.request.freepost;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Builder
public class FreePostSearch {
    private String title;
    private String createdBy;
    private SortBy sortBy;

    public FreePostSearch(String title, String createdBy, SortBy sortBy) {
        this.title = title;
        this.createdBy = createdBy;
        this.sortBy = sortBy;
    }
}