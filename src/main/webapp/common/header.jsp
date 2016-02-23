<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="bg-dark dk header navbar navbar-fixed-top-xs">
    <div class="navbar-header aside-md" >
        <a class="btn btn-link visible-xs"
           data-toggle="class:nav-off-screen,open" data-target="#nav,html">
            <i class="fa fa-bars"></i>
        </a> <a href="#" class="navbar-brand"
                data-toggle="fullscreen"><img
            src="${ctx}/notebook/notebook_files/logo.png" class="m-r-sm">Notebook</a>
        <a class="btn btn-link visible-xs" data-toggle="dropdown"
           data-target=".nav-user"> <i class="fa fa-cog"></i>
        </a>
    </div>
    <ul class="nav navbar-nav hidden-xs">
        <li class="dropdown">
            <a href="index.html#" class="dropdown-toggle dker" data-toggle="dropdown">
                <i class="fa fa-building-o"></i>
                <span class="font-bold">Activity</span>
            </a>
            <section
                    class="dropdown-menu aside-xl on animated fadeInLeft no-borders lt">
                <div class="wrapper lter m-t-n-xs">
                    <a href="index.html#" class="thumb pull-left m-r"> <img
                            src="${ctx}/notebook/notebook_files/avatar.jpg"
                            class="img-circle">
                    </a>
                    <div class="clear">
                        <a href="index.html#"><span class="text-white font-bold">@Mike
										Mcalidek</span></a> <small class="block">Art Director</small> <a
                            href="index.html#" class="btn btn-xs btn-success m-t-xs">Upgrade</a>
                    </div>
                </div>
                <div class="row m-l-none m-r-none m-b-n-xs text-center">
                    <div class="col-xs-4">
                        <div class="padder-v">
                            <span class="m-b-xs h4 block text-white">245</span> <small
                                class="text-muted">Followers</small>
                        </div>
                    </div>
                    <div class="col-xs-4 dk">
                        <div class="padder-v">
                            <span class="m-b-xs h4 block text-white">55</span> <small
                                class="text-muted">Likes</small>
                        </div>
                    </div>
                    <div class="col-xs-4">
                        <div class="padder-v">
                            <span class="m-b-xs h4 block text-white">2,035</span> <small
                                class="text-muted">Photos</small>
                        </div>
                    </div>
                </div>
            </section>
        </li>
        <li>
            <div class="m-t m-l">
                <a href="price.html"
                   class="dropdown-toggle btn btn-xs btn-primary" title="Upgrade"><i
                        class="fa fa-long-arrow-up"></i></a>
            </div>
        </li>
    </ul>
    <ul class="nav navbar-nav navbar-right m-n hidden-xs nav-user">
        <li class="hidden-xs"><a href="index.html#"
                                 class="dropdown-toggle dk" data-toggle="dropdown"> <i
                class="fa fa-bell"></i> <span
                class="badge badge-sm up bg-danger m-l-n-sm count"
                style="display: inline-block;">3</span>
        </a>
            <section class="dropdown-menu aside-xl">
                <section class="panel bg-white">
                    <header class="panel-heading b-light bg-light">
                        <strong>You have <span class="count"
                                               style="display: inline;">3</span> notifications
                        </strong>
                    </header>
                    <div class="list-group list-group-alt animated fadeInRight">
                        <a href="index.html#" class="media list-group-item"
                           style="display: block;"><span
                                class="pull-left thumb-sm text-center"><i
                                class="fa fa-envelope-o fa-2x text-success"></i></span><span
                                class="media-body block m-b-none">Sophi sent you a email<br>
										<small class="text-muted">1 minutes ago</small></span></a> <a
                            href="index.html#" class="media list-group-item"> <span
                            class="pull-left thumb-sm"> <img
                            src="${ctx}/notebook/notebook_files/avatar.jpg"
                            alt="John said" class="img-circle">
								</span> <span class="media-body block m-b-none"> Use awesome
										animate.css<br> <small class="text-muted">10
                            minutes ago</small>
								</span>
                    </a> <a href="index.html#" class="media list-group-item"> <span
                            class="media-body block m-b-none"> 1.0 initial released<br>
										<small class="text-muted">1 hour ago</small>
								</span>
                    </a>
                    </div>
                    <footer class="panel-footer text-sm">
                        <a href="index.html#" class="pull-right"><i
                                class="fa fa-cog"></i></a> <a href="index.html#notes"
                                                              data-toggle="class:show animated fadeInRight">See all the
                        notifications</a>
                    </footer>
                </section>
            </section></li>
        <li class="dropdown hidden-xs"><a href="index.html#"
                                          class="dropdown-toggle dker" data-toggle="dropdown"><i
                class="fa fa-fw fa-search"></i></a>
            <section class="dropdown-menu aside-xl animated fadeInUp">
                <section class="panel bg-white">
                    <form role="search">
                        <div class="form-group wrapper m-b-none">
                            <div class="input-group">
                                <input type="text" class="form-control" placeholder="Search">
										<span class="input-group-btn">
											<button type="submit" class="btn btn-info btn-icon">
                                                <i class="fa fa-search"></i>
                                            </button>
										</span>
                            </div>
                        </div>
                    </form>
                </section>
            </section></li>
        <li class="dropdown">
            <a href="index.html#" class="dropdown-toggle" data-toggle="dropdown">
					<span class="thumb-sm avatar pull-left">
					<img src="${ctx}/notebook/notebook_files/avatar.jpg">
					</span> ${sessionScope.userFormMap.accountName} <b class="caret"></b>
            </a>
            <ul class="dropdown-menu animated fadeInRight">
                <span class="arrow top"></span>
                <li><a href="index.shtml#">Settings</a></li>
                <li><a href="#" onclick="javascript:updatePasswordLayer();">密码修改</a></li>
                <li><a href="index.html#"> <span class="badge bg-danger pull-right">3</span> Notifications
                </a></li>
                <li><a href="docs.html">Help</a></li>
                <li class="divider"></li>
                <li><a href="logout.shtml">Logout</a></li>
            </ul>
        </li>
    </ul>
</header>
