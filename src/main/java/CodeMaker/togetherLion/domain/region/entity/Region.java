package CodeMaker.togetherLion.domain.region.entity;

import CodeMaker.togetherLion.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();
}
