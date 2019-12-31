// 设置rem单位比例
document.addEventListener('DOMContentLoaded', function () {
    var deviceWidth = document.documentElement.clientWidth;
    document.documentElement.style.fontSize = deviceWidth / 10 + 'px';
}, false);
window.onresize = function () {
    var deviceWidth = document.documentElement.clientWidth;
    document.documentElement.style.fontSize = deviceWidth / 10 + 'px';
}