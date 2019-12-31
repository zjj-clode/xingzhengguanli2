package com.cloudinte.zhaosheng.modules.chat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinte.zhaosheng.modules.chat.entity.ChatCustom;
import com.cloudinte.zhaosheng.modules.chat.service.ChatCustomService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 在线客服人员Controller
 *
 * @author pengrz
 * @version 2017-09-09
 */
@Controller
@RequestMapping(value = "${adminPath}/chat/chatCustom")
public class ChatCustomController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "在线客服人员";

	@Autowired
	private ChatCustomService chatCustomService;

	@ModelAttribute
	public ChatCustom get(@RequestParam(required = false) String id) {
		ChatCustom entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = chatCustomService.get(id);
		}
		if (entity == null) {
			entity = new ChatCustom();
		}
		return entity;
	}

	@RequiresPermissions("chat:chatCustom:view")
	@RequestMapping(value = { "list", "" })
	public String list(ChatCustom chatCustom, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ChatCustom> page = chatCustomService.findPage(new Page<ChatCustom>(request, response), chatCustom);
		model.addAttribute("page", page);
		model.addAttribute("ename", "chatCustom");
		setBase64EncodedQueryStringToEntity(request, chatCustom);
		return "modules/chat/chatCustom/chatCustomList";
	}

	@RequiresPermissions("chat:chatCustom:view")
	@RequestMapping(value = "form")
	public String form(ChatCustom chatCustom, Model model) {
		if (StringUtils.isBlank(chatCustom.getId())) {
			if (chatCustom.getPriority() == null) {
				chatCustom.setPriority(5);
			}
		}
		model.addAttribute("chatCustom", chatCustom);
		model.addAttribute("ename", "chatCustom");
		return "modules/chat/chatCustom/chatCustomForm";
	}

	@RequiresPermissions("chat:chatCustom:edit")
	@RequestMapping(value = "save")
	public String save(ChatCustom chatCustom, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, chatCustom)) {
			return form(chatCustom, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(chatCustom.getId()) ? "保存" + FUNCTION_NAME_SIMPLE + "成功" : "修改" + FUNCTION_NAME_SIMPLE + "成功");
		chatCustomService.save(chatCustom);
		return "redirect:" + adminPath + "/chat/chatCustom/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(chatCustom));
	}

	@RequiresPermissions("chat:chatCustom:edit")
	@RequestMapping(value = "delete")
	public String delete(ChatCustom chatCustom, RedirectAttributes redirectAttributes) {
		chatCustomService.delete(chatCustom);
		addMessage(redirectAttributes, "删除" + FUNCTION_NAME_SIMPLE + "成功");
		return "redirect:" + adminPath + "/chat/chatCustom/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(chatCustom));
	}

}