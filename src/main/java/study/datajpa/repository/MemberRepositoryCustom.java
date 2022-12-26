package study.datajpa.repository;

import study.datajpa.domain.Member;

import java.util.List;

//복잡한 쿼리를 구현할 때 보통 이렇게 커스텀 인터페이스를 만들어서 사용.
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
