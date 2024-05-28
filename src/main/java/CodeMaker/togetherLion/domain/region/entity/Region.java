package CodeMaker.togetherLion.domain.region.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regionId;

    @Column
    private String city; // 시

    @Column
    private String district; // 구

    @Column
    private String townName; // 동
}
