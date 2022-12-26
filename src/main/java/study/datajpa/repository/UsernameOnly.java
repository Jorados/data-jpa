package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

//인터페이스기반 Projections
//엔티티 대신에 DTO를 편리하게 조회할 때 사용
//Projections 딱  필요한 엔티티 데이터만 조회할수있다.
//이건 인터페이스고, data jpa가 구현체까지 (proxy로)만들어서 데이터 담아서 반환해준다.
public interface UsernameOnly {
    String getUsername();
}
