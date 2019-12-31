/**
 * Created by Administrator on 2017/4/20.
 */
var app = angular.module('paper', []);
app.controller('paperController', function ($scope, $http, $location) {

    init();

    function init() {
        var paperId = getUrlParam("id");

        if(paperId) {

            $http.get(ctx + "/paper/question/list?paperId=" + paperId).success(function (questions) {
                $scope.questions = questions;
                console.log($scope.questions);
            });

        }
    }

    //--------private method------------------------

    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }

});

app.filter('trusted', function ($sce) {
    return function (text) {
        return $sce.trustAsHtml(text);
    };
});

function saveAnswer() {
    var data = $("#fm").serialize();
    console.log(data);
    $.post(ctx+"/paper/answer/save", data ,function () {

    });
}