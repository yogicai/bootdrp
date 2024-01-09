package com.bootdo.modular.system.controller;

import com.bootdo.modular.system.domain.ContentDO;
import com.bootdo.modular.system.service.BlogContentService;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.core.pojo.response.PageR;
import com.bootdo.core.pojo.request.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@RequestMapping("/blog")
@Controller
public class BlogController {
	@Resource
	private BlogContentService blogContentService;

	@GetMapping()
	String blog() {
		return "system/blog/index/main";
	}

	@ResponseBody
	@GetMapping("/open/list")
	public PageR opentList(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<ContentDO> bContentList = blogContentService.list(query);
		int total = blogContentService.count(query);

		PageR pageR = new PageR(bContentList, total);

		return pageR;
	}

	@GetMapping("/open/post/{cid}")
	String post(@PathVariable("cid") Long cid, Model model) {
		ContentDO bContentDO = blogContentService.get(cid);
		model.addAttribute("bContent", bContentDO);
		model.addAttribute("gtmModified", DateUtils.format(bContentDO.getGtmModified()));
		return "system/blog/index/post";
	}

	@GetMapping("/open/page/{categories}")
	String about(@PathVariable("categories") String categories, Model model) {
		Map<String, Object> map = new HashMap<>(16);
		map.put("categories", categories);
		ContentDO bContentDO = blogContentService.list(map).get(0);
		model.addAttribute("bContent", bContentDO);
		return "system/blog/index/post";
	}
}
