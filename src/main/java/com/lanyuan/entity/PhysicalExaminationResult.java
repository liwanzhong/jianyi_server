package com.lanyuan.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/7/14.
 */
public class PhysicalExaminationResult implements Serializable {

    private Long id;
    private Long examination_record_id;
    private Long bit_item_id;
    private Long small_item_id;
    private BigDecimal gen_min_value;
    private BigDecimal gen_max_value;
    private BigDecimal gen_in_value;
    private BigDecimal in_value_score;
    private BigDecimal check_value;
    private BigDecimal item_score;
    private Long orgin_leve_id;
    private BigDecimal gen_quanzhong;
    private BigDecimal quanzhong_score;
    private Long tzed_leve_id;


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

    public Long getBit_item_id() {
        return bit_item_id;
    }

    public void setBit_item_id(Long bit_item_id) {
        this.bit_item_id = bit_item_id;
    }

    public Long getSmall_item_id() {
        return small_item_id;
    }

    public void setSmall_item_id(Long small_item_id) {
        this.small_item_id = small_item_id;
    }

    public BigDecimal getGen_min_value() {
        return gen_min_value;
    }

    public void setGen_min_value(BigDecimal gen_min_value) {
        this.gen_min_value = gen_min_value;
    }

    public BigDecimal getGen_max_value() {
        return gen_max_value;
    }

    public void setGen_max_value(BigDecimal gen_max_value) {
        this.gen_max_value = gen_max_value;
    }

    public BigDecimal getGen_in_value() {
        return gen_in_value;
    }

    public void setGen_in_value(BigDecimal gen_in_value) {
        this.gen_in_value = gen_in_value;
    }

    public BigDecimal getIn_value_score() {
        return in_value_score;
    }

    public void setIn_value_score(BigDecimal in_value_score) {
        this.in_value_score = in_value_score;
    }

    public BigDecimal getCheck_value() {
        return check_value;
    }

    public void setCheck_value(BigDecimal check_value) {
        this.check_value = check_value;
    }

    public BigDecimal getItem_score() {
        return item_score;
    }

    public void setItem_score(BigDecimal item_score) {
        this.item_score = item_score;
    }

    public Long getOrgin_leve_id() {
        return orgin_leve_id;
    }

    public void setOrgin_leve_id(Long orgin_leve_id) {
        this.orgin_leve_id = orgin_leve_id;
    }

    public BigDecimal getGen_quanzhong() {
        return gen_quanzhong;
    }

    public void setGen_quanzhong(BigDecimal gen_quanzhong) {
        this.gen_quanzhong = gen_quanzhong;
    }

    public BigDecimal getQuanzhong_score() {
        return quanzhong_score;
    }

    public void setQuanzhong_score(BigDecimal quanzhong_score) {
        this.quanzhong_score = quanzhong_score;
    }

    public Long getTzed_leve_id() {
        return tzed_leve_id;
    }

    public void setTzed_leve_id(Long tzed_leve_id) {
        this.tzed_leve_id = tzed_leve_id;
    }
}
