package site.metacoding.junitproject.web.dto.reponse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.junitproject.domain.Book;

@NoArgsConstructor
@Getter
@Setter
public class BookRespDto {

    private Long id;
    private String title;
    private String author;

    @Builder
    public BookRespDto(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    // 잘못 작성
    // public BookRespDto toDto(Book bookPS){
    // this.id = bookPS.getId();
    // this.title = bookPS.getTitle();
    // this.author = bookPS.getAuthor();
    // return this;
    // }

}