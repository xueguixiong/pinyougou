//添加控制器
app.controller('brandController',function($scope,$controller,baseService){

    //指定brandController继承baseController
    $controller('baseController',{$scope : $scope});

    /** 重新加载列表数据 */
    $scope.reload = function () {
        /** 切换页码  */
        $scope.search($scope.paginationConf.currentPage,
            $scope.paginationConf.itemsPerPage);
    };
    $scope.searchEntity = {};
    //分页查询品牌
    $scope.search=function (page,rows) {
        //调用服务层发送异步请求
        baseService.findByPage("/brand/findByPage",page,rows,$scope.searchEntity).then(function (response) {
            //获取响应数据 response.data{"total":100,"row":[{},{}]}
            //获取分页的品牌数据
            $scope.dataList = response.data.rows;
            //设置总记录数
            $scope.paginationConf.totalItems = response.data.total;
        });
    };

    //添加或修改
    $scope.saveOrUpdate = function () {
        //定义请求url
        var url = "save";
        if ($scope.entity.id){
            url="update";
        }

        baseService.sendPost("/brand/"+url,$scope.entity).then(function (response) {
            // response.data : true/false
            if (response.data){
                $scope.reload();
            }else{
                alert("添加失败！！！");
            }
        });
    };

    /** 为修改按钮绑定点击事件 */
    $scope.show = function(entity){
        // 把entity的json对象转化成一个新的json对象
        var jsonStr = JSON.stringify(entity);
        $scope.entity = JSON.parse(jsonStr);
    };

    //删除品牌
    $scope.delete = function () {
        if ($scope.ids.length > 0){
            baseService.deleteById("/brand/delete",$scope.ids).then(function (response) {
                if (response.data){ //删除成功
                    //清空数组
                    $scope.ids=[];
                    //重新加载品牌数据
                    $scope.reload();
                }
            });
        }else{
            alert("请选择要删除的品牌!");
        }
    }
});