package CodeMaker.togetherLion.domain.cheat.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Cheat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cheatId;

    @Column
    private String cheatAccount;

    @Column
    private String cheatCount;
}
