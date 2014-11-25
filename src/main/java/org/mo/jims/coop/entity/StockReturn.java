/***********************************************************************
 * Module:  TbStockReturn.java
 * Author:  YK
 * Purpose: Defines the Class TbStockReturn
 ***********************************************************************/
package org.mo.jims.coop.entity;

import java.io.Serializable;
import java.util.Date;

import org.mo.jims.coop.enumtype.Approval;
import org.mo.open.common.entity.User;
import org.mo.open.common.util.Sequence;

/**
 * 进货退货信息
 * 
 */
public class StockReturn implements Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.String id;

	private int varietyAmount;

	private float unitCost;

	private int amount;

	private float sum;

	private java.lang.String check_conclusion;

	private java.util.Date time;

	private java.lang.String clearingForm;

	private java.lang.String handler;

	private Approval approval;

	private ProviderInfo providerInfo;

	private User user;

	private GoodInfo goodInfo;

	public StockReturn() {
		super();
	}

	public StockReturn(String id, int varietyAmount, float unitCost,
			int amount, float sum, String check_conclusion, Date time,
			String clearingForm, String handler,Approval approval, ProviderInfo providerInfo,
			User user, GoodInfo goodInfo) {
		super();
		this.id = id;
		this.varietyAmount = varietyAmount;
		this.unitCost = unitCost;
		this.amount = amount;
		this.sum = sum;
		this.check_conclusion = check_conclusion;
		this.time = time;
		this.clearingForm = clearingForm;
		this.handler = handler;
		this.approval = approval;
		this.providerInfo = providerInfo;
		this.user = user;
		this.goodInfo = goodInfo;
	}

	public StockReturn(int varietyAmount, float unitCost, int amount,
			float sum, String check_conclusion, String clearingForm,
			String handler,Approval approval, ProviderInfo providerInfo, User user,
			GoodInfo goodInfo) {
		super();
		this.id = "stockr" + Sequence.nextId();
		this.varietyAmount = varietyAmount;
		this.unitCost = unitCost;
		this.amount = amount;
		this.sum = sum;
		this.check_conclusion = check_conclusion;
		this.clearingForm = clearingForm;
		this.handler = handler;
		this.approval = approval;
		this.providerInfo = providerInfo;
		this.goodInfo = goodInfo;
		this.user = user;
	}

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public int getVarietyAmount() {
		return varietyAmount;
	}

	public void setVarietyAmount(int varietyAmount) {
		this.varietyAmount = varietyAmount;
	}

	public float getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(float unitCost) {
		this.unitCost = unitCost;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public float getSum() {
		return sum;
	}

	public void setSum(float sum) {
		this.sum = sum;
	}

	public java.lang.String getCheck_conclusion() {
		return check_conclusion;
	}

	public void setCheck_conclusion(java.lang.String check_conclusion) {
		this.check_conclusion = check_conclusion;
	}

	public java.util.Date getTime() {
		return time;
	}

	public void setTime(java.util.Date time) {
		this.time = time;
	}

	public java.lang.String getClearingForm() {
		return clearingForm;
	}

	public void setClearingForm(java.lang.String clearingForm) {
		this.clearingForm = clearingForm;
	}

	public java.lang.String getHandler() {
		return handler;
	}

	public void setHandler(java.lang.String handler) {
		this.handler = handler;
	}

	public Approval getApproval() {
		return approval;
	}

	public void setApproval(Approval approval) {
		this.approval = approval;
	}

	public ProviderInfo getProviderInfo() {
		return providerInfo;
	}

	public void setProviderInfo(ProviderInfo providerInfo) {
		this.providerInfo = providerInfo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public GoodInfo getGoodInfo() {
		return goodInfo;
	}

	public void setGoodInfo(GoodInfo goodInfo) {
		this.goodInfo = goodInfo;
	}

	@Override
	public String toString() {
		return "StockReturn [id=" + id + ", varietyAmount=" + varietyAmount
				+ ", unitCost=" + unitCost + ", amount=" + amount + ", sum="
				+ sum + ", check_conclusion=" + check_conclusion + ", time="
				+ time + ", clearingForm=" + clearingForm + ", handler="
				+ handler + ", approval=" + approval.getContent()
				+ ", providerInfo=" + providerInfo.toString() + ", user="
				+ user.toString() + ", goodInfo=" + goodInfo.toString() + "]";
	}

}