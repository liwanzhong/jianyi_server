package com.lanyuan.vo;

import java.io.Serializable;

/**
 * Created by liwanzhong on 2016/5/21.
 */
public class LineChartJSONVO implements Serializable {

    /*{
        "label": "气道障碍系数",
            "value": "97.83",
            "anchorBorderColor" : "#cc3333",
            "anchorBgColor" : "#ff9900"
    }*/

    private String label;

    private String value;

    private String anchorBorderColor;

    private String anchorBgColor;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAnchorBorderColor() {
        return anchorBorderColor;
    }

    public void setAnchorBorderColor(String anchorBorderColor) {
        this.anchorBorderColor = anchorBorderColor;
    }

    public String getAnchorBgColor() {
        return anchorBgColor;
    }

    public void setAnchorBgColor(String anchorBgColor) {
        this.anchorBgColor = anchorBgColor;
    }
}
