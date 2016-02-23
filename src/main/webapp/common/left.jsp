<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<aside class="bg-light lter b-r aside-md hidden-print hidden-xs" id="nav">
    <section class="vbox">
        <section class="w-f scrollable">
            <div class="slim-scroll" data-height="auto"
                 data-disable-fade-out="true" data-distance="0" data-size="5px"
                 data-color="#333333">
                <!-- nav -->
                <nav class="nav-primary hidden-xs">
                    <ul class="nav" id="left_menu_ul_itemsid">
                        <c:forEach var="key" items="${sessionScope.list}" varStatus="s">
                            <c:if test="${key.ishide == 0}">
                                <!-- <li class="active"> 某一项展开-->
                                <li <c:if test="${s.index==0}">class="active"</c:if>>
                                    <a href="javascript:void(0)"
                                       <c:if test="${s.index==0}">class="active"</c:if>>
                                        <i class="fa fa-th-list icon"> <b class="bg-success"></b></i>
												<span class="pull-right">
													<i class="fa fa-angle-down text"></i>
													<i class="fa fa-angle-up text-active"></i>
												</span>
                                        <span>${key.name}</span>
                                    </a>
                                        <%--默认第一层为目录
                                        最多支持三层
                                        第二层判断是否是目录--%>
                                    <ul class="nav lt">
                                        <c:forEach var="kc" items="${key.children}">
                                            <c:if test="${kc.ishide == 0}">
                                                <c:choose>
                                                    <c:when test="${kc.type == 1}">
                                                        <%--菜单链接 --%>
                                                        <li class="active">
                                                            <%--<a href="javascript:void(0)" class="active" nav-n="${key.name},${kc.name},${kc.resUrl}?id=${kc.id}"> <i class="fa fa-angle-right"></i> <span>${kc.name}</span></a>--%>
                                                                <a href="${ctx}${kc.resUrl}?id=${kc.id}" class="active">
                                                                    <i class="fa fa-angle-right"></i>
                                                                    <span>${kc.name}</span>
                                                                </a>
                                                        </li>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <li class="active">
                                                            <a href="javascript:void(0)">
                                                                <i class="fa fa-book icon"> <b class="bg-info"></b></i>
                                                                <span class="pull-right"><i class="fa fa-angle-down text"></i><i class="fa fa-angle-up text-active"></i></span>
                                                                <span>${kc.name}</span>
                                                            </a>

                                                            <ul class="nav lt">
                                                                <c:forEach var="kchild" items="${kc.children}">
                                                                    <li class="active">
                                                                        <%--<a href="javascript:void(0)" class="active" nav-n="${kchild.name},${kchild.name},${kchild.resUrl}?id=${kchild.id}"> <i class="fa fa-angle-right"></i> <span>${kchild.name}</span>
                                                                        </a>--%>
                                                                            <a href="${ctx}${kchild.resUrl}?id=${kchild.id}" class="active">
                                                                                <i class="fa fa-angle-right"></i>
                                                                                <span>${kc.name}</span>
                                                                            </a>
                                                                    </li>
                                                                </c:forEach>
                                                            </ul>
                                                        </li>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>

                                        </c:forEach>
                                    </ul>
                                </li>
                            </c:if>

                        </c:forEach>
                    </ul>
                </nav>
                <!-- / nav -->
            </div>
        </section>
        <footer class="footer lt hidden-xs b-t b-dark">
            <div id="chat" class="dropup">
                <section class="dropdown-menu on aside-md m-l-n">
                    <section class="panel bg-white">
                        <header class="panel-heading b-b b-light">Active
                            chats</header>
                        <div class="panel-body animated fadeInRight">
                            <p class="text-sm">No active chats.</p>
                            <p>
                                <a href="#" class="btn btn-sm btn-default">Start a chat</a>
                            </p>
                        </div>
                    </section>
                </section>
            </div>
            <div id="invite" class="dropup">
                <section class="dropdown-menu on aside-md m-l-n">
                    <section class="panel bg-white">
                        <header class="panel-heading b-b b-light">
                            John <i class="fa fa-circle text-success"></i>
                        </header>
                        <div class="panel-body animated fadeInRight">
                            <p class="text-sm">No contacts in your lists.</p>
                            <p>
                                <a href="#" class="btn btn-sm btn-facebook"><i
                                        class="fa fa-fw fa-facebook"></i> Invite from Facebook</a>
                            </p>
                        </div>
                    </section>
                </section>
            </div>
            <a href="#nav" data-toggle="class:nav-xs"
               class="pull-right btn btn-sm btn-dark btn-icon"> <i
                    class="fa fa-angle-left text"></i> <i
                    class="fa fa-angle-right text-active"></i>
            </a>
            <div class="btn-group hidden-nav-xs">
                <button type="button" title="Chats"
                        class="btn btn-icon btn-sm btn-dark" data-toggle="dropdown"
                        data-target="#chat">
                    <i class="fa fa-comment-o"></i>
                </button>
                <button type="button" title="Contacts"
                        class="btn btn-icon btn-sm btn-dark" data-toggle="dropdown"
                        data-target="#invite">
                    <i class="fa fa-facebook"></i>
                </button>
            </div>
        </footer>
    </section>
</aside>