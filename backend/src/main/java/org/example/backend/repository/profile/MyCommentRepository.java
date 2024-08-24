package org.example.backend.repository.profile;

import org.example.backend.model.dto.IMyReplyDto;
import org.example.backend.model.entity.board.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * packageName : org.example.backend.repository.profile
 * fileName : MyCommentRepository
 * author : sjuk2
 * date : 2024-06-10
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-10         sjuk2          최초 생성
 */
@Repository
public interface MyCommentRepository extends JpaRepository<Reply, Long> {
    @Query(value = "SELECT B.BOARD_ID AS boardId, B.BOCODE AS bocode, B.SMCODE AS smcode, R.REPLY_ID AS replyId, R.MEMBER_ID AS memberId, R.REPLY AS reply, R.ADD_DATE AS addDate\n" +
            "FROM TB_BOARD B, TB_REPLY R\n" +
            "WHERE B.BOARD_ID = R.BOARD_ID\n" +
            "AND R.MEMBER_ID = :memberId\n" +
            "AND B.STATUS = 'Y'\n" +
            "AND R.STATUS = 'Y'"
            , nativeQuery = true)
    Page<IMyReplyDto> findByMemberIdOfComment(@Param("memberId") String memberId, Pageable pageable);
}
