$(function(){
  window.onload = function(){
    var $li1 = $('#tab1 .tabList li');
    var $li2 = $('#tab2 .tabList li');
    var $ul1 = $('.tabCon1 div');
    var $ul2 = $('.tabCon2 div');
          
    $li1.click(function(){
      var $this = $(this);
      var $t = $this.index();
      $li1.removeClass();
      $this.addClass('current');
      $ul1.css('display','none');
      $ul1.eq($t).css('display','block');
    });
    
    $li2.click(function(){
      var $this = $(this);
      var $t = $this.index();
      $li2.removeClass();
      $this.addClass('current');
      $ul2.css('display','none');
      $ul2.eq($t).css('display','block');
    });
  }
});

