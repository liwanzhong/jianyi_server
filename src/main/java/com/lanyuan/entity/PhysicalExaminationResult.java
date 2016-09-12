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


    private BigDecimal random_max;
    private BigDecimal random_min;
    private Integer bmiorage;
    private Long bmiorage_leve_change;
    private BigDecimal check_value_tzbef;
    private BigDecimal item_score_tzbef;


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

    public BigDecimal getRandom_max() {
        return random_max;
    }

    public void setRandom_max(BigDecimal random_max) {
        this.random_max = random_max;
    }

    public BigDecimal getRandom_min() {
        return random_min;
    }

    public void setRandom_min(BigDecimal random_min) {
        this.random_min = random_min;
    }

    public Integer getBmiorage() {
        return bmiorage;
    }

    public void setBmiorage(Integer bmiorage) {
        this.bmiorage = bmiorage;
    }

    public Long getBmiorage_leve_change() {
        return bmiorage_leve_change;
    }

    public void setBmiorage_leve_change(Long bmiorage_leve_change) {
        this.bmiorage_leve_change = bmiorage_leve_change;
    }

    public BigDecimal getCheck_value_tzbef() {
        return check_value_tzbef;
    }

    public void setCheck_value_tzbef(BigDecimal check_value_tzbef) {
        this.check_value_tzbef = check_value_tzbef;
    }

    public BigDecimal getItem_score_tzbef() {
        return item_score_tzbef;
    }

    public void setItem_score_tzbef(BigDecimal item_score_tzbef) {
        this.item_score_tzbef = item_score_tzbef;
    }
}
