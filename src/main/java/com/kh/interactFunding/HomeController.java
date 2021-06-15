package com.kh.interactFunding;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.interactFunding.common.util.HelloSpringUtils;
import com.kh.interactFunding.funding.model.service.FundingService;
import com.kh.interactFunding.funding.model.vo.Funding;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j

public class HomeController {
	@Autowired
	private FundingService fundingService;

	// 펀딩하기
	@RequestMapping(value = "/",method = RequestMethod.GET )
	public String  indexfundingList(Model model ,
									HttpSession session,
									//코드 간결하게 하였습니다.
									@CookieValue(value="count", defaultValue="0", required=true) String value,
									HttpServletResponse response,
									HttpServletRequest request) {
				try{
					int i = Integer.parseInt(value);
					value = Integer.toString(++i);
				}catch(Exception ex){
					value = "1";
				}
		try {
			
			Cookie cookie = new Cookie("count", value);
			cookie.setMaxAge(60 * 60 * 24 * 365); // 1년간 유지
			cookie.setPath("/"); // /경로 이하에 모두 쿠키 적용
			response.addCookie(cookie);
			
			/*
			 * log.debug("cpage={}",cpage); final int limit =3; final int offset =(cpage -1
			 * ) * limit;
			 */
			 
			Map<String,Object> param =new HashMap<>();
			/*
			 * param.put("limit", limit); param.put("offset", offset);
			 */
			List<Funding>list =fundingService.indexfundingList();
			//earlyList 페이지 리스트
			List<Funding>earlylist= fundingService.indexEarlyList();
			//회원님이 좋아할 (쿠키)
			List<Funding>viewlist=fundingService.indexviewlist();
			
			
			log.debug("earlylist={}",earlylist);
			
			/*
			 * int totalContents=fundingService.indexTotalContents(); String url=
			 * request.getRequestURI(); log.debug("totalContents = {}, url = {}",
			 * totalContents, url); String pageBar =
			 * HelloSpringUtils.getPageBar(totalContents, cpage, limit, url);
			 */
			model.addAttribute("list",list);
			model.addAttribute("earlylist",earlylist);
			model.addAttribute("viewlist",viewlist);
		
			/* model.addAttribute("pageBar", pageBar); */
			// loginMember 세션속성으로 저장하려면, class에 @SessionAttributes로 등록
			log.info("list={}",list);
			log.info("earlylist={}",earlylist);
			log.info("viewlist={}",viewlist);
			
			/* log.info("pageBar={}",pageBar) */;
		}catch (Exception e) {
			log.error("메인페이지 조회가 안됩니다",e);
			throw e;
		}
		//404 에러 forward 해준다.
		return "forward:/index.jsp";
	}
}
