/**
 * Created by Administrator on 2017/4/20.
 */
var app = angular.module('paper', []);
app.controller('paperController', function ($scope, $http, $sce, $compile) {
    var editor;
    var remarkEditor;
    var validator;
    var refresh = true;

    /**
     * 单选题
     */
    var scq = {
        editing:true,
        type:1,
        title:"标题",
        required:1,
        options:[
            {
                title:"选项一",
                allowEmpty:0,
                standard:0,
                sort:0
            },
            {
                title:"选项二",
                allowEmpty:0,
                standard:0,
                sort:1
            },
            {
                title:"选项三",
                allowEmpty:0,
                standard:0,
                sort:2
            },
            {
                title:"选项四",
                allowEmpty:0,
                standard:0,
                sort:3
            }
        ]
    }

    /**
     * 多选题
     */
    var mcq = {
        editing:true,
        type:2,
        title:"标题",
        required:1,
        options:[
            {
                title:"选项一",
                allowEmpty:0,
                standard:0,
                sort:1
            },
            {
                title:"选项二",
                allowEmpty:0,
                standard:0,
                sort:2
            },
            {
                title:"选项三",
                allowEmpty:0,
                standard:0,
                sort:3
            },
            {
                title:"选项四",
                allowEmpty:0,
                standard:0,
                sort:4
            }
        ]
    }

    /**
     * 填空题
     */
    var fbq = {
        editing:true,
        type:3,
        title:"姓名：___",
        required:1,
        options:[
            {
                title:"",
                allowEmpty:0,
                standard:0
            }
        ]
    }

    /**
     * 问答题
     */
    var esq = {
        editing:true,
        type:4,
        title:"标题",
        required:1,
        options:[
            {
                title:"参考答案",
                allowEmpty:0,
                standard:0
            }
        ]
    }

    init();
    initKindeditor();
    initValidate();
    initUnreload();

    function init() {
        var paperId = getUrlParam("paperId");
        if(paperId) {
            $scope.maxStep = 3;
            $scope.step = 3;

            $http.get(ctx + "/paper/load?id=" + paperId).success(function (paper) {
                $scope.paper = paper;
                scq.paperId = $scope.paper.id;
                mcq.paperId = $scope.paper.id;
                esq.paperId = $scope.paper.id;
                fbq.paperId = $scope.paper.id;
                editor.html($scope.paper.description);
            });

            $http.get(ctx + "/paper/question/list?paperId=" + paperId).success(function (questions) {
                $scope.questions = questions;
            });

        } else {
            $scope.maxStep = 2;
            $scope.step = 1;
            $scope.questions = [];
            $scope.paper = {
                type: 1,
                allowAnon: 1
            }
        }
        $("#content").show();
    }

    function initKindeditor() {

        KindEditor.ready(function(K) {
            editor = K.create('textarea[name="description"]', {
                width : '100%',
                resizeType : 1,
                allowPreviewEmoticons : false,
                allowImageUpload : false,
                items : [
                    'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                    'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                    'insertunorderedlist', '|', 'emoticons', 'link']
            });

            remarkEditor = K.create('textarea[name="remark"]', {
                width : '100%',
                resizeType : 1,
                allowPreviewEmoticons : false,
                allowImageUpload : false,
                items : [
                    'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                    'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                    'insertunorderedlist', '|', 'emoticons', 'link']
            });
        });
    }

    function initValidate() {
        validator = $("#paperForm").validate({
            ignore: "",
            errorPlacement: function(error, element) {
                error.appendTo(element.parents(".col-md-8").next());
            }
        });
    }

    function initUnreload() {
        document.body.onbeforeunload = function (event) {
            if($scope.maxStep==3 && refresh) {
                var c = event || window.event;
                if (/webkit/.test(navigator.userAgent.toLowerCase())) {
                    return "离开页面将导致数据丢失！";
                } else {
                    c.returnValue = "离开页面将导致数据丢失！";
                }
            }
        }
    }

    $scope.changeStep = function () {
        if($scope.maxStep==3) {
            $scope.step = 3;
        }
    }

    $scope.savePaper = function() {
        editor.sync();
        if(!validator.form()){
            return;
        }
        var cfg = {
            headers: {
                "Content-Type": "application/json"
            }
        }
        $scope.paper.description = editor.html();
        $http.post(ctx + "/paper/save", $scope.paper, cfg).success(function (result) {
            refresh = false;
            window.location = ctx +"/paper/edit?paperId="+result;
        });
    }
    
    $scope.finishEdit = function() {
        var cfg = {
            headers: {
                "Content-Type": "application/json"
            }
        }
        $http.post(ctx + "/paper/question/saveAll", $scope.questions, cfg).success(function (result) {
            refresh = false;
            window.location = ctx +"/paper";
        });
    }

    //-------------问题操作----------------------------------
    function cleanEditStatus() {
        angular.forEach($scope.questions, function (question) {
            question.editing = false;
        });
    }
    
    $scope.addScq = function () {
        addQuestion(scq);
    }

    $scope.addMcq = function () {
        addQuestion(mcq);
    }

    $scope.addFbq = function () {
        addQuestion(fbq);
    }

    $scope.addEsq = function () {
        addQuestion(esq);
    }

    function addQuestion(copy) {
        cleanEditStatus();
        var qst = angular.copy(copy);
        qst.sort = $scope.questions.length;
        $scope.questions.push(qst);
        scrollToBottom();
    }

    $scope.editQuestion = function (question) {
        cleanEditStatus();
        question.editing = true;
    }

    $scope.saveQuestion = function (question, $index) {
        var cfg = {
            headers: {
                "Content-Type": "application/json"
            }
        }
        $http.post(ctx + "/paper/question/save", question, cfg).success(function (result) {
            $scope.questions[$index] = result;
            question.editing = false;
        });
    }
    
    $scope.copyQuestion = function ($index) {
        var copyQuestion = angular.copy($scope.questions[$index]);
        copyQuestion.id = "";
        copyQuestion.editing = true;
        copyQuestion.sort = $scope.questions.length;
        angular.forEach(copyQuestion.options, function (option) {
            option.id = "";
        })

        addQuestion(copyQuestion);
    }

    $scope.removeQuestion = function ($index) {
        if($scope.questions[$index].id) {
            $http.get(ctx + "/paper/question/delete?questionId="+$scope.questions[$index].id).success(function () {
                $scope.questions.splice($index, 1);
            });
        } else {
            $scope.questions.splice($index, 1);
        }
        resetQuestionSort();
    }
    
    $scope.getFillBlackTitle = function (question) {
        var arr = question.title.split(/_{3,}/);
        var html = "<span class='form-inline'>";
        var size = 0;
        angular.forEach(arr, function (str, index) {
            html += str;
            if(str!="" && (index+1) != arr.length) {
                size ++ ;
                if(!question.options[index]) {
                    question.options.push({
                        title:"",
                        allowEmpty:0,
                        standard:0,
                        sort:index
                    });
                }

                html += "<input type='text' class='form-control'/>";
            }
        });
        question.options.length = size;
        html += "</span>";
        return $sce.trustAsHtml(html);
    }

    $scope.moveQuestionUp = function ($index) {
        var temp = $scope.questions[$index-1];
        $scope.questions[$index-1] = $scope.questions[$index];
        $scope.questions[$index] = temp;
        resetQuestionSort();
    }

    $scope.moveQuestionDown = function ($index) {
        var temp = $scope.questions[$index+1];
        $scope.questions[$index+1] = $scope.questions[$index];
        $scope.questions[$index] = temp;
        resetQuestionSort();
    }

    $scope.moveQuestionTop = function ($index) {
        var qst = $scope.questions.splice($index, 1);
        $scope.questions.unshift(qst[0]);
        resetQuestionSort();
    }

    $scope.moveQuestionTail = function ($index) {
        var qst = $scope.questions.splice($index, 1);
        $scope.questions.push(qst[0]);
        resetQuestionSort();
    }

    function resetQuestionSort() {
        angular.forEach($scope.questions, function (question, index) {
            question.sort = index;
        });
    }

    //-------------选项操作-------------------------------
    $scope.updateOptionsDefault = function (question, checked, $index) {
        if (question.type == 1 && checked == 1) {//单选设置
            angular.forEach(question.options, function (option, index) {
                if ($index != index) {
                    option.standard = 0;
                }
            });
        }
    }

    $scope.removeOption = function (question, $index) {
        question.options.splice($index, 1);
        resetOptionSort(question);
    }

    $scope.moveUp = function (question, $index) {
        var temp = question.options[$index-1];
        question.options[$index-1] = question.options[$index];
        question.options[$index] = temp;
        resetOptionSort(question);
    }

    $scope.moveDown = function (question, $index) {
        var temp = question.options[$index+1];
        question.options[$index+1] = question.options[$index];
        question.options[$index] = temp;
        resetOptionSort(question);
    }

    $scope.addOption = function (question) {
        if(!question.options) {
            question.options = [];
        }
        question.options.push({
            title:"",
            image:"",
            remark:"",
            allowEmpty:0,
            standard:0
        });
        resetOptionSort(question);
    }

    function resetOptionSort(question) {
        angular.forEach(question.options, function (option, index) {
            option.sort = index;
        });
    }
    
    $scope.editRemark = function (option) {
        $scope.currentOption = option;
        if (option.remark) {
            remarkEditor.html(option.remark);
        } else {
            remarkEditor.html("");
        }
        $('#remarkModal').modal('show');
    }

    $scope.setOptionRemark = function () {
        $scope.currentOption.remark = remarkEditor.html();
        $('#remarkModal').modal('hide');
    }

    //--------private method------------------------

    function scrollToBottom() {
        setTimeout(function () {
            var h = $(document).height()-$(window).height();
            $(document).scrollTop(h);
        }, 100);
    }

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

app.directive('contenteditable', function() {
    return {
        restrict: 'A', // only activate on element attribute
        require: '?ngModel', // get a hold of NgModelController
        link: function(scope, element, attrs, ngModel) {
            if (!ngModel) {
                return;
            } // do nothing if no ng-model
            // Specify how UI should be updated
            ngModel.$render = function() {
                element.html(ngModel.$viewValue || '');
            };
            // Listen for change events to enable binding
            element.on('blur', function() {
                scope.$apply(readViewText);
            });
            // No need to initialize, AngularJS will initialize the text based on ng-model attribute
            // Write data to the model
            function readViewText() {
                var html = element.html();
                // When we clear the content editable the browser leaves a <br> behind
                // If strip-br attribute is provided then we strip this out
                if (attrs.stripBr && html === '<br>') {
                    html = '';
                }
                ngModel.$setViewValue(html);
            }
        }
    };
});