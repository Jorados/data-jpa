package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.domain.Member;
import study.datajpa.dto.MemberDto;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {
    //List<Member> findByUsername(String username);
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    List<Member> findHelloBy(); //find ... By 까지만 하면 전체조회
    List<Member> findTop3HelloBy();

    //네임드 쿼리 호출
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    //문법적인 에러를 다 잡아주는 장점
    @Query("select m from Member m where m.username =:username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " + "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);
}
