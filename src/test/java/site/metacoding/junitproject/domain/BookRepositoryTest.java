package site.metacoding.junitproject.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩
public class BookRepositoryTest {

    @Autowired // DI DB와 관련된 컴포넌트를 메모리에 로딩했기 떄문에 주입 가능.
    private BookRepository bookRepository;

    // @BeforeAll // 테스트 시작전에 한번만 실행
    @BeforeEach // 각 테스트 시작전에 한번씩 실행
    public void dataReady() {
        System.out.println("======================================================== 데이터 준비");
        String title = "junit";
        String author = "겟인데어";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    } // 트랜잭션 종료 (저장된 데이터를 초기화함)
      // 가정 1 : [ 데이터준비() + 1. 책등록 ] (T) , [ 데이터준비() + 2. 책목록보기] (T) -> 사이즈 1 (검증 완료)
      // 가정 2 : [ 데이터준비() + 1. 책등록 + 데이터준비() + 2. 책목록보기 ] (T) -> 사이즈 2 (검증 실패)

    // JUnit 테스트
    // 1. 테스트 메서드 3개가 있다. (순서 보장이 안된다) - Order() 어노테이션
    // (1) 메서드 1
    // (2) 메서드 2
    // (3) 메서드 3
    // 2. 테스트 메서드가 하나 실행 후 종료되면 데이터가 초기화된다. - Transactional() 어노테이션
    // (1) 1건
    // (2) 2건
    // -> 트랜잭션 종료 -> 데이터 초기화
    // *** primary key_auto_increment 값이 초기화가 안됨

    // 1. 책 등록
    @Test
    public void 책등록_test() {
        // given (데이터 준비)
        String title = "junit5";
        String author = "메타코딩";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when (테스트 실행)
        Book bookPS = bookRepository.save(book);

        // then (검증)
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    } // 트랜잭션 종료 (저장된 데이터를 초기화함)

    // 2. 책 목록보기
    @Test
    public void 책목록보기_test() {
        // given
        String title = "junit";
        String author = "겟인데어";
        // when
        List<Book> booksPS = bookRepository.findAll();

        System.out.println("사이즈 : ============================================     : " + booksPS.size());
        // then
        assertEquals(title, booksPS.get(0).getTitle());
        assertEquals(author, booksPS.get(0).getAuthor());
    } // 트랜잭션 종료 (저장된 데이터를 초기화함)

    // 3. 책 한건보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책한건보기_test() {
        // given
        String title = "junit";
        String author = "겟인데어";

        // when
        Book bookPS = bookRepository.findById(1L).get();

        // then
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    } // 트랜잭션 종료 (저장된 데이터를 초기화함)

    // 4. 책 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test() {
        // given
        Long id = 1L;

        // when
        bookRepository.deleteById(id);

        // then
        assertFalse(bookRepository.findById(id).isPresent());

    }

    // 실행 전 junit, 겟인데어 들어옴
    // 5. 책 수정
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책수정_test() {
        // given
        Long id = 1L;
        String title = "junit5";
        String author = "메타코딩";
        Book book = new Book(id, title, author);

        // when
        // bookRepository.findAll().stream()
        // .forEach(b -> {
        // System.out.println(b.getId());
        // System.out.println(b.getTitle());
        // System.out.println(b.getAuthor());
        // System.out.println("======================= 1");
        // });

        Book bookPs = bookRepository.save(book);

        // bookRepository.findAll().stream()
        // .forEach(b -> {
        // System.out.println(b.getId());
        // System.out.println(b.getTitle());
        // System.out.println(b.getAuthor());
        // System.out.println("======================= 2");
        // });

        // then
        assertEquals(id, bookPs.getId());
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());
    }
}
