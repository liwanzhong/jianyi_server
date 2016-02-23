
<%@ page language="java" pageEncoding="UTF-8"%>
<html lang="en" class="app">
<head>
    <%@include file="/common/common.jspf"%>
</head>
<body>
<section class="vbox">
    <%@include file="/common/header.jsp"%>
    <section>
        <section class="hbox stretch">
            <!-- .aside -->
            <%@include file="/common/left.jsp"%>
            <!-- /.aside -->
            <section id="content">
                <section id="id_vbox" class="vbox">
                    <ul class="breadcrumb no-border no-radius b-b b-light" id="topli">
                    </ul>
                    <section class="scrollable" style="margin-top: 35px;">
                        <div>

                            <iframe id="ifm" src="${pageContext.request.contextPath}/monitor/info.shtml" width="100%" height="1300px" frameborder="no" border="0" marginwidth="0" marginheight="0" allowtransparency="yes"></iframe>
                        </div>
                    </section>
                </section>
            </section>
            <aside class="bg-light lter b-l aside-md hide" id="notes">
                <div class="wrapper">Notification</div>
            </aside>
        </section>
    </section>
</section>
<!-- Bootstrap -->
<div id="flotTip" style="display: none; position: absolute;"></div>
</body>
</html>


