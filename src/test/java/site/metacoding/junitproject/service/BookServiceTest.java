package site.metacoding.junitproject.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.util.MailSender;
import site.metacoding.junitproject.web.dto.BookRespDto;
import site.metacoding.junitproject.web.dto.BookSaveReqDto;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock  // 가짜 환경(Mockito)에 뜬다.
    private BookRepository bookRepository; 

    @Mock  // 가짜 환경(Mockito)에 뜬다.
    private MailSender mailSender; 

    // 문제점 -> 서비스만 테스트하고 싶은데, 레포지토리 레이어가 함께 테스트 된다는 점!!
    @Test
    public void 책등록하기_test(){
        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit");
        dto.setAuthor("메타코딩");

        // stub (가설)
        // when(bookRepository.save(dto.toEntity())).thenReturn(dto.toEntity());
        // => 위의 경우는 save 매개변수에 dto.toEntity()와 동일한 객체(같은 메모리주소)여야 발동함. 그렇지 않으면 오류발생
        // 따라서 any()로 수정해서 테스트
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
                            
        when(mailSender.send()).thenReturn(true);
        
        // when
        BookRespDto BookRespDto = bookService.책등록하기(dto);

        // then
        //assertEquals(dto.getTitle(), BookRespDto.getTitle());
        //assertEquals(dto.getAuthor(), BookRespDto.getAuthor());
        assertThat(dto.getTitle()).isEqualTo(BookRespDto.getTitle());
        assertThat(dto.getAuthor()).isEqualTo(BookRespDto.getAuthor());

    }
}
