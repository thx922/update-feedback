$(function(){

    var cook= document.cookie;
    console.log("cook:"+cook);
    var $color = $('header').css('background-color');
    var current = window.location.hash.slice(1,4);//5-1
    console.log("current:"+current);
    if(!current){
        console.log("发hi而");
        var defaultHtml = $('nav .mainbav').eq(0).find('.menu').eq(0).children('a').attr("id");
        var myattr = $('nav .mainbav').eq(0).find('.menu').eq(0).children('a').attr("my-attr");
        $.ajax({
            url:'/interfaceApiDoc/tpl/'+myattr+'-'+defaultHtml,
            type:'GET',
            success:function(data){
                console.log("成功的:"+data);
                $('.container').html(data);
                var arr = [0,0,0];
                // var rhtml = $('nav .mainbav').eq(arr[0]).children('a').html().split('<')[0];
                // var shtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').html().split('<')[0];
                var rhtml = $('nav .mainbav').eq(arr[0]).children('a').children('span').html();
                var shtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').children('span').html();
                var lhtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change').children('li').eq(arr[2]).children('a').html()
                var contitle = rhtml + '>' + shtml + '>' + lhtml;
                $('.conTitle').html(contitle);
                $('nav  .mainbav').eq(arr[0]).children('a').next().show();
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').next().show();
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change li').eq(arr[2]).children('a').addClass('active');
                $('nav  .mainbav').eq(arr[0]).children('a').addClass('ac').children('.iconfont').html("&#xe607;").css("color", $color);
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').addClass('bc').children('.iconfont').html("&#xe607;").css("color", $color);
                window.location.hash = '#0-0-0';

            },
            error: function(e) {
                console.log("失败的:"+e);
                alert(e);
            }
        })
    }else {
        var arr =window.location.hash.slice(1,7).split('-');
        var listId = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').attr('id');
        var myattr = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').attr('my-attr');
        $.ajax({
            url:'/interfaceApiDoc/tpl/'+myattr+'-'+listId,
            type:'GET',
            success:function(data){
                $('.container').html(data);

                // var rhtml = $('nav .mainbav').eq(arr[0]).children('a').html().split('<')[0];
                // var shtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').html().split('<')[0];
                var rhtml = $('nav .mainbav').eq(arr[0]).children('a').children('span').html();
                var shtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').children('span').html();
                var lhtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change').children('li').eq(arr[2]).children('a').html()
                var contitle = rhtml + '>' + shtml + '>' + lhtml

                $('.conTitle').html(contitle)
                // var $color = $('header').css('background-color');

                var setgo = window.location.hash.slice(1,7);
                $('nav  .mainbav').eq(arr[0]).children('a').next().show();
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').next().show();
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change li').eq(arr[2]).children('a').addClass('active');
                $('nav  .mainbav').eq(arr[0]).children('a').addClass('ac').children('.iconfont').html("&#xe607;").css("color",$color);
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').addClass('bc').children('.iconfont').html("&#xe607;").css("color",$color);

                var rid = ++arr[0];
                var sid = ++arr[1];
                var lid = ++arr[2];

                var goid = '#'+rid+'-'+sid+'-'+lid;
                var gotop = $(goid).offset().top;
                $(window).scrollTop(gotop);
                //$('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).siblings('.menu').children('a').children('.iconfont').html("&#xe608;").css("color","#555B5E");


            }
        })
    }


    $('nav .menu a').click(function(){
        var lastIndex = $('.active').parent().index();
        var secondIndex = $('.active').parents('.menu').index();
        var rootIndex = $('.active').parents('.mainbav').index();
        var hash = '#'+rootIndex+'-'+secondIndex+'-'+lastIndex;
        window.location.hash = hash;
        var rI = rootIndex+1;
        var sI = secondIndex+1;
        var lI = lastIndex+1;
        var myid = $('.active').parents('.menu').children('a').attr('id');
        var myattr = $('.active').parents('.menu').children('a').attr('my-attr');
        $.ajax({
            url:'/interfaceApiDoc/tpl/'+myattr+'-'+myid,
            type:'GET',
            success:function(data){
                $('.container').html(data);

                // var rhtml = $('nav .mainbav').eq(rootIndex).children('a').html().split('<')[0];
                // var shtml = $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).children('a').html().split('<')[0];
                var rhtml = $('nav .mainbav').eq(rootIndex).children('a').children('span').html();
                var shtml = $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).children('a').children('span').html();
                var lhtml = $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).find('.change').children('li').eq(lastIndex).children('a').html()
                var contitle = rhtml + '>' + shtml + '>' + lhtml

                $('.conTitle').html(contitle)
                var goId = '#'+rI+'-'+sI+'-'+lI;
                var actop =$(goId).offset().top;
                $(window).scrollTop(actop);
            }
        })

    })

})