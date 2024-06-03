package CodeMaker.togetherLion.domain.post.dto;

import CodeMaker.togetherLion.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPostDto {
    private PostRes postRes;
    private int waitingDealsCount;
}
