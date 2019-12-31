// 引入 gulp
var gulp = require('gulp');

// 引入组件
var less = require('gulp-less'); //less编译
var livereload = require('gulp-livereload'); //浏览器自动更新
var minifycss = require('gulp-minify-css'); //压缩css
var sourcemaps = require('gulp-sourcemaps'); //储存位置信息
var rename = require('gulp-rename'); //重命名
var concat = require('gulp-concat'); //文件合并
var uglify = require('gulp-uglify'); //压缩js
var imagemin = require('gulp-imagemin'); //图片压缩
// var requirejsOptimize = require("gulp-requirejs-optimize");  //require优化，解决模块间的依赖关系


// 编译Less
gulp.task('Less', function() {
    gulp.src(['less/style.less'])
        .pipe(sourcemaps.init())
        .pipe(less())
        .pipe(sourcemaps.write())
        .pipe(gulp.dest('css')) //将会在css目录下生成css文件
        .pipe(minifycss())
        .pipe(rename({ suffix: '.min' }))
        .pipe(gulp.dest('css'));
});

// 打包requirejs
gulp.task('jsmin', function() {
    return gulp.src('js/*.js')
        .pipe(rename({ suffix: '.min' }))
        .pipe(uglify())
        .pipe(gulp.dest('script'));
});


gulp.task('lesswatch', function() {
    //当所有less文件发生改变时，调用Less任务
    gulp.watch('less/**/*.less', ['Less']);
});

gulp.task('jswatch', function() {
    //
    gulp.watch('js/**/*.js', ['jsmin']);
});

/******************[]]*********************报刊压缩 合并，压缩css *******************************/
// gulp.task('styles', function() {
//     gulp.src('./spiderwap/baokan20/css/css/*.css')
//         //.pipe(concat('all.css'))
//         //.pipe(gulp.dest('./dist'))
//         //.pipe(rename('all.min.css'))//自定义命名
//         .pipe(minifycss())
//         .pipe(gulp.dest('./spiderwap/baokan20/css'));//生成的文件夹
// 		gulp.watch('./spiderwap/baokan20/js/js/*.js', function(){
//         gulp.run('scripts');
//     });

// });

// 合并，压缩文件
// gulp.task('scripts', function() {
//     gulp.src('./spiderwap/baokan20/js/js/*.js')
//         //.pipe(concat('common.js'))
//         //.pipe(gulp.dest('./dist'))
//         //.pipe(rename('common.min.js'))//自定义命名
// 		//.pipe(rename({ suffix: '.min' }))//重命名
//         .pipe(uglify())
//         .pipe(gulp.dest('./spiderwap/baokan20/js'));//生成的文件夹
// 		gulp.watch('./spiderwap/baokan20/css/css/*.css', function() {
// 		gulp.run('styles');
// 	});
// });

// gulp.task('default',['styles','scripts']);



/***************************************报刊压缩 合并，压缩css *******************************/

// // 默认任务
gulp.task('default', ['lesswatch']);

// 默认任务
// gulp.task('default', function(){
//     // 监听文件变化
//     gulp.watch('build/less/**/*.less', ['Less']);
// });