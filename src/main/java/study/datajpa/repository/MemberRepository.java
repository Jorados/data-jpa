package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.domain.Member;
import study.datajpa.dto.MemberDto;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

//스프링 data jpa는 트랜잭션을 따로 서비스계층에서나 리포지토리에서나 설정해주지않아도
//알아서 내부적으로 트랜잭션을 한다. //트랜잭션이 리포지토리 계층에 걸려있음.
//transaction(readonly=true)를 하면 기본적으로 트랜젝션이 끝나면 flush를 해서 영속성 컨텍스트에 있는
//정보를 디비에 보내는데 단순 조회용일 때는 flush를 생략해서 약간의 성능 향상을 얻을 수 있음

//data jpa제공하는 save()는 새로운 엔티티는 persist(), 없으면 merge()
//merge()단점은 디비에 select 쿼리가 한번 나간다. 매우 안좋음
// -> 해결법 merge()는 왠만하면 쓰지말고 변경감지(더티체킹)로 디비에 업데이트 하자. //트렌잭션이 알아서 커밋,플러쉬해줌.


public interface MemberRepository extends JpaRepository<Member,Long> , MemberRepositoryCustom {

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

    //반환타입 컬렉션,단건,단건(옵셔녈)
    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

    @Query(value ="select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    @Modifying(clearAutomatically = true) //쿼리 나가고 클리어 과정 자동
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    //Member를 조회할 때 연관되어있는 team을 같이 한방에 다 조회
    List<Member> findMemberFetchJoin();

    //방식 1
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //방식 2
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //방식 3 //member의 username 조회하면서 team 진짜 객체 정보도 조회 가능.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name="org.hibernate.readOnly", value="true"))
    Member findReadOnlyByUsername(String username);

    //조회하면서 변경못하게 Lock걸기
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    //네이티브쿼리
    @Query(value="select * from Member where username =?", nativeQuery = true)
    Member findByNativeQuery(String username);

    //Projection을 이용한 조회
    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

    //Projection을 이용한 정적쿼리를 네이티브 쿼리로 작성
    //장점 : 페이징 가능
    @Query(value = "select m.member_id as id, m.username, t.name as teamName" +
            "from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);

}
