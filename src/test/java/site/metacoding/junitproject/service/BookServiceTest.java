package site.metacoding.junitproject.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.util.MailSender;
import site.metacoding.junitproject.web.dto.BookRespDto;
import site.metacoding.junitproject.web.dto.BookSaveReqDto;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock // 가짜 환경(Mockito)에 뜬다.
    private BookRepository bookRepository;

    @Mock // 가짜 환경(Mockito)에 뜬다.
    private MailSender mailSender;

    // 문제점 -> 서비스만 테스트하고 싶은데, 레포지토리 레이어가 함께 테스트 된다는 점!!
    @Test
    public void 책등록하기_test() {
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
        BookRespDto bookRespDtoList = bookService.책등록하기(dto);

        // then
        // assertEquals(dto.getTitle(), bookRespDtoList.getTitle());
        // assertEquals(dto.getAuthor(), bookRespDtoList.getAuthor());
        assertThat(dto.getTitle()).isEqualTo(bookRespDtoList.getTitle());
        assertThat(dto.getAuthor()).isEqualTo(bookRespDtoList.getAuthor());

    }

    @Test
    public void 책목록보기_테스트() {

        // given (파라미터로 들어올 데이터)

        // stub (가설)
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit 강의", "메타코딩"));
        books.add(new Book(2L, "spring 강의", "겟인데어"));
        when(bookRepository.findAll()).thenReturn(books);

        // when (실행)
        List<BookRespDto> bookRespDtoList = bookService.책목록보기();

        // print
        // bookRespDtoList.stream().forEach(dto -> {
        // System.out.println("======================= 테스트");
        // System.out.println(dto.getId());
        // System.out.println(dto.getTitle());
        // });

        // then (검증)
        assertThat(bookRespDtoList.get(0).getTitle()).isEqualTo("junit 강의");
        assertThat(bookRespDtoList.get(0).getAuthor()).isEqualTo("메타코딩");
        assertThat(bookRespDtoList.get(1).getTitle()).isEqualTo("spring 강의");
        assertThat(bookRespDtoList.get(1).getAuthor()).isEqualTo("겟인데어");

    }

    @Test
    public void 책한건보기_테스트() {
        // given
        Long id = 1L;

        // stub
        Book book = new Book(1L, "junit강의", "메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.책한건보기(id);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    public void 책수정하기_테스트() {
        // given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("spring강의"); // junit강의
        dto.setAuthor("겟인데어"); // 메타코딩

        // stub
        Book book = new Book(1L, "junit강의", "메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.책수정하기(id, dto);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
    }
}
