package study.datajpa.repository;

//엔티티 대신에 DTO를 편리하게 조회할 때 사용
//Projections 딱  필요한 엔티티 데이터만 조회할수있다.
public interface MemberProjection {
    Long getId();
    String getUsername();
    String getTeamName();
}
