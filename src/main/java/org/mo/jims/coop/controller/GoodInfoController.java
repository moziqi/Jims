package org.mo.jims.coop.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mo.jims.coop.dto.GoodInfoDTO;
import org.mo.jims.coop.entity.GoodInfo;
import org.mo.jims.coop.entity.ProviderInfo;
import org.mo.jims.coop.service.GoodInfoService;
import org.mo.jims.coop.service.ProviderInfoService;
import org.mo.open.common.converter.SpringDateConverter;
import org.mo.open.common.exception.MyRuntimeException;
import org.mo.open.common.util.JsonResponse;
import org.mo.open.common.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class GoodInfoController {
	
	private static Logger logger = LoggerFactory.getLogger(GoodInfoController.class);
	
	private GoodInfoService goodInfoService;
	
	private ProviderInfoService providerInfoService;

	@RequestMapping(value = "coop/shangpinguanli.html", method = RequestMethod.GET)
	public ModelAndView show(ModelMap model) {
		model.put("baseActive", "baseManage");
		model.put("active", "ShangPinGuanLi");
		logger.info("进入商品管理界面");
		return new ModelAndView("coop/baseManage/ShangPinGuanLi");
	}
	
	@RequestMapping(value="coop/searchByProviderName",method=RequestMethod.GET)
	@ResponseBody
	public List<GoodInfo> searchProviderByName(@RequestParam(required=true)String name){
		String newStr = "";
		try {
			// 编码有问题,get传过来的参数
			newStr = new String(name.getBytes("iso8859-1"), "UTF-8");
			//TODO:测试用
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<GoodInfo> goodInfoByProvider = goodInfoService.getGoodInfoByProvider(newStr);
		return goodInfoByProvider;
	}
	
	@RequestMapping(value="coop/searchGoodById",method=RequestMethod.POST)
	@ResponseBody
	public GoodInfo searchById(@RequestParam(required=true)  final String searchId){
		String[] ids = searchId.split(",");
		GoodInfo goodInfo = null;
		if (ids.length < 1) {
			goodInfo = goodInfoService.getGoodInfoByPK(searchId);
		} else {
			goodInfo = goodInfoService.getGoodInfoByPK(ids[0]);
		}
		//TODO:测试用
		System.out.println("search-----"+goodInfo.toString());
		return goodInfo;
	}

	@RequestMapping(value = "coop/pageOfGoodsByCriteria", method = RequestMethod.GET)
	@ResponseBody
	public Page<GoodInfo> listByCriteria(
			@RequestParam(required = true, defaultValue = "1") int page,
			@RequestParam(required = true, defaultValue = "10") int size,
			@RequestParam final String name,
			@RequestParam final String beginTime,
			@RequestParam final String endTime) {
		String newStr = "";
		try {
			// 编码有问题,get传过来的参数
			newStr = new String(name.getBytes("iso8859-1"), "UTF-8");
			//TODO:测试用
			System.out.println(beginTime + "---" + endTime);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SpringDateConverter springDateConverter = new SpringDateConverter();
		Page<GoodInfo> goodInfoByNameOrAbbreviation = goodInfoService
				.getGoodInfoByCriteria(newStr,
						springDateConverter.convert(beginTime),
						springDateConverter.convert(endTime), page, size);
		return goodInfoByNameOrAbbreviation;
	}
	
	@RequestMapping(value = "admin/editGood", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, String> edit(@RequestBody final GoodInfoDTO goodInfoDTO,
			HttpServletResponse response, HttpSession session){
		//TODO:测试用
		System.out.println("edit-----"+goodInfoDTO.toString());
		final Map<String, String> modelMap = validateCutomerInfoDTO(goodInfoDTO);//校验表单
		String formtoken = goodInfoDTO.getFormtoken();
		String token = (String) session.getAttribute("GoodEidtToken");
		if (formtoken.equals(token)) {//防止重复提及表单
			if (modelMap.isEmpty()) {//对表单校验,空继续执行
				modelMap.put("success", "ok");
				//启动线程,1.7新特性
				ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
				newCachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
							GoodInfo entity = goodInfoDTO.toEditObject();
							goodInfoService.alterGoodInfo(entity);
						} catch (InterruptedException e) {
							modelMap.put("tip", "修改失败");
						}
					}
				});
				session.removeAttribute("GoodEidtToken");
			}
		}else {
			modelMap.put("tip", "请不要重复提交");
		}
		return modelMap;
	}
	

	@RequestMapping(value = "admin/deleteGood", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse delete(@RequestParam(required=true) String deleteId) {
		JsonResponse jsonResponse =null;
		String[] ids = deleteId.split(",");
		if (ids.length > 1) {
			boolean batchRemove = goodInfoService.batchRemove(ids);
			 jsonResponse = deleteTip(batchRemove);
		}
		boolean removeByPK = goodInfoService.removeGoodInfoByPK(deleteId);
		jsonResponse = deleteTip(removeByPK);
		return jsonResponse;
	}

	@RequestMapping(value = "admin/addGood", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, String> add(
			@RequestBody final GoodInfoDTO addGoodInfoDTO,
			HttpServletResponse response, HttpSession session) {
		final Map<String, String> modelMap = validateCutomerInfoDTO(addGoodInfoDTO);//校验表单
		String formtoken = addGoodInfoDTO.getFormtoken();
		String token = (String) session.getAttribute("token");
		if (formtoken.equals(token)) {//防止重复提及表单
			if (modelMap.isEmpty()) {//对表单校验,空继续执行
				modelMap.put("success", "ok");
				//启动线程,1.7新特性
				ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
				newCachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
							GoodInfo entity = addGoodInfoDTO.toAddObject();
							//TODO 商品添加好没做啊,记得来修改啊,关于多对多的问题啊
							ProviderInfo providerInfoByName = providerInfoService.getProviderInfoByName(addGoodInfoDTO.getProviderName());
							if (providerInfoByName != null) {
								goodInfoService.saveGoodInfo(entity,
										providerInfoByName);
							}else{
								modelMap.put("tip", "供应商不存在哦");
							}
						} catch (InterruptedException e) {
							modelMap.put("tip", "保存失败");
							throw new MyRuntimeException(
									"保存失败");
						}
					}
				});
				session.removeAttribute("token");
			}
		}else {
			modelMap.put("tip", "请不要重复提交");
		}
		return modelMap;
	}

	private Map<String, String> validateCutomerInfoDTO(GoodInfoDTO goodInfoDTO) {
		Map<String, String> modelMap = new HashMap<String, String>();
		if ("".equals(goodInfoDTO.getName())) {
			modelMap.put("name", "商品名称不能为空");
		}
		if ("".equals(goodInfoDTO.getAbbreviation())) {
			modelMap.put("abbreviation", "简称不能为空");
		}
		if ("".equals(goodInfoDTO.getApprovalNum())) {
			modelMap.put("address", "批准文号不能为空");
		}
		if ("".equals(goodInfoDTO.getOrigin())) {
			modelMap.put("origin", "产地不能为空");
		}
		if ("".equals(goodInfoDTO.getUnits())) {
			modelMap.put("units", "单位不能为空");
		}
		if ("".equals(goodInfoDTO.getPack())) {
			modelMap.put("pack", "包装不能为空");
		}
		if ("".equals(goodInfoDTO.getApprovalNum())) {
			modelMap.put("approvalNum", "批准文号不能为空");
		}
		if ("".equals(goodInfoDTO.getBatchNum())) {
			modelMap.put("batchNum", "批号不能为空");
		}
		return modelMap;
	}
	
	private JsonResponse deleteTip(boolean removeByPK) {
		JsonResponse jsonResponse = new JsonResponse();
		if (removeByPK) {
			jsonResponse.setSuccess(true);
			jsonResponse.setMessage("删除成功");
		} else {
			jsonResponse.setSuccess(false);
			jsonResponse.setMessage("删除失败");
		}
		return jsonResponse;
	}
	
	public GoodInfoService getGoodInfoService() {
		return goodInfoService;
	}

	@Resource(name = "goodInfoService")
	public void setGoodInfoService(GoodInfoService goodInfoService) {
		this.goodInfoService = goodInfoService;
	}

	public ProviderInfoService getProviderInfoService() {
		return providerInfoService;
	}
	
	@Resource(name = "providerInfoService")
	public void setProviderInfoService(ProviderInfoService providerInfoService) {
		this.providerInfoService = providerInfoService;
	}
	
	
}