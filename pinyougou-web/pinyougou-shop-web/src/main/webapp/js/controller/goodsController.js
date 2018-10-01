/** 定义控制器层 */
app.controller('goodsController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 添加或修改 */
    $scope.saveOrUpdate = function(){
        //保存商品富文本编辑器内容
        $scope.goods.goodsDesc.introduction = editor.html();
        /** 发送post请求 */
        baseService.sendPost("/goods/save",$scope.goods)
            .then(function(response){
                if (response.data){
                    /** 清空数据 */
                    $scope.goods = {};
                    editor.html('');
                    alert("添加成功！");
                }else{
                    alert("操作失败！");
                }
            });
    };


    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function(page, rows){
        baseService.findByPage("/goods/findByPage", page,
			rows, $scope.searchEntity)
            .then(function(response){
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };





    /** 显示修改 */
    $scope.show = function(entity){
       /** 把json对象转化成一个新的json对象 */
       $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/goods/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };

    /*上传图片*/
    $scope.uploadFile = function () {
        baseService.uploadFile().then(function (response) {
            //如果上传成功，取出url
            if (response.data.status == 200){
                /*设置图片访问地址*/
                $scope.picEntity.url = response.data.url;
            }else {
                alert("上传失败！")
            }
        });
    };


    /* 定义数据存储结构 */
    $scope.goods = { goodsDesc : {itemImages : [],specificationItems : []}};

    /* 添加图片到数组 */
    $scope.appPic = function () {
        $scope.goods.goodsDesc.itemImages.push($scope.picEntity);
    };

    /* 删除上传的图片 */
    $scope.removePic = function (idx) {
        $scope.goods.goodsDesc.itemImages.splice(idx,1);
    };

    /* 查询商品的一级分类 */
    $scope.findItemCatByParentId = function (parentId,name) {
        baseService.sendGet("/itemCat/findItemCatByParentId?parentId=" + parentId)
            .then(function(response){
            $scope[name] = response.data;
        });
    };

    /** 监控 goods.category1Id 变量，查询二级分类 */
    $scope.$watch('goods.category1Id',function (newValue,oldValue) {
        if(newValue){
            /** 根据选择的值查询二级分类 */
            $scope.findItemCatByParentId(newValue,"itemCatList2");
        }else {
            $scope.itemCatList2 = null;
        }
    });

    /** 监控 goods.category2Id 变量，查询三级分类 */
    $scope.$watch('goods.category2Id',function (newValue,oldValue) {
        if(newValue){
            /** 根据选择的值查询三级分类 */
            $scope.findItemCatByParentId(newValue,"itemCatList3");
        }else {
            $scope.itemCatList3 = null;
        }
    });


    $scope.$watch('goods.category3Id',function (newValue,oldValue) {
        if(newValue){
            /** 根据选择的值查询三级分类 */
            //点击三级分类时，为数组对象
            //循环数组
            for (var i = 0; i < $scope.itemCatList3.length; i++) {
                //获取一个json对象
                var json = $scope.itemCatList3[i];
                //判断用户选择的是不是当前分类
                if(json.id == newValue){
                    //获取类型模板id
                    $scope.goods.typeTemplateId = json.typeId;
                    break;
                }
            }
        }
    });

    /** 监控 goods.typeTemplateId 模板ID */
    $scope.$watch('goods.typeTemplateId',function (newValue,oldValue) {
        if(newValue){
            /** 根据类型模板Id查询类型模板对象 */
            baseService.sendGet("/typeTemplate/findOne?id=" + newValue).then(function (response) {
                //获取响应数据
                $scope.brandIds = JSON.parse(response.data.brandIds);
                //获取扩展属性 [{"text":"分辨率","value":"1920*1080(FHD)"},{"text":"摄像头","value":"1200万像素"}]
                $scope.goods.goodsDesc.customAttributeItems= JSON.parse(response.data.customAttributeItems);
            });

            /** 查询该模版对应的规格与规格选项 */
            baseService.sendGet("/typeTemplate/findSpecByTemplateId?id=" + newValue)
                .then(function (response) {
                $scope.specList = response.data;
            });
        }
    });

    /* 为规格选项checkbox绑定点击事件 */
    $scope.updateSpecAttr = function ($event,specName,optionName) {
        /* 格式：
        [{"attributeValue":["64G","128G"],
                "attributeName":"机身内存"},
        {"attributeValue":["移动4G","联通4G","电信4G"],
                "attributeName":"网络"}]
         */
        var json = $scope.searchJsonByKey($scope.goods.goodsDesc.specificationItems,"attributeName",specName);
        // 判断数组是否存在，存在就加进数组，不存在就新建数组
        if (json){ //存在
            //判断checkbox是否选中
            if ($event.target.checked){
                json.attributeValue.push(optionName);
            }else { //没有选中
                //得到索引号
                var idx = json.attributeValue.indexOf(optionName);
                //删除数组元素
                json.attributeValue.splice(idx,1);

                //判断数组长度是否等于0
                if (json.attributeValue.length == 0){
                    //得到索引号
                    var idx = $scope.goods.goodsDesc.specificationItems.indexOf(json);
                    $scope.goods.goodsDesc.specificationItems.splice(idx,1);
                }
            }
        }else {
            $scope.goods.goodsDesc.specificationItems.push({attributeName : specName , attributeValue : [optionName]});
        }
    };

    //创建SKU数据的方法tb_item
    $scope.createItems = function () {
        //定义SKU数组变量并初始化
        $scope.goods.items = [{spec:{}, price:0, num:9999,
            status:'0', isDefault:'0' }];

        //根据用户选中的规格选项，不断往SKU数字扩充数组元素
        //获取用户选中的规格选项
        var specItems = $scope.goods.goodsDesc.specificationItems;
        //循环用户选中的规格选项
        /* 用户放的数据：
        [{"attributeValue":["64G","128G"],
                "attributeName":"机身内存"},
        {"attributeValue":["移动4G","联通4G","电信4G"],
                "attributeName":"网络"}]
         */
        for (var i = 0; i < specItems.length; i++) {
            //获取一个数组元素
            /*
                {"attributeValue":["64G","128G"],"attributeName":"机身内存"}
             */
            var specItem = specItems[i];
            //对原数组做扩充方法
            $scope.goods.items = swapItems($scope.goods.items,specItem.attributeValue,specItem.attributeName);

        }


    };

    //对原数组做扩充方法
    var swapItems = function (items,attributeValue,attributeName) {

        //定义新的SKU数组
        var newItems = [];

        // items [{spec:{}, price:0, num:9999,status:'0', isDefault:'0' }]
        // attributeValue ["移动4G","联通4G","电信4G"]
        for (var i = 0; i < items.length; i++) {
            // [{spec:{}, price:0, num:9999,status:'0', isDefault:'0' }]
            var item = items[i];

            // ["移动4G","联通4G","电信4G"]
            for (var j = 0; j < attributeValue.length; j++) {
                //转化一个新的json对象
                var newItem = JSON.parse(JSON.stringify(item));
                //spec : {"网络":"联通4G","机身内存":"64G"}
                newItem.spec[attributeName] = attributeValue[j];
                //添加到新的SKU数组
                newItems.push(newItem);
            }
        }
        return newItems;
    };


    //定义状态码查询数组
    $scope.status = ['未审核','已审核','审核未通过','关闭']

});