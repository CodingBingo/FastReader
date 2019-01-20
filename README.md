# FastReader

	一款开源的电子图书阅览器。
	
## Todo:

~~1. 打开大型txt文档~~

    打开大于10M的txt文本不会出现奔溃现象且阅读流畅。

~~2. 拆分章节~~

    将书籍拆分成若干章节。

~~3. 加载动画~~
    
    书籍在处理的过程中动画并提示处理书籍的比例。

~~4. 断点加载~~
    
    书籍正在加载，由于其他事件中断，下次进入的时候继续上次的位置继续处理书籍。
    
~~5. 动态设置阅读背景颜色~~

    在阅读过程中，用户可以调整阅读文本背景。目前内置6种颜色。

~~6. 动态设置阅读字体大小~~

    在阅读中，用户可以动态调整字体的大小。

~~7. 拖动进度条跳到指定章节~~
    
    在阅读页面，拖动进度条，动态跳转到该章节。

~~8. 删除已经添加的书籍~~

    长按书本，可以删除书本。


~~9. 多书籍同时加载~~
    
    多书籍同时加载，显示正常
    
10. 添加下载小说

11. 添加广告功能

12. 支持各种格式书籍（目前只支持txt文本）

## 示例
* 主页
    <div style="display: -webkit-flex; display: flex; flex-direction: row; flex-wrap; justify-content: space-between">
    <img width="300" height="534" src="/doc/images/MainView.png"/>
    <img width="300" height="534" src="/doc/images/Loading.png"/>
    </div>

* 阅读页
    <div style="display: -webkit-flex; display: flex; flex-direction: row; flex-wrap; justify-content: space-between">
    <img width="300" height="534" src="/doc/images/ReadingView.png"/>
    <img width="300" height="534" src="/doc/images/ReadingViewWithController.png"/>
    <img width="300" height="534" src="/doc/images/ReadingViewWithStyleController.png"/>
    </div>
    
* 本地文件添加
    <div style="display: -webkit-flex; display: flex; flex-direction: row; flex-wrap; justify-content:center">
    <img width="300" height="534" src="/doc/images/FileList.png"/>
    </div>

