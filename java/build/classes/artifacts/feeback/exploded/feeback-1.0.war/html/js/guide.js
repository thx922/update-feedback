$(function() {
    //查看localstorage里面
    var d = new Date();
    var dateNow = d.getFullYear()+""+(d.getMonth()+1)+""+d.getDate();
    var localValue = localStorage.getItem("localStoragePageInfo");
    if( localValue != null ){
        var newlocalStorage ="";
        var objlocal = localValue.split(";");
        for(var item =0 ;item<objlocal.length;item++){
            if(objlocal[item] != ""){
                var itemlocal = objlocal[item].replace(/'/g,'"');
                var localJson = JSON.parse(itemlocal);

                var datesub = parseInt(dateNow) - parseInt(localJson.datetime);
                var alength = $("a").length;
                for(var aLen =0 ;aLen< alength;aLen++){
                    var atr = $("a").eq(aLen).attr("my-attr");
                    var atrId = $("a").eq(aLen).attr("id");
                    if( atr == localJson.key && atrId == localJson.keyId && datesub<=2){
                        $("a").eq(aLen).find("span").html(localJson.value);
                        var first = $("a").eq(aLen).parents(".mainbav").find(".point").length;
                        if( first == 0){
                            $("a").eq(aLen).parents(".mainbav").find(".new").remove();
                        }
                        if(newlocalStorage !=""){
                            newlocalStorage = newlocalStorage + objlocal[item]+";";
                        }else{
                            newlocalStorage = objlocal[item]+";";
                        }
                    }
                }

                /*var mainbavLength = $(".mainbav").find(".new").length;
                var projectName = $("#spanId").html();
                //spanId
                if( mainbavLength ==0 ){
                    var docFlag = localStorage.getItem(projectName);
                    if( docFlag != null ){
                        localStorage.removeItem(projectName);
                    }else{
                        localStorage.setItem(projectName,"hasNew");
                    }
                }*/
            }
        }
        if(newlocalStorage !=""){
            localStorage.removeItem("localStoragePageInfo");
            localStorage.setItem("localStoragePageInfo",newlocalStorage);
        }
    }


    //改变主题
    $('.changeColor a').click(function () {
        var style = $(this).attr("id");
        $("link[title='" + style + "']").removeAttr("disabled");
        $("link[title!='" + style + "']").attr("disabled", "disabled");
        $.cookie('mystyle', style, {expires: 30});
        $('.changeColor a').show();
        $(this).hide()
        var $color = $("link[title='" + style + "']").attr('my-color');
        $('nav a.ac .iconfont').css('color', $color);
        $('nav a.bc .iconfont').css('color', $color);

    });
    //设置cookie
    var cookie_style = $.cookie("mystyle");
    if (cookie_style == null) {
        $("link[title='theme_red']").removeAttr("disabled");
    } else {
        $("link[title='" + cookie_style + "']").removeAttr("disabled");
        $("link[title!='" + cookie_style + "']").attr("disabled", "disabled");
        $('.changeColor a').show();
        $('#' + cookie_style).hide();
    }

    $('.subnav').hide();
    $('.change').hide()
    $('.mainbavClick').click(function () {
        var $color = $('header').css('background-color');
        if (!$(this).hasClass('ac')) {
            $(this).parent('.mainbav').siblings('.mainbav').children('.mainbavClick').removeClass('ac');
            $('.menuClick').removeClass('bc')
            $(this).next().show();
            $(this).addClass('ac');
            $(this).children('i').html("&#xe607;");
            $(this).children('i').css("color", $color);
            $(this).parent('.mainbav').siblings('.mainbav').children('.subnav').hide()
            $(this).parent('.mainbav').siblings('.mainbav').find('.iconfont').html('&#xe608;');
            $(this).parent('.mainbav').siblings('.mainbav').find('.iconfont').css("color", "#555B5E")
            $(this).next().find(' a:first').click();

        } else {
            $(this).removeClass('ac');
            $(this).next().hide();
            $(this).children('i').html("&#xe608;");
            $(this).children('i').css("color", "#555B5E");
            $('.change').hide()
        }
    })

    $('.menuClick').click(function () {
        var $color = $('header').css('background-color');
        if (!$(this).hasClass('bc')) {
            $('nav menu a').removeClass('active');
            $('nav a').removeClass('bc')
            $(this).addClass('bc');
            $(this).children('i').html("&#xe607;");
            $(this).children('i').css("color", $color);
            $(this).parent('.menu').siblings('.menu').children('a').children('.iconfont').html("&#xe608;").css("color", "#555B5E");
            $(this).parent().siblings('.menu').children('.change').hide();
            $(this).next().show();
            $(this).next().find('li:first a:first').click();
        } else {
            $(this).removeClass('bc');
            $(this).next().hide();
            $(this).children('i').html("&#xe608;");
            $(this).children('i').css("color", "#555B5E");
        }
    });
    $('.submenu').click(function () {
        var $color = $('header').css('background-color');
        $('.change .submenu').removeClass('active');
        $(this).parents('.mainbav').siblings('.mainbav').children('.subnav').hide();
        $(this).parents('.mainbav').siblings('.mainbav').children('.mainbavClick').children('i').html("&#xe608;").css('color',"#555B5E");
        $('.mainbavClick').removeClass('ac');
        $('.menuClick').removeClass('bc');
        $(this).parents('.mainbav').children('a').addClass('ac');
        $(this).parents('.mainbav').children('a').children('i').html("&#xe607;");
        $(this).parents('.mainbav').children('a').children('i').css("color", $color);
        $(this).parent('.mainbav').siblings('.mainbav').children('.subnav').hide()
        $(this).parents('.mainbav').children('a').next().show()

        $(this).parents('.menu').children('a').addClass('bc');
        $(this).parents('.menu').children('a').children('i').html("&#xe607;");
        $(this).parents('.menu').children('a').children('i').css("color", $color);
        $(this).parents('.menu').siblings('.menu').children('a').children('.iconfont').html("&#xe608;").css("color", "#555B5E");
        $(this).parents('.menu').siblings('.menu').children('.change').hide();
        $(this).parents('.menu').children('a').next().show();
        $(this).addClass('active');

    })

    //滚动监听
    $(window).scroll(function () {
        var wst = $(window).scrollTop();
        var i = 0, j = 0, rl, sl, ll;
        var lastIndex = $('nav .active').parent('li').index();
        var secondIndex = $('nav .active').parents('.menu').index();
        var rootIndex = $('nav .active').parents('.mainbav').index();

        rl = rootIndex + 1;
        sl = secondIndex + 1;
        ll = lastIndex + 1
        var navLen = $('nav .active').parents('.change').children('li').length;
        for (var i = 0; i < navLen; i++) {
            ll = i + 1
            var scrId = '#' + rl + '-' + sl + '-' + ll

            if($(scrId).offset().top<=wst+80){
                $('.change a').removeClass('active');
                $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).find('.change li').eq(i).children('a').addClass('active');
            }
        }
        var newhash = '#' + rootIndex + '-' + secondIndex + '-' + lastIndex;
        if (window.location.hash === "#-1--1--1") {
            window.location.hash = "#0-0-0"
        } else {

            window.location.hash = newhash;
            // var rhtml = $('nav .mainbav').eq(rootIndex).children('a').html().split('<')[0];
            // var shtml = $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).children('a').html().split('<')[0];
            var rhtml = $('nav .mainbav').eq(rootIndex).children('a').children('span').html();
            var shtml = $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).children('a').children('span').html();
            var lhtml = $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).find('.change').children('li').eq(lastIndex).children('a').html();
            var contitle = rhtml + '>' + shtml + '>' + lhtml;

            $('.conTitle').html(contitle);
        }

    })
    //搜索框
    var searchArr = new Array();
    var searchArr = [];
    var searchLen = $('nav').find('.submenu').length;
    for (var i = 0; i < searchLen; i++) {
        var allsubmenu = $('nav').find('.submenu').eq(i).text();
        var allid = $('nav').find('.submenu').eq(i).attr('id');
        var obj = {};
        obj.text = allsubmenu;
        obj.id = allid;
        searchArr.push(obj)
    }
    $('#search').keyup(function () {
        var inputTxt = $('#search').val();

        if (inputTxt != "") {
            $('.searchtxt').show();
            var tab = "<ul style='max-height:100px'>";
            $.each(searchArr, function (n, item) {
                if(item.text.indexOf(inputTxt) != -1) {
                    tab +="<li><a href='javascript:void(0)' myid ='"+ item.id+"'>"+item.text+"</a></li>"
                }
            })
            tab += "</ul>";
            $('.searchtxt').html(tab);
            $('.searchtxt li a').eq(0).addClass('act')
            $('.searchtxt a').click(function () {
                $('nav span').removeClass('allsear')
                var findmenu = $(this).text();
                var $color = $('header').css('background-color');
                var findid = $(this).attr('myid');
                var arr = findid.slice('3').split('-');
                $('nav .mainbav').eq(arr[0]).children('a').click();
                $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').click();
                $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change li').eq(arr[2]).children('a').click();


                $("nav .submenu:contains('" + findmenu + "')").parents('.mainbav').children('a').children('span').addClass('allsear');
                $("nav .submenu:contains('" + findmenu + "')").parents('.menu').children('a').children('span').addClass('allsear');


                    // var mainindex = $("nav .submenu:contains('" + findmenu + "')").parents('.mainbav').index();
                // var menuindex = $("nav .submenu:contains('" + findmenu + "')").parents('.menu').index();
                // var subindex = $("nav .submenu:contains('" + findmenu + "')").parent().index();
                // $("nav .submenu:contains('" + findmenu + "')").click()
                $('.searchtxt').hide();
                $('#search').val(findmenu);
            });
            $('.searchtxt li a').mouseover(function () {
                $('.searchtxt li a').removeClass('active')
                $(this).addClass('active')
            })
        } else {
            $('.searchtxt').hide();
        }
    });
    $('.searchBtn').click(function(){
        var findmenu = $('#search').val();
        if(!findmenu){
            return;
        }
        $("nav .submenu:contains('" + findmenu + "')").eq(0).click()
        $('.searchtxt').hide();
    })
    $('body').click(function(){
        $('.searchtxt').hide();
    })

    $('nav .menuClick').click(function () {
        var datetime = new Date();
        var dates = datetime.getFullYear()+""+(datetime.getMonth()+1)+""+datetime.getDate();
        var removeBefore =0;
        if( $(this).parents(".menu").find("span").find(".point").length >0 ){
            removeBefore = $(this).parents(".menu").find("span").find(".point").length;
        }
        $(this).find('.point').remove();
        if ($(this).parents('.mainbav').find('.point').length == 0) {
            $(this).parents('.mainbav').find('.new').remove()
        }
        var points = $(this).parents(".menu").find("span").find(".point").length;
        var key = $(this).parents(".menu").children("a").attr("my-attr");
        var keyId = $(this).parents(".menu").children("a").attr("id");
        var value = $(this).parents(".menu").find("span").html();
        //<span>RegisterAct1<strong class="point"></strong></span><i class="iconfont" style="color: rgb(229, 74, 92);"></i>
        if( points == 0){
            if( removeBefore >0){
                // var localStorageArr = new Array();
                // var localStorageArr = [];
                // var objs = {};
                // objs.keys = key;
                // objs.keyId = keyId;
                // objs.values = value;
                // objs.dates = datetime.getFullYear()+""+(datetime.getMonth()+1)+""+datetime.getDate();
                var getlocal = localStorage.getItem("localStoragePageInfo");
                if(getlocal != null ){
                    localStorage.setItem("localStoragePageInfo", getlocal+";"+"{'key':'"+key+"','keyId':'"+keyId+"','value':'"+value+"','datetime':"+dates+"}");
                }else{
                    localStorage.setItem("localStoragePageInfo", "{'key':'"+key+"','keyId':'"+keyId+"','value':'"+value+"','datetime':"+dates+"}");
                }

            }
            docStorage(dates);
        }



    })
})

function docStorage( dates) {
    var docStorage ="";
    var mainbavLength = $(".mainbav").find(".new").length;//控制首页index.html的新标志
    var docFlag = localStorage.getItem("docFlag");
    var projectName = $("#spanId").html();
    if( docFlag != null ) {
        var objdoclocal = docFlag.split(";");
        for (var item = 0; item < objdoclocal.length; item++) {
            if (objdoclocal[item] != "") {
                var itemlocal = objdoclocal[item].replace(/'/g, '"');
                var localdocJson = JSON.parse(itemlocal);
                var docTime ="";
                if (projectName == localdocJson.key && mainbavLength == 0) {
                    if( localdocJson.value == "noNew"){
                        docTime = localdocJson.dates;
                    }else{
                        docTime = dates;
                    }
                    docStorage = docStorage + ";" + "{'key':'" + localdocJson.key + "','value':'noNew','dates':"+docTime+"}";
                } else if (projectName == localdocJson.key && mainbavLength != 0) {
                    if( localdocJson.value == "noNew"){
                        docTime =dates; ;
                    }else{
                        docTime = localdocJson.dates;
                    }
                    docStorage = "{'key':'"+projectName+"','value':'hasNew','dates':"+docTime+"}";
                }else if (projectName != localdocJson.key && mainbavLength == 0) {
                    docStorage = docStorage + ";" + "{'key':'" + localdocJson.key + "','value':'noNew','dates':"+dates+"}";
                }else if (projectName != localdocJson.key && mainbavLength != 0) {
                    docStorage = "{'key':'"+projectName+"','value':'hasNew','dates':"+dates+"}";
                }
            }
        }
    } else if (docFlag == null && mainbavLength == 0) {
        docStorage = "{'key':'" + projectName+"','value':'noNew','dates':"+dates+"}";
    }else if( docFlag == null && mainbavLength !=0){
        docStorage = "{'key':'"+projectName+"','value':'hasNew','dates':"+dates+"}";
    }
    localStorage.removeItem("docFlag");
    localStorage.setItem("docFlag" , docStorage);
}




