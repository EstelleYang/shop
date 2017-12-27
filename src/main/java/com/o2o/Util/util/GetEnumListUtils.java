package com.o2o.Util.util;

import com.deppon.cubc.around.client.cod.constant.CodAuditConstants;
import com.deppon.cubc.around.client.cod.constant.CodFlagConstants;
import com.deppon.cubc.around.client.cod.constant.CodGenreConstants;
import com.deppon.cubc.asset.client.verify.constant.VerifyIsSystemConstants;
import com.deppon.cubc.asset.client.verify.constant.VerifyIsYesConstants;
import com.deppon.cubc.asset.client.verify.constant.VerifyTypeConstants;
import com.deppon.cubc.commons.lang.MEnum;
import com.deppon.cubc.trade.order.client.constant.CustomerTypeConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 公用下拉列表 枚举调用Controller
 * @author 347251  申圣
 * 
 */
@Controller
@RequestMapping(value = "/getEnumList")
public class GetEnumListUtils {
	
	/**
	 * 是否有效
	 * @return
	 */
	@RequestMapping("isActive")
	public @ResponseBody
    Object isActive(){
		return MEnum.getEnumList(VerifyIsYesConstants.class);
	}
	
	
	/**
	 * 核销方式
	 * @return
	 */
	@RequestMapping("writeoffCreateType")
	public @ResponseBody
    Object writeoffCreateType(){
		return MEnum.getEnumList(VerifyIsSystemConstants.class);
	}
	
	
	/**
	 *  核销类型
	 * @return
	 */
	@RequestMapping("writeoffType")
	public @ResponseBody
    Object writeoffType(){
		return MEnum.getEnumList(VerifyTypeConstants.class);
	}
	
	
	/**
	 * 客户类型
	 * @return
	 */
	@RequestMapping("customerType")
	public @ResponseBody
    Object customerType(){
		return MEnum.getEnumList(CustomerTypeConstants.class);
	}
	
	/**
	 * 账户类型
	 * @return
	 */
	@RequestMapping("accountGenre")
	public @ResponseBody
    Object accountGenre(){
		return MEnum.getEnumList(CodGenreConstants.class);
	}
	/**
	 * 账户性质
	 * @return
	 */
	@RequestMapping("accountType")
	public @ResponseBody
    Object accountType(){
		return MEnum.getEnumList(CodFlagConstants.class);
	}
	
	/**
	 * 代收货款审核状态
	 * @return
	 */
	@RequestMapping("codAuditType")
	public @ResponseBody
    Object codAuditType(){
		return MEnum.getEnumList(CodAuditConstants.class);
	}
	
	
}
