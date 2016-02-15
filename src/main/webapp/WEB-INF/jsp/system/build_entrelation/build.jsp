<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="/common/common.jspf"%>
</head>
<style type="text/css">

    #mytable {
        width: 660px;
        padding: 0;
        margin: 0;
    }

    caption {
        padding: 0 0 5px 0;
        width: 660px;
        font: italic 13px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
        text-align: right;
    }

    th {
        font: bold 13px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
        color: #4f6b72;
        border-right: 1px solid #C1DAD7;
        border-bottom: 1px solid #C1DAD7;
        border-top: 1px solid #C1DAD7;
        letter-spacing: 2px;
        text-transform: uppercase;
        text-align: left;
        padding: 6px 6px 6px 12px;
    }

    th.nobg {
        border-top: 0;
        border-left: 0;
        border-right: 1px solid #C1DAD7;
        system: none;
    }

    #mytable td {
        border-right: 1px solid #C1DAD7;
        border-bottom: 1px solid #C1DAD7;
        system: #fff;
        font-size:11px;
        padding: 6px 6px 6px 12px;
        color: #4f6b72;
    }

    .lanyuan_bb{
        border-bottom: 1px solid #C1DAD7;
    }

    td.alt {
        system: #F5FAFA;
        color: #797268;
    }

    th.spec {
        border-left: 1px solid #C1DAD7;
        border-top: 0;
        system: #fff ;
        font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
    }

    th.specalt {
        border-left: 1px solid #C1DAD7;
        border-top: 1px solid #C1DAD7;
        system: #f5fafa ;
        font: bold 13px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
        color: #797268;
    }
    /*---------for IE 5.x bug*/
    html>body td{ font-size:13px;}
</style>
<script type="text/javascript">

    function sub(){
        ly.ajax({
            async : false, //请勿改成异步，下面有些程序依赖此请数据
            type : "POST",
            data : $("#from").serializeJson(),
            url : rootPath + '/build_entrelation/buildRelation.shtml',
            dataType : 'json',
            success : function(json) {
                if (json == "success") {
                    layer.confirm('分配成功！是否关闭窗口？',{icon: 3,offset: '-100px'}, function(index) {
                        parent.layer.close(parent.pageii);
                        return false;
                    });
                } else {
                    layer.alert(json,{icon: 2,offset: '-100px'});
                }

            }
        });
    }
</script>
<body>
<form method="post" id="from" name="form">
    <input id='userId' name="userEntrelationFormMap.user_id" type="hidden" value="${userformmap.id}">
    <%--todo 选择企业，选择检测点，保存记录(级联下拉)--%>
    <section class="panel panel-default">
        <div class="panel-body">
            <div class="form-group">
                <div class="col-sm-3">
                    <label class="control-label">用户名</label>
                </div>
                <div class="col-sm-9">
                    ${userformmap.userName}
                </div>
            </div>
            <div class="line line-dashed line-lg pull-in"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">所属企业</label>
                <div class="col-sm-9">
                    <select  class="form-control" name="userEntrelationFormMap.ent_id" id="enterprise" onchange="showsub_point()"></select>
                </div>
            </div>
            <div class="line line-dashed line-lg pull-in"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">所属检测点</label>
                <div class="col-sm-9">
                    <select  class="form-control" name="userEntrelationFormMap.sub_point_id" id="sub_point" > </select>
                </div>
            </div>
            <div class="line line-dashed line-lg pull-in"></div>
        </div>
        <footer class="panel-footer text-right bg-light lter">
            <a href="#" class="btn btn-s-md btn-success btn-rounded" onclick="sub()">保存</a>
        </footer>
    </section>
    <br>
</form>
<script type="text/javascript">
    $.ajax({
        type : "POST",
        data : {

        },
        url : rootPath + '/enterprise/findAll.shtml',
        dataType : 'json',
        success : function(json) {
            $("#enterprise").empty();
            for (index in json) {
                $("#enterprise").append('<option value="'+json[index].id+'">'+json[index].name+'</option>');
            }
        }
    });


    function showsub_point(){
        $.ajax({
            type : "POST",
            data : {
                "entid":$("#enterprise").val()
            },
            url : rootPath + '/sub_point/findbyEntid.shtml',
            dataType : 'json',
            success : function(json) {
                $("#sub_point").empty();
                for (index in json) {
                    $("#sub_point").append('<option value="'+json[index].id+'">'+json[index].name+'</option>');
                }
            }
        });
    }
</script>
</body>
</html>