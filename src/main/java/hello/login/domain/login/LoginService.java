package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    /**
     *
     * @param longinId
     * @param password
     * @return null 로그인 실패
     */
    public Member login(String longinId, String password){
        return memberRepository.findByLoginId(longinId)
                .filter(m->m.getPassword().equals(password))
                .orElse(null);
    }
}
