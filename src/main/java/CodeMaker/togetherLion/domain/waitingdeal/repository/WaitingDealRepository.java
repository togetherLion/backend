package CodeMaker.togetherLion.domain.waitingdeal.repository;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import org.springframework.data.jpa.repository.JpaRepository;


    public interface WaitingDealRepository extends JpaRepository<WaitingDeal, Integer> {

    }

