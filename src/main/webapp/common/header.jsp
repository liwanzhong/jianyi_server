<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="bg-dark dk header navbar navbar-fixed-top-xs">
    <div class="navbar-header aside-md" >
        <a class="btn btn-link visible-xs"
           data-toggle="class:nav-off-screen,open" data-target="#nav,html">
            <i class="fa fa-bars"></i>
        </a> <a href="#" class="navbar-brand"
                data-toggle="fullscreen">
        <%--<img src="${ctx}/notebook/notebook_files/logo.png" class="m-r-sm">--%>
        简医检测管理系统</a>
        <a class="btn btn-link visible-xs" data-toggle="dropdown"
           data-target=".nav-user"> <i class="fa fa-cog"></i>
        </a>
    </div>
    <ul class="nav navbar-nav navbar-right m-n hidden-xs nav-user">
        <li class="dropdown">
            <a href="index.html#" class="dropdown-toggle" data-toggle="dropdown">
					<span class="thumb-sm avatar pull-left">
					<img src="${ctx}/notebook/notebook_files/avatar.jpg">
					</span> ${sessionScope.userFormMap.accountName} <b class="caret"></b>
            </a>
            <ul class="dropdown-menu animated fadeInRight">
                <span class="arrow top"></span>
                <li><a href="#" onclick="javascript:updatePasswordLayer();">密码修改</a></li>
                <li class="divider"></li>
                <li><a href="${ctx}/logout.shtml">登出(Logout)</a></li>
            </ul>
        </li>
    </ul>
</header>
