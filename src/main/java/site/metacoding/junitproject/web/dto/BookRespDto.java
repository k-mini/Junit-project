package site.metacoding.junitproject.web.dto;

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

    public BookRespDto toDto(Book bookPs){
        this.id = bookPs.getId();
        this.title = bookPs.getTitle();
        this.author = bookPs.getAuthor();
        return this;
    }
}