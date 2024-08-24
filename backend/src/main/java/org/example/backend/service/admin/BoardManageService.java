package org.example.backend.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.BoardManageDto;
import org.example.backend.model.dto.BoardEditDto;
import org.example.backend.model.entity.CmCode;
import org.example.backend.repository.admin.BoardManageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

/**
 * packageName : org.example.backend.service.board
 * fileName : CmCodeService
 * author : GGG
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         GGG          최초 생성
 */
@Service
@RequiredArgsConstructor
public class BoardManageService {

    private final BoardManageRepository boardManageRepository;

//    CmCode 조회
public List<BoardManageDto> getCmCodeList() {
    return boardManageRepository.retrieveCmCode();
}

//  게시판 추가
    @Transactional
    public CmCode addBoard(BoardEditDto boardEditDto) {
        if (boardManageRepository.existsById(boardEditDto.getCmCd())) {
            throw new IllegalArgumentException("이미 존재하는 게시판 코드입니다.");
        }
        if (boardEditDto.getCmCdName() == null || boardEditDto.getCmCdName().isEmpty()) {
            throw new IllegalArgumentException("게시판명은 필수 항목입니다.");
        }
        if (boardEditDto.getCmCdComment() == null || boardEditDto.getCmCdComment().isEmpty()) {
            throw new IllegalArgumentException("게시판 설명은 필수 항목입니다.");
        }
        CmCode cmCode = new CmCode();
        cmCode.setCmCd(boardEditDto.getCmCd());
        cmCode.setUpCmCd(boardEditDto.getUpCmCd());
        cmCode.setCmCdName(boardEditDto.getCmCdName());
        cmCode.setCmCdComment(boardEditDto.getCmCdComment());
        return boardManageRepository.save(cmCode);
    }

    // 게시판 수정
    @Transactional
    public CmCode updateBoard(BoardEditDto boardEditDto) {
        if (boardEditDto.getCmCdName() == null || boardEditDto.getCmCdName().isEmpty()) {
            throw new IllegalArgumentException("게시판명은 필수 항목입니다.");
        }
        if (boardEditDto.getCmCdComment() == null || boardEditDto.getCmCdComment().isEmpty()) {
            throw new IllegalArgumentException("게시판 설명은 필수 항목입니다.");
        }
        CmCode cmCode = boardManageRepository.findById(boardEditDto.getCmCd()).orElseThrow(() -> new RuntimeException("Board not found"));
        cmCode.setUpCmCd(boardEditDto.getUpCmCd());
        cmCode.setCmCdName(boardEditDto.getCmCdName());
        cmCode.setCmCdComment(boardEditDto.getCmCdComment());
        return boardManageRepository.save(cmCode);
    }

    @Transactional
    public void deleteBoard(String cmCd) {
        // 하위 게시판 삭제 로그
        boardManageRepository.deleteByUpCmCd(cmCd);

        // 상위 게시판 삭제 로그
        boardManageRepository.deleteById(cmCd);
    }

    @Transactional
    public void reactivateBoard(String cmCd) {
        boardManageRepository.reactivateBoard(cmCd);
    }

}
