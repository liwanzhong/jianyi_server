<%--
  Created by IntelliJ IDEA.
  User: liwanzhong
  Date: 2016/2/5
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>fashioncharts</title>
    <script type="text/javascript" src="./js/fushioncharts/fusioncharts.js"></script>
    <script type="text/javascript" src="./js/fushioncharts/fusioncharts.charts.js"></script>
    <script type="text/javascript" src="./js/fushioncharts/themes/fusioncharts.theme.fint.js"></script>
</head>
<body>

<div id="chartContainer">FusionCharts XT will load here!</div>
<script>
    FusionCharts.ready(function(){
        var revenueChart = new FusionCharts({
            "type": "column2d",
            "renderAt": "chartContainer",
            "width": "500",
            "height": "300",
            "dataFormat": "json",
            "dataSource": {
                "chart": {
                    "caption": "Monthly revenue for last year",
                    "subCaption": "Harry's SuperMart",
                    "xAxisName": "Month",
                    "yAxisName": "Revenues (In USD)",
                    "theme": "fint"
                },
                "data": [
                    {
                        "label": "Jan",
                        "value": "420000"
                    },
                    {
                        "label": "Feb",
                        "value": "810000"
                    },
                    {
                        "label": "Mar",
                        "value": "720000"
                    },
                    {
                        "label": "Apr",
                        "value": "550000"
                    },
                    {
                        "label": "May",
                        "value": "910000"
                    },
                    {
                        "label": "Jun",
                        "value": "510000"
                    },
                    {
                        "label": "Jul",
                        "value": "680000"
                    },
                    {
                        "label": "Aug",
                        "value": "620000"
                    },
                    {
                        "label": "Sep",
                        "value": "610000"
                    },
                    {
                        "label": "Oct",
                        "value": "490000"
                    },
                    {
                        "label": "Nov",
                        "value": "900000"
                    },
                    {
                        "label": "Dec",
                        "value": "730000"
                    }
                ]
            }
        });

        revenueChart.render();
    })
</script>
</body>

</html>
