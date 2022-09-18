package controller;

import com.fasterxml.classmate.MemberResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class RestMemberController {

    private MemberDao memberDao;
    private MemberRegisterService memberRegisterService;

    @GetMapping("/api/members")
    public List<Member> members() {
        return memberDao.selectAll();
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<Object> member(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Member member = memberDao.selectById(id);
        if (member == null) {
            //response.sendError(HttpServletResponse.SC_NOT_FOUND);
            //return null;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("no member"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    public void setMemberDao(MemberDao memberDao) {

        this.memberDao = memberDao;
    }

    public void setMemberRegisterService(MemberRegisterService memberRegisterService) {
        this.memberRegisterService = memberRegisterService;
    }

    @PostMapping("/api/members")
    public void newMember(@RequestBody @Valid RegisterRequest regReq, HttpServletResponse response) throws IOException {
        try {
            Long newMemberId = memberRegisterService.regist(regReq);
            response.setHeader("Location", "/api/members" + newMemberId);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (DuplicateMemberException duplicateMemberException) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoData() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("no member"));
    }
}
