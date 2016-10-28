$(function(){

    var datetime = new Date();
    var dateNow = datetime.getFullYear()+""+(datetime.getMonth()+1)+""+datetime.getDate();
    var projectNameLen = $(".btn").length;
    for (var flag = 0; flag < projectNameLen; flag++) {
        var projectNameFlag = $(".btn").eq(flag).attr("doc-Attr");
        if (projectNameFlag != undefined) {
            var docStorage ="";
            var docFlag = localStorage.getItem("docFlag");
            if (docFlag != null) {
                var objdoclocal = docFlag.split(";");
                for (var item = 0; item < objdoclocal.length; item++) {
                    if (objdoclocal[item] != "") {
                        var itemlocal = objdoclocal[item].replace(/'/g, '"');
                        var localdocJson = JSON.parse(itemlocal);
                        var datesub = parseInt(dateNow) - parseInt(localdocJson.dates);
                        if (projectNameFlag == localdocJson.key && localdocJson.value == "noNew"  ) {
                            if( datesub>2){
                                docStorage = docStorage +";{'key':'"+localdocJson.key+"','value':'hasNew','dates':"+dateNow+"}";
                            }else{
                                docStorage = docStorage +";"+objdoclocal[item];
                                $(".btn").eq(flag).parents("li").find(".new").remove();
                            }
                        }else{
                            docStorage = docStorage +";"+objdoclocal[item];
                        }
                    }
                }
                localStorage.removeItem("docFlag");
                localStorage.setItem("docFlag",docStorage);
            }

        }

    }

    // $.ajax({
    //     url: '/interfaceApiDoc/tpl/' + myattr + '-' + listId,
    //     type: 'GET',
    //     success: function (data) {
    //         $('.container').html(data);
    //     }
    // });
    $('.page a').each(function(){
        $(this).click(function(){
            $('.page a').removeClass('active');
            $(this).addClass('active')
        })
    });
    $('.previous').click(function(){
        var i = $('.active').parent('li').index()-1
        if(i<0){
            i=0
        }
        $('.page a').removeClass('active');
        $('.page li').eq(i).find('a').addClass('active')
        
    })
    $('.next').click(function(){

        var i = $('.active').parent('li').index()+1;
        var j = $('.page li').length-1;
        if(i>j){
            i=j;
        }
        $('.page a').removeClass('active');
        $('.page li').eq(i).find('a').addClass('active')
    })



    $('.blue').click(function(){
        $('link').attr('href','/html/css/index_blue.css');
        $('.changeColor a').show();
        $(this).hide();
    })
    $('.yellow').click(function(){
        $('link').attr('href','/html/css/index_yellow.css');
        $('.changeColor a').show();
        $(this).hide();
    })
    $('.red').click(function(){
        $('link').attr('href','/html/css/index_red.css');
        $('.changeColor a').show();
        $(this).hide();
    })
    $('.green').click(function(){
        $('link').attr('href','/html/css/index_green.css');
        $('.changeColor a').show();
        $(this).hide();
    })

    // $('.btn a').click(function(){
    //    var str= $(this).attr("id");
    //     console.log("高度侧:"+str);
    //     $.ajax({
    //         url: '/interfaceApiDoc/index/'+"web2guide",
    //         type: 'GET',
    //         success: function (data) {
    //             // console.log(data);
    //             $('.').html(data);
    //             // location.href();
    //         }
    //     });
    // })

    /*// var indexPage = $(".previous");//首页
    var upPage = $(".previous");
    var downPage = $(".next");
    // var endPage = document.createElement_x("a");
    var nowpage = 1;

    var bodyUl =$(".mainbody ul");
    var bodyli = $(".mainbody ul li");
    var countRecord = bodyli.length;
    //每页显示的记录数
    var PAGESIZE = 2;
    //总页数
    var countPage = (countRecord % PAGESIZE == 0 ? countRecord / PAGESIZE
        : Math.ceil(countRecord / PAGESIZE));

    var pages =$(".page");

    //当点击上一页的操作
    upPage.onclick = function() {
        if (nowpage - 1 > 1) {
            nowpage -= 1;
        } else {
            nowpage = 1;
            indexPageInfo(countRecord, bodyli);
        }
        //显示上一页记录
        var startindex = (nowpage - 1) * PAGESIZE;
        // alert(startindex + "====================++++++");
        var endindex = startindex + PAGESIZE;
        PageInfo(startindex, endindex, countRecord, bodyli)
    }

    downPage.onclick = function() {
        if (nowpage + 1 >= countPage) {
            nowpage = countPage;
        } else {
            nowpage += 1;
        }
        //显示上一页记录
        var startindex = (nowpage - 1) * PAGESIZE;
        // alert(startindex + "====================++++++");
        var endindex = startindex + PAGESIZE;
        PageInfo(startindex, endindex, countRecord, bodyli)
    }*/

})
