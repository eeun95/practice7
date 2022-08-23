package spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChangePasswordService {

    @Autowired
    private MemberDao memberDao;

    @Transactional
    //(value = "test") 이 속성 없으면 PlatformTransactionManager 타입으로 찾아서 사용
    public void changePassword(String email, String oldPwd, String newPwd) {
        Member member = memberDao.selectByEmail(email);
        if(member == null)
            throw new MemberNotFoundException();

        member.changePassword(oldPwd, newPwd);

        memberDao.update(member);
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
}
