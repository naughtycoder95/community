package org.example.backend.repository.board;

import org.example.backend.model.common.BoardIdMemberIdPk;
import org.example.backend.model.entity.board.VoteMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName : org.example.backend.repository.board
 * fileName : VoteMemberRepository
 * author : gumiji
 * date : 6/11/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 6/11/24         gumiji          최초 생성
 */
public interface VoteMemberRepository extends JpaRepository<VoteMember, BoardIdMemberIdPk> {
    void deleteByBoardId(Long boardId);

//    Optional<VoteMember> findById(Long boardId, String memberId);
}
