package com.lanyuan.entity;

import com.lanyuan.annotation.TableSeg;
import com.lanyuan.util.FormMap;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * user实体表
 */
public class PhysicalExaminationSickRiskResult implements Serializable{


	private Long id;
	private Long examination_record_id;
	private Long check_item_id;
	private Integer check_item_type;
	private Long sick_risk_item_id;
	private BigDecimal risk_rout;
	private BigDecimal score;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExamination_record_id() {
		return examination_record_id;
	}

	public void setExamination_record_id(Long examination_record_id) {
		this.examination_record_id = examination_record_id;
	}

	public Long getCheck_item_id() {
		return check_item_id;
	}

	public void setCheck_item_id(Long check_item_id) {
		this.check_item_id = check_item_id;
	}

	public Integer getCheck_item_type() {
		return check_item_type;
	}

	public void setCheck_item_type(Integer check_item_type) {
		this.check_item_type = check_item_type;
	}

	public Long getSick_risk_item_id() {
		return sick_risk_item_id;
	}

	public void setSick_risk_item_id(Long sick_risk_item_id) {
		this.sick_risk_item_id = sick_risk_item_id;
	}

	public BigDecimal getRisk_rout() {
		return risk_rout;
	}

	public void setRisk_rout(BigDecimal risk_rout) {
		this.risk_rout = risk_rout;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}
}
