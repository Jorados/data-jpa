package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
//@Query로 안될 때,복잡한 쿼리를 구현할 때 보통 이렇게 커스텀 인터페이스를 만들어서 사용.
//커스텀 구현체.
//(규칙) DataJpa 인터페이스 이름 MemberRepository랑 이름 맞추고 + Impl을 붙혀준다.
//이렇게 규칙에 맞추면 jpa가 알아서 조립해준다.
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom(){
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
