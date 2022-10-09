package com.kh.Ulsan_Hanmadang;

import com.kh.Ulsan_Hanmadang.domain.PEvent;
import com.kh.Ulsan_Hanmadang.domain.PromotionHome;
import com.kh.Ulsan_Hanmadang.domain.common.code.CodeDAO;
import com.kh.Ulsan_Hanmadang.domain.common.paging.FindCriteria;
import com.kh.Ulsan_Hanmadang.domain.member.Member;
import com.kh.Ulsan_Hanmadang.domain.member.svc.MemberSVC;
import com.kh.Ulsan_Hanmadang.domain.post.svc.PostSVC;
import com.kh.Ulsan_Hanmadang.web.Code;
import com.kh.Ulsan_Hanmadang.web.form.member.LoginForm;
import com.kh.Ulsan_Hanmadang.web.session.LoginMember;
import com.kh.Ulsan_Hanmadang.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

  final MemberSVC memberSVC;
  private final PostSVC postSVC;
  private final CodeDAO codeDAO;
  String test_date = "2022.09.25";

  @Autowired
  @Qualifier("fc6") //동일한 타입의 객체가 여러개있을때 빈이름을 명시적으로 지정해서 주입받을때
  private FindCriteria fc;

  //게시판 코드,디코드 가져오기
  @ModelAttribute("classifier")
  public List<Code> classifier(){
    return codeDAO.code("B01");
  }

  @GetMapping
  private String home(Model model) {

    List<PEvent> elist = postSVC.getDisplyEvents(test_date);
    log.info("elist={}", elist);

    List<PromotionHome> plist = postSVC.getDisplyProms(test_date);
    log.info("plist={}", plist);

    model.addAttribute("elist", elist);
    model.addAttribute("plist", plist);

    return "home";
  }

  //로그인 화면
  @GetMapping("/login")
  public String loginForm(@ModelAttribute("form") LoginForm loginForm){

    return "member/login";
  }


  //로그인 처리
  @PostMapping("/login")
  public String login(@Valid @ModelAttribute("form") LoginForm loginForm,
                      BindingResult bindingResult,
                      HttpServletRequest request,
                      @RequestParam(value = "requestURI",required = false,defaultValue = "/") String requestURI){

    //기본 검증
    if (bindingResult.hasErrors()) {
      //log.info("bindingResult={}",bindingResult);
      return "member/login";
    }

    //회원유무
    Optional<Member> member = memberSVC.login(loginForm.getEmail(), loginForm.getPassword());
    //log.info("member={}", member);
    if(member.isEmpty()){
      bindingResult.reject("LoginForm.login","회원정보가 없습니다.");
      return "member/login";
    }

    //회원인경우
    Member findedMember = member.get();

    //세션에 회원정보 저장
    LoginMember loginMember = new LoginMember(findedMember.getMember_id(), findedMember.getEmail(),findedMember.getPassword(),
        findedMember.getName(),findedMember.getNickname(), findedMember.getPhone(), findedMember.getBirthday(),
        findedMember.getSms_service(), findedMember.getEmail_service(),findedMember.getGubun(), findedMember.getCdate(),findedMember.getUdate());

    //request.getSession(true) : 세션정보가 있으면 가져오고 없으면 세션을 많듬
    HttpSession session = request.getSession(true);
    session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

    String URI = request.getQueryString();

    if(URI == null) {
      // 바로 로그인 눌렀을때
      //log.info("URI str null? ={}",URI);

      return "redirect:/";
    }
    // 게시판 열람은 비로그인도 사용가능함. [예외 상황용 코드]
//    else if( URI.contains("/post/list")){
//      // 게시판 눌렀을때
//      URI = URI.substring(URI.lastIndexOf("=")+1);
//      if(URI.contains("&")){
//        URI = URI.replace("&","?");
//
//        if(URI.contains("%3D")){
//          URI = URI.replace("%3D","=");
//        }
//      }
//      log.info("URI2 str ={}",URI);
//
//      return "redirect:"+URI;
//    }
    else if(URI.contains("/members//info")){
      // 내 정보 눌렀을때
      return "redirect:/members/"+loginMember.getEmail()+"/info";
    }
    else {
      // 예외 페이지는 우선 홈으로
      //log.info("URI str else? ={}",URI);

      return "redirect:/";
    }
  }


  //로그아웃
  @GetMapping("/logout")
  public String logout(HttpServletRequest request){
    //request.getSession(false) : 세션정보가 있으면 가져오고 없으면 세션을 만들지 않음
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return "redirect:/"; //초기화면 이동
  }
}