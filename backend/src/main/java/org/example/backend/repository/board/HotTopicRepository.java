package org.example.backend.repository.board;

import org.example.backend.model.dto.MainPageDto;
import org.example.backend.model.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName : org.example.backend.repository.board
 * fileName : HotTopicBoardRepository
 * author : GGG
 * date : 2024-06-03
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-03         GGG          최초 생성
 */

@Repository
public interface HotTopicRepository extends JpaRepository<Board, Long> {
    @Query(value =
            "WITH all_replies AS (" +
                    "    SELECT BOARD_ID, COUNT(*) AS total_reply_count" +
                    "    FROM TB_REPLY" +
                    "    WHERE STATUS = 'Y' AND TO_DATE(ADD_DATE, 'YYYY-MM-DD HH24:MI:SS') > SYSDATE - 30" +
                    "    GROUP BY BOARD_ID" +
                    ")" +
                    " SELECT '핫토픽게시판' AS board," +
                    "        B.BOARD_ID AS boardId," +
                    "        B.BOARD_TITLE AS boardTitle," +
                    "        M.NICKNAME AS nickName," +
                    "(SELECT COUNT(*) FROM TB_RECOMMEND R WHERE R.BOARD_ID = B.BOARD_ID) AS good, " +
                    "        NVL(R.total_reply_count, 0) AS totalReplyCount," +
                    "        B.ADD_DATE AS addDate," +
                    "        B.BOCODE AS boCode" +
                    " FROM TB_BOARD B" +
                    " LEFT JOIN TB_MEMBER M ON B.MEMBER_ID = M.MEMBER_ID" +
                    " LEFT JOIN all_replies R ON B.BOARD_ID = R.BOARD_ID" +
                    " WHERE B.STATUS = 'Y' AND B.BOCODE = 'BO03' AND NVL(R.total_reply_count, 0) >= 10" +
                    " ORDER BY TO_DATE(B.ADD_DATE, 'YYYY-MM-DD HH24:MI:SS') DESC", nativeQuery = true)
    List<MainPageDto> findHotTopics();
}