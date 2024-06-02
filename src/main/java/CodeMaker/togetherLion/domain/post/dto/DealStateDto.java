package CodeMaker.togetherLion.domain.post.dto;

import CodeMaker.togetherLion.domain.post.model.DealState;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DealStateDto {
    private DealState dealState;
    private String message;
}