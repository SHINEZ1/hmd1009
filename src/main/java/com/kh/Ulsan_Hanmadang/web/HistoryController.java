package com.kh.Ulsan_Hanmadang.web;

import com.kh.Ulsan_Hanmadang.domain.member.svc.MemberSVC;
import com.kh.Ulsan_Hanmadang.domain.post.Post;
import com.kh.Ulsan_Hanmadang.domain.post.svc.PostSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

  final MemberSVC memberSVC;

  final PostSVC postSVC;

  // 내가 쓴 글 GET
  @GetMapping("/{email}/post")
  public String post(@PathVariable String email, Model model){

    List<Post> posts = postSVC.myPost(email);
    log.info("내가 쓴 글 = {}",posts);


    model.addAttribute("postList", posts);

    return "member/history/history_post";
  }

  // 내가 쓴 댓글 GET
  @GetMapping("/{email}/comment")
  public String comment(@PathVariable String email){

    log.info("이메일={}",email);
    return "member/history/history_comment";
  }

  // 좋아요 목록 GET
  @GetMapping("/{email}/list")
  public String likelist(@PathVariable String email){
    log.info("이메일={}",email);
    return "member/history/history_list";
  }
}