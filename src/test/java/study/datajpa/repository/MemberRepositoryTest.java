package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.dto.MemberDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndGreaterThen(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void findHelloBy(){
        List<Member> helloBy = memberRepository.findHelloBy();
        List<Member> hello = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }


    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
    @Test
    public void returnType(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        System.out.println("aaa size = " + aaa.size());
    }
    @Test
    public void paging(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age =10;
        PageRequest pageRequest = PageRequest.of(0,3,Sort.by(Sort.Direction.DESC,"username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //(실무)api 리턴할때는 엔티티 노출이 안되기때문에 map에 담아서 리턴
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        //then
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void SlicePaging(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age =10;
        PageRequest pageRequest = PageRequest.of(0,3,Sort.by(Sort.Direction.DESC,"username"));

        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        //assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        //assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",19));
        memberRepository.save(new Member("member3",20));
        memberRepository.save(new Member("member4",21));
        memberRepository.save(new Member("member5",40));
        //when
        //벌크연산 문제점 -> (member5) 디비에 바로 +1 쿼리를 때리는데 영속성
        //                 컨텍스트에는 아직 40살로 정보가 담겨있다.
        //해결법 -> 벌크연산 후에는 영속성컨텍스트를 한번 다 날려야함
        int resultCount = memberRepository.bulkAgePlus(20);
//        em.flush(); //혹시 남아있는 변경되지 않은 정보들 db에 반영
//        em.clear(); //날리기
        //아니면 @Modifying 에 clearAutomatically = true 설정하기

        List<Member> result = memberRepository.findListByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);
        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        //Member member3 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush(); //영속성 컨택스트정보 (캐시정보)를 디비에 다 반영하고
        em.clear(); //날리기

        //when  -> N + 1 문제 -> 10개를 찾기 위해서 한번의 쿼리를 날렸는데 10개의 쿼리가 더 나가는 것
        //      -> jpa 에서는 이 문제를 fetch join 으로 해결한다.
        //List<Member> members = memberRepository.findAll();
        //List<Member> members = memberRepository.findMemberFetchJoin();
        List<Member> members = memberRepository.findAll();
        //List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            //Team 의 실제 데이터(getName)를 조회하기 때문에 Team 쿼리가 나가게된다.
            //지연로징딩 Lazy 라서 member를 조회할때 연관관계인 team의 proxy객체가 조회된다.
            System.out.println("member.getTeam() = " + member.getTeam().getName());
        }
    }
    @Test
    public void queryHint(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //스냅샷 : 향후 변경 감지를 위해서 원본을 복사해서 만들어두는 객체 , 변경 감지가 일어났을 때 1차캐시에 있는 원본 객체가 중간에 변경되었는지 이 스냅샷으로 확인하게 된다.
        //@QueryHint 를 사용하면 스냅샷도 없고, 변경감지를 무시하고 디비에 정보를 저장하지 않고 member를 조회할때
        //jpa 캐시(em)에서 member2 라는 이름을 조회만 할수있다. //하이버네이트에서 제공하는 기능.
        //조회용으로만 사용하기 위해서 업데이트 쿼리를 보내지 않고 최적화하는 법.
        //업데이트 쿼리 방지
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        //member1의 이름을 바꿔버리고 flush 해주면 변경감지(더티 체킹)라는 기능이 작동해서 디비에
        //업데이트 쿼리가 나감. 정보를 반영한다.
        em.flush();

    }

    @Test
    public void Lock(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByUsername("member1");
    }
}
