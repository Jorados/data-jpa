package study.datajpa.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Item {

    @Id @GeneratedValue //제네레이트벨류는 객체가 persist 되어야 만들어서 주입해준다.
    private Long id;
}
